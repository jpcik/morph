package es.upm.fi.oeg.morph.stack

import backtype.storm.topology.base.BaseRichSpout
import backtype.storm.spout.SpoutOutputCollector
import backtype.storm.task.TopologyContext
import backtype.storm.utils.Utils
import backtype.storm.tuple.Values
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.tuple.Fields
import collection.JavaConversions._
import es.upm.fi.oeg.siq.tools.ParameterUtils
import backtype.storm.topology.base.BaseBasicBolt
import backtype.storm.tuple.Tuple
import backtype.storm.topology.BasicOutputCollector

case class SpoutConfig(rate:Long,fields:Seq[String])
object SpoutConfig{
  def apply(id:String)={
    val props=ParameterUtils.load(getClass.getResourceAsStream("/config/morph.properties"))
    new SpoutConfig(props.getProperty(id+".rate").toLong,
      props.getProperty(id+".fields").split(",").toSeq)  
  }
}

class PeriodicSpout(id:String) extends BaseRichSpout{  
  private var _collector:SpoutOutputCollector=_
  val config=SpoutConfig(id)
  
  override def open(conf:java.util.Map[_,_],context:TopologyContext,collector:SpoutOutputCollector){
    _collector=collector;
  }  
  override def nextTuple{
    Utils.sleep(config.rate)
    next
  }
  def next{}
  protected def emit(values:Values)=_collector.emit(values)
  override def ack(id:Object){}
  override def fail(id:Object){}
  override def declareOutputFields(declarer:OutputFieldsDeclarer){
    declarer.declare(new Fields(config.fields))
  }  
  
}

class PrinterBolt extends BaseBasicBolt {
  
  override def execute(tuple:Tuple, collector:BasicOutputCollector ) {
    println(tuple)
  }

  override def declareOutputFields(ofd:OutputFieldsDeclarer) {}    
}