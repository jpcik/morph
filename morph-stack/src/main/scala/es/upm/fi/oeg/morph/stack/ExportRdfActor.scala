package es.upm.fi.oeg.morph.stack

import akka.actor.Actor
import java.util.Properties
import collection.JavaConversions._

class ExportRdfActor(id:String,props:Properties) extends Actor {
  type DataTuples=(Seq[Array[Any]],Array[String])
  val rdfExp=new ExportRdfBolt(id,props)
  rdfExp.configure
  
  def receive={
    case (d:DataTuples,s:String) =>
      rdfExp.sparqlDelete(s)
      rdfExp.sparqlUpdate(d._1, d._2)
    case d:DataTuples=>
      println(d._1)
      rdfExp.sparqlUpdate(d._1, d._2)
    case _=> println("unknown!")
  }
}