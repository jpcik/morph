package es.upm.fi.oeg.morph.relational
import es.upm.fi.dia.oeg.morph.relational.RelationalModel
import java.util.Properties
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.io.InputStream
import java.sql.SQLWarning
import java.io.Reader
import java.sql.Ref
import java.sql.Blob
import java.sql.Clob
import java.sql.Statement
import java.util.Calendar
import java.net.URL
import java.sql.RowId
import java.sql.NClob
import java.sql.SQLXML
import com.sun.jersey.api.client.Client
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.sun.jersey.api.client.UniformInterfaceException
import com.google.gson.Gson
import collection.JavaConversions._
import javax.sql.RowSetMetaData
import javax.sql.rowset.RowSetMetaDataImpl
import java.sql.Types

class RestRelationalModel extends RelationalModel{
  override def configure(props:Properties){////throws InstantiationException, IllegalAccessException, ClassNotFoundException;
  }
  override def query(queryString:String):ResultSet={
    val c = Client.create();
    val webResource = c.resource("http://api.citybik.es/bizi.json")
    val queryParams = new MultivaluedMapImpl();
			//queryParams.add("interval", "0");*/
		   //queryParams.add("param2", "val2");
		   
    val wr = webResource.queryParams(queryParams)
	println(wr.getURI().toString())
	val res=try{
	  wr.get(classOf[String]);
	} catch {case e:UniformInterfaceException=>
			  //println(e..getResponse().get e.printStackTrace)
	  throw e
	}
	val cons=Class.forName("es.upm.fi.oeg.morph.relational.Station$").getDeclaredConstructors()
	cons(0).setAccessible(true)
	cons(0).newInstance().asInstanceOf[Decoder].getResultSet(res)
  } ////throws SQLException;
}

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

abstract case class RestResult(resultName:String,fieldNames:Set[String]){
  def getValue(fieldName:String):String
}

  
  
class RestResultSet(val records:Stream[RestResult],val metadata:Map[String,String]) extends ResultSet{
  val it=records.iterator
  var current:RestResult=_
  
  override def unwrap[T](iface:java.lang.Class[T]):T={
    null.asInstanceOf[T]
  } //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

  override def isWrapperFor(iface:java.lang.Class[_]):Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

  override def next:Boolean ={ 
	if (it.hasNext){
      current=it.next
      true
	}
	else false
  }

  override def close {
    records.clear
  }
	
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
		//md.setColumnLabel(i, "extentname");
		//columns.put("extentname", i);

		//return md;*/
//}
	
	override def findColumn(columnLabel:String):Int=0 //throws SQLException {
		//if (columns==null)
			//getMetaData();		
		//return columns.get(columnLabel);
	

	override def getMetaData:ResultSetMetaData= //getMetaData() //throws SQLException
	{
	  /*
		if (metadata==null)
			metadata = createMetaData();
		//return metadata;*/metaData
	}

	override def getObject(columnIndex:Int):Object=// //throws SQLException 
	{
		//String label = metadata.getColumnLabel(columnIndex);
		////return getObject(label);
		null
		//throw new QueryException("No data for index "+columnIndex+" "+label+" "+colName);
	}

  override def getObject(columnLabel:String):Object={// //throws SQLException {
    current.getValue(columnLabel)
	  /*
		if (columnLabel.equals("extentname"))
			//return current.getId();
		for (Environment e:query.getEnvironments())
		{
			if (current.getId().equals(e.getId()))
			{
				if (e.getTimeAlias().contains(columnLabel))
					//return currentDs.getAt();
				else
				for (Datastream ds: e.getDatastreams())
				{
					if (ds.getAlias().equals(columnLabel))
						//return currentDs.getCurrent_value();
					//else if (ds.getTimeAlias().equals(columnLabel))
						////return currentDs.getAt();
				}
			}
		}
			
		throw new QueryException("No data for column "+columnLabel);
		*/	
	  
	}
	
	override def getString(columnLabel:String):String=getObject(columnLabel:String).toString
	
	
	override def wasNull():Boolean=false// //throws SQLException {

	override def getString(columnIndex:Int):String=null// //throws SQLException {

	override def getBoolean(columnIndex:Int):Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;

	override def getByte(columnIndex:Int):Byte=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def getShort(columnIndex:Int):Short=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	
	override def getInt(columnIndex:Int):Int=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	
	override def getLong(columnIndex:Int):Long=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def getFloat(columnIndex:Int):Float=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def getDouble(columnIndex:Int):Double=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  getBigDecimal(columnIndex:Int, scale:Int):java.math.BigDecimal=null
			//throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def getBytes(columnIndex:Int):Array[Byte]=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def getDate(columnIndex:Int):Date=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getTime(columnIndex:Int):Time=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getTimestamp(columnIndex:Int):Timestamp=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getAsciiStream(columnIndex:Int):InputStream=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getUnicodeStream(columnIndex:Int):InputStream=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getBinaryStream(columnIndex:Int):InputStream=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	


	override def  getBoolean(columnLabel:String):Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def getByte(columnLabel:String):Byte=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def getShort(columnLabel:String):Short=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	
	override def  getInt(columnLabel:String):Int=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  getLong(columnLabel:String):Long=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  getFloat(columnLabel:String):Float=0//throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  getDouble(columnLabel:String):Double=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  getBigDecimal(columnLabel:String, scale:Int):java.math.BigDecimal=null
			//throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def getBytes(columnLabel:String):Array[Byte]=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getDate(columnLabel:String):Date=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getTime(columnLabel:String):Time=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getTimestamp(columnLabel:String):Timestamp=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def getAsciiStream(columnLabel:String):InputStream=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getUnicodeStream(columnLabel:String):InputStream=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getBinaryStream(columnLabel:String):InputStream=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def getWarnings():SQLWarning=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  clearWarnings() {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  getCursorName():String=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	


	override def  getCharacterStream(columnIndex:Int):Reader=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getCharacterStream(columnLabel:String):Reader=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def getBigDecimal(columnIndex:Int):java.math.BigDecimal=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getBigDecimal(columnLabel:String):java.math.BigDecimal=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  isBeforeFirst():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  isAfterLast():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  isFirst():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  isLast():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  beforeFirst() {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  afterLast() {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  first():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  last():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def getRow():Int=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  absolute(row:Int):Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  relative(rows:Int):Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  previous():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  setFetchDirection(direction:Int){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  getFetchDirection():Int=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  setFetchSize(rows:Int) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  getFetchSize():Int=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  getType():Int=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  getConcurrency():Int=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  rowUpdated():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  rowInserted():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  rowDeleted():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  updateNull(columnIndex:Int) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBoolean(columnIndex:Int, x:Boolean) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateByte(columnIndex:Int, x:Byte) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateShort(columnIndex:Int, x:Short) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateInt(columnIndex:Int, x:Int){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateLong(columnIndex:Int, x:Long) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateFloat(columnIndex:Int, x:Float) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateDouble(columnIndex:Int, x:Double) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBigDecimal(columnIndex:Int, x:java.math.BigDecimal){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateString(columnIndex:Int, x:String) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBytes(columnIndex:Int, x:Array[Byte]){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateDate(columnIndex:Int, x:Date){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateTime(columnIndex:Int, x:Time){}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateTimestamp(columnIndex:Int, x:Timestamp){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateAsciiStream(columnIndex:Int, x:InputStream, length:Int){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBinaryStream(columnIndex:Int, x:InputStream, length:Int){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateCharacterStream(columnIndex:Int, x:Reader, length:Int){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateObject(columnIndex:Int, x:Object, scaleOrLength:Int){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateObject(columnIndex:Int, x:Object){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateNull(columnLabel:String){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBoolean(columnLabel:String, x:Boolean){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateByte(columnLabel:String, x:Byte){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateShort(columnLabel:String, x:Short){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateInt(columnLabel:String, x:Int){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateLong(columnLabel:String, x:Long){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateFloat(columnLabel:String, x:Float){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateDouble(columnLabel:String, x:Double){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBigDecimal(columnLabel:String, x:java.math.BigDecimal){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateString(columnLabel:String, x:String) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBytes(columnLabel:String, x:Array[Byte]){} //throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateDate(columnLabel:String, x:Date){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateTime(columnLabel:String, x:Time){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateTimestamp(columnLabel:String, x:Timestamp){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateAsciiStream(columnLabel:String, x:InputStream, length:Int){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBinaryStream(columnLabel:String, x:InputStream, length:Int){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateCharacterStream(columnLabel:String, reader:Reader,
			length:Int){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateObject(columnLabel:String, x:Object, scaleOrLength:Int){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateObject(columnLabel:String, x:Object) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  insertRow(){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateRow(){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  deleteRow() {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  refreshRow() {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  cancelRowUpdates() {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  moveToInsertRow(){} //throws SQLException {
		// TODO Auto-generated method stub
		
	override def  moveToCurrentRow() {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def getStatement():Statement=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def getObject(columnIndex:Int, map:java.util.Map[String, java.lang.Class[_]]):Object=null
			//throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getRef(columnIndex:Int):Ref=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getBlob(columnIndex:Int):Blob=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getClob(columnIndex:Int):Clob=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getArray(columnIndex:Int):java.sql.Array=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getObject(columnLabel:String, map:java.util.Map[String, java.lang.Class[_]]):Object=null
			//throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getRef(columnLabel:String):Ref=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getBlob(columnLabel:String):Blob=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getClob(columnLabel:String):Clob=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getArray(columnLabel:String):java.sql.Array=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getDate(columnIndex:Int, cal:Calendar):Date=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getDate(columnLabel:String, cal:Calendar):Date=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getTime(columnIndex:Int, cal:Calendar):Time=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;


	override def  getTime(columnLabel:String, cal:Calendar):Time=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;

	override def  getTimestamp(columnIndex:Int, cal:Calendar):Timestamp=null
			//throws SQLException {
		// TODO Auto-generated method stub
		//return null;

	override def  getTimestamp(columnLabel:String, cal:Calendar):Timestamp=null
			//throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def getURL(columnIndex:Int):URL=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;

	override def  getURL(columnLabel:String):URL=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  updateRef(columnIndex:Int, x:Ref){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateRef(columnLabel:String, x:Ref) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBlob(columnIndex:Int, x:Blob){} //throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateBlob(columnLabel:String, x:Blob){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateClob(columnIndex:Int, x:Clob){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def updateClob(columnLabel:String, x:Clob){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateArray(columnIndex:Int, x:java.sql.Array){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateArray(columnLabel:String, x:java.sql.Array){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def getRowId(columnIndex:Int):RowId=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getRowId(columnLabel:String):RowId=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  updateRowId(columnIndex:Int, x:RowId){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateRowId(columnLabel:String, x:RowId){} //throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  getHoldability():Int=0 //throws SQLException {
		// TODO Auto-generated method stub
		//return 0;
	

	override def  isClosed():Boolean=false //throws SQLException {
		// TODO Auto-generated method stub
		//return false;
	

	override def  updateNString(columnIndex:Int, nString:String){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateNString(columnLabel:String, nString:String){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateNClob(columnIndex:Int, nClob:NClob) {}//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateNClob(columnLabel:String, nClob:NClob){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  getNClob(columnIndex:Int):NClob=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getNClob(columnLabel:String):NClob=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getSQLXML(columnIndex:Int):SQLXML=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getSQLXML(columnLabel:String):SQLXML=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  updateSQLXML(columnIndex:Int, xmlObject:SQLXML ){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateSQLXML(columnLabel:String, xmlObject:SQLXML ){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def getNString(columnIndex:Int):String=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getNString(columnLabel:String):String=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getNCharacterStream(columnIndex:Int):Reader=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  getNCharacterStream(columnLabel:String):Reader=null //throws SQLException {
		// TODO Auto-generated method stub
		//return null;
	

	override def  updateNCharacterStream(columnIndex:Int, x:Reader, length:Long){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateNCharacterStream(columnLabel:String, reader:Reader,
			length:Long) {}//throws SQLException {
		// TODO Auto-generated method stub
		


	override def  updateAsciiStream(columnIndex:Int, x:InputStream, length:Long){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBinaryStream(columnIndex:Int, x:InputStream, length:Long){}
			//throws SQLException {
		// TODO Auto-generated method stub
		


	override def  updateCharacterStream(columnIndex:Int, x:Reader, length:Long){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateAsciiStream(columnLabel:String, x:InputStream, length:Long){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateBinaryStream(columnLabel:String, x:InputStream,
			length:Long){} //throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateCharacterStream(columnLabel:String, reader:Reader,
			length:Long){} //throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateBlob(columnIndex:Int, inputStream:InputStream, length:Long){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateBlob(columnLabel:String, inputStream:InputStream,
			length:Long){} //throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateClob(columnIndex:Int, reader:Reader, length:Long){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateClob(columnLabel:String, reader:Reader, length:Long){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateNClob(columnIndex:Int, reader:Reader, length:Long){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateNClob(columnLabel:String, reader:Reader, length:Long){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateNCharacterStream(columnIndex:Int, x:Reader){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateNCharacterStream(columnLabel:String, reader:Reader){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateAsciiStream(columnIndex:Int, x:InputStream){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateBinaryStream(columnIndex:Int, x:InputStream){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateCharacterStream(columnIndex:Int, x:Reader){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

	override def  updateAsciiStream(columnLabel:String, x:InputStream){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateBinaryStream(columnLabel:String, x:InputStream){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateCharacterStream(columnLabel:String, reader:Reader){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateBlob(columnIndex:Int, inputStream:InputStream ){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateBlob(columnLabel:String, inputStream:InputStream ){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def updateClob(columnIndex:Int, reader:Reader){} //throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateClob(columnLabel:String, reader:Reader){}
			//throws SQLException {
		// TODO Auto-generated method stub
		

	override def  updateNClob(columnIndex:Int, reader:Reader){} //throws SQLException {
		// TODO Auto-generated method stub
		
	
	override def  updateNClob(columnLabel:String, reader:Reader){}
			//throws SQLException {
		// TODO Auto-generated method stub
		
	

}