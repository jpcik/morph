package es.upm.fi.oeg.morph.tc

import java.io.FileOutputStream
import java.util.zip.ZipFile
import scala.collection.JavaConversions._
import org.scalatest.FlatSpec
import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitSuite
import java.io.File

abstract class R2RMLTest(val name:String) extends FlatSpec with Matchers  {
  val testPath=
    if (new File("test-suite").exists) "test-suite"
    else "../test-suite"
  val suit=new SuiteTester(testPath,name)
 
  def generate(tcName:String)={
    val tc=suit.getTc(tcName)	
	val ds=suit.testTc(tc.get)
	ds.asDatasetGraph
  }
  
  private def downloadSuite(){
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

  //  downloadSuite    
  "TC0000" should "be empty" in {
	val dg=generate("R2RMLTC0000")
	dg.listGraphNodes.size should be (0)
	dg.getDefaultGraph.size should be (0)
  }
}
