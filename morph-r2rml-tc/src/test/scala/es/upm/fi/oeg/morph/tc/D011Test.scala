package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D011Test  extends R2RMLTest("D011-M2MRelations") {

  @Before def initialize() {}
   
  @Test def testTC011a{
	val tc=suit.getTc("R2RMLTC0011a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (19)
  }
  @Test def testTC011b{
	val tc=suit.getTc("R2RMLTC0011b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (16)
  }

}