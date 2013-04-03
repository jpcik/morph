package es.upm.fi.oeg.morph.stack

import dispatch._
import backtype.storm.topology.base.BaseRichSpout
import backtype.storm.spout.SpoutOutputCollector
import collection.JavaConversions._
import backtype.storm.task.TopologyContext
import collection.JavaConverters._
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.tuple.Fields
import backtype.storm.utils.Utils
import backtype.storm.tuple.Values
import backtype.storm.topology.base.BaseBasicBolt
import backtype.storm.topology.BasicOutputCollector
import backtype.storm.tuple.Tuple
import backtype.storm.task.OutputCollector
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.execute.RdfGenerator
import es.upm.fi.oeg.morph.relational.DataResultSet
import org.openjena.riot.RiotWriter
import backtype.storm.topology.IRichBolt
import backtype.storm.topology.base.BaseRichBolt
import com.hp.hpl.jena.query.Dataset
import java.io.StringWriter
import es.upm.fi.oeg.morph.voc.RDFFormat
import com.hp.hpl.jena.update.UpdateFactory
import com.hp.hpl.jena.sparql.modify.UpdateProcessRemote

class WundergroundSpout(ids:Array[String]) extends BaseRichSpout{  
  var _collector:SpoutOutputCollector=_
  
  override def open(conf:java.util.Map[_,_],context:TopologyContext,collector:SpoutOutputCollector){
    _collector=collector;
  }  
  override def nextTuple{
    Utils.sleep(5000)
    ids.foreach{id=>
      val r=getData(id)
      _collector.emit(new Values(r._1,r._2,r._3))
    }
  }
  
  override def ack(id:Object){}
  override def fail(id:Object){}
  override def declareOutputFields(declarer:OutputFieldsDeclarer){
    declarer.declare(new Fields("name","temp","timed"))
  }  
  
  def getData(id:String)={
    val svc = url("http://api.wunderground.com/weatherstation/WXCurrentObXML.asp")
      .addQueryParameter("ID",id)
    val res = Http(svc OK as.xml.Elem)
    (extract(res()))
  }
  
  def extract(xml:scala.xml.Elem)={
    val timing= xml \ "observation_time_rfc822" text
    val stat = xml \"station_type" text
    val temp = (xml \"temp_c").text
    val neigb=((xml\"location").head \"neighborhood").text
    (timing,neigb,temp)
  }
}




class PrinterBolt extends BaseBasicBolt {
  
  override def execute(tuple:Tuple, collector:BasicOutputCollector ) {
    println(tuple)
  }

  override def declareOutputFields(ofd:OutputFieldsDeclarer) {}    
}

class RdfExportBolt extends BaseRichBolt {
  private var _collector:OutputCollector=_ 
  private var r2rml:R2rmlReader=_ //R2rmlReader("mappings/data.ttl")
  private var gen:RdfGenerator=_ //new RdfGenerator(r2rml,null)
  
  override def prepare(conf:java.util.Map[_,_], context:TopologyContext, collector:OutputCollector ) {
    _collector = collector
    r2rml=R2rmlReader("mappings/data.ttl")
    gen= new RdfGenerator(r2rml,null)
  
  }
  
  private def update(ds:Dataset) {    
    val graphs=ds.listNames.toArray
    val sw=new StringWriter
    val str=graphs.map{g=>
      sw.append("GRAPH <"+g+"> { ")
      ds.getNamedModel(g).write(sw,RDFFormat.TTL)
      sw.append("} ")
    }
    ds.getDefaultModel.write(sw,RDFFormat.TTL)    
    val update = "http://localhost:3030/ds/update";
    val query = "INSERT DATA  { "+ sw.toString +" }"
    println (query)
    val u = UpdateFactory.create
     u.add(query)
     val p = new UpdateProcessRemote(u, update,null)
     p.execute
    
  }
  
  override def execute(tuple:Tuple){ //, collector:BasicOutputCollector ) {
    val names:Array[String]=tuple.getFields().map(f=>f).toArray
    val res=new DataResultSet(Stream(tuple.getValues().toArray),names.map(a=>a->a).toMap,names  )
    val ds=gen.generate(res)        
    update(ds)
        
    RiotWriter.writeNQuads(System.out,ds.asDatasetGraph)
  }

  override def declareOutputFields(ofd:OutputFieldsDeclarer) {}    
}