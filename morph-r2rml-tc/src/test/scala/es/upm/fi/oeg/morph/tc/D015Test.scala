package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore
import es.upm.fi.oeg.morph.r2rml.R2rmlModelException

class D015Test extends R2RMLTest("D015-1table3columns1composityeprimarykey3rows2languages") {

  @Before def initialize() {}
   
  @Test def testTC015a{
	val ds=suit.testTc(suit.getTc("R2RMLTC0015a").get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test def testTC015b{
	val tc=suit.getTc("R2RMLTC0015b")
    println(intercept[R2rmlModelException]{val ds=suit.testTc(tc.get)})
  }

}