package es.upm.fi.oeg.morph.stack

import java.util.Properties
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.task.OutputCollector
import es.upm.fi.oeg.morph.execute.RdfGenerator
import backtype.storm.task.TopologyContext
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.relational.RestRelationalModel
import com.hp.hpl.jena.query.Dataset
import java.io.StringWriter
import es.upm.fi.oeg.morph.voc.RDFFormat
import com.hp.hpl.jena.update.UpdateFactory
import com.hp.hpl.jena.sparql.modify.UpdateProcessRemote
import backtype.storm.tuple.Tuple
import es.upm.fi.oeg.morph.relational.DataResultSet
import org.apache.jena.riot.RiotWriter
import backtype.storm.topology.OutputFieldsDeclarer
import collection.JavaConversions._

class ExportRdfBolt (id:String,props:Properties) extends BaseRichBolt {
  private var _collector:OutputCollector=_ 
  private var gen:RdfGenerator=_
  private val mapping=props.getProperty("morph.stack."+id+".mapping")
  private val sparqlUpdateUrl=props.getProperty("morph.stack.sparqlUpdate")
  
  def configure={
    val r2rml=R2rmlReader(mapping)    
    gen= new RdfGenerator(r2rml,new RestRelationalModel,"http://example.com/id#")
    
    
  }
  override def prepare(conf:java.util.Map[_,_], context:TopologyContext, collector:OutputCollector ) {
    _collector = collector
    configure
  }
  
  private def update(ds:Dataset) {    
    val graphs=ds.listNames.toArray
    val sw=new StringWriter
    graphs.map{g=>
      sw.append("GRAPH <"+g+"> { ")
      ds.getNamedModel(g).write(sw,RDFFormat.TTL)
      sw.append("} ")
    }
    ds.getDefaultModel.write(sw,RDFFormat.TTL)    
   //"http://localhost:3030/ds/update";
    val query = "INSERT DATA  { "+ sw.toString +" }"
    println (query)
    val u = UpdateFactory.create
     u.add(query)
     val p = new UpdateProcessRemote(u, sparqlUpdateUrl,null)
     p.execute
    
  }
  
  def sparqlDelete(delete:String)={
    println(delete)
    val u = UpdateFactory.create
     u.add(delete)
     val p = new UpdateProcessRemote(u, sparqlUpdateUrl,null)
     p.execute
    
  }
  
  def sparqlUpdate(values:Seq[Array[Any]],names:Seq[String])={
    val res=new DataResultSet(values,names.map(a=>a->a).toMap,names.toArray)
    val ds=gen.generate(res)        
    update(ds)        
    RiotWriter.writeNQuads(System.out,ds.asDatasetGraph)
  }

  
  override def execute(tuple:Tuple){ 
    val names:Seq[String]=tuple.getFields().map(f=>f).toSeq
    val anyValues=tuple.getValues.asInstanceOf[List[Any]]
    sparqlUpdate(Seq(anyValues.toArray), names)
  }

  override def declareOutputFields(ofd:OutputFieldsDeclarer) {}    
}


