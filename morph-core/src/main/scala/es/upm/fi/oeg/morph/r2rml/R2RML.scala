package es.upm.fi.oeg.morph.r2rml
import es.upm.fi.oeg.morph.voc.Vocabulary

class R2RML {}
object R2RML extends Vocabulary{
  override val prefix="http://www.w3.org/ns/r2rml#"
 
  val sqlQuery = property("sqlQuery");
  val classProperty = property("class");
  val constant = property("constant");
  val column = property("column");
  val subject = property("subject");
  val subjectMap = property("subjectMap");
  val predicateObjectMap = property("predicateObjectMap");
  val predicateMap = property("predicateMap");
  val objectMap = property("objectMap");
  //val refPredicateObjectMap = property("refPredicateObjectMap");
  //val refPredicateMap = property("refPredicateMap");
  //val refObjectMap = property("refObjectMap");
    
  val predicate = property("predicate");
  //val propertyColumn = property("propertyColumn");
  //val rdfTypeProperty = property("RDFTypeProperty");
  val datatype = property("datatype");
  val tableName = property("tableName");
  val logicalTable = property("logicalTable");	

  val template = property("template");
  val objectProp = property("object");
  //val tableGraphIRI =property("tableGraphIRI");
  //val rowGraph = property("rowGraph");
  val inverseExpression = property("inverseExpression"); 
  //val columnGraphIRI = property("columnGraphIRI");

  val termType = property("termType");
  val graph = property("graph");
  //val graphColumn = property("graphColumn");
  val TriplesMap = resource("TriplesMap");

  val joinCondition = property("joinCondition");
  val parentTriplesMap = property("parentTriplesMap");
  
  val IRI=resource("IRI")
  val BlankNode=resource("BlankNode")
  val Literal=resource("Literal")

}
