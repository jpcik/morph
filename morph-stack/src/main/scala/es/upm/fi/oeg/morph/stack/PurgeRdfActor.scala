package es.upm.fi.oeg.morph.stack

import com.hp.hpl.jena.update.UpdateFactory
import com.hp.hpl.jena.sparql.modify.UpdateProcessRemote
import java.util.Properties
import akka.actor.Actor

class PurgeRdfActor(props:Properties) extends Actor {
  val sparqlUpdateUrl=props.getProperty("morph.stack.sparqlUpdate")
  def receive={
    case d:DeleteGraph=>sparqlDeleteGraph(d.graph)
    case d:DeleteData=>sparqlDelete(d.delete)
    case _=> println("unknown!")
  }
  private def sparqlDeleteGraph(graphUri:String)={
    val query = "DELETE WHERE { GRAPH  <"+graphUri+">{ ?s ?p ?o }}"
    println (query)
    val u = UpdateFactory.create
    
     u.add(query)
     val p = new UpdateProcessRemote(u, sparqlUpdateUrl,null)
     p.execute
  }
  private def sparqlDelete(delete:String)={
    //val query = "DELETE WHERE { GRAPH  <"+graphUri+">{ ?s ?p ?o }}"
    println ("trasgps "+delete)
    val u = UpdateFactory.create
                  println("fsfsllllldft all deleted")

     u.add(delete)
              println("fsfsdft all deleted")

     val p = new UpdateProcessRemote(u, sparqlUpdateUrl,null)
         println("not all deleted")

     p.execute
     println("all deleted")
  }
}
case class DeleteGraph(graph:String)
case class DeleteData(delete:String)