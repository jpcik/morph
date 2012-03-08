package es.upm.fi.oeg.morph.querygen
import es.upm.fi.dia.oeg.morph.r2rml.TriplesMap

class RdbQueryGenerator(val tMap:TriplesMap) {
  
  def query={
    if (tMap.getSubjectMap.getColumn!=null)
    "SELECT \""+tMap.getSubjectMap.getColumn+ "\" FROM \""+tMap.getTableName+"\""
    else
    "SELECT \""+tMap.getSubjectMap.getTemplate.split(Array('{','}'))(1)+ "\" FROM \""+tMap.getTableName+"\""
    //"SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'"
  }
}