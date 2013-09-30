package es.upm.fi.oeg.morph

import com.typesafe.config.ConfigFactory
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.execute.RdfGenerator
import es.upm.fi.oeg.morph.relational.RestRelationalModel
import es.upm.fi.oeg.morph.relational.RelationalModel
import es.upm.fi.oeg.morph.voc.RDFFormat
import es.upm.fi.oeg.morph.relational.JDBCRelationalModel

class Morph {
  private val conf=ConfigFactory.load.getConfig("morph")
  private lazy val rest:RelationalModel=new RestRelationalModel
  private lazy val jdbc= new JDBCRelationalModel(conf.getConfig("jdbc"))
  
  def generateJdbc(mapping:String)=generate(jdbc,mapping)
  def generateRest(mapping:String)=generate(rest,mapping)
  
  def generate(model:RelationalModel,mapping:String)={
    val reader=R2rmlReader(mapping)
    
    val ds=new RdfGenerator(reader,model,conf.getString("baseUri")).generate
    //ds.getDefaultModel.write(System.out,RDFFormat.N3)
    ds
  }

}