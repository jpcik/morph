package es.upm.fi.oeg.morph.relational

import java.sql.ResultSet
import com.hp.hpl.jena.datatypes.RDFDatatype
import es.upm.fi.oeg.siq.tools.XsdTypes
import org.slf4j.LoggerFactory


trait Dataset extends Iterator[Row]{
  override def hasNext:Boolean
  override def next:Row
  def getObject(name:String):Any
  def getString(name:String):String
  def getType(name:String):RDFDatatype
}

class Row()


class DbDataset(rs:ResultSet) extends Dataset{
  private val logger = LoggerFactory.getLogger(classOf[DbDataset])
  val metadata={
    val md=rs.getMetaData
    logger.debug("Columns in result: "+md.getColumnCount)
    (1 to md.getColumnCount).map{i=>
      logger.debug("column name: "+md.getColumnLabel(i))
      md.getColumnLabel(i)->XsdTypes.sqlType2XsdType(md.getColumnType(i))      
    }.toMap
  }
  private def hasMore={
    try {(!rs.isLast && !rs.isAfterLast)}
    catch {case ex:Exception=>false}
  }
  override def hasNext=rs.next
  override def next={
    //rs.next    
    new Row()
  }
  override def getObject(name:String)=rs.getObject(name)
  override def getString(name:String)=rs.getString(name)
  override def getType(name:String)=metadata(name.toUpperCase) 
  
}