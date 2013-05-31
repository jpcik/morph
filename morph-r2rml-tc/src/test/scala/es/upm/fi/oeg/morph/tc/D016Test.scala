package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D016Test extends R2RMLTest("D016-1table1primarykey10columns3rowsSQLdatatypes") {

  @Before def initialize() {}
   
  @Test def testTC016a{
	val tc=suit.getTc("R2RMLTC0016a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (15)
  }
  @Test def testTC016b{
	val tc=suit.getTc("R2RMLTC0016b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (9)
  }
  @Test def testTC016c{
	val tc=suit.getTc("R2RMLTC0016c")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (9)
  }
  @Test def testTC016d{
	val tc=suit.getTc("R2RMLTC0016d")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (6)
  }
  @Test@Ignore def testTC016e{
	val tc=suit.getTc("R2RMLTC0016e")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }

}