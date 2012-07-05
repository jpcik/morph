package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test

class D005Test extends R2RMLTest("D005-1table3columns3rows2duplicates") {

  @Before def initialize() {}
  
  @Test def testTC0005a{
	val tc=suit.getTc("R2RMLTC0005a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }

  @Test def testTC0005b{
	val tc=suit.getTc("R2RMLTC0005b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }

}