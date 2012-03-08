package es.upm.fi.oeg.morph.execute
import es.upm.fi.dia.oeg.morph.r2rml.R2RModel
import collection.JavaConversions._
import es.upm.fi.oeg.morph.querygen.RdbQueryGenerator
import es.upm.fi.dia.oeg.morph.relational.RelationalModel
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.query.DatasetFactory
import es.upm.fi.dia.oeg.morph.TriplesMapInstance
import com.hp.hpl.jena.rdf.model.ResourceFactory
import es.upm.fi.dia.oeg.morph.PredicateObjectMapInstance

class RdfGenerator(model:R2RModel,relational:RelationalModel) {
  def generate={
    val m = ModelFactory.createDefaultModel()
	val ds = DatasetFactory.create(m) 
		
    val queries=model.getTriplesMap.foreach{tMap=>
      val q=new RdbQueryGenerator(tMap).query
      println("query "+q)
      val res=relational.query(q)
      val tIns = new TriplesMapInstance(tMap)
      tIns.setResultSet(res)
      tIns.setDataSource(ds)
      while (res.next){        
        //(1 to 9).foreach(i=>println(res.getString(i)))
        val subj = tIns.getGeneratedSubject()
        val tMapModel = ds.getDefaultModel//getModel(d, tIns.getGeneratedGraphUri());
				
        if (tIns.getGeneratedRdfType() !=null)
			tMapModel.add(subj,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
				    tIns.getGeneratedRdfType)
								
				
		tMap.getPropertyObjectMaps.foreach{prop=>
		  val propIns = new PredicateObjectMapInstance(prop,tIns)
		  propIns.setResultSet(res) //TODO this is awful
					
		  val pModel = ds.getDefaultModel//getModel(d,propIns.getGeneratedGraphUri());
							
		  val column = prop.getObjectMap.getColumn
		  val datatype = prop.getObjectMap.getDatatype
		  //logger.info("bigbig"+propIns.getGeneratedProperty());
		  if (column!=null){
		    pModel.add(subj,propIns.getGeneratedProperty,res.getString(column),datatype)		
		  }
		  else				
		    pModel.add(subj,propIns.getGeneratedProperty,prop.getObjectMap.getObject)
		}
      }
    }
    ds
  }
}