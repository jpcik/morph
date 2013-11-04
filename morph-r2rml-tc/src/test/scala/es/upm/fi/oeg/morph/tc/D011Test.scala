package es.upm.fi.oeg.morph.tc

class D011Test  extends R2RMLTest("D011-M2MRelations") {

   
  "TC011a" should "gen 19 in DG" in{
	val dg=generate("R2RMLTC0011a")	
	dg.getDefaultGraph.size should be (19)
  }
  "TC011b" should "gen 16 in DG" in{
	val dg=generate("R2RMLTC0011b")	
	dg.getDefaultGraph.size should be (16)
  }

}