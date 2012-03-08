package es.upm.fi.oeg.morph.tc.voc
import com.hp.hpl.jena.rdf.model.ResourceFactory
import com.hp.hpl.jena.rdf.model.Resource

abstract class Vocabulary {
  val prefix:String
  def property(local:String)={ 
		ResourceFactory.createProperty(prefix,local)}
  def resource(local:String)={ 
		ResourceFactory.createResource(prefix+local)}

}

object Rdb2RdfTest extends Vocabulary{
  override val prefix="http://purl.org/NET/rdb2rdf-test#"
  val sqlScriptFile=property("sqlScriptFile")
  val relatedTestCase=property("relatedTestCase")
  val mappingDocument=property("mappingDocument")
  val dataBase=resource("DataBase")
  val r2rml=resource("R2RML") 	
}

object DCTerms extends Vocabulary{
  override val prefix="http://purl.org/dc/elements/1.1/"
  val identifier=property("identifier")
}

object RDF extends Vocabulary{
  override val prefix="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  val typeProp=property("type")
}
 