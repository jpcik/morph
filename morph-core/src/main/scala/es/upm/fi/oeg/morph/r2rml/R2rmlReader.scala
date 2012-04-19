package es.upm.fi.oeg.morph.r2rml

import collection.JavaConversions._
import java.net.URI
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import es.upm.fi.dia.oeg.morph.r2rml.InvalidR2RLocationException
import java.io.InputStream
import com.hp.hpl.jena.rdf.model.RDFReader
import com.hp.hpl.jena.rdf.model.ModelFactory
import es.upm.fi.oeg.morph.voc.RDFFormat
import com.hp.hpl.jena.n3.turtle.TurtleParseException
import es.upm.fi.dia.oeg.morph.r2rml.InvalidR2RDocumentException
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

class R2rmlReader() extends Sparql with XSDtypes with Logging {  
  val model=ModelFactory.createDefaultModel
  val triplesMaps=new collection.mutable.HashMap[String,TriplesMap]
  
  lazy val tMaps=readTriplesMap(null)

  def read(mappingUrl:URI){ //throws InvalidR2RDocumentException, InvalidR2RLocationException
	try {
	  val in = new FileInputStream(mappingUrl.toString())
	  this.read(in);
	  in.close();
	} catch {
	  case e:FileNotFoundException => try{
	    val iss = getClass.getClassLoader().getResourceAsStream(mappingUrl.toString());
		this.read(iss);
		} catch {
		  case ex:IOException=>
			throw new InvalidR2RLocationException(ex.getMessage(), ex);
		}				
	  }
  }
	
  def read(in:InputStream){ //throws InvalidR2RDocumentException
	val arp:RDFReader= model.getReader(RDFFormat.TTL)
	try{
	  arp.read(model,in,"")
	} catch {
	  case e:TurtleParseException=>
		val msg = "Error parsing the r2r document: "+e.getMessage
		logger.error(msg)
		throw new InvalidR2RDocumentException(msg,e)
	  case e:RiotException =>
		throw new InvalidR2RDocumentException("Error reading  r2r document: "+e.getMessage,e)
	}
	model.write(System.out,RDFFormat.TTL)
	//readTriplesMap(null);
  }

  private def readTriplesMap(triplesMapUri:Resource):Seq[TriplesMap]=	{
      if (triplesMapUri!=null && triplesMaps.contains(triplesMapUri.getURI))
        List(triplesMaps(triplesMapUri.getURI))
        else{
	val tMapVar:Node = if (triplesMapUri!=null) triplesMapUri
	  else "tMap";
    val query=new Query
    query.setQuerySelectType
    query.addResultVars("tMap","query","table","sClass","sCol","sTerm","sConst",
				"sInv","sTemp")
	val group=
	Group(List(Tgp(tMapVar,(RDF.typeProp,R2RML.TriplesMap),
                            (R2RML.logicalTable,"lt"),
                            (R2RML.subjectMap,"sMap"))),                            
        OpTgp("lt",(R2RML.sqlQuery,"query")),
        OpTgp("lt",(R2RML.tableName,"table")),
        OpTgp("sMap",(R2RML.classProperty,"sClass")),
        OpTgp("sMap",(R2RML.column,"sCol")),
        OpTgp("sMap",(R2RML.template,"sTemp")),
        OpTgp("sMap",(R2RML.termType,"sTerm")),
        OpTgp("sMap",(R2RML.inverseExpression,"sInv")),
        OpTgp("sMap",(R2RML.constant,"sConst"))).groupelement
   
    query.setQueryPattern(group)                                  
                                  	
    logger.debug("Query tripleMap: "+query.serialize);
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
		      		
		val sMap=new SubjectMap(constant,column,template,TermType(termType),sClass,null)
		val pos=readPOMaps(uri)
		val tm=new TriplesMap(uri.getURI,LogicalTable(table,sqlQuery,null),sMap,pos.toArray)
		triplesMaps.put(tm.uri,tm)
		tm
	  }
    }
		//subjectMap.setTriplesMap(tMap);
		//tMap.setSubjectMap(subjectMap );
		      
		      /*
		      readIndexes(tMap);
		      readGraphs(subjectMap);
		      readGraphColumns(subjectMap);
		      readPropertyObjectMap(tMap);
		      readRefPropertyObjectMap(tMap);
		      */
		      //triplesMap.put(uri, tMap);
	
		//this.triplesMap=maps;
	rs.toList	
        }
  }
  
  private def readPOMaps(tMapUri:Node)={
    val query=new Query
    query.setQuerySelectType
    query.addResultVars("predicate","pCol","oConst","oCol","oTemp","odType","gCol","graph","parentTMap")
	val group=
	Group(List(Tgp(tMapUri,(RDF.typeProp,R2RML.TriplesMap),
                           (R2RML.predicateObjectMap,"poMap")),
               Tgp("poMap",(R2RML.predicateMap,"pMap"),
                           (R2RML.objectMap,"oMap"))),
          OpTgp("poMap",(R2RML.graph,"graph")),
          OpTgp("pMap",(R2RML.constant,"predicate")),
          OpTgp("pMap",(R2RML.column,"pCol")),
          OpTgp("oMap",(R2RML.column,"oCol")),
          OpTgp("oMap",(R2RML.constant,"oConst")),
          OpTgp("oMap",(R2RML.template,"oTemp")),
          OpTgp("oMap",(R2RML.termType,"oTerm")),
          OpTgp("oMap",(R2RML.datatype,"odType")),
          OpTgp("oMap",(R2RML.parentTriplesMap,"parentTMap"))
          ).groupelement
   
    query.setQueryPattern(group)                                                                   	
    logger.debug("Query po: "+query.serialize);
    val qexec = QueryExecutionFactory.create(query.serialize, model)
    val results = qexec.execSelect
    results.map{soln=>
      val p=PredicateMap(soln.res("predicate"),soln.lit("pCol"),null)
      if (soln.res("parentTMap")!=null){
        if (!triplesMaps.contains(soln.res("parentTMap").getURI))
          readTriplesMap(soln.res("parentTMap"))
        val ro=RefObjectMap(triplesMaps(soln.res("parentTMap").getURI),null)  
        new PredicateObjectMap(p,ro)
      }
      else{
        val o=ObjectMap(soln.res("oConst"),soln.lit("oCol"),soln.lit("oTemp"),TermType(soln.res("oTerm")),
          null,toDatatype(soln.res("odType")),null)
        new PredicateObjectMap(p,o)
      }
      
    }
  }
  
   
    
}

object R2rmlReader{
  def apply(mappingUrl:String)={
    val r=new R2rmlReader
    r.read(new URI(mappingUrl))
    r
  }
}