package es.upm.fi.oeg.siq.tools
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.prop.Checkers
import org.junit.Before
import org.junit.Test
import java.text.DateFormat
import java.util.Locale
import java.util.TimeZone
import java.text.SimpleDateFormat
import java.net.URLEncoder
import java.net.URI

class DateTimeTest extends JUnitSuite with ShouldMatchersForJUnit with Checkers {
  @Before def initialize() {}
  
  
  @Test def encode(){
    val url="http://example.com/34/Bolivia, Plurinational State of"
    
    val tup=URLTools.encode(url)
    //val uri=new URI(url)
    
    println(tup)
  }
  @Test def testParseDate(){    
    val sdf=new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS")
    val df=DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.MEDIUM,Locale.UK)
      df.setTimeZone(TimeZone.getTimeZone("GMT"))
      val d=sdf.parse("2012-04-24 18:19:28.996245".dropRight(3))
      println(d.toString)
  }

}