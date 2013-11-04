package es.upm.fi.oeg.morph.tc
import es.upm.fi.oeg.morph.execute.RelationalQueryException
import es.upm.fi.oeg.morph.r2rml.R2rmlModelException

class D003Test extends R2RMLTest("D003-1table3columns1row") {

  
  "TC0003a" should "fail" in{
	println(intercept[R2rmlModelException]{
	val dg=generate("R2RMLTC0003a")	
	})
  }
  "TC0003b" should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0003b")	
	dg.getDefaultGraph.size should be (1)
  }
  "TC0003c" should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0003c")	
	dg.getDefaultGraph.size should be (1)
  }

}