package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D014Test extends R2RMLTest("D014-3tables1primarykey1foreignkey") {

  @Before def initialize() {}
   
  @Test def testTC014a{
	val tc=suit.getTc("R2RMLTC0014a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
  @Test def testTC014b{
	val tc=suit.getTc("R2RMLTC0014b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (14)
  }
  @Test def testTC014c{
	val tc=suit.getTc("R2RMLTC0014c")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (14)
  }
  @Test def testTC014d{
	val tc=suit.getTc("R2RMLTC0014d")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
}