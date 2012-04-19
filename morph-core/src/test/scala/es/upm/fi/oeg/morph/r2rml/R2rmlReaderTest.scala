package es.upm.fi.oeg.morph.r2rml
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.prop.Checkers
import org.junit.Before
import org.junit.Test
import java.net.URI

class R2rmlReaderTest extends JUnitSuite with ShouldMatchersForJUnit with Checkers {
  @Before def initialize() {}
  
  @Test def testRead(){
    val reader=new R2rmlReader
    reader.read(new URI("mappings/bikes.ttl"))
    reader.tMaps.foreach(t=>println(t.uri +" "+t.poMaps.size))
    
  }
}