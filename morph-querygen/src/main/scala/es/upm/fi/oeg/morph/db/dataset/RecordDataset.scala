package es.upm.fi.oeg.morph.db.dataset

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype
import org.slf4j.LoggerFactory

class RecordDataset(val records:Stream[Record],val metadata:Metadata) extends Dataset{
  val it=records.iterator
  var current:Record=_
  
  override def hasNext=it.hasNext
  override def next={
    current=it.next; new Row
  }
  override def getType(name:String)={
    XSDDatatype.XSDstring
  }
  override def getObject(columnLabel:String):Any={
    current.getValue(columnLabel)
  }	
  override def getString(columnLabel:String):String={
    val obj=getObject(columnLabel:String)
    if (obj!=null) obj.toString else null
  }	
}

case class Metadata(datasetName:String,fieldsMeta:Seq[(String,Any)]){
  val fields=fieldsMeta.zipWithIndex.map{f=>
    f._1._1->RecordMetadata(f._1._1,f._1._2,f._2)
  }.toMap
}

object Metadata{
  def apply(datasetName:String,fields:Seq[String])(implicit d: DummyImplicit)=
    new Metadata(datasetName,fields.map(f=>f->XSDDatatype.XSDstring))
}

case class RecordMetadata(name:String,datatype:Any,pos:Int)  

abstract class Record(val fieldMeta:Map[String,RecordMetadata]){
  def getValue(fieldName:String):Any
}

class MapRecord(metadata:Map[String,RecordMetadata],values:Seq[Any]) extends Record(metadata){
  private val logger = LoggerFactory.getLogger(classOf[MapRecord])
  logger.debug(values.mkString("--"))
  override def getValue(fieldName:String)=values(metadata(fieldName).pos)
}

