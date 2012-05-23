package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import es.upm.fi.oeg.morph.execute.RelationalQueryException
import es.upm.fi.oeg.morph.r2rml.R2rmlModelException

class D003Test extends R2RMLTest("D003-1table3columns1row") {

  @Before def initialize() {}
  
  @Test def testTC0003a{
	val tc=suit.getTc("R2RMLTC0003a")	
	println(intercept[R2rmlModelException]{
	  val ds=suit.testTc(tc.get)
	})
  }
  @Test def testTC0003b{
	val tc=suit.getTc("R2RMLTC0003b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
  @Test def testTC0003c{
	val tc=suit.getTc("R2RMLTC0003c")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }

}