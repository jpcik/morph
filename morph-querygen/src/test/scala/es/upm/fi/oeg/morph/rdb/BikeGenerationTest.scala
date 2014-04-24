package es.upm.fi.oeg.morph.rdb
import es.upm.fi.oeg.morph.voc.RDFFormat
import es.upm.fi.oeg.morph.db.RelationalModel
import es.upm.fi.oeg.morph.Morph
import org.apache.jena.riot.RiotWriter
import org.scalatest.FunSpec
import org.scalatest.Matchers

class BikeGenerationTest extends FunSpec with Matchers {

  describe("A REST dataset mapped") {
    val morph = Morph.createRestModel   
    val ds = morph.generate("mappings/bikes.ttl")
    //.getDefaultModel.write(System.out,RDFFormat.N3)
    RiotWriter.writeNQuads(System.out, ds.asDatasetGraph)
  }

}