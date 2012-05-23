package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D020Test extends R2RMLTest("D020-1table1column5rows") {

  @Before def initialize() {}
   
  @Test@Ignore def testTC020a{
	val tc=suit.getTc("R2RMLTC020a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC020b{
	val tc=suit.getTc("R2RMLTC020b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }

}