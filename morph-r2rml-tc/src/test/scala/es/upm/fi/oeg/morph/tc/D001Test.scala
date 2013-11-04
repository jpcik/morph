package es.upm.fi.oeg.morph.tc

class D001Test extends R2RMLTest("D001-1table1column1row") {
  
  "TC0001a" should "generate 1 triple" in{
	val dg=generate("R2RMLTC0001a")	
	dg.getDefaultGraph.size should be (1)
  }
  "TC0001b" should "generate 1 graph" in{
	val dg=generate("R2RMLTC0001b")
	dg.getDefaultGraph.size should be (1)
	//println(ds.getDefaultModel.listStatements.nextStatement.getSubject.getId)
  }
}
