package es.upm.fi.oeg.morph.tc

import java.io.FileInputStream
import collection.JavaConversions._
import com.hp.hpl.jena.rdf.model._
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.QueryExecution
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QuerySolution
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.graph.Node
import com.hp.hpl.jena.graph.Triple
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock
import es.upm.fi.oeg.morph.tc.voc.Rdb2RdfTest
import es.upm.fi.oeg.morph.tc.voc.RDF
import com.hp.hpl.jena.sparql.core.TriplePath
import es.upm.fi.oeg.morph.tc.voc.DCTerms

object RDFFormat extends Enumeration("TURTLE","N3"){
  type RDFFormat=Value
  val TTL,N3=Value
  implicit def getString(v:RDFFormat.Value)=v.toString
}


class Database(val id:String,val title:String,
    val scriptFile:String,val testcases:Array[TestCase])

class TestCase(val id:String, val title:String, val mappingDoc:String)

class Manifest(val name:String,val database:Database) {
  }

trait Sparql{
  implicit def str2Node(s:String)=Node.createVariable(s)
  implicit def res2Node(r:Resource)=r.asNode
  implicit def prop2Node(p:Property)=p.asNode
  implicit def etb2tp(e:ElementTriplesBlock)=new TriplePattern(e)
  implicit def query2sp(q:Query)=new SparqlQuery(q)
  
  case class TripleGraph(s:Node,po:(Node,Node)*){
    def list=po.map(a=>new Triple(s,a._1,a._2))
  }
  class TriplePattern(val e:ElementTriplesBlock){
    def add(tg:TripleGraph)=tg.list.foreach(e.addTriple(_))
  }  
  case class SparqlQuery(q:Query){
    def add(tg:TripleGraph)=Unit
    def addResultVars(vars:String*)=vars.foreach(q.addResultVar(_))
  }  
}

object Manifest extends Sparql{
  val prefixes="prefix test: <http://www.w3.org/2006/03/test-description#> "
    
  val queryt=new Query  
  queryt.setQuerySelectType
  queryt.addResultVars("db","id","script","tc","tcid","mapping")
  val block=new ElementTriplesBlock
  block.add(TripleGraph("db",(RDF.typeProp,Rdb2RdfTest.dataBase),
                             (DCTerms.identifier,"id"),
                             (Rdb2RdfTest.sqlScriptFile,"script"),
                             (Rdb2RdfTest.relatedTestCase,"tc")))
  block.add(TripleGraph("tc",(RDF.typeProp,Rdb2RdfTest.r2rml),
                             (DCTerms.identifier,"tcid"),
                             (Rdb2RdfTest.mappingDocument,"mapping")))
  queryt.setQueryPattern(block)
  /*
  def iterator(rs:ResultSet)=new Iterator[QuerySolution]{
    override def next=rs.next
    override def hasNext=rs.hasNext
  }  */
               
  def apply(path:String,name:String)={
    val m=ModelFactory.createDefaultModel
    val reader=m.getReader(RDFFormat.TTL)
    reader.read(m,new FileInputStream(path),"")
	val qexec = QueryExecutionFactory.create(queryt.serialize, m) 
	val results = qexec.execSelect.toList

	val tcases=results.map(qs=>
	    new TestCase("",qs.getLiteral("tcid").getString,qs.getLiteral("mapping").getString))
	println(tcases.length)
	val db=new Database(results.head.getLiteral("id").getString,"",
	    results.head.getLiteral("script").getString,tcases.toArray)
	new Manifest(name,db)  
  }
		
  
  
}

