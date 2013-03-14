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
import es.upm.fi.oeg.morph.r2rml.R2RML._
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
import java.net.URL

class R2rmlReader(mappingStream:InputStream) extends Sparql with XSDtypes with Logging {  
  private val model=ModelFactory.createDefaultModel
  val triplesMaps=new collection.mutable.HashMap[String,TriplesMap]
  read(mappingStream)
  
  lazy val tMaps=triplesMaps.values//readTriplesMap(null)
  def filterBySubject(uri:String)=tMaps.filter(t=>t.subjectMap.rdfsClass.getURI.equals(uri))
  def filterByPredicate(uri:String)=tMaps.map(t=>
    t.poMaps.filter(_.predicateIs(uri)).map((_,t))).flatten
  def allPredicates=tMaps.map(t=>t.poMaps.map((_,t))).flatten
     
  def this(mappingFile:String) = this{
    val uri=new URI(mappingFile)
    val fis:InputStream =// try
      R2RML.getClass.getClassLoader.getResourceAsStream(mappingFile)
    if (fis==null) new FileInputStream(mappingFile)
    else fis
  }
    	
  private def read(in:InputStream){ 
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
	in.close
	readTriplesMap(null)
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
	  ^(tMapVar,(RDF.a,R2RML.TriplesMap),(logicalTable,"lt"),(subjectMap,"sMap")),                            
      Optional("lt",sqlQuery,"query"),
      Optional("lt",sqlVersion,"version"),
      Optional("lt",tableName,"table"),
      Optional("lt",MorphVoc.pk,"pks"),
      Optional("sMap",classProperty,"sClass"),
      Optional("sMap",column,"sCol"),
      Optional("sMap",template,"sTemp"),
      Optional("sMap",termType,"sTerm"),
      Optional("sMap",inverseExpression,"sInv"),
      Optional("sMap",constant,"sConst"),
      Optional("sMap",graph,"directGraph"),
      Optional(^("gMap",termType,"gTerm"),^("sMap",graphMap,"gMap")),
      Optional(^("gMap",constant,"gConst"),^("sMap",graphMap,"gMap")),
      Optional(^("gMap",template,"gTemp"),^("sMap",graphMap,"gMap"))
    )
    val vars=Array("tMap","query","version","table","pks","sClass","sCol","sTerm","sConst",
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
		val pks=soln.lit("pks")
		val pklist:Set[String]=if (pks!=null) pks.getString.split(',').toSet
		  else Set()
		println("pk list loaded: " +pklist)
		val lt=LogicalTable(soln.lit("table"),soln.lit("query"),soln.res("version"),pklist)
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
      ^(tMapUri,predicateObjectMap,"poMap"),
      Optional("poMap",graph,"graph"),
      Optional(^("pMap",constant,"predicate"),^("poMap",predicateMap,"pMap")),
      Optional(^("pMap",column,"pCol"),^("poMap",predicateMap,"pMap")),                   
      Optional(^("oMap",template,"oTemp"),^("poMap",objectMap,"oMap")),
      Optional(^("oMap",constant,"oConst"),^("poMap",objectMap,"oMap")),
      Optional(^("oMap",column,"oCol"),^("poMap",objectMap,"oMap")),
      Optional(^("poMap",objectMap,"oMap"),^("oMap",termType,"oTerm")),
      Optional(^("poMap",objectMap,"oMap"),^("oMap",datatype,"odType")),
      Optional(^("poMap",objectMap,"oMap"),^("oMap",language,"lang")),
      Optional(^("poMap",objectMap,"oMap"),^("oMap",parentTriplesMap,"parentTMap")),
      Optional(^("poMap",objectMap,"oMap"),^("oMap",joinCondition,"join"),
               ^("join",parent,"joinParent"),^("join",child,"joinChild")),
	  Group(Union(
		Group(^("poMap",predicateMap,"pMap"),^("poMap",objectMap,"oMap")),
		Group(^("poMap",predicate,"directPredicate"),^("poMap",objectMap,"oMap")),
		Group(^("poMap",predicate,"directPredicate"),^("poMap",objectProp,"directObject")),
		Group(^("poMap",predicateMap,"pMap"),^("poMap",objectProp,"directObject"))
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
  def apply(mappingUrl:String)=new R2rmlReader(mappingUrl)
}
