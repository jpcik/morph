package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import es.upm.fi.oeg.morph.r2rml.R2rmlModelException
import org.openjena.riot.SysRIOT
import collection.JavaConversions._

class D007Test extends R2RMLTest("D007-1table1primarykey2columns1row") {

  @Before def initialize() {
    SysRIOT.wireIntoJena()
  }
  
  @Test def testTC0007a{
	val tc=suit.getTc("R2RMLTC0007a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
  @Test def testTC0007b{
	val tc=suit.getTc("R2RMLTC0007b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (0)
	dg.listGraphNodes.toList.first.getURI.toString should be ("http://example.com/PersonGraph")
  }
  @Test def testTC0007c{
	val tc=suit.getTc("R2RMLTC0007c")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test def testTC0007d{
	val tc=suit.getTc("R2RMLTC0007d")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (4)
  }
  @Test def testTC0007e{
	val tc=suit.getTc("R2RMLTC0007e")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (0)
	dg.listGraphNodes.toList.first.getURI.toString should be ("http://example.com/PersonGraph")
  }
  @Test def testTC0007f{
	val tc=suit.getTc("R2RMLTC0007f")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (0)
	dg.listGraphNodes.toList.first.getURI.toString should be ("http://example.com/PersonGraph")
  }
  @Test def testTC0007g{
	val tc=suit.getTc("R2RMLTC0007g")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (0)
  }
  @Test def testTC0007h{
	val tc=suit.getTc("R2RMLTC0007h")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (2)
  }
  @Test def testTC0007i{
	val tc=suit.getTc("R2RMLTC0007i")	
	println(intercept[R2rmlModelException]{
	  val ds=suit.testTc(tc.get)
	})
  }
}