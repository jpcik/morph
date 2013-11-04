package es.upm.fi.oeg.morph.tc

class D012Test extends R2RMLTest("D012-2tables2duplicates0nulls") {

   
  "TC012a" should "gen 4 in DG" in{
	val dg=generate("R2RMLTC0012a")	
	dg.getDefaultGraph.size should be (4)
  }
  "TC012b" should "gen 4 in DG" in{
	val dg=generate("R2RMLTC0012b")	
	dg.getDefaultGraph.size should be (4)
  }
  "TC012c" should "gen 1 in DG" in{
    println(intercept[Exception]{	val dg=generate("R2RMLTC0012c")})
  }
  "TC012d" should "gen 1 in DG" in{
	println(intercept[Exception]{	val dg=generate("R2RMLTC0012d")	})
  }

  "TC012e" should "gen 16 in DG" in{
	val dg=generate("R2RMLTC0012e")	
	dg.getDefaultGraph.size should be (16)
  }

}