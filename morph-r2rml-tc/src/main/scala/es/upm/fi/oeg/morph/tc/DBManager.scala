package es.upm.fi.oeg.morph.tc
import java.sql.DriverManager._
import java.sql.PreparedStatement
import org.slf4j.LoggerFactory

class DBManager(driver:String,db:String,user:String,pass:String,createSchema:Boolean) {
  
  val logger = LoggerFactory.getLogger(classOf[DBManager])
  
  val c=  Class.forName(driver).newInstance
  def connection=getConnection(db,user,pass)
  def createDB(script:String){
    val con = connection		
    if (createSchema)
      con.prepareStatement("CREATE SCHEMA PUBLIC").execute

    script.split(';').foreach{s=>
      logger.debug("executing: "+s)
      if (s.size>5)        
        con.prepareStatement(s).execute
    }		
	con.close
  }
  def clearDB:Unit={
    val con=connection
    con.prepareStatement("DROP SCHEMA PUBLIC CASCADE").execute
    con.close
  }
  
}