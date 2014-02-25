package es.upm.fi.oeg.siq.tools
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import java.text.DateFormat
import java.util.Locale
import java.util.TimeZone
import java.text.SimpleDateFormat
import java.net.URLEncoder
import java.net.URI
import org.scalatest.FlatSpec
import java.util.Calendar
import org.scalatest.Matchers
import org.slf4j.LoggerFactory

class DateTimeSpec extends FlatSpec with Matchers  {
  
  "Encoding URL" should "produce string" in {
    val url="http://example.com/34/Bolivia, Plurinational State of"    
    val str=URLTools.encode(url)
    str should be ("http://example.com/34/Bolivia%2C%20Plurinational%20State%20of")
  }
  "Parsed date" should "produce a Date" in{    
    val sdf=new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS")
    val df=DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.MEDIUM,Locale.UK)
      df.setTimeZone(TimeZone.getTimeZone("GMT"))
      val d=sdf.parse("2012-04-24 18:19:28.996245".dropRight(3))
      val cal=Calendar.getInstance
      cal.setTime(d)
      println(d.toString)
      cal.get(Calendar.YEAR) shouldBe 2012
      cal.get(Calendar.MONTH)+1 shouldBe 4
      cal.get(Calendar.DAY_OF_MONTH) shouldBe 24
      cal.get(Calendar.HOUR_OF_DAY) shouldBe 18
      cal.get(Calendar.MINUTE) shouldBe 19
      cal.get(Calendar.MILLISECOND) shouldBe 996
      

  }

}