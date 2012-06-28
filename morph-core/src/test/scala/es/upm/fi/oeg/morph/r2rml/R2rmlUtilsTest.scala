package es.upm.fi.oeg.morph.r2rml
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.junit.Before
import org.junit.Test

class R2rmlUtilsTest extends JUnitSuite with ShouldMatchersForJUnit with Checkers {
  @Before def initialize() {}
  
  @Test def extractTemplate1Value(){
    val v=R2rmlUtils.extractTemplateVals("first{val1}second")
    v(0) should be ("val1")
    println(v.mkString(","))
  }
  @Test def extractTemplate3Values(){
    val v=R2rmlUtils.extractTemplateVals("first{val1}second{val2}sssss{val3}")
     println(v.mkString(","))
     v(0) should be ("val1")
    v(1) should be ("val2")
    v(2) should be ("val3")
  }
  @Test def extractTemplate3ValueStart(){
    val v=R2rmlUtils.extractTemplateVals("{val1}ffff{val2}ssss{val3}tttt")
    println(v.mkString(","))
    v(0) should be ("val1")
    v(1) should be ("val2")
    v(2) should be ("val3")
  }

  @Test def removeTemplateVars(){
    val v=R2rmlUtils.removeTemplateVars("aaa{val1}ffff{val2}ssss{val3}tttt")
    println(v)
    v should be ("aaa}ffff}ssss}tttt")
    
  }


}