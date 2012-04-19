package es.upm.fi.oeg.morph.querygen
import es.upm.fi.oeg.morph.r2rml.TriplesMap

class RdbQueryGenerator(val tm:TriplesMap) {
  def formatTemplate(template:String,alias:String)={
    val parts=template.split(Array('{','}'))
    if (parts.size==3)
      "('"+parts(0)+"' || \""+parts(1)+"\" || '"+parts(2)+"') AS "+alias
    else
      "('"+parts(0)+"' || \""+parts(1)+"\") AS "+alias
  }
  def formatSubjectTemplate=
    formatTemplate(tm.subjectMap.template,"subj")
  
  def formatColumn(column:String)="\""+column+"\""
  def formatTable(table:String)="\""+table+"\""
  def fromTable=
    if (tm.logicalTable.tableName!=null) formatTable(tm.logicalTable.tableName)
    else "("+tm.logicalTable.sqlQuery+")"
  def selectSubject=
    if (tm.subjectMap.column!=null) formatColumn(tm.subjectMap.column)
    else formatSubjectTemplate
    
  def query={    
    "SELECT "+selectSubject+" FROM "+fromTable
  }
}