package es.upm.fi.oeg.morph.relational
import java.util.Properties
import java.sql.ResultSet
import java.sql.ResultSetMetaData
//import com.sun.jersey.api.client.Client
//import com.sun.jersey.core.util.MultivaluedMapImpl
//import com.sun.jersey.api.client.UniformInterfaceException
//import com.google.gson.Gson
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

class RestRelationalModel(props:Properties) extends RelationalModel(props,true){
  override def query(queryString:String):ResultSet={
    def metadata=Seq("id","name","timestamp","bikes","free")
    val metawithPos=metadata.zipWithIndex.toMap
    val svc = url("http://api.citybik.es/bizi.json")
    val resp = Http(svc OK as.String)
    val json:JsArray = Json.parse(resp()).asInstanceOf[JsArray]
    val data=json.value.map{js=>
      val tt=new MapResult("bizi",metawithPos,metadata.map{s=>
       (js \ s) match{
          case st:JsString=>st.value
          case p =>p.toString          
        }
        })
      tt
    }
    new RestResultSet(data.toStream,metadata.map(_->null).toMap)

    /*
    println(data.mkString("."))
    val c = Client.create
    val webResource = c.resource("http://api.citybik.es/bizi.json")
    val queryParams = new MultivaluedMapImpl()
		   
    val wr = webResource.queryParams(queryParams)
	println(wr.getURI.toString)
	val res=try wr.get(classOf[String])
	catch {case e:UniformInterfaceException=>throw e}
    
	val cons=Class.forName("es.upm.fi.oeg.morph.relational.Station$").getDeclaredConstructors()
	cons(0).setAccessible(true)
	cons(0).newInstance().asInstanceOf[Decoder].getResultSet(res)*/
  } 
}
/*
case class Station(id:String,name:String,lat:Long,lng:Long,timestamp:String,bikes:Int,free:Int)
    extends RestResult(id,Station.metadata){
  
  override def getValue(fieldName:String)=
    if (fieldName.equals("timestamp")) timestamp
    else if (fieldName.equals("bikes")) bikes.toString
    else throw new IllegalArgumentException("Unknown field: "+fieldName)
}
object Station extends Decoder{
  def metadata=Set("id","name","timestamp","bikes","free")
  def getResultSet(json:String)={
    val g = new Gson();
	val col = g.fromJson(json, classOf[Array[Station]]);
    new RestResultSet(col.toStream,Station.metadata.map(_->null).toMap)
  }
}

abstract class Decoder{
  def getResultSet(json:String):RestResultSet
}
*/
class MapResult(name:String,metadata:Map[String,Int],values:Seq[String]) extends RestResult(name,metadata.keys.toSet){
  println(values.mkString("--"))
  override def getValue(fieldName:String)=values(metadata(fieldName))
}

abstract class RestResult(val resultName:String,val fieldNames:Set[String]){
  def getValue(fieldName:String):String
}

class RestResultSet(val records:Stream[RestResult],val metadata:Map[String,String]) extends BaseResultSet{
  val it=records.iterator
  var current:RestResult=_
  
  override def next:Boolean ={ 
	if (it.hasNext){
      current=it.next
      true
	}
	else false
  }

  override def close {records.clear }
	
  private val metaData:ResultSetMetaData= {
	val md:RowSetMetaData = new RowSetMetaDataImpl   
	md.setColumnCount(metadata.size)
	var i=1
	metadata.foreach{e=>
	  md.setColumnLabel(i, e._1)
	  md.setColumnType(i, Types.VARCHAR)
	  i+=1
	}
	md
  }
				
  override def getMetaData:ResultSetMetaData=  metaData  
  override def findColumn(column:String)=0
  override def getObject(i:Int)=null	
  override def getObject(columnLabel:String):Object={
    current.getValue(columnLabel)
  }	
  override def getString(columnLabel:String):String=getObject(columnLabel:String).toString
	
}