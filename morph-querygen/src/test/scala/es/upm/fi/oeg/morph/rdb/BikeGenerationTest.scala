package es.upm.fi.oeg.morph.rdb
import es.upm.fi.oeg.morph.Morph
import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.apache.jena.riot.RDFDataMgr
import org.apache.jena.riot.Lang

class BikeGenerationTest extends FunSpec with Matchers {

  describe("A REST dataset mapped") {
    val morph = Morph.createRestModel   
    val ds = morph.generate("mappings/bikes.ttl")
    //.getDefaultModel.write(System.out,RDFFormat.N3)
    RDFDataMgr.write(System.out, ds.asDatasetGraph,Lang.NQUADS)

  }

}