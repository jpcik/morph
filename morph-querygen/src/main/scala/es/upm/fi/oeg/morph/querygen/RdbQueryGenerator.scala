package es.upm.fi.oeg.morph.querygen
import es.upm.fi.oeg.morph.r2rml.TriplesMap

class RdbQueryGenerator(val tm:TriplesMap) {
  def formatTemplate(template:String,alias:String)={
    val parts=template.split(Array('{','}'))
    if (parts.size==3)
      "('"+parts(0)+"' || "+formatColumn(parts(1))+" || '"+parts(2)+"') AS "+alias
    else
      "('"+parts(0)+"' || "+formatColumn(parts(1))+") AS "+alias
  }
  def formatSubjectTemplate=
    formatTemplate(tm.subjectMap.template,"subject")
  
  def formatColumn(column:String)=column
  def formatTable(table:String)=table
  def fromTable=
    if (tm.logicalTable.tableName!=null) formatTable(tm.logicalTable.tableName)
    else "("+tm.logicalTable.sqlQuery+")"
  def selectSubject=
    if (tm.subjectMap.column!=null) formatColumn(tm.subjectMap.column) +"AS subject"
    else formatSubjectTemplate
   
  def selectObject={
    tm.poMaps.map{po=>
      if (po.objectMap!=null) po.objectMap.col+" AS "+po.id+","
      else ""}.mkString.dropRight(1)  
  }
    
  def query={    
    "SELECT "+selectSubject+","+selectObject+" FROM "+fromTable
  }
}