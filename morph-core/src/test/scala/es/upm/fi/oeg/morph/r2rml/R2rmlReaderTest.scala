package es.upm.fi.oeg.morph.r2rml
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.prop.Checkers
import java.net.URI
import org.scalatest.FunSpec
import org.scalatest.Matchers

class R2rmlReaderTest extends FunSpec with Matchers  {
  
  describe("A Mapping"){
    describe("when read") {
      val reader=new R2rmlReader("mappings/bikes.ttl")
      it("should have a subjectmap"){
        reader.tMaps.foreach(tMap=>tMap.subjectMap should not be null)         
      }
      it("should have two tmaps"){
        reader.tMaps.size shouldBe 2
      }
      it("TripleMap1 should have two pomaps"){
        reader.triplesMaps("http://mappingpedia.org/rdb2rdf/r2rml/tc/TriplesMap1").poMaps.size shouldBe 2
      }
      it("TripleMap2 should have 1 pomaps"){
        reader.triplesMaps("http://mappingpedia.org/rdb2rdf/r2rml/tc/TriplesMap2").poMaps.size shouldBe 1
      }

    }

           
  }
}