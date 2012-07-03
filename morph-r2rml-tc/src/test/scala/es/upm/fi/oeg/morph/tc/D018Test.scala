package es.upm.fi.oeg.morph.tc
import org.junit.Test
import org.junit.Before
import org.junit.Ignore

class D018Test extends R2RMLTest("D018-1table1primarykey2columns3rows") {

  @Before def initialize() {}
   
  @Test def testTC018a{
	val tc=suit.getTc("R2RMLTC0018a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (9)
  }

}