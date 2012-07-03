package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D010Test  extends R2RMLTest("D010-1table1primarykey3colums3rows") {

  @Before def initialize() {}
   
  @Test def testTC0010a{
	val tc=suit.getTc("R2RMLTC0010a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (3)
  }
  @Test def testTC0010b{
	val tc=suit.getTc("R2RMLTC0010b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (3)
  }
  @Test def testTC0010c{
	val tc=suit.getTc("R2RMLTC0010c")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (3)
  }

}