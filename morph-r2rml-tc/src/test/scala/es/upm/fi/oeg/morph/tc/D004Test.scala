package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import es.upm.fi.oeg.morph.r2rml.R2rmlModelException

class D004Test extends R2RMLTest("D004-1table2columns1row") {

  @Before def initialize() {}
  
  @Test def testTC0004a{
	val tc=suit.getTc("R2RMLTC0004a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test def testTC0004b{
	val tc=suit.getTc("R2RMLTC0004b")	
	println(intercept[R2rmlModelException]{
	  val ds=suit.testTc(tc.get)
	})
  }

}