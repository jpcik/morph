package es.upm.fi.oeg.morph.tc
import org.junit.Test
import org.junit.Before
import es.upm.fi.oeg.morph.r2rml.R2rmlModelException
import org.junit.Ignore

class D009Test  extends R2RMLTest("D009-2tables1primarykey1foreignkey") {

  @Before def initialize() {}
   
  @Test@Ignore def testTC0009a{
	val tc=suit.getTc("R2RMLTC0009a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC0009b{
	val tc=suit.getTc("R2RMLTC0009b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test@Ignore def testTC0009c{
	val tc=suit.getTc("R2RMLTC0009c")
	println(intercept[R2rmlModelException]{
	  val ds=suit.testTc(tc.get)
	})
  }
  @Test@Ignore def testTC0009d{
	val tc=suit.getTc("R2RMLTC0009d")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }


}