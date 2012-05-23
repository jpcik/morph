package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D015Test extends R2RMLTest("D015-1table3columns1composityeprimarykey3rows2languages") {

  @Before def initialize() {}
   
  @Test@Ignore def testTC015a{
	val tc=suit.getTc("R2RMLTC015a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC015b{
	val tc=suit.getTc("R2RMLTC015b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }

}