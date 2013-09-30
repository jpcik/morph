package es.upm.fi.oeg.morph.stack

import akka.actor.Actor
import akka.actor.Props
import scala.concurrent.duration._
import es.upm.fi.oeg.morph.stack.citybikes.CitybikesSpout
import akka.actor.ActorSystem
import es.upm.fi.oeg.siq.tools.ParameterUtils
import es.upm.fi.oeg.morph.stack.emt.EmtStreamer
import akka.pattern.{ ask, pipe }
import akka.util.Timeout


class StackLite{
  //val spout=new CitybikesSpout(Seq("sevici","valenbisi"))
  val props=ParameterUtils.load(getClass.getResourceAsStream("/config/morph.properties"))
  val system = ActorSystem("StackLite")
  val deleteGraph=props.getProperty("morph.stack.deleteGraph")
  val streamerIds=Array("30","31","5459","5602","1486","5603","5604","29","3568","5605","5606")
  
  def start={
    val rdfexp = system.actorOf(Props(new ExportRdfActor("emt",props)), name ="rdfexp")
    //val rdfpurge = system.actorOf(Props(new PurgeRdfActor(props)), name ="rdfpurge")
    //implicit val timeout = Timeout(5 seconds) // needed for `?` below

    val streamers=streamerIds.map(id=>new EmtStreamer(id))
    

    val stops=streamers.head.getStops(441662, 4479735)
    rdfexp ! (stops,Array("stopId","name","lat","long"))
    /*
    import system.dispatcher
    system.scheduler.schedule(0 seconds,60 seconds){  
      
     //println("hacking")
      streamers.foreach{str=>
        rdfexp ! ((str.getData,str.getNames),str.deleteExpression)
      }
    }*/
    /*
    system.scheduler.schedule(0 seconds,90 seconds){
      rdfpurge ! DeleteGraph(deleteGraph)
    } */  
  }

}

object StackLite{
  def main(args:Array[String]):Unit={
    new StackLite().start
  }
}