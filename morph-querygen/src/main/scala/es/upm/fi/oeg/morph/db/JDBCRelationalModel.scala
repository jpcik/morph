package es.upm.fi.oeg.morph.db
import java.sql.DriverManager
import com.typesafe.config.Config
import org.slf4j.LoggerFactory
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException
import es.upm.fi.oeg.morph.db.dataset.DbDataset
import es.upm.fi.oeg.morph.r2rml.TriplesMap
import es.upm.fi.oeg.morph.r2rml.R2rmlReader

class JDBCRelationalModel(conf: Config, url: String) extends RelationalModel(conf, false) {

  val logger = LoggerFactory.getLogger(classOf[JDBCRelationalModel])

  private val driver = conf.getString("driver")
  Class.forName(driver).newInstance
  private val sourceUrl = url //conf.getString("source.url")
  private val user = conf.getString("source.user")
  private val password = conf.getString("source.password")

  val sqlDialect = SQLDialect(sourceUrl.replaceAll("jdbc:(.*)://.*", "$1"))
  logger.debug("using SQL_DIALECT {}\n", sqlDialect)

  def this(conf: Config) = this(conf, conf.getString("source.url"))
  
  override def queries(r2rml:R2rmlReader)={
    implicit val r2r=r2rml
    r2rml.tMaps.map {tMap =>    
      new RdbQueryGenerator(tMap,sqlDialect).query
    }.toSeq
  }
  override def query(query: String) = {
    val st = getConnection.createStatement    
    logger.debug("\n{}\n", query)    
    try new DbDataset(st.executeQuery(query))
    catch {
        case ex: SQLSyntaxErrorException => throw new RelationalQueryException("Invalid query syntax: " + ex.getMessage, ex)
        case ex: SQLException => throw new RelationalQueryException("Invalid query: " + ex.getMessage, ex)
    }  
  }
    
  private def getConnection = DriverManager.getConnection(sourceUrl, user, password)
}
