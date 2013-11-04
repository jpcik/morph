package es.upm.fi.oeg.morph.tc
import es.upm.fi.oeg.morph.r2rml.R2rmlModelException

class D009Test  extends R2RMLTest("D009-2tables1primarykey1foreignkey") {
   
  "TC0009a" should "gen 4 in DG" in{
	val dg=generate("R2RMLTC0009a")	
	dg.getDefaultGraph.size should be (4)
  }
  "TC0009b" should "gen 0 in DG" in{
	val dg=generate("R2RMLTC0009b")	
	dg.getDefaultGraph.size should be (0)
  }
  // Why should this be a non comformant R2RML?
  "TC0009c" should "gen 2 in DG" in{
	val dg=generate("R2RMLTC0009c")
	dg.getDefaultGraph.size should be (2)
  }
  "TC0009d" should "gen 4 in DG" in{
	val dg=generate("R2RMLTC0009d")	
	dg.getDefaultGraph.size should be (4)
  }


}