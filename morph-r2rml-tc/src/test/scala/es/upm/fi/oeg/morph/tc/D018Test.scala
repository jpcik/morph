package es.upm.fi.oeg.morph.tc

class D018Test extends R2RMLTest("D018-1table1primarykey2columns3rows") {

  "TC018a" should "gen 9 in DG" in{
	val dg=generate("R2RMLTC0018a")	
	dg.getDefaultGraph.size should be (9)
  }

}