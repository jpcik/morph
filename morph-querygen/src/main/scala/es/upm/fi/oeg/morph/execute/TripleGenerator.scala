package es.upm.fi.oeg.morph.execute
import es.upm.fi.oeg.morph.r2rml.TriplesMap
import com.hp.hpl.jena.query.DataSource
import com.hp.hpl.jena.rdf.model.AnonId
import es.upm.fi.oeg.morph.r2rml.R2rmlUtils._
import java.sql.ResultSet
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

case class TripleGenerator(d:DataSource,tm:TriplesMap, baseUri:String){
  def genModel=d.getDefaultModel
   
  def slugify(str: String): String = {
    import java.text.Normalizer
    Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\w ]", "").replace(" ", "-").toLowerCase
  }
  
  def genSubject(rs:ResultSet):Resource=genSubject(rs,"SUBJECT")   
   
  def genSubject(rs:ResultSet,col:String)={
    val subj=tm.subjectMap
    println("metacolumn "+rs.getMetaData.getColumnName(1))
    println("id column "+col)
    val id=rs.getString(col)
    if (id==null) null
    else {
      val ttype=subj.termType
      if (ttype.isIRI){
        val value=
          if (subj.template!=null) URLTools.encode(id)
          else id
        val finalval=if (URLTools.isAbsIRI(value))
          value else baseUri+value
        genModel.createResource(finalval)
      }
      else if (ttype.isBlank)
		genModel.createResource(new AnonId(URLTools.encode(id)))
	  else throw new Exception("Invalid Term Type "+ttype)
    }
  }

  def genRdfType()={
	tm.subjectMap.rdfsClass		
  } 
  
  def genGraph(rs:ResultSet)={
    val gMap=tm.subjectMap.graphMap
    if (gMap.template!=null)
      URLTools.encode(rs.getString("SUBJECTGRAPH"))
     else null
  }
  
  def graph(res:ResultSet,gmap:GraphMap)={
    if (gmap==null) d.getDefaultModel
    else if (gmap.constant!=null) 
      if (d.getNamedModel(gmap.constant.asResource.getURI)!=null)
        d.getNamedModel(gmap.constant.asResource.getURI)
      else {
        d.addNamedModel(gmap.constant.asResource.getURI,ModelFactory.createDefaultModel)
        d.getNamedModel(gmap.constant.asResource.getURI)
      }
    else if (gmap.template!=null){
      val gname=genGraph(res)
      if (d.getNamedModel(gname)==null)
        d.addNamedModel(gname,ModelFactory.createDefaultModel)
      d.getNamedModel(gname)                     
    }
    else null    
  }

}

case class POGenerator(ds:DataSource,poMap:PredicateObjectMap,parentTMap:TriplesMap,baseUri:String){
  def genPredicate={
    ResourceFactory.createProperty(
      poMap.predicateMap.constant.asResource.getURI)
  }
  
  def genRefObject(rs:ResultSet)={
    poMap.refObjectMap.joinCondition.child
  }
  
  def generate(subj:Resource,rs:ResultSet)={
    //RefObjectMap
    if (poMap.refObjectMap!=null){
	  val obj=new TripleGenerator(ds,parentTMap,baseUri).genSubject(rs,poMap.id)
	  (obj,null)
	}
    //ObjectMap
    else{ 
	  val typeInResult=rs.getMetaData.getColumnType(rs.findColumn(poMap.id))
	  val datatype = if (poMap.objectMap.dtype!=null) 
	      poMap.objectMap.dtype
		else XsdTypes.sqlType2XsdType(typeInResult)	
	  if (poMap.objectMap.termType == IRIType)
	    (rs.getString(poMap.id),null)
	  if (poMap.objectMap.termType == LiteralType)
	    (rs.getString(poMap.id),datatype)
	  else if (poMap.objectMap.constant==null)
		(rs.getString(poMap.id),datatype)		
	  else				
		(poMap.objectMap.constant,null)
    }
  }
}