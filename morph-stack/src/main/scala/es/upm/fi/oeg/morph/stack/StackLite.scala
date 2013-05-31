package es.upm.fi.oeg.morph.stack

import akka.actor.Actor
import akka.actor.Props
import scala.concurrent.duration._
import es.upm.fi.oeg.morph.stack.citybikes.CitybikesSpout
import akka.actor.ActorSystem
import es.upm.fi.oeg.siq.tools.ParameterUtils

class StackLite{
  val spout=new CitybikesSpout(Seq("sevici","valenbisi"))
  val props=ParameterUtils.load(getClass.getResourceAsStream("/config/morph.properties"))
  val system = ActorSystem("StackLite")
  val deleteGraph=props.getProperty("morph.stack.deleteGraph")
    
  def start={
    val rdfexp = system.actorOf(Props(new ExportRdfActor(props)), name ="rdfexp")
    val rdfpurge = system.actorOf(Props(new PurgeRdfActor(props)), name ="rdfpurge")
    import system.dispatcher
    system.scheduler.schedule(0 seconds,10 seconds){
      spout.getAllData.foreach(d=>rdfexp ! (d.toStream,spout.metadata))
    }
    system.scheduler.schedule(0 seconds,90 seconds){
      rdfpurge ! DeleteGraph(deleteGraph)
    }   
  }

}

object StackLite{
  def main(args:Array[String]):Unit={
    new StackLite().start
  }
}