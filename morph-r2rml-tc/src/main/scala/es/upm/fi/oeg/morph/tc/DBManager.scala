package es.upm.fi.oeg.morph.tc
import java.sql.DriverManager._
import java.sql.PreparedStatement

class DBManager {
  val c=  Class.forName("org.hsqldb.jdbcDriver").newInstance
  def connection=getConnection("jdbc:hsqldb:mem:test","SA","")
  def createDB(script:String){
    val c = connection		
    script.split(';').foreach{s=>
      println("executing: "+s)
      if (s.size>5)        
        c.prepareStatement(s).execute
    }		
	c.close
  }
  def clearDB{
    val c=connection
    c.prepareStatement("DROP SCHEMA PUBLIC CASCADE").execute
    c.close
  }
  
}