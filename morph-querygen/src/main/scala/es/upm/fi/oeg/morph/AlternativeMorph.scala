package es.upm.fi.oeg.morph

import com.typesafe.config.ConfigFactory
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.execute.RdfGenerator
import es.upm.fi.oeg.morph.relational.RestRelationalModel
import es.upm.fi.oeg.morph.relational.RelationalModel
import es.upm.fi.oeg.morph.voc.RDFFormat
import es.upm.fi.oeg.morph.relational.JDBCRelationalModel
import java.net.URI
import java.net.URL
import com.hp.hpl.jena.graph.GraphListener
import com.hp.hpl.jena.rdf.model.ModelChangedListener
import com.typesafe.config.Config
import es.upm.fi.oeg.morph.relational.EmptyModel
import org.slf4j.LoggerFactory

/**
 * this is the companion object containing the factory methods for instantiating different models
 * (JDBC, REST, etc)
 */
object AlternativeMorph {

  def createJdbcModel = {
    def conf = ConfigFactory.load.getConfig("morph")
    new AlternativeMorph(conf, new JDBCRelationalModel(conf.getConfig("jdbc")))
  }

  def createRestModel = new AlternativeMorph(ConfigFactory.load.getConfig("morph"), new RestRelationalModel)

}

class AlternativeMorph(
  conf: Config,
  model: RelationalModel,
  listeners: Array[GraphListener] = Array.empty) {

  val logger = LoggerFactory.getLogger(AlternativeMorph.getClass())

  def generate(mapping: String) = {

    val reader = R2rmlReader(mapping)
    val generator = new RdfGenerator(reader, model, conf.getString("baseUri"))

    generator.registerListeners(listeners)

    val ds = generator.generate

    ds
  }

  def registerListeners(listeners: Array[GraphListener] = Array.empty) = {
    logger.debug("register listeners: {}", listeners)
    val morph = new AlternativeMorph(conf, model, listeners)
    morph
  }

}


