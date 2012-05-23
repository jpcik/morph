package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import collection.JavaConversions._

class D006Test extends R2RMLTest("D006-1table1primarykey1column1row") {

  @Before def initialize() {}
  
  @Test def testTC0006a{
	val tc=suit.getTc("R2RMLTC0006a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getGraph(dg.listGraphNodes.toList.first).size should be (1)
  }
}