package es.upm.fi.oeg.morph.execute
import es.upm.fi.oeg.morph.r2rml.TriplesMap
import com.hp.hpl.jena.rdf.model.AnonId
import es.upm.fi.oeg.morph.r2rml.R2rmlUtils._
import es.upm.fi.oeg.morph.r2rml.R2rmlUtils
import es.upm.fi.oeg.morph.r2rml.PredicateObjectMap
import com.hp.hpl.jena.rdf.model.ResourceFactory
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype
import java.sql.Types
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.rdf.model.Statement
import com.hp.hpl.jena.graph.Triple
import java.net.URLEncoder
import es.upm.fi.oeg.morph.r2rml.GraphMap
import com.hp.hpl.jena.rdf.model.ModelFactory
import es.upm.fi.oeg.siq.tools.XsdTypes
import es.upm.fi.oeg.siq.tools.URLTools
import es.upm.fi.oeg.morph.r2rml.IRIType
import es.upm.fi.oeg.morph.r2rml.LiteralType
import es.upm.fi.oeg.morph.r2rml.SubjectMap
import es.upm.fi.oeg.morph.r2rml.TermMap
import com.hp.hpl.jena.rdf.model.Model
import org.slf4j.LoggerFactory
import es.upm.fi.oeg.morph.relational.Dataset

trait MapGenerator {

  protected def createIriOrBNode(m: Model, value: String, map: TermMap, baseUri: String) = {
    if (map.termType.isIRI) {
      val valueenc =
        if (map.template != null && map.template.startsWith("{")) URLTools.encodeAll(value)
        else if (map.template != null) URLTools.encode(value)
        else value
      val finalval =
        if (URLTools.isAbsIRI(valueenc)) valueenc
        else baseUri + valueenc
      m.createResource(finalval)
    } else if (map.termType.isBlank)
      m.createResource(new AnonId(URLTools.encode(value)))
    else throw new Exception("Invalid Term Type " + map.termType)
  }

}

case class TripleGenerator(d: com.hp.hpl.jena.query.Dataset, tm: TriplesMap, baseUri: String) extends MapGenerator {

  val logger = LoggerFactory.getLogger(classOf[TripleGenerator])

  def genModel = d.getDefaultModel

  def slugify(str: String): String = {
    import java.text.Normalizer
    Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\w ]", "").replace(" ", "-").toLowerCase
  }

  def genSubject(rs: Dataset): Resource = genSubject(rs, "SUBJECT")

  def genSubject(rs: Dataset, col: String) = {
    val subj = tm.subjectMap
    //logger.debug("metacolumn " + rs.getMetaData.getColumnName(1))
    logger.debug("id column " + col)
    val id = rs.getString(col)
    if (id == null) null
    else {
      createIriOrBNode(genModel, id, subj, baseUri)
    }
  }

  def genSubjectPostProc(rs: Dataset) = {
    val subj = tm.subjectMap

    val id =
      if (subj.template != null) {
        val vars = R2rmlUtils.extractTemplateVals(subj.template)
        val result = R2rmlUtils.replaceTemplate(subj.template, vars.map(v => rs.getString(v)))
        result
      } else if (subj.column != null)
        rs.getString(subj.column)
      else
        subj.constant.toString
    //val id=rs.getString(col)
    if (id == null) null
    else createIriOrBNode(genModel, id, subj, baseUri)
  }

  def genRdfTypes() = {
    tm.subjectMap.classes
  }

  def genGraph(rs: Dataset) = {
    val gMap = tm.subjectMap.graphMap
    if (gMap.template != null)
      URLTools.encode(rs.getString("SUBJECTGRAPH"))
    else null
  }

  def graph(res: Dataset, gmap: GraphMap) = {
    if (gmap == null) d.getDefaultModel
    else if (gmap.constant != null)
      if (d.getNamedModel(gmap.constant.asResource.getURI) != null)
        d.getNamedModel(gmap.constant.asResource.getURI)
      else {
        d.addNamedModel(gmap.constant.asResource.getURI, ModelFactory.createDefaultModel)
        d.getNamedModel(gmap.constant.asResource.getURI)
      }
    else if (gmap.template != null) {
      val gname = genGraph(res)
      if (d.getNamedModel(gname) == null)
        d.addNamedModel(gname, ModelFactory.createDefaultModel)
      d.getNamedModel(gname)
    } else null
  }

}

case class POGenerator(ds:com.hp.hpl.jena.query.Dataset, poMap:PredicateObjectMap, parentTMap: TriplesMap, baseUri: String) extends MapGenerator {
  def genPredicate = {
    ResourceFactory.createProperty(
      poMap.predicateMap.constant.asResource.getURI)
  }

  def genRefObject(rs: Dataset) = {
    poMap.refObjectMap.joinCondition.child
  }

  def generate(subj: Resource, rs: Dataset) = {
    //RefObjectMap
    if (poMap.refObjectMap != null) {
      val obj = new TripleGenerator(ds, parentTMap, baseUri).genSubject(rs, poMap.id)
      (obj, null)
    } //ObjectMap
    else {
      //val typeInResult = rs.getMetaData.getColumnType(rs.findColumn(poMap.id))
      val typeInResult = rs.getType(poMap.id)
      val datatype = if (poMap.objectMap.dtype != null)
        poMap.objectMap.dtype
      else typeInResult//XsdTypes.sqlType2XsdType(typeInResult)
      if (poMap.objectMap.termType == IRIType)
        //(rs.getString(poMap.id),null)
        (createIriOrBNode(ds.getDefaultModel, rs.getString(poMap.id), poMap.objectMap, baseUri), null)
      if (poMap.objectMap.termType == LiteralType)
        (rs.getObject(poMap.id), datatype)
      else if (poMap.objectMap.constant == null)
        (rs.getString(poMap.id), datatype)
      else
        (poMap.objectMap.constant, null)
    }
  }

  def generate(rs: Dataset) = {
    //RefObjectMap
    if (poMap.refObjectMap != null) {
      val obj = new TripleGenerator(ds, parentTMap, baseUri).genSubjectPostProc(rs)
      (obj, null)
    } else {
      val datatype =
        if (poMap.objectMap.termType == IRIType) null
        else if (poMap.objectMap.dtype != null) poMap.objectMap.dtype
        else if (poMap.objectMap.column != null) {
          //val typeInResult = rs.getMetaData.getColumnType(rs.findColumn(poMap.objectMap.column))
          //XsdTypes.sqlType2XsdType(typeInResult)
          rs.getType(poMap.objectMap.column)
        } else null

      if (poMap.objectMap.template != null) {
        val vars = R2rmlUtils.extractTemplateVals(poMap.objectMap.template)
        val result = R2rmlUtils.replaceTemplate(poMap.objectMap.template, vars.map(v => rs.getString(v)))
        (result, datatype)
      } else if (poMap.objectMap.column != null)
        (rs.getString(poMap.objectMap.column), datatype)
      else
        (poMap.objectMap.constant, null)
    }
  }
}