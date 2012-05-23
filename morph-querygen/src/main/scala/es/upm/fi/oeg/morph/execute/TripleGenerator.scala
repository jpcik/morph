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

case class TripleGenerator(d:DataSource,tm:TriplesMap){
  def genModel=d.getDefaultModel
   
  def slugify(str: String): String = {
    import java.text.Normalizer
    Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\w ]", "").replace(" ", "-").toLowerCase
  }
  
  def urlize(url:String)= {
   //URLEncoder.encode(url.toString(),"UTF-8");
   url.replace(" ","%20") 
  } 
  
  def genSubject(rs:ResultSet)={
    val subj=tm.subjectMap
	val col="SUBJECT"//extractColumn(subj)
    println("metacolumn "+rs.getMetaData.getColumnName(1))
    val id = urlize( rs.getString(col))
	val ttype=subj.termType
	if (ttype.isBlank)
	  genModel.createResource(new AnonId(id))
	else if (subj.template!=null)
	  genModel.createResource(id)//generateTemplateVal(subj.template,id))
	else
	  genModel.createResource(id)
  }

  def genRdfType()={
	tm.subjectMap.rdfsClass		
  } 
  def genGraph(rs:ResultSet)={
    val gMap=tm.subjectMap.graphMap
    if (gMap.template!=null)
      urlize(rs.getString("SUBJECTGRAPH"))
     else null
  }
}

case class POGenerator(ds:DataSource,poMap:PredicateObjectMap,parentTMap:TriplesMap){
  def genPredicate={
    ResourceFactory.createProperty(
      poMap.predicateMap.constant.asResource.getURI)
  }
  
  			
  def generate(subj:Resource,rs:ResultSet)={
    if (poMap.refObjectMap!=null){
	  val obj=new TripleGenerator(ds,parentTMap).genSubject(rs)
	  (obj,null)
	}
    else{
	  val typeInResult=rs.getMetaData.getColumnType(rs.findColumn(poMap.id))
	  val datatype = if (poMap.objectMap.dtype!=null) 
	      poMap.objectMap.dtype
		else sqlType2XsdType(typeInResult)	
		
	  if (poMap.objectMap.constant==null)
		(rs.getString(poMap.id),datatype)		
	  else				
		(poMap.objectMap.constant,null)
    }
  }
  
  def sqlType2XsdType(sqlType:Int)=
    if (sqlType==Types.NVARCHAR)  XSDDatatype.XSDstring
	else if (sqlType==Types.INTEGER) XSDDatatype.XSDinteger
	else if (sqlType==Types.DOUBLE) XSDDatatype.XSDdouble
	else null
  
}

