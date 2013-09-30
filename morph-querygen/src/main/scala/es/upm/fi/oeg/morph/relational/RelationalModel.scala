package es.upm.fi.oeg.morph.relational
import java.sql.ResultSet
import com.typesafe.config.Config

abstract class RelationalModel(conf:Config,val postProc:Boolean) {
  //def configure(props:Properties):Unit
  def query(query:String):ResultSet
  
}