package es.upm.fi.oeg.morph.db
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import es.upm.fi.oeg.morph.db.dataset.Dataset
import es.upm.fi.oeg.morph.r2rml.TriplesMap
import es.upm.fi.oeg.morph.r2rml.R2rmlReader

abstract class RelationalModel(conf: Config, val postProc: Boolean) {
  def query(query: String): Dataset
  def queries(r2rml:R2rmlReader):Seq[String]
}

/*object EmptyModel extends RelationalModel(ConfigFactory.empty("morph"), false) {
  def query(query: String): Dataset = throw new RuntimeException("query not defined")
}*/

class RelationalQueryException(msg: String, e: Throwable) extends Exception(msg, e)
