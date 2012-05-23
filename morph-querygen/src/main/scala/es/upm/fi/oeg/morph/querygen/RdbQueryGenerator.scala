package es.upm.fi.oeg.morph.querygen
import es.upm.fi.oeg.morph.r2rml.TriplesMap

class RdbQueryGenerator(val tm:TriplesMap) {
  def formatTemplate(template:String,alias:String)={
    
    val parts=template.split(Array('{','}'))
    val vars= template.split('{').map{part=>
      if (part.endsWith("}")) formatColumn(part.dropRight(1))
      else if (part.contains("}")) part.substring(0,part.indexOf('}'))+" || '"+part.substring(part.indexOf('}')+1)+"'"
      else "'"+part+"'"
    }.mkString(" || ")
    "("+vars+") AS "+alias 
  }
  def formatSubjectTemplate=
    formatTemplate(tm.subjectMap.template,"subject")
    
  
    
  def formatColumn(column:String)=column
  def formatTable(table:String)=table
  def fromTable=
    if (tm.logicalTable.tableName!=null) formatTable(tm.logicalTable.tableName)
    else "("+tm.logicalTable.sqlQuery+")"
  def selectSubject=
    if (tm.subjectMap.column!=null) formatColumn(tm.subjectMap.column) +" AS subject"
    else if (tm.subjectMap.template!=null) formatSubjectTemplate
    else "'"+tm.subjectMap.constant+ "' AS subject"
   
  def selectObject={
    tm.poMaps.map{po=>
      if (po.objectMap!=null && po.objectMap.col!=null) po.objectMap.col+" AS "+po.id
      else if (po.objectMap!=null && po.objectMap.temp!=null) formatTemplate(po.objectMap.temp,po.id)
      else if (po.objectMap!=null && po.objectMap.constant!=null) "'"+po.objectMap.constant+"' AS "+po.id
   //   else if (po.refObjectMap!=null) 
      else ""}.mkString(",")
  }
  
  def selectGraph={
    val gMap=tm.subjectMap.graphMap
    if (gMap!=null && gMap.template!=null) ","+formatTemplate(gMap.template,"subjectGraph")
    else ""
  }
    
  def query={    
    "SELECT "+selectSubject+selectGraph+","+selectObject+" FROM "+fromTable
  }
}