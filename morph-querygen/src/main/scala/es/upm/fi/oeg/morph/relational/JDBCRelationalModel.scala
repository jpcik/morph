package es.upm.fi.oeg.morph.relational
import java.util.Properties
import java.sql.DriverManager
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

class JDBCRelationalModel(conf: Config, url: String) extends RelationalModel(conf, false) {

  val logger = LoggerFactory.getLogger(classOf[JDBCRelationalModel])

  val driver = conf.getString("driver")
  Class.forName(driver).newInstance
  val sourceUrl = url //conf.getString("source.url")
  val user = conf.getString("source.user")
  val password = conf.getString("source.password")

  val SQL_DIALECT = sourceUrl.replaceAll("jdbc:(.*)://.*", "$1")
  logger.debug("using SQL_DIALECT {}\n", SQL_DIALECT)

  def this(conf: Config) = this(conf, conf.getString("source.url"))

  override def query(query: String) = {
    val conn = getConnection
    val st = conn.createStatement
    st.executeQuery(query)
  }

  private def getConnection = DriverManager.getConnection(sourceUrl, user, password)
}
