package es.upm.fi.oeg.morph.tc
import es.upm.fi.oeg.morph.r2rml.R2rmlModelException
import org.openjena.riot.SysRIOT
import collection.JavaConversions._

class D007Test extends R2RMLTest("D007-1table1primarykey2columns1row") {

  //SysRIOT.wireIntoJena()
  
  "TC0007a" should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0007a")	
	dg.getDefaultGraph.size should be (1)
  }
  "TC0007b" should "gen 0 in DG" in{
	val dg=generate("R2RMLTC0007b")	
	dg.getDefaultGraph.size should be (0)
	dg.listGraphNodes.toList.head.getURI.toString should be ("http://example.com/PersonGraph")
  }
  "TC0007c" should "gen 4 in DG" in{
	val dg=generate("R2RMLTC0007c")	
	dg.getDefaultGraph.size should be (4)
  }
  "TC0007d" should "gen 4 in DG" in{
	val dg=generate("R2RMLTC0007d")	
	dg.getDefaultGraph.size should be (4)
  }
  "TC0007e" should "gen 0 in DG" in{
	val dg=generate("R2RMLTC0007e")	
	dg.getDefaultGraph.size should be (0)
	dg.listGraphNodes.toList.head.getURI.toString should be ("http://example.com/PersonGraph")
  }
  "TC0007f" should "gen 0 in DG" in{
	val dg=generate("R2RMLTC0007f")	
	dg.getDefaultGraph.size should be (0)
	dg.listGraphNodes.toList.head.getURI.toString should be ("http://example.com/PersonGraph")
  }
  "TC0007g" should "gen 2 in DG" in{
	val dg=generate("R2RMLTC0007g")	
	dg.getDefaultGraph.size should be (2)
  }
  "TC0007h" should "gen 1 in DG" in{		
	println(intercept[R2rmlModelException]{val dg=generate("R2RMLTC0007h")})
  }
}