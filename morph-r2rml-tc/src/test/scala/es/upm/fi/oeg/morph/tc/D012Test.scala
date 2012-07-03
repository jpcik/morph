package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D012Test extends R2RMLTest("D012-2tables2duplicates0nulls") {

  @Before def initialize() {}
   
  @Test def testTC012a{
	val tc=suit.getTc("R2RMLTC0012a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test def testTC012b{
	val tc=suit.getTc("R2RMLTC0012b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test def testTC012c{
	val tc=suit.getTc("R2RMLTC0012c")
    println(intercept[Exception]{val ds=suit.testTc(tc.get)})
  }
  @Test def testTC012d{
	val tc=suit.getTc("R2RMLTC0012d")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }

  @Test def testTC012e{
	val tc=suit.getTc("R2RMLTC0012e")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (16)
  }

}