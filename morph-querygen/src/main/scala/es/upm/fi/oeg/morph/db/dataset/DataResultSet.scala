package es.upm.fi.oeg.morph.db.dataset
import org.slf4j.LoggerFactory
import javax.sql.rowset.RowSetMetaDataImpl
import javax.sql.RowSetMetaData
import java.sql.ResultSetMetaData
import java.sql.Types

@Deprecated
class DataResultSet(val records:Seq[Array[Any]], 
    val metadata: Map[String, String],queryVars:Array[String]) extends BaseResultSet {
  private var it = records.iterator
  private var current: Seq[Any] = _
  private val logger = LoggerFactory.getLogger(this.getClass)
  
  override def next:Boolean = {
    if (it.hasNext) {
      current = it.next
      logger.trace(current.mkString("----"))
      true
    } else false
  }

  override def beforeFirst{it=records.iterator}
  override def close {}
  override def getHoldability():Int = 0
  override def isClosed():Boolean = false

  protected def createMetadata={
    val md: RowSetMetaData = new RowSetMetaDataImpl
    md.setColumnCount(metadata.size)
    var i=1
    metadata.foreach{e=>
      logger.trace("metadata "+e._1+"--"+e._2)
      md.setColumnLabel(i, e._1)
      md.setColumnType(i, Types.VARCHAR)
      i+=1
    }
    md
  }
  private val metaData: ResultSetMetaData = createMetadata
  
  private val internalLabels=queryVars.zipWithIndex.toMap

  private val compoundLabels={
    val spk=internalLabels.keys.toArray.filter(k=>k.contains('_')).map{k=>
      val sp=k.split('_')
      (sp(0),sp(1))      
    }
    val grouped=spk.groupBy(_._1).map(v=>(v._1,v._2.map(v2=>v2._2)))
    grouped
  }
  
  private val labelPos=
    (1 to metaData.getColumnCount).map(i=>metaData.getColumnLabel(i)->i).toMap
    
  override def findColumn(columnLabel:String):Int = labelPos(columnLabel)
  override def getMetaData:ResultSetMetaData=metaData
  override def getObject(columnIndex:Int):Object=current(columnIndex-1).asInstanceOf[Object]
  
  override def getObject(columnLabel:String):Object={ 
    logger.trace(internalLabels.mkString)
    current(internalLabels(columnLabel)).asInstanceOf[Object]
  }

  override def getString(columnLabel:String):String = 
    getObject(columnLabel:String).toString
}

