package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D011Test  extends R2RMLTest("D011-M2MRelations") {

  @Before def initialize() {}
   
  @Test@Ignore def testTC011a{
	val tc=suit.getTc("R2RMLTC011a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC011b{
	val tc=suit.getTc("R2RMLTC011b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }

}