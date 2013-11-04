package es.upm.fi.oeg.morph.tc
import collection.JavaConversions._

class D006Test extends R2RMLTest("D006-1table1primarykey1column1row") {
  
  "TC0006a" should "gen 1 in NG" in{
	val dg=generate("R2RMLTC0006a")	
	dg.getGraph(dg.listGraphNodes.toList.head).size should be (1)
  }
}