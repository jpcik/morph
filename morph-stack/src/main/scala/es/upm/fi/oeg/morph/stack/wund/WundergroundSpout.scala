package es.upm.fi.oeg.morph.stack.wund

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
import backtype.storm.topology.base.BaseRichBolt
import com.hp.hpl.jena.query.Dataset
import java.io.StringWriter
import es.upm.fi.oeg.morph.voc.RDFFormat
import com.hp.hpl.jena.update.UpdateFactory
import com.hp.hpl.jena.sparql.modify.UpdateProcessRemote
import es.upm.fi.oeg.morph.relational.RestRelationalModel
import es.upm.fi.oeg.morph.voc.RDFFormat.getString
import scala.Array.canBuildFrom
import es.upm.fi.oeg.morph.stack.PeriodicSpout

class WundergroundSpout(ids:Array[String]) extends PeriodicSpout("morph.stack.wund"){  
  override def next{
    ids.foreach{id=>
      val r=getData(id)
      emit(new Values(r._1,r._2,r._3))
    }
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




