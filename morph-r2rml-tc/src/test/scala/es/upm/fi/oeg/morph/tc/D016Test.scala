package es.upm.fi.oeg.morph.tc

class D016Test extends R2RMLTest("D016-1table1primarykey10columns3rowsSQLdatatypes") {
   
  "TC016a" should "gen 15 in DG" in{
	val dg=generate("R2RMLTC0016a")	
	dg.getDefaultGraph.size should be (15)
  }
  "TC016b" should "gen 9 in DG" in{
	val dg=generate("R2RMLTC0016b")	
	dg.getDefaultGraph.size should be (9)
  }
  "TC016c" should "gen 9 in DG" in{
	val dg=generate("R2RMLTC0016c")	
	dg.getDefaultGraph.size should be (9)
  }
  "TC016d" should "gen 6 in DG" in{
	val dg=generate("R2RMLTC0016d")	
	dg.getDefaultGraph.size should be (6)
  }
  "TC016e" should "gen 4 in DG" ignore{
	val dg=generate("R2RMLTC0016e")	
	dg.getDefaultGraph.size should be (4)
  }

}