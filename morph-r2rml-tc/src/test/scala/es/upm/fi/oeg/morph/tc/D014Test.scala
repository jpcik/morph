package es.upm.fi.oeg.morph.tc
import org.junit.Before
import org.junit.Test
import org.junit.Ignore
//import com.google.gson.Gson

class D014Test extends R2RMLTest("D014-3tables1primarykey1foreignkey") {

      case class SparqlBindings(head:Head,bindings:Array[Map[String,String]])

case class Head(vars:Array[String],link:Array[String])

case class BindingValue()

  @Before def initialize() {
    val bin=(1 to 10).map(i=>List(("mata",i+"gato"),("pata",i+"tato")).toMap).toArray
    val sp=SparqlBindings(Head(Array("rata","bata"),Array()),bin)
    //val json=new Gson().toJson(bin)
    println(bin)
    //println(json)

  }
   
  @Test def testTC014a{
	val tc=suit.getTc("R2RMLTC0014a")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
  @Test def testTC014b{
	val tc=suit.getTc("R2RMLTC0014b")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (14)
  }
  @Test def testTC014c{
	val tc=suit.getTc("R2RMLTC0014c")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (14)
  }
  @Test def testTC014d{
	val tc=suit.getTc("R2RMLTC0014d")	
	val ds=suit.testTc(tc.get)
	val dg=ds.asDatasetGraph
	dg.getDefaultGraph.size should be (1)
  }
}