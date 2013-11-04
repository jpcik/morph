package es.upm.fi.oeg.morph.tc

class D020Test extends R2RMLTest("D020-1table1column5rows") {

  "TC020a" should "gen 5 in DG" in{
	val dg=generate("R2RMLTC0020a")	
	dg.getDefaultGraph.size should be (5)
  }
  "TC020b" should "gen 5 in DG" in{
	val dg=generate("R2RMLTC0020b")	
	dg.getDefaultGraph.size should be (5)
  }

}