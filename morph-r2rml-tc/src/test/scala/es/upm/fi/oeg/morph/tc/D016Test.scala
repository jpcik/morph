package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D016Test extends R2RMLTest("D016-1table1primarykey10columns3rowsSQLdatatypes") {

  @Before def initialize() {}
   
  @Test@Ignore def testTC016a{
	val tc=suit.getTc("R2RMLTC016a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC016b{
	val tc=suit.getTc("R2RMLTC016b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC016c{
	val tc=suit.getTc("R2RMLTC016c")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC016d{
	val tc=suit.getTc("R2RMLTC016d")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC016e{
	val tc=suit.getTc("R2RMLTC016e")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }

}