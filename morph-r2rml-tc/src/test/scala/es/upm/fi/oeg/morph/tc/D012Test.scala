package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D012Test extends R2RMLTest("D012-2tables2duplicates0nulls") {

  @Before def initialize() {}
   
  @Test@Ignore def testTC012a{
	val tc=suit.getTc("R2RMLTC012a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC012b{
	val tc=suit.getTc("R2RMLTC012b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC012c{
	val tc=suit.getTc("R2RMLTC012c")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC012d{
	val tc=suit.getTc("R2RMLTC012d")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }

}