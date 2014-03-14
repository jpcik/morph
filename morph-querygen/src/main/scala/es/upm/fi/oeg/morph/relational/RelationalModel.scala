package es.upm.fi.oeg.morph.relational
import java.sql.ResultSet
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

abstract class RelationalModel(conf: Config, val postProc: Boolean) {
  //def configure(props:Properties):Unit
  def query(query: String): ResultSet
}
object EmptyModel extends RelationalModel(ConfigFactory.empty("morph"), false) {
  def query(query: String): ResultSet = throw new RuntimeException("query not defined")
}
