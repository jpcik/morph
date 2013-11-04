package es.upm.fi.oeg.morph.tc
import es.upm.fi.oeg.morph.r2rml.R2rmlModelException

class D004Test extends R2RMLTest("D004-1table2columns1row") {
  
  "TC0004a" should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0004a")	
	dg.getDefaultGraph.size should be (4)
  }
  "TC0004b" should "gen 1 in DG" in{
	println(intercept[R2rmlModelException]{
   	  val dg=generate("R2RMLTC0004b")	
	})
  }

}