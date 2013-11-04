package es.upm.fi.oeg.morph.tc

class D019Test extends R2RMLTest("D019-1table1primarykey3columns3rows") {

  "TC019a" should "gen 2 in DG" in{
	val dg=generate("R2RMLTC0019a")	
	dg.getDefaultGraph.size should be (2)
  }
  "TC019b" should "gen 3 in DG" in{
	val dg=generate("R2RMLTC0019b")	
	dg.getDefaultGraph.size should be (3)
  }


}