package es.upm.fi.oeg.morph.stack

import akka.actor.Actor
import java.util.Properties
import collection.JavaConversions._

class ExportRdfActor(props:Properties) extends Actor {
  type DataTuples=(Stream[Array[Object]],Array[String])
  val rdfExp=new ExportRdfBolt(props)
  rdfExp.configure
  
  def receive={
    case d:DataTuples=>rdfExp.sparqlUpdate(d._1, d._2)
    case _=> println("unknown!")
  }
}