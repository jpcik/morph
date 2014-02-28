package es.upm.fi.oeg.morph.stack.citybikes

import es.upm.fi.oeg.morph.stack.PeriodicSpout
import backtype.storm.tuple.Values
import dispatch._
import play.api.libs.json.Json._
import play.api.libs.json.Json
import play.api.libs.json.JsArray
import collection.JavaConversions._
import play.api.libs.json.JsString
import akka.actor.Actor

class CitybikesSpout(ids: Seq[String]) extends PeriodicSpout("morph.stack.citybikes") {
  val metadata = config.fields.toArray //Array("id","name","timestamp","bikes","free")
  override def next {
    ids.foreach { id =>
      val r = getData(id)
      //emit(new Values(r._1,r._2,r._3))
    }
  }
  def getAllData = ids.map(getData(_))
  def getData(id: String) = {
    val svc = url("http://api.citybik.es/" + id + ".json")
    val res = Http(svc OK as.String)
    (extract(res()))
  }

  def extract(string: String) = {

    val json = Json.parse(string).as[JsArray]
    json.value.map { js =>
      metadata.map(key => (js \ key) match {
        case st: JsString => st.value
        case p => p.toString
      })
    }

  }
}
