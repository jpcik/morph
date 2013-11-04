package es.upm.fi.oeg.morph.r2rml
import org.scalatest.Matchers
import org.scalatest.FlatSpec

class R2rmlUtilsTest extends FlatSpec with Matchers  {
  
  "Template with 1 var" should "get 1 var" in{
    val v=R2rmlUtils.extractTemplateVals("first{val1}second")
    v.size shouldBe 1
    v(0) should be ("val1")
    println(v.mkString(","))
  }
  
  "Extract form Template with 3 vars" should "get 3 vars" in{
    val v=R2rmlUtils.extractTemplateVals("first{val1}second{val2}sssss{val3}")
    println(v.mkString(","))
    v(0) should be ("val1")
    v(1) should be ("val2")
    v(2) should be ("val3")
  }
  
  "Template with 3 vars starting at 0" should "get 3 vars" in{
    val v=R2rmlUtils.extractTemplateVals("{val1}ffff{val2}ssss{val3}tttt")
    println(v.mkString(","))
    v.size shouldBe 3
    v(0) should be ("val1")
    v(1) should be ("val2")
    v(2) should be ("val3")
  }

  "Removing vars from template" should "erase all vars" in{
    val v=R2rmlUtils.removeTemplateVars("aaa{val1}ffff{val2}ssss{val3}tttt")
    println(v)
    v should be ("aaa}ffff}ssss}tttt")
    
  }


}