package es.upm.fi.oeg.morph.voc
import com.hp.hpl.jena.rdf.model.ResourceFactory
import scala.language.implicitConversions

abstract class Vocabulary {
  val prefix:String
  def property(local:String)={ 
		ResourceFactory.createProperty(prefix,local)}
  def resource(local:String)={ 
		ResourceFactory.createResource(prefix+local)}

}

object RDF extends Vocabulary{
  override val prefix="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  val typeProp=property("type")
  val a=typeProp
}


object RDFFormat extends Enumeration{
  type RDFFormat=Value
  val TTL,N3=Value
  implicit def getString(v:RDFFormat.Value)=v.toString
}
