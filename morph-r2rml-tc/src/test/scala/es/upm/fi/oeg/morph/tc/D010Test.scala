package es.upm.fi.oeg.morph.tc

class D010Test  extends R2RMLTest("D010-1table1primarykey3colums3rows") {

   
  "TC0010a" should "gen 3 in DG" in{
	val dg=generate("R2RMLTC0010a")	
	dg.getDefaultGraph.size should be (3)
  }
  "TC0010b" should "gen 3 in DG" in{
	val dg=generate("R2RMLTC0010b")	
	dg.getDefaultGraph.size should be (3)
  }
  "TC0010c" should "gen 3 in DG" in{
	val dg=generate("R2RMLTC0010c")	
	dg.getDefaultGraph.size should be (3)
  }

}