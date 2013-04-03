package es.upm.fi.oeg.siq.sparql
import com.hp.hpl.jena.sparql.engine.binding.BindingMap
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.sparql.core.Var
import com.hp.hpl.jena.sparql.engine.ResultSetStream
import com.hp.hpl.jena.sparql.resultset.JSONOutput
import collection.JavaConversions._
import com.hp.hpl.jena.sparql.engine.binding.Binding
import com.hp.hpl.jena.sparql.engine.QueryIterator
import com.hp.hpl.jena.shared.PrefixMapping
import org.apache.jena.atlas.io.IndentedWriter
import com.hp.hpl.jena.sparql.serializer.SerializationContext
import com.hp.hpl.jena.graph.Node
import com.hp.hpl.jena.rdf.model.ResourceFactory
import com.hp.hpl.jena.datatypes.RDFDatatype
import com.hp.hpl.jena.sparql.engine.binding.BindingHashMap

class SparqlResults(varNames:List[String],bindings:Iterator[SparqlBinding]) {    
  private val iter=new BindingsIterator(bindings)
    //json.format(System.out,rs)   
    //val json=new JSONOutput

  def getResultSet={
    val m= ModelFactory.createDefaultModel  
    new ResultSetStream(varNames,m,iter)
  }
}

case class SparqlBinding(bindings:Map[String,Node]){
  private val bin=new BindingHashMap
  bindings.foreach(b=>
    bin.add(Var.alloc(b._1),b._2) )
  def toBinding=bin
}


class BindingsIterator(bindings:Iterator[SparqlBinding]) extends QueryIterator{
  def nextBinding=bindings.next.toBinding
  def cancel {}
  def close {}
  def abort{}
  def remove {}
  def hasNext=bindings.hasNext
  def toString(map:PrefixMapping) = ""
  def output(w:IndentedWriter) = {}
  def output(w:IndentedWriter,c:SerializationContext) = {}
  def next=nextBinding
}