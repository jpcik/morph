package es.upm.fi.oeg.morph.relational
import java.util.Properties
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import collection.JavaConversions._
import javax.sql.RowSetMetaData
import javax.sql.rowset.RowSetMetaDataImpl
import java.sql.Types
import dispatch._
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.JsArray
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import org.slf4j.LoggerFactory
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype

class RestRelationalModel() extends RelationalModel(null,true){
  override def query(queryString:String):Dataset={
    def metadata=Seq("id","name","timestamp","bikes","free")
    val metawithPos=metadata.zipWithIndex.toMap
    val svc = url("http://api.citybik.es/valenbisi.json")
    val resp = Http(svc OK as.String)
    val json:JsArray = Json.parse(resp()).asInstanceOf[JsArray]
    val data=json.value.map{js=>
      val tt=new MapResult("bizi",metadata,metadata.map{s=>
       (js \ s) match{
          case st:JsString=>st.value
          case p =>p.toString          
        }
        })
      tt
    }
    new RestResultSet(data.toStream,metadata.map(_->null).toMap)
  } 
}

class MapResult(name:String,metadata:Seq[String],values:Seq[String]) extends RestResult(name,metadata){
  private val logger = LoggerFactory.getLogger(classOf[MapResult])
  val idx=metadata.zipWithIndex.toMap
  logger.debug(values.mkString("--"))
  override def getValue(fieldName:String)=values(idx(fieldName))
}

abstract class RestResult(val resultName:String,val fieldNames:Seq[String]){
  def getValue(fieldName:String):String
}

class RestResultSet(val records:Stream[RestResult],val metadata:Map[String,String]) extends Dataset{
  val it=records.iterator
  var current:RestResult=_
  
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
  override def getString(columnLabel:String):String=getObject(columnLabel:String).toString
	
}