package es.upm.fi.oeg.morph.tc
import es.upm.fi.oeg.morph.r2rml.R2rmlModelException

class D015Test extends R2RMLTest("D015-1table3columns1composityeprimarykey3rows2languages") {
   
  "TC015a" should "gen 4 in DG" in{
	val dg=generate("R2RMLTC0015a")
	dg.getDefaultGraph.size should be (4)
  }
  "TC015b" should "gen 1 in DG" in{	
    println(intercept[R2rmlModelException]{val dg=generate("R2RMLTC0015b")})
  }

}