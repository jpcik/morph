package es.upm.fi.oeg.morph.tc

import com.hp.hpl.jena.graph.Node
import com.hp.hpl.jena.graph.NodeFactory

class D008Test extends R2RMLTest("D008-1table1compositeprimarykey3columns1row") {
  
  "TC0008a" should "gen 0 in DG" in{
	val dg=generate("R2RMLTC0008a")	
	dg.getDefaultGraph.size should be (0)
	dg.getGraph(NodeFactory.createURI("http://example.com/graph/Student/10/Venus%20Williams")) should not be (null)
	
  }
  "TC0008b" should "gen 5 in DG" in{
	val dg=generate("R2RMLTC0008b")	
	dg.getDefaultGraph.size should be (5)
  }
  "TC0008c" should "gen 2 in DG" in{
	val dg=generate("R2RMLTC0008c")	
	dg.getDefaultGraph.size should be (2)
  }

}