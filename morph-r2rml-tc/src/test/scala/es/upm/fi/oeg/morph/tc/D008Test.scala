package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D008Test extends R2RMLTest("D008-1table1compositeprimarykey3columns1row") {

  @Before def initialize() {}
  
  @Test def testTC0008a{
	val tc=suit.getTc("R2RMLTC0008a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (0)
	ds.getNamedModel("http://example.com/graph/Student/10/Venus%20Williams") should not be (null)
	
  }
  @Test def testTC0008b{
	val tc=suit.getTc("R2RMLTC0008b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (5)
  }
  @Test def testTC0008c{
	val tc=suit.getTc("R2RMLTC0008c")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (2)
  }

}