package es.upm.fi.oeg.morph.relational
import java.util.Properties
import java.sql.DriverManager
import com.typesafe.config.Config

class JDBCRelationalModel(conf:Config) extends RelationalModel(conf,false){
  val driver=conf.getString("driver")
  Class.forName(driver).newInstance
  val sourceUrl=conf.getString("source.url")
  val user=conf.getString("source.user")
  val password=conf.getString("source.password")
  override def query(query:String)={
    val conn=getConnection
    val st=conn.createStatement
    st.executeQuery(query)    
  } 
  
  
  private def getConnection=
    DriverManager.getConnection(sourceUrl,user,password)
}