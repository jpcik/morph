package es.upm.fi.oeg.morph.execute
import collection.JavaConversions._
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.query.DatasetFactory
import es.upm.fi.oeg.morph.voc.RDF
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.querygen.RdbQueryGenerator
import es.upm.fi.oeg.morph.relational.RelationalModel
import java.sql.Types
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype
import java.sql.SQLSyntaxErrorException
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.sparql.core.DatasetGraph
import es.upm.fi.oeg.morph.r2rml.GraphMap
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.Property
import com.hp.hpl.jena.datatypes.RDFDatatype
import es.upm.fi.oeg.morph.r2rml.PredicateObjectMap
import es.upm.fi.oeg.morph.r2rml.LiteralType
import es.upm.fi.oeg.morph.r2rml.IRIType
import java.sql.SQLException
import java.sql.ResultSet
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import es.upm.fi.oeg.siq.tools.URLTools
import org.slf4j.LoggerFactory
import es.upm.fi.oeg.morph.relational.JDBCRelationalModel
import es.upm.fi.oeg.morph.querygen.SQLDialect
import es.upm.fi.oeg.morph.querygen.DefaultSQL
import com.hp.hpl.jena.graph.GraphListener
import com.hp.hpl.jena.graph.GraphEventManager
import com.hp.hpl.jena.rdf.model.ModelChangedListener

class RelationalQueryException(msg: String, e: Throwable) extends Exception(msg, e)

class RdfGenerator(r2rml: R2rmlReader, relational: RelationalModel, baseUri: String) {

  val logger = LoggerFactory.getLogger(classOf[RdfGenerator])

  val model: Model = ModelFactory.createDefaultModel

  //val baseUri="http://example.com/base/"
  type GeneratedTriple = (Resource, Property, Object, RDFDatatype)

  val df = new DecimalFormat("0.0##E0");
  val datef = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  private def addTriple(m: Model, subj: Resource, p: Property, obj: (Object, RDFDatatype), poMap: PredicateObjectMap) = {
    val (ob, datatype) = obj
    logger.debug("more data " + obj)
    val oMap = poMap.objectMap
    if (ob != null)
      if (ob.isInstanceOf[Resource])
        m.add(subj, p, ob.asInstanceOf[Resource])
      else if (oMap.dtype != null && datatype != null)
        m.add(subj, p, ob.toString, datatype)
      else if (oMap.template != null && oMap.termType.equals(IRIType))
        m.add(subj, p, m.createResource(ob.toString))
      else if (oMap.template != null && oMap.termType.equals(LiteralType))
        m.add(subj, p, ob.toString, oMap.language)
      else if (oMap.column != null && oMap.termType == IRIType)
        m.add(subj, p, m.createResource(ob.toString))
      else if (oMap.column != null && datatype != null && datatype == XSDDatatype.XSDstring)
        m.add(subj, p, ob.toString, oMap.language)
      else if (oMap.column != null && datatype != null && datatype == XSDDatatype.XSDdouble)
        m.add(subj, p, df.format(ob), datatype)
      else if (oMap.column != null && datatype != null && datatype == XSDDatatype.XSDdateTime)
        m.add(subj, p, datef.format(ob), datatype)
      else if (oMap.column != null && datatype != null && datatype != XSDDatatype.XSDstring)
        m.add(subj, p, ob.toString, datatype)
      else if (oMap.constant != null && oMap.termType.equals(LiteralType))
        m.add(subj, p, ob.toString, oMap.language)
      else if (oMap.constant != null)
        m.add(subj, p, ob.toString, oMap.language)
      else
        m.add(subj, p, m.createResource(ob.toString))
  }

  /**
   * This method permits to register multiple listeners on the Jena graph/model.
   * This is useful to intercept triples/statements while added to the model.
   * (For example to trascribe triples while they are added, or to send them to a remote triplestore)
   */
  def registerListeners[G](listeners: Seq[G]) = listeners.foreach(listener => registerListener(listener))

  /**
   * This method permits to register a Graph listener on the Jena graph,
   * or a Model listener on the Jena Model.
   * This is useful to intercept triples/statements while added to the model.
   * (For example to trascribe triples while they are added, or to send them to a remote triplestore)
   */
  private def registerListener[L](listener: L) {
    listener match {
      case l: GraphListener => this.model.getGraph().getEventManager().register(l)
      case l: ModelChangedListener => this.model.register(l)
      case _ => throw new RuntimeException("type " + listener.getClass + " not supported as Listener")
    }
  }

  def generate(res: ResultSet) = {

    val ds = DatasetFactory.create(model)
    if (r2rml.tMaps.isEmpty)
      throw new Exception("No valid R2RML mappings in the provided document.")
    val queries = r2rml.tMaps.foreach { tMap =>
      logger.debug("now this one " + tMap.uri)
      val tgen = new TripleGenerator(ds, tMap, baseUri)
      res.beforeFirst
      iterateGenerate(res, tgen)
    }
    ds
  }

  def generate = {

    val ds = DatasetFactory.create(model)

    if (r2rml.tMaps.isEmpty)
      throw new Exception("No valid R2RML mappings in the provided document.")

    // SQL Dialect detection, useful for tracking small differences in query construction
    val sqlDialect = if (relational.isInstanceOf[JDBCRelationalModel])
      SQLDialect(relational.asInstanceOf[JDBCRelationalModel].SQL_DIALECT)
    else
      new DefaultSQL

    //////////////////////////////////////////////////////////
    val queries = r2rml.tMaps.foreach { tMap =>
      val q = new RdbQueryGenerator(tMap, r2rml, sqlDialect).query
      logger.debug("query {}\n", q)

      val res = try relational.query(q)
      catch {
        case ex: SQLSyntaxErrorException => throw new RelationalQueryException("Invalid query syntax: " + ex.getMessage, ex)
        case ex: SQLException => throw new RelationalQueryException("Invalid query: " + ex.getMessage, ex)
      }

      val tgen = new TripleGenerator(ds, tMap, baseUri)
      iterateGenerate(res, tgen)
    }

    //////////////////////////////////////////////////////////
    ds
  }

  private def iterateGenerate(res: ResultSet, tgen: TripleGenerator) = {
    val ds = tgen.d
    val tMap = tgen.tm
    while (res.next) {

      val subj =
        if (relational.postProc) tgen.genSubjectPostProc(res)
        else tgen.genSubject(res)
      val tMapModel = tgen.graph(res, tMap.subjectMap.graphMap)

      if (subj != null) {
        if (tgen.genRdfTypes != null)
          tgen.genRdfTypes.foreach { typ =>
            tMapModel.add(subj, RDF.typeProp, typ)
          }

        tMap.poMaps.foreach { prop =>
          val parentTmap = if (prop.refObjectMap != null)
            r2rml.triplesMaps(prop.refObjectMap.parentTriplesMap)
          else null
          val propIns = POGenerator(ds, prop, parentTmap, baseUri)
          val po =
            if (relational.postProc) propIns.generate(res)
            else propIns.generate(null, res)
          logger.debug("data" + po)
          addTriple(tMapModel, subj, propIns.genPredicate, po, prop)
          if (prop.graphMap != null) {
            logger.debug("graph:" + prop.graphMap)
            val poModel = tgen.graph(res, prop.graphMap)
            addTriple(poModel, subj, propIns.genPredicate, po, prop)
          }
        }
      }

    }

  }

}