package es.upm.fi.oeg.morph.tc
import es.upm.fi.oeg.morph.execute.RelationalQueryException

class D002Test extends R2RMLTest("D002-1table2columns1row") {
  
  "TC0002a" should "gen 3 t in DG" in{
	val dg=generate("R2RMLTC0002a")	
	dg.getDefaultGraph.size should be (3)
  }
  "TC0002b" should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0002b")	
	dg.getDefaultGraph.size should be (1)
  }
  "TC0002c"  should "fail" in{	
	intercept[RelationalQueryException]{
	  val tc=generate("R2RMLTC0002c")
	}
		
  }
  "TC0002d"  should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0002d")	
	dg.getDefaultGraph.size should be (1)
  }
  "TC0002e"  should "fail" in{		
	intercept[RelationalQueryException]{
  	  val dg=generate("R2RMLTC0002e")
	}
  }
  "TC0002f"  should "fail" in{
	
	intercept[RelationalQueryException]{
  	  val dg=generate("R2RMLTC0002f")
	}
  }
  "TC0002g"  should "fail" in{
	println(intercept[RelationalQueryException]{
	  val dg=generate("R2RMLTC0002g")
	})
  }
  "TC0002h"  should "fail" in{
	println(intercept[RelationalQueryException]{
	val dg=generate("R2RMLTC0002h")	
	})
  }
  "TC0002i"  should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0002i")	
	dg.getDefaultGraph.size should be (1)
  }
  "TC0002j"  should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0002j")	
	dg.getDefaultGraph.size should be (1)
  }
 
  
}