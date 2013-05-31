package es.upm.fi.oeg.morph.stack

import com.hp.hpl.jena.update.UpdateFactory
import com.hp.hpl.jena.sparql.modify.UpdateProcessRemote
import java.util.Properties
import akka.actor.Actor

class PurgeRdfActor(props:Properties) extends Actor {
  val sparqlUpdateUrl=props.getProperty("morph.stack.sparqlUpdate")
  def receive={
    case d:DeleteGraph=>sparqlDelete(d.graph)
    case _=> println("unknown!")
  }
  private def sparqlDelete(graphUri:String)={
    val query = "DELETE WHERE { GRAPH  <"+graphUri+">{ ?s ?p ?o }}"
    println (query)
    val u = UpdateFactory.create
     u.add(query)
     val p = new UpdateProcessRemote(u, sparqlUpdateUrl,null)
     p.execute
  }
}
case class DeleteGraph(graph:String)