package es.upm.fi.oeg.morph.tc

class D005Test extends R2RMLTest("D005-1table3columns3rows2duplicates") {
  
  "TC0005a" should "gen 4 in DG" in{
	val dg=generate("R2RMLTC0005a")	
	dg.getDefaultGraph.size should be (4)
  }

  "TC0005b" should "gen 8 in DG" in{
	val dg=generate("R2RMLTC0005b")	
	dg.getDefaultGraph.size should be (8)
  }

}