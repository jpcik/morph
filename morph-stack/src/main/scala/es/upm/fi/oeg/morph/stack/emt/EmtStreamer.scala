package es.upm.fi.oeg.morph.stack.emt

import es.upm.fi.oeg.morph.stack.PeriodicSpout
import es.upm.fi.oeg.morph.stack.PeriodicStreamer
import dispatch._
import org.w3c.dom.Text
import org.joda.time.DateTime

class EmtStreamer(stopId:String) extends PeriodicStreamer{
  val names=Array("stopId","lineId","destination","timeLeft","distanceLeft","timestamp")
  
  def getStops(coordX:Long,coordY:Long)={
    val svc = url("https://servicios.emtmadrid.es:8443/geo/servicegeo.asmx/getStopsFromXY")
      .addQueryParameter("idClient","")
      .addQueryParameter("passKey", "")
      .addQueryParameter("coordinateX", coordX.toString)
      .addQueryParameter("coordinateY", coordY.toString)
      .addQueryParameter("Radius", "3000")
      .addQueryParameter("statistics", " ").addQueryParameter("cultureInfo", " ")    
    val xml = Http(svc OK as.xml.Elem)
    
    
    val arrives= xml() \\ "Stop" 
    val data=arrives.map{arr=>
      Array((arr \ "IdStop") text,(arr \ "Name") text,
          (arr \ "CoordinateY").text.toDouble,(arr \ "CoordinateX").text.toDouble)
    }
    println("datas "+ data.mkString("\r"))
    data
  }
  
  override def getNames=names
  override def getData={
      val svc = url("https://servicios.emtmadrid.es:8443/geo/servicegeo.asmx/getArriveStop")
      .addQueryParameter("idClient","WEB.SERVICIOS.FIUPM")
      .addQueryParameter("passKey", "")
      .addQueryParameter("idStop", stopId)
      .addQueryParameter("statistics", " ").addQueryParameter("cultureInfo", " ")    
    val res = Http(svc OK as.xml.Elem)
    extract(res())
  }

  private def extract(xml:scala.xml.Elem)={
    val arrives= xml \\ "Arrive" 
    val data=arrives.map{arr=>
      Array(stopId,(arr \ "idLine") text,(arr \ "Destination") text,
          (arr \ "TimeLeftBus").text.toInt,(arr \ "DistanceBus").text.toInt,DateTime.now.toDate)
    }
    println("datas "+ data.mkString("\r"))
    data
  }
  
  val deleteExpression={
    val stopUri="<http://transporte.linkeddata.es/emt/busstop/id/"+stopId+">"
    "PREFIX emt: <http://transporte.linkeddata.es/emt#> " +
    "DELETE {?o ?p ?v} WHERE{ ?o ?p ?v; emt:busStop "+stopUri+" } "
  }

}