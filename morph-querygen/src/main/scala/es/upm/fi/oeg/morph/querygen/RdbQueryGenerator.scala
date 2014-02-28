package es.upm.fi.oeg.morph.querygen
import es.upm.fi.oeg.morph.r2rml.TriplesMap
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.r2rml.JoinCondition
import es.upm.fi.oeg.morph.r2rml.RefObjectMap
import es.upm.fi.oeg.morph.r2rml.R2rmlUtils._

class RdbQueryGenerator(val tm: TriplesMap, r2rml: R2rmlReader, sqlDialect: SQLDialect = new DefaultSQL) {

  def formatTemplate(template: String, alias: String): String =
    formatTemplate(template, alias, null, null)

  def formatTemplate(template: String, alias: String, colName: String, parent: TriplesMap, concatChar: String = sqlDialect.concatChar) = {

    val tableName = if (parent != null) parent.logicalTable.tableName
    else "TABLE1"
    val escaped = template.replace("\\{", "$lbrace").replace("\\}", "$rbrace")
    val vars = escaped.split('{').map { part =>
      if (part.endsWith("}"))
        formatColumn(if (colName != null) colName else part.dropRight(1), tableName)
      else if (part.contains("}"))
        formatColumn(part.substring(0, part.indexOf('}')), tableName) + " " + concatChar + " '" + part.substring(part.indexOf('}') + 1) + "'"
      else "'" + part + "'"
    }
      .mkString(" " + concatChar + " ")
      .replace("$lbrace", "{").replace("$rbrace", "}")
    "(" + vars + ") AS " + alias
  }

  def formatSubjectTemplate =
    formatTemplate(tm.subjectMap.template, "subject")

  def formatColumn(column: String): String = formatColumn(column, null)
  def formatColumn(column: String, table: String) =
    if (table != null) table + "." + column
    else if (column.startsWith("\"")) column
    else column // "\""+column+"\""

  def formatTable(table: String) = table
  def fromTable =
    if (tm.logicalTable.tableName != null) formatTable(tm.logicalTable.tableName) + " TABLE1 " + joinTables
    else formatQuery(tm.logicalTable.sqlQuery) + " TABLE1 " + joinTables

  private def formatQuery(q: String) = {
    val qu = q.replaceAll("\t", "").replaceAll("\n", "").trim
    "(" + (if (qu.trim.endsWith(";")) qu.dropRight(1) else qu) + ")"
  }

  lazy val joinTables = {
    val roMaps = tm.poMaps.map(_.refObjectMap).filter(_ != null)
    roMaps.map { roMap =>
      val lt = r2rml.triplesMaps(roMap.parentTriplesMap).logicalTable
      val table = if (lt.tableName != null) lt.tableName else formatQuery(lt.sqlQuery)
      " LEFT OUTER JOIN " + table + " TABLE2 ON " + formatJoinCondition(roMap)
    }.mkString

  }

  lazy val tableNames = {
    val parents = tm.poMaps.map(_.refObjectMap).filter(_ != null).map(_.parentTriplesMap)
    val logTables = parents.map(p => r2rml.triplesMaps(p)).map(_.logicalTable)
    val tables = logTables.map(lt => if (lt.tableName != null) lt.tableName + " TABLE2 " else formatQuery(lt.sqlQuery) + " TABLE2")
    tables.map(t => " INNER JOIN " + t).mkString
    //if (tables.size>0) ","+tables.mkString(",")
    //else ""
  }

  def selectSubject =
    if (tm.subjectMap.column != null) formatColumn(tm.subjectMap.column) + " AS subject"
    else if (tm.subjectMap.template != null) formatSubjectTemplate
    else "'" + tm.subjectMap.constant + "' AS subject"

  def selectObject = {
    tm.poMaps.map { po =>
      if (po.objectMap != null && po.objectMap.col != null) formatColumn(po.objectMap.col, "TABLE1") + " AS " + po.id
      else if (po.objectMap != null && po.objectMap.temp != null) formatTemplate(po.objectMap.temp, po.id)
      else if (po.objectMap != null && po.objectMap.constant != null) "'" + po.objectMap.constant + "' AS " + po.id
      else if (po.refObjectMap != null) {
        val parent = r2rml.tMaps.find(_.uri == po.refObjectMap.parentTriplesMap).get
        val childCol = if (po.refObjectMap.joinCondition != null) po.refObjectMap.joinCondition.child
        else parent.subjectMap.column
        if (parent.subjectMap.template != null)
          formatTemplate(parent.subjectMap.template, po.id, childCol, null)
        else
          parent.subjectMap.column + " AS " + po.id
      } else po.objectMap.toString
    }.mkString(",")
  }

  def selectGraph = {
    val gMap = tm.subjectMap.graphMap
    if (gMap != null && gMap.template != null) "," + formatTemplate(gMap.template, "subjectGraph")
    else ""
  }

  private def formatJoinCondition(roMap: RefObjectMap) = {
    val parent = r2rml.triplesMaps(roMap.parentTriplesMap)
    val join = roMap.joinCondition
    if (join == null)
      "TABLE2." + extractColumn(parent.subjectMap) + "= TABLE1." + extractColumn(parent.subjectMap)
    else "TABLE2." + join.parent + "=" + "TABLE1." + join.child
  }

  lazy val joinConditions = {
    val roMaps = tm.poMaps.map(_.refObjectMap).filter(_ != null)
    roMaps.filter(_.joinCondition != null).map(romap => formatJoinCondition(romap))
  }

  def where =
    if (joinConditions.size > 0)
      " WHERE " + joinConditions.mkString(" and ")
    else ""

  def query = {
    "SELECT " + selectSubject + selectGraph + "," + selectObject + " FROM " + fromTable //+where 
  }
}

