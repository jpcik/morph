package es.upm.fi.oeg.morph.r2rml
import es.upm.fi.oeg.morph.voc.Vocabulary

object MorphVoc extends Vocabulary{
  override val prefix="http://oeg-upm.net/ns/morph#"
  val pk = property("pk")
} 

object R2RML extends Vocabulary{
  override val prefix="http://www.w3.org/ns/r2rml#"
 
  object SqlVersion extends Vocabulary{
    lazy val versions=Array(SQL2008,MSSQLServer,MySQL,PostgreSQL,HSQLDB,Oracle)
    override val prefix="http://www.w3.org/ns/r2rml#"
    val MySQL=resource("MySQL")  
    val Oracle=resource("Oracle")
    val SQL2008=resource("SQL2008")
    val MSSQLServer=resource("MSSQLServer")
    val HSQLDB=resource("HSQLDB")
    val PostgreSQL=resource("PostgreSQL")
  }  
    
  val TriplesMap = resource("TriplesMap")
  val subjectMap = property("subjectMap")
  val classProperty = property("class")
  val sqlQuery = property("sqlQuery")
  val sqlVersion = property("sqlVersion")
  val tableName = property("tableName")
  val logicalTable = property("logicalTable")	
  val subject = property("subject")
  
  val predicateObjectMap = property("predicateObjectMap")
  val predicateMap = property("predicateMap")
  val objectMap = property("objectMap")
  val graphMap = property("graphMap")
  val graph = property("graph")

  val constant = property("constant")
  val column = property("column")
  val datatype = property("datatype")
  val template = property("template")
  val termType = property("termType")
  val predicate = property("predicate")
  val objectProp = property("object")
  val language = property("language")

  val inverseExpression = property("inverseExpression") 
  val joinCondition = property("joinCondition")
  val parent=property("parent")
  val child=property("child")
  val parentTriplesMap = property("parentTriplesMap")
  val defaultGraph=resource("defaultGraph")
  
  val IRI=resource("IRI")
  val BlankNode=resource("BlankNode")
  val Literal=resource("Literal")    
}
