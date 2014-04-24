package es.upm.fi.oeg.morph.db
import collection.JavaConversions._
import dispatch._
import play.api.libs.json._
import org.slf4j.LoggerFactory
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype
import com.typesafe.config.Config
import es.upm.fi.oeg.morph.db.dataset.Dataset
import es.upm.fi.oeg.morph.db.dataset.Row
import es.upm.fi.oeg.morph.db.dataset.Metadata
import es.upm.fi.oeg.morph.db.dataset.RecordDataset
import es.upm.fi.oeg.morph.db.dataset.MapRecord
import es.upm.fi.oeg.morph.r2rml.TriplesMap
import es.upm.fi.oeg.morph.r2rml.R2rmlReader

class RestRelationalModel(conf:Config) extends RelationalModel(null,true){
  private val fieldNames=conf.getStringList("fields")
  val metadata=Metadata("",fieldNames)
  //val fieldNames=metadata.fields.keys
  
  override def query(queryString:String):Dataset={
    val svc = url(conf.getString("url"))
    val resp = Http(svc OK as.String)
    val json:JsArray = Json.parse(resp()).asInstanceOf[JsArray]
    val data=json.value.map{js=>
      val dd=fieldNames.map{ s =>
         (js \ s) match{
          case st:JsString=>st.value
          case p =>p.toString                    
        }
      }.toSeq
      new MapRecord(metadata.fields,dd)      
    }
    new RecordDataset(data.toStream,metadata)
  }
  
 
  
  override def queries(r2rml:R2rmlReader)=
    r2rml.tMaps.map { tMap =>
       tMap.logicalTable.tableName
    }.toSeq
}
