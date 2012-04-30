package es.upm.fi.oeg.siq.tools
import java.text.DateFormat
import java.util.Locale
import java.util.TimeZone

object DateTools {
  def convert(){
      val df=DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.MEDIUM,Locale.UK)
      df.setTimeZone(TimeZone.getTimeZone("GMT"))
      df.parse("2012-04-24 16:52:10.305275")
  }
}