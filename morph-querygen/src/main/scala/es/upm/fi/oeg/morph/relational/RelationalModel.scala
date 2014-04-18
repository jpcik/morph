package es.upm.fi.oeg.morph.relational
import java.sql.ResultSet
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

abstract class RelationalModel(conf: Config, val postProc: Boolean) {
  def query(query: String): Dataset
}
object EmptyModel extends RelationalModel(ConfigFactory.empty("morph"), false) {
  def query(query: String): Dataset = throw new RuntimeException("query not defined")
}
