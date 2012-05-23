package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import es.upm.fi.oeg.morph.execute.RelationalQueryException

class D002Test extends R2RMLTest("D002-1table2columns1row") {

  @Before def initialize() {}
  
  @Test def testTC0002a{
	val tc=suit.getTc("R2RMLTC0002a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (3)
  }
  @Test def testTC0002b{
	val tc=suit.getTc("R2RMLTC0002b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
  @Test def testTC0002c{
	val tc=suit.getTc("R2RMLTC0002c")
	println(intercept[RelationalQueryException]{
	  val ds=suit.testTc(tc.get)
	})
	
	
	//val dg=ds.asDatasetGraph
	//dg.getDefaultGraph.size should be (1)
  }
  @Test def testTC0002d{
	val tc=suit.getTc("R2RMLTC0002d")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
  @Test def testTC0002e{
	val tc=suit.getTc("R2RMLTC0002e")	
	println(intercept[RelationalQueryException]{
  	  val ds=suit.testTc(tc.get)
	})
  }
  @Test def testTC0002f{
	val tc=suit.getTc("R2RMLTC0002f")
	println(intercept[RelationalQueryException]{
  	  val ds=suit.testTc(tc.get)
	})
  }
  @Test def testTC0002g{
	val tc=suit.getTc("R2RMLTC0002g")
	println(intercept[RelationalQueryException]{
	  val ds=suit.testTc(tc.get)
	})
  }
  @Test def testTC0002h{
	val tc=suit.getTc("R2RMLTC0002h")	
	println(intercept[RelationalQueryException]{
	val ds=suit.testTc(tc.get)
	})
  }
  @Test def testTC0002i{
	val tc=suit.getTc("R2RMLTC0002i")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
  @Test def testTC0002j{
	val tc=suit.getTc("R2RMLTC0002j")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
 
  
}