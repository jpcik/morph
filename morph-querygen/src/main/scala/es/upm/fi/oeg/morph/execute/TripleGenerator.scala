package es.upm.fi.oeg.morph.execute
import es.upm.fi.oeg.morph.r2rml.TriplesMap
import com.hp.hpl.jena.query.DataSource
import com.hp.hpl.jena.rdf.model.AnonId
import es.upm.fi.dia.oeg.morph.r2rml.SubjectMap
import es.upm.fi.oeg.morph.r2rml.R2rmlUtils._
import java.sql.ResultSet
import es.upm.fi.oeg.morph.r2rml.R2rmlUtils
import es.upm.fi.oeg.morph.r2rml.PredicateObjectMap
import com.hp.hpl.jena.rdf.model.ResourceFactory

case class TripleGenerator(d:DataSource,tm:TriplesMap){
  def genModel=d.getDefaultModel
   
  def genSubject(rs:ResultSet)={
    val subj=tm.subjectMap
	val col =extractColumn(subj)
    val id = rs.getString(col)
	val ttype=subj.termType
	if (ttype.isBlank)
	  genModel.createResource(new AnonId(id))
	else if (subj.template!=null)
	  genModel.createResource(generateTemplateVal(subj.template,id))
	else
	  genModel.createResource(id)
  }

  def genRdfType()={
	tm.subjectMap.rdfsClass		
  }  
}

case class POGenerator(d:DataSource,poMap:PredicateObjectMap){
  def genPredicate={
    ResourceFactory.createProperty(
      poMap.predicateMap.constant.asResource.getURI)
  }
  
}

