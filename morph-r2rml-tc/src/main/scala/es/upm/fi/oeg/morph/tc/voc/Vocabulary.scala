package es.upm.fi.oeg.morph.tc.voc
import com.hp.hpl.jena.rdf.model.ResourceFactory
import com.hp.hpl.jena.rdf.model.Resource
import es.upm.fi.oeg.morph.voc.Vocabulary



object Rdb2RdfTest extends Vocabulary{
  override val prefix="http://purl.org/NET/rdb2rdf-test#"
  val sqlScriptFile=property("sqlScriptFile")
  val relatedTestCase=property("relatedTestCase")
  val mappingDocument=property("mappingDocument")
  val hasExpectedOutput=property("hasExpectedOutput")
  val output=property("output")
  val dataBase=resource("DataBase")
  val r2rml=resource("R2RML")  
}

object DCTerms extends Vocabulary{
  override val prefix="http://purl.org/dc/elements/1.1/"
  val identifier=property("identifier")
}


 