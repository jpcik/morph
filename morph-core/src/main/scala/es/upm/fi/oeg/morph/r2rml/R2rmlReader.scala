package es.upm.fi.oeg.morph.r2rml

import collection.JavaConversions._
import java.net.URI
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import com.hp.hpl.jena.rdf.model.RDFReader
import com.hp.hpl.jena.rdf.model.ModelFactory
import es.upm.fi.oeg.morph.voc.RDFFormat
import com.hp.hpl.jena.n3.turtle.TurtleParseException
import org.openjena.riot.RiotException
import com.weiglewilczek.slf4s.Logging
import es.upm.fi.oeg.siq.sparql.Sparql
import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock
import es.upm.fi.oeg.morph.voc.RDF
import com.hp.hpl.jena.sparql.syntax.ElementGroup
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.rdf.model.Literal
import com.hp.hpl.jena.query.QuerySolution
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype._
import es.upm.fi.oeg.siq.sparql.XSDtypes
import com.hp.hpl.jena.graph.Node
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.shared.JenaException

class R2rmlReader() extends Sparql with XSDtypes with Logging {  
  val model=ModelFactory.createDefaultModel
  val triplesMaps=new collection.mutable.HashMap[String,TriplesMap]
  
  lazy val tMaps=readTriplesMap(null)
  def filterBySubject(uri:String)=tMaps.filter(t=>t.subjectMap.rdfsClass.getURI.equals(uri))
  def filterByPredicate(uri:String)=tMaps.map(t=>
    t.poMaps.filter(_.predicateIs(uri))map((_,t))).flatten
  def allPredicates=tMaps.map(t=>t.poMaps.map((_,t))).flatten
    
    
  def read(mappingUrl:URI){ 
	try {
	  val in = new FileInputStream(mappingUrl.toString)
	  this.read(in)
	  in.close
	} catch {
	  case e:FileNotFoundException => try{
	    val iss = getClass.getClassLoader.getResourceAsStream(mappingUrl.toString)
		this.read(iss)
		} catch {
		  case ex:IOException=>throw new IllegalArgumentException("Cannot read r2rml "+mappingUrl)
		}				
	}
  }
	
  def read(in:InputStream){ 
	val arp:RDFReader= model.getReader(RDFFormat.TTL)
	try arp.read(model,in,"")
	catch {
	  case e:TurtleParseException=>
		val msg = "Error parsing the r2r document: "+e.getMessage
		logger.error(msg)
		throw new IllegalArgumentException(msg)
	  case e:RiotException => throw new IllegalArgumentException("Error parsing r2rml ",e)
	  case e:JenaException => throw new R2rmlModelException("Invalid R2RML ",e)
	}
	model.write(System.out,RDFFormat.TTL)	
  }

  private def readTriplesMap(triplesMapUri:Resource):Seq[TriplesMap]={
    if (triplesMapUri!=null && triplesMaps.contains(triplesMapUri.getURI))
      List(triplesMaps(triplesMapUri.getURI))
    else
      loadTriplesMap(triplesMapUri)
  }
  
  private def loadTriplesMap(triplesMapUri:Resource):Seq[TriplesMap]={
	val tMapVar:Node = if (triplesMapUri!=null) triplesMapUri
	  else "tMap"

	val group=Group(
	  ^(tMapVar,(RDF.a,R2RML.TriplesMap),(R2RML.logicalTable,"lt"),(R2RML.subjectMap,"sMap")),                            
      Optional("lt",R2RML.sqlQuery,"query"),
      Optional("lt",R2RML.sqlVersion,"version"),
      Optional("lt",R2RML.tableName,"table"),
      Optional("sMap",R2RML.classProperty,"sClass"),
      Optional("sMap",R2RML.column,"sCol"),
      Optional("sMap",R2RML.template,"sTemp"),
      Optional("sMap",R2RML.termType,"sTerm"),
      Optional("sMap",R2RML.inverseExpression,"sInv"),
      Optional("sMap",R2RML.constant,"sConst"),
      Optional("sMap",R2RML.graph,"directGraph"),
      Optional(^("gMap",R2RML.termType,"gTerm"),^("sMap",R2RML.graphMap,"gMap")),
      Optional(^("gMap",R2RML.constant,"gConst"),^("sMap",R2RML.graphMap,"gMap")),
      Optional(^("gMap",R2RML.template,"gTemp"),^("sMap",R2RML.graphMap,"gMap"))
    )
    val vars=Array("tMap","query","version","table","sClass","sCol","sTerm","sConst",
				   "sInv","sTemp","gConst","directGraph","gMap","gTemp","gTerm")	    	    
	val query=SelectSparqlQuery(group,vars)
	
    logger.debug("Query tripleMap: "+query.serialize)
    val qexec = QueryExecutionFactory.create(query.serialize, model)
    val rs={
      val results = qexec.execSelect
      results.map{soln=>
		val uri:Node = 
		if (triplesMapUri==null) {
		  if (soln.get("tMap").isAnon) soln.get("tMap").asNode
		  else if (soln.get("tMap").isResource) soln.get("tMap").asResource
		  else triplesMapUri
		}
		else triplesMapUri

	    logger.debug("Triples map found: "+uri);
		   		
		if ((soln.get("gConst")==null && soln.get("gTemp")==null) && soln.get("gMap")!=null)
		  throw new R2rmlModelException("graphMap without constant graph or template IRI not allowed")
		val constgraph=if (soln.get("gConst")!=null) soln.res("gConst") else soln.res("directGraph")
		val gMap=GraphMap(constgraph,null,soln.lit("gTemp"),TermType(soln.res("gTerm")))
		val sMap=new SubjectMap(soln.res("sConst"),soln.lit("sCol"),soln.lit("sTemp"),
		    TermType(soln.res("sTerm")),soln.res("sClass"),gMap)
		val pos=readPOMaps(uri)
		val lt=LogicalTable(soln.lit("table"),soln.lit("query"),soln.res("version"))
		val tm=new TriplesMap(uri.getURI,lt,sMap,pos.toArray)
		triplesMaps.put(tm.uri,tm)
		tm
	  }
    }	
	rs.toList	        
  }
  
  
  private def readPOMaps(tMapUri:Node)={
    val vars=Array("predicate","pCol","oConst","oCol","oTemp","odType","oTerm","lang",
        "gCol","graph","parentTMap","directPredicate","directObject","joinParent","joinChild")

    val group=Group(
	  ^(tMapUri,RDF.a,R2RML.TriplesMap),
      ^(tMapUri,R2RML.predicateObjectMap,"poMap"),
      Optional("poMap",R2RML.graph,"graph"),
      Optional(^("pMap",R2RML.constant,"predicate"),^("poMap",R2RML.predicateMap,"pMap")),
      Optional(^("pMap",R2RML.column,"pCol"),^("poMap",R2RML.predicateMap,"pMap")),                   
      Optional(^("oMap",R2RML.template,"oTemp"),^("poMap",R2RML.objectMap,"oMap")),
      Optional(^("oMap",R2RML.constant,"oConst"),^("poMap",R2RML.objectMap,"oMap")),
      Optional(^("oMap",R2RML.column,"oCol"),^("poMap",R2RML.objectMap,"oMap")),
      Optional(^("poMap",R2RML.objectMap,"oMap"),^("oMap",R2RML.joinCondition,"join"),
               ^("join",R2RML.parent,"joinParent"),^("join",R2RML.child,"joinChild")),
      Optional(^("poMap",R2RML.objectMap,"oMap"),^("oMap",R2RML.termType,"oTerm")),
      Optional("oMap",R2RML.datatype,"odType"),
      Optional("oMap",R2RML.language,"lang"),
      Optional(^("poMap",R2RML.objectMap,"oMap"),^("oMap",R2RML.parentTriplesMap,"parentTMap")),

	  Group(Union(
		Group(^("poMap",R2RML.predicateMap,"pMap"),^("poMap",R2RML.objectMap,"oMap")),
		Group(^("poMap",R2RML.predicate,"directPredicate"),^("poMap",R2RML.objectMap,"oMap")),
		Group(^("poMap",R2RML.predicate,"directPredicate"),^("poMap",R2RML.objectProp,"directObject")),
		Group(^("poMap",R2RML.predicateMap,"pMap"),^("poMap",R2RML.objectProp,"directObject"))
	   ))		          		          
     )
          
    val query=SelectSparqlQuery(group,vars)
    logger.debug("Query po: "+query.serialize);
    val qexec = QueryExecutionFactory.create(query.serialize, model)
    val results = qexec.execSelect
    results.map{soln=>      
      println( soln.varNames.map(v=>v+"="+soln.get(v)).mkString("--"))
      val predicate=if (soln.res("predicate")!=null) soln.res("predicate")
        else soln.res("directPredicate")
      val p=PredicateMap(predicate,soln.lit("pCol"),null)
      val gmap=if (soln.res("graph")!=null) new GraphMap(soln.res("graph"))
      else null
        
      if (soln.res("parentTMap")!=null){
        val join=if (soln.get("joinParent")!=null) 
          JoinCondition(soln.lit("joinParent"),soln.lit("joinChild"))
          else null
        val ro=RefObjectMap(soln.res("parentTMap").getURI,join)  
        new PredicateObjectMap(p,ro,gmap)
      }
      else{
        val constantObject=if (soln.get("oConst")!=null) soln.get("oConst") else soln.get("directObject")
        val o=ObjectMap(constantObject,soln.lit("oCol"),soln.lit("oTemp"),TermType(soln.res("oTerm")),
          soln.lit("lang"),toDatatype(soln.res("odType")),null)
        new PredicateObjectMap(p,o,gmap)
      }
      
    }.toList
  }
         
}

object R2rmlReader{
  def apply(mappingUrl:String)={
    val r=new R2rmlReader
    r.read(new URI(mappingUrl))
    r
  }
}
