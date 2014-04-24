package es.upm.fi.oeg.morph

import org.slf4j.LoggerFactory

import com.hp.hpl.jena.graph.GraphListener
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

import es.upm.fi.oeg.morph.db.JDBCRelationalModel
import es.upm.fi.oeg.morph.db.RelationalModel
import es.upm.fi.oeg.morph.db.RestRelationalModel
import es.upm.fi.oeg.morph.execute.RdfGenerator
import es.upm.fi.oeg.morph.r2rml.R2rmlReader

/**
 * this is the companion object containing the factory methods for instantiating different models
 * (JDBC, REST, etc)
 */
object Morph {

  val conf= ConfigFactory.load.getConfig("morph")
  def createJdbcModel = {
    new Morph(conf, new JDBCRelationalModel(conf.getConfig("jdbc")))
  }

  def createRestModel = {
    new Morph(conf, new RestRelationalModel(conf.getConfig("rest")))
  }

}

class Morph(
  conf: Config,model: RelationalModel,
  listeners: Array[GraphListener] = Array.empty) {

  val logger = LoggerFactory.getLogger(Morph.getClass())

  def generate(mapping: String) = {

    val reader = R2rmlReader(mapping)
    val generator = new RdfGenerator(reader, model, conf.getString("baseUri"))

    generator.registerListeners(listeners)

    val ds = generator.generate

    ds
  }

  def registerListeners(listeners: Array[GraphListener] = Array.empty) = {
    logger.debug("register listeners: {}", listeners)
    val morph = new Morph(conf, model, listeners)
    morph
  }

}


