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
	try{
	  arp.read(model,in,"")
	} catch {
	  case e:TurtleParseException=>
		val msg = "Error parsing the r2r document: "+e.getMessage
		logger.error(msg)
		throw new IllegalArgumentException(msg)
	  case e:RiotException =>
		throw new IllegalArgumentException("Error parsing r2rml ",e)
	  case e:JenaException => throw new R2rmlModelException("Invalid R2RML ",e)
	}
	model.write(System.out,RDFFormat.TTL)
	
  }

  private def readTriplesMap(triplesMapUri:Resource):Seq[TriplesMap]={
    if (triplesMapUri!=null && triplesMaps.contains(triplesMapUri.getURI))
      List(triplesMaps(triplesMapUri.getURI))
    else{
      val tm=loadTriplesMap(triplesMapUri)
      tm
    }
  }
  
  private def loadTriplesMap(triplesMapUri:Resource):Seq[TriplesMap]={
	val tMapVar:Node = if (triplesMapUri!=null) triplesMapUri
	  else "tMap"

	val group=
	Group(Tgp(tMapVar,(RDF.typeProp,R2RML.TriplesMap),
                            (R2RML.logicalTable,"lt"),
                            (R2RML.subjectMap,"sMap")),                            
        OpTgp("lt",(R2RML.sqlQuery,"query")),
        OpTgp("lt",(R2RML.sqlVersion,"version")),
        OpTgp("lt",(R2RML.tableName,"table")),
        //OpTgp("sMap",(R2RML.graphMap,"gMap")),
        OpTgp("sMap",(R2RML.classProperty,"sClass")),
        OpTgp("sMap",(R2RML.column,"sCol")),
        OpTgp("sMap",(R2RML.template,"sTemp")),
        OpTgp("sMap",(R2RML.termType,"sTerm")),
        OpTgp("sMap",(R2RML.inverseExpression,"sInv")),
        OpTgp("sMap",(R2RML.constant,"sConst")),
        OpTgp("sMap",(R2RML.graph,"directGraph")),
        Optional(Tgp("gMap",(R2RML.termType,"gTerm")),
                 Tgp("sMap",(R2RML.graphMap,"gMap"))).block,
        Optional(Tgp("gMap",(R2RML.constant,"gConst")),
                 Tgp("sMap",(R2RML.graphMap,"gMap"))).block,
        Optional(Tgp("gMap",(R2RML.template,"gTemp")),
                 Tgp("sMap",(R2RML.graphMap,"gMap"))).block
            ).groupelement
	    
	    
	val query=SelectSparqlQuery(group,Array("tMap","query","version","table","sClass","sCol","sTerm","sConst",
				"sInv","sTemp","gConst","directGraph","gMap","gTemp","gTerm"))
	
    logger.debug("Query tripleMap: "+query.serialize)
    val qexec = QueryExecutionFactory.create(query.serialize, model)
    val rs={
      val results = qexec.execSelect
      results.map{soln=>
		val uri:Node = 
		if (triplesMapUri==null) {
		  if (soln.get("tMap").isAnon)
			soln.get("tMap").asNode
		  else if (soln.get("tMap").isResource)
		    soln.get("tMap").asResource
		  else triplesMapUri
		}
		else triplesMapUri

		//      if (triplesMap.containsKey(uri))
		//    	  continue;
	    logger.debug("Triples map found: "+uri);
		   
		val sClass = soln.res("sClass"); 
		val sqlQuery = soln.lit("query");
		val inverse = soln.lit("sInv");
		val column = soln.lit("sCol");
		val termType = soln.res("sTerm");
		val constant = soln.res("sConst");
		val table = soln.lit("table");
		val template = soln.lit("sTemp");
        if (soln.get("gTerm")!=null && !soln.res("gTerm").getURI.equals(R2RML.IRI))
          throw new R2rmlModelException("Non IRI terms not allowed for graphMap")
		if ((soln.get("gConst")==null && soln.get("gTemp")==null) && soln.get("gMap")!=null)
		  throw new R2rmlModelException("graphMap without constant graph or template IRI not allowed")
		val constantGraph=if (soln.get("gConst")!=null) {
		  val g=soln.res("gConst")
		  if (g.getURI.equals(R2RML.defaultGraph.getURI)) null
		  else g
		} 	
		else {
		  val g=soln.res("directGraph")
		  if (g!=null && g.equals(R2RML.defaultGraph)) null
		  else g
		}
		val gMap=if (constantGraph!=null) new GraphMap(constantGraph) 
		  else if (soln.lit("gTemp")!=null){
		    new GraphMap(soln.lit("gTemp").getString)
		  } 
		  else null
		val sMap=new SubjectMap(constant,column,template,TermType(termType),sClass,gMap)
		val pos=readPOMaps(uri)
		val tm=new TriplesMap(uri.getURI,LogicalTable(table,sqlQuery,soln.res("version")),sMap,pos.toArray)
		triplesMaps.put(tm.uri,tm)
		tm
	  }
    }
	
	rs.toList	
        
  }
  
  
  private def readPOMaps(tMapUri:Node)={
    val query=new Query
    query.setQuerySelectType
    query.addResultVars("predicate","pCol","oConst","oCol","oTemp","odType",
        "gCol","graph","parentTMap","directPredicate","directObject","joinParent","joinChild")
	val group=	
	Group(Tgp(tMapUri,(RDF.typeProp,R2RML.TriplesMap),
                      (R2RML.predicateObjectMap,"poMap")),
          OpTgp("poMap",(R2RML.graph,"graph")),
          Optional(Tgp("pMap",(R2RML.constant,"predicate")),
                   Tgp("poMap",(R2RML.predicateMap,"pMap"))).block,
          Optional(Tgp("pMap",(R2RML.column,"pCol")),
                   Tgp("poMap",(R2RML.predicateMap,"pMap"))).block,
          Optional(Tgp("oMap",(R2RML.template,"oTemp")),
                   Tgp("poMap",(R2RML.objectMap,"oMap"))).block,
          Optional(Tgp("oMap",(R2RML.constant,"oConst")),
                   Tgp("poMap",(R2RML.objectMap,"oMap"))).block,
          Optional(Tgp("oMap",(R2RML.column,"oCol")),
                   Tgp("poMap",(R2RML.objectMap,"oMap"))).block,
          Optional(Tgp("poMap",(R2RML.objectMap,"oMap")),
                   Tgp("oMap",(R2RML.joinCondition,"join")),
                   Tgp("join",(R2RML.parent,"joinParent")),
                   Tgp("join",(R2RML.child,"joinChild"))).block,
          OpTgp("oMap",(R2RML.termType,"oTerm")),
          OpTgp("oMap",(R2RML.datatype,"odType")),
          Optional(Tgp("poMap",(R2RML.objectMap,"oMap")),
                   Tgp("oMap",(R2RML.parentTriplesMap,"parentTMap"))).block,

		  Group(Union(
		    Group(Tgp("poMap",(R2RML.predicateMap,"pMap")),
		                   Tgp("poMap",(R2RML.objectMap,"oMap"))).groupelement,
		    Group(Tgp("poMap",(R2RML.predicate,"directPredicate")),
		                   Tgp("poMap",(R2RML.objectMap,"oMap"))).groupelement,
		    Group(Tgp("poMap",(R2RML.predicate,"directPredicate")),
		                   Tgp("poMap",(R2RML.objectProp,"directObject"))).groupelement,
		    Group(Tgp("poMap",(R2RML.predicateMap,"pMap")),
		                   Tgp("poMap",(R2RML.objectProp,"directObject"))).groupelement).unionElement).groupelement		          		          
               ).groupelement

          
   
    query.setQueryPattern(group)                                                                   	
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
        val constantObject=if (soln.get("oConst")!=null) soln.get("oConst") else soln.res("directObject")
        val o=ObjectMap(constantObject,soln.lit("oCol"),soln.lit("oTemp"),TermType(soln.res("oTerm")),
          null,toDatatype(soln.res("odType")),null)
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

//class InvalidR2rmlDocumentException(msg:String,e:Throwable) extends java.lang.Exception(msg,e)
//class InvalidR2rmlLocationException(msg:String,e:Throwable) extends java.lang.Exception()
  
