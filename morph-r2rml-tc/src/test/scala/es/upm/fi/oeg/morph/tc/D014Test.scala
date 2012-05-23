package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D014Test extends R2RMLTest("D014-3tables1primarykey1foreignkey") {

  @Before def initialize() {}
   
  @Test@Ignore def testTC014a{
	val tc=suit.getTc("R2RMLTC014a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC014b{
	val tc=suit.getTc("R2RMLTC014b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC014c{
	val tc=suit.getTc("R2RMLTC014c")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC014d{
	val tc=suit.getTc("R2RMLTC014d")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
}