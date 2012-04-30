package es.upm.fi.oeg.morph.relational
import java.util.Properties
import java.sql.DriverManager

class JDBCRelationalModel(props:Properties) extends RelationalModel(props){
  val driver=props.getProperty("jdbc.driver")
  Class.forName(driver).newInstance
  val sourceUrl=props.getProperty("jdbc.source.url")
  val user=props.getProperty("jdbc.source.user")
  val password=props.getProperty("jdbc.source.password")
  override def query(query:String)={
    val conn=getConnection
    val st=conn.createStatement
    st.executeQuery(query)
  } 
  private def getConnection=
    DriverManager.getConnection(sourceUrl,user,password)
}