package es.upm.fi.oeg.morph.tc

class D013Test extends R2RMLTest("D013-1table1primarykey3columns2rows1nullvalue") {

  "TC013a" should "gen 1 in DG" in{
	val dg=generate("R2RMLTC0013a")	
	dg.getDefaultGraph.size should be (1)
  }
}