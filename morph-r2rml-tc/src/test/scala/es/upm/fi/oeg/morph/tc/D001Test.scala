package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test

class D001Test extends R2RMLTest("D001-1table1column1row") {

  @Before def initialize() {}
  
  @Test def testTC0001a{
	val tc=suit.getTc("R2RMLTC0001a")
	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
  @Test def testTC0001b{
	val tc=suit.getTc("R2RMLTC0001b")
	val ds=suit.testTc(tc.get)	
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
	println(ds.getDefaultModel.listStatements.nextStatement.getSubject.getId)
  }
}
