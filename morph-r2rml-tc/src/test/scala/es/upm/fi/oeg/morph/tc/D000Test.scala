package es.upm.fi.oeg.morph.tc

import collection.JavaConversions._
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.prop.Checkers
import org.scalatest.junit.JUnitSuite
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

abstract class R2RMLTest(val name:String) extends JUnitSuite with ShouldMatchersForJUnit with Checkers {
  val testPath="./test-suite"
  val suit=new SuiteTester(testPath,name)
}


class D000Test extends R2RMLTest("D000-1table1column0rows") {

  @Before def initialize() {
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
