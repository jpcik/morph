package es.upm.fi.oeg.morph.tc

import collection.JavaConversions._
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.prop.Checkers
import org.scalatest.junit.JUnitSuite
import org.junit.Before
import org.junit.Test
import org.junit.Assert._
import java.util.zip.ZipFile
import java.io.File
import scala.io.Source
import java.io.FileOutputStream
import java.io.FileWriter

abstract class R2RMLTest(val name:String) extends JUnitSuite with ShouldMatchersForJUnit with Checkers {
  val testPath="../test-suite"
  val suit=new SuiteTester(testPath,name)
  
  def downloadSuite(){
    val path="http://dvcs.w3.org/hg/rdb2rdf-tests/archive/tip.zip"
    val zip=new ZipFile(path)
    
    zip.entries.foreach{e=>
      //val s=Source.fromInputStream(zip.getInputStream(e))
      val input=zip.getInputStream(e)
      val fos=new FileOutputStream("/home/jpc/temp/"+e.getName)
      var read= -1
      while ({read = input.read; read != -1}) {
        fos.write(read)
      }
      fos.close
    }
  }
  
}

class D000Test extends R2RMLTest("D000-1table1column0rows") {

  @Before def initialize() {
  //  downloadSuite
  }
  
  @Test def testTC0000{
	val tc=suit.getTc("R2RMLTC0000")
	tc.isDefined should be (true)
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.listGraphNodes.size should be (0)
	dg.getDefaultGraph.size should be (0)
  }
}
