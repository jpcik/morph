package es.upm.fi.oeg.morph.relational
import java.util.Properties
import java.sql.ResultSet

abstract class RelationalModel(props:Properties) {
  //def configure(props:Properties):Unit
  def query(query:String):ResultSet
}