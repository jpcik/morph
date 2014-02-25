package es.upm.fi.oeg.morph.tc
import java.sql.DriverManager._
import java.sql.PreparedStatement
import org.slf4j.LoggerFactory

class DBManager(driver:String,db:String,user:String,pass:String,createSchema:Boolean) {
  
  val logger = LoggerFactory.getLogger(classOf[DBManager])
  
  val c=  Class.forName(driver).newInstance
  def connection=getConnection(db,user,pass)
  def createDB(script:String){
    val c = connection		
    if (createSchema)
      c.prepareStatement("CREATE SCHEMA PUBLIC").execute

    script.split(';').foreach{s=>
      logger.debug("executing: "+s)
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