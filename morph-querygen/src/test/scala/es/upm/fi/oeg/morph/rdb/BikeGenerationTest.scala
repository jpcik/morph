package es.upm.fi.oeg.morph.rdb
import com.hp.hpl.jena.rdf.model.ModelFactory
import java.io.InputStream
import java.util.Properties
import es.upm.fi.oeg.morph.execute.RdfGenerator
import es.upm.fi.oeg.morph.voc.RDFFormat
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import java.net.URI
import es.upm.fi.oeg.morph.relational.JDBCRelationalModel
import es.upm.fi.oeg.morph.relational.RestRelationalModel
import es.upm.fi.oeg.morph.relational.RelationalModel
import es.upm.fi.oeg.morph.Morph
import org.apache.jena.riot.RiotWriter
import org.scalatest.FunSpec
import org.scalatest.Matchers

class BikeGenerationTest extends FunSpec with Matchers {

  describe("A REST dataset mapped") {
    val morph = new Morph
    val ds = morph.generateRest("mappings/bikes.ttl")
    //.getDefaultModel.write(System.out,RDFFormat.N3)
    RiotWriter.writeNQuads(System.out, ds.asDatasetGraph)
  }

}