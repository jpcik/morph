package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

class D013Test extends R2RMLTest("D013-1table1primarykey3columns2rows1nullvalue") {

  @Before def initialize() {}
   
  @Test@Ignore def testTC013a{
	val tc=suit.getTc("R2RMLTC013a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
}