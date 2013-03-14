package es.upm.fi.oeg.morph.r2rml

import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.rdf.model.RDFNode
import com.hp.hpl.jena.datatypes.RDFDatatype
import es.upm.fi.oeg.morph.r2rml.R2rmlUtils._
import java.net.URI
import es.upm.fi.oeg.siq.tools.URLTools
import java.util.Locale

case class TriplesMap(uri:String,logicalTable:LogicalTable,
    subjectMap:SubjectMap,poMaps:Array[PredicateObjectMap]){
  if (subjectMap==null)
    throw new R2rmlModelException("Invalid TriplesMap without subject "+uri)
}

class TermType(val resource:Resource){
  def isBlank=resource.equals(R2RML.BlankNode)
  def isIRI=resource.equals(R2RML.IRI)
  def isLiteral=resource.equals(R2RML.Literal)
  override def toString="Term("+resource.toString+")"
  def equals(tt:TermType):Boolean=if (tt==null) false
    else (resource == null && tt.resource==null) ||
      (resource!=null && resource.equals(tt.resource))
}

object TermType {
  def apply(resource:Resource)=
    if (resource==null) null
    else resource match{
    case R2RML.BlankNode=>BlankNodeType
    case R2RML.IRI=>IRIType
    case R2RML.Literal=>LiteralType
    case _=>throw new R2rmlModelException("Invalid TermType "+resource)
  }
}

object IRIType extends TermType(R2RML.IRI)
object BlankNodeType extends TermType(R2RML.BlankNode)
object LiteralType extends TermType(R2RML.Literal)

abstract class TermMap(val constant:RDFNode,val column:String,
  val template:String,val termType:TermType){
  def allnull(a:Any*)=a.forall(_==null)
}
    
case class SubjectMap(const:RDFNode,col:String,temp:String,term:TermType,
  rdfsClass:Resource,graphMap:GraphMap) extends TermMap(const,col,temp,term){
  override val termType=if (term==null) IRIType else term
  if (termType.equals(LiteralType)) 
    throw new R2rmlModelException("Invalid Term Type sor SubjectMap: "+termType)
  if (allnull(constant,column,template))
    throw new R2rmlModelException("SubjectMap template, column or constant must be defined ")
  
  def this(constant:RDFNode,rdfsClass:Resource,graphMap:GraphMap)=
    this(constant,null,null,IRIType,rdfsClass,graphMap)
  def this(template:String,termType:TermType,rdfsClass:Resource,graphMap:GraphMap)=
    this(null,null,template,termType,rdfsClass,graphMap)  
}

class GraphMap(const:RDFNode,col:String,temp:String) 
  extends TermMap(const,col,temp,IRIType){
  def this(constant:RDFNode)=this(constant,null,null)
  def this(template:String)=this(null,null,template)
}

object GraphMap{
  def apply(constant:RDFNode,column:String,template:String,termType:TermType)={
    if (termType!=null && termType != IRIType)
      throw new R2rmlModelException("Non IRI terms not allowed for graphMap")
    if (constant !=null && constant.asResource.equals(R2RML.defaultGraph)) null
    else if (constant !=null) new GraphMap(constant)
    else if (template !=null) new GraphMap(template)
    else null
  }
}

case class ObjectMap(const:RDFNode,col:String,temp:String,term:TermType,
    language:String,dtype:RDFDatatype,inv:String)
  extends TermMap(const,col,temp,term){  
  if (language!=null){
    if (!LocaleUtils.locales.contains(language))
      throw new R2rmlModelException("invalid language code: "+language)
  }
  override val termType=if (term!=null) term else
    if (col!=null || language!=null || dtype!=null) LiteralType
    else IRIType
}

object LocaleUtils{
  val locales=Locale.getAvailableLocales.map(_.toString).toSet
}


case class RefObjectMap(parentTriplesMap:String,joinCondition:JoinCondition) 

case class PredicateMap(const:RDFNode,col:String,temp:String)
  extends TermMap(const,col,temp,IRIType){
  if (allnull(const,col,temp)) throw new R2rmlModelException("PredicateMap requires constant, column or template")
  def this(constant:RDFNode)=this(constant,null,null)

}

case class JoinCondition(parent:String,child:String)

case class PredicateObjectMap(predicateMap:PredicateMap,objectMap:ObjectMap,
    refObjectMap:RefObjectMap,graphMap:GraphMap){
  private val objId= if (objectMap!=null) extractColumn(objectMap)
    else new URI(refObjectMap.parentTriplesMap).getFragment
  val id=idfy(predicateMap.const.asResource.getLocalName+ (if (objId!=null) objId else ""))
  private def idfy(value:String)=URLTools.stripSpecial(value)
  def this(predicateMap:PredicateMap,objectMap:ObjectMap,graphMap:GraphMap)=this(predicateMap,objectMap,null,graphMap)
  def this(predicateMap:PredicateMap,refObjectMap:RefObjectMap,graphMap:GraphMap)=this(predicateMap,null,refObjectMap,graphMap)
  def predicateIs(uri:String)=predicateMap.constant!=null && predicateMap.constant.asResource.getURI.equals(uri)
}

case class LogicalTable(tableName:String,sqlQuery:String,sqlVersion:RDFNode,pk:Set[String]){
  if (sqlVersion!=null && !exists(sqlVersion.asResource))
    throw new R2rmlModelException("Unsupported SQL Version: "+sqlVersion,null)
  def this(tableName:String)=this(tableName,null,null,null)
  private def exists(version:Resource)= {
   val vers =R2RML.SqlVersion.versions
   !vers.filter( _.getURI.equals(version.getURI)).isEmpty 
  }
}

class R2rmlModelException(msg:String,e:Throwable) extends Exception(msg,e){
  def this(msg:String)=this(msg,null)
}