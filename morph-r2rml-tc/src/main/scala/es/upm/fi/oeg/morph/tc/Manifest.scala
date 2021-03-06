package es.upm.fi.oeg.morph.tc

import java.io.FileInputStream

import scala.collection.JavaConversions.asScalaIterator

import org.slf4j.LoggerFactory

import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.rdf.model.ModelFactory

import es.upm.fi.oeg.morph.tc.voc.DCTerms
import es.upm.fi.oeg.morph.tc.voc.Rdb2RdfTest
import es.upm.fi.oeg.morph.voc.RDF
import es.upm.fi.oeg.morph.voc.RDFFormat
import es.upm.fi.oeg.morph.voc.RDFFormat.getString
import es.upm.fi.oeg.siq.sparql.Sparql

class Database(val id:String,val title:String,
    val scriptFile:String,val testcases:Array[TestCase])

class TestCase(val id:String, val title:String, val mappingDoc:String, 
    val hasOutput:Boolean,val output:String)

class Manifest(val name:String,val database:Database) 

object Manifest extends Sparql{
  
  val logger = LoggerFactory.getLogger(classOf[Manifest]) 

  val tgs=Group(
    ^("db",(RDF.typeProp,Rdb2RdfTest.dataBase),
           (DCTerms.identifier,"id"),
           (Rdb2RdfTest.sqlScriptFile,"script"),
           (Rdb2RdfTest.relatedTestCase,"tc")),
    ^("tc",(RDF.typeProp,Rdb2RdfTest.r2rml),
           (DCTerms.identifier,"tcid"),
           (Rdb2RdfTest.mappingDocument,"mapping"),
           //(Rdb2RdfTest.output,"output"),
           (Rdb2RdfTest.hasExpectedOutput,"hasoutput")),
    Optional("tc",Rdb2RdfTest.output,"output"))
    
  val queryt=SelectSparqlQuery(tgs,Array("db","id","script","tc","tcid","mapping","hasoutput","output"))
               
  def apply(path:String,name:String)={
    val m=ModelFactory.createDefaultModel
    val reader=m.getReader(RDFFormat.TTL)
    reader.read(m,new FileInputStream(path),"")
	val qexec = QueryExecutionFactory.create(queryt.serialize, m) 
	val results = qexec.execSelect.toList
	val tcases=results.map(qs=>
	    new TestCase(qs.getLiteral("tcid").getString,"",qs.getLiteral("mapping"),
	        qs.getLiteral("hasoutput").getBoolean,qs.getLiteral("output")))
	logger.debug(tcases.length.toString)

    if (results.size>0){ 
  	  val db=new Database(results.head.getLiteral("id").getString,"",
	      results.head.getLiteral("script").getString,tcases.toArray)
	  new Manifest(name,db)
    }
    else new Manifest(name,null)
  }		
  
  
}

