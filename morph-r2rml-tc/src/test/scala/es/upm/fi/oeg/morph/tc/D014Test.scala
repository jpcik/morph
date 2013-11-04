package es.upm.fi.oeg.morph.tc

class D014Test extends R2RMLTest("D014-3tables1primarykey1foreignkey") {
   
  "TC014a" should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0014a")	
	dg.getDefaultGraph.size should be (1)
  }
  "TC014b" should "gen 14 in DG" in{
	val dg=generate("R2RMLTC0014b")	
	dg.getDefaultGraph.size should be (14)
  }
  "TC014c" should "gen 14 in DG" in{
	val dg=generate("R2RMLTC0014c")	
	dg.getDefaultGraph.size should be (14)
  }
  "TC014d" should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0014d")	
	dg.getDefaultGraph.size should be (1)
  }
}