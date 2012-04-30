package es.upm.fi.oeg.morph.execute
import collection.JavaConversions._
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.query.DatasetFactory
import es.upm.fi.oeg.morph.voc.RDF
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.querygen.RdbQueryGenerator
import es.upm.fi.oeg.morph.relational.RelationalModel

class RdfGenerator(model:R2rmlReader,relational:RelationalModel) {
  def generate={
    val m = ModelFactory.createDefaultModel()
	val ds = DatasetFactory.create(m) 
		
    val queries=model.tMaps.foreach{tMap=>
      val q=new RdbQueryGenerator(tMap).query
      println("query "+q)
      val res=relational.query(q)
      
      val tgen=new TripleGenerator(ds,tMap)
      //val tIns = new TriplesMapInstance(tMap)
      //tIns.setResultSet(res)
      //tIns.setDataSource(ds)
      while (res.next){        
        val subj = tgen.genSubject(res)//tIns.getGeneratedSubject()
        val tMapModel = ds.getDefaultModel//getModel(d, tIns.getGeneratedGraphUri());
				
        //if (tIns.getGeneratedRdfType() !=null)
        if (tgen.genRdfType!=null)
          tMapModel.add(subj,RDF.typeProp,tgen.genRdfType)
								
				
		tMap.poMaps.foreach{prop=>
		  val propIns = POGenerator(ds,prop)
		  //propIns.setResultSet(res) //TODO this is awful
					
		  //val pModel = ds.getDefaultModel//getModel(d,propIns.getGeneratedGraphUri());
		  if (prop.refObjectMap!=null){
		    val obj=new TripleGenerator(ds,prop.refObjectMap.parentTriplesMap).genSubject(res)
		    tMapModel.add(subj,propIns.genPredicate,obj)
		  }
		  else{
		  
		  val column = prop.objectMap.column.replace("\"","")
		  val datatype = prop.objectMap.dtype
		  //logger.info("bigbig"+propIns.getGeneratedProperty());
		  if (column!=null){
		    tMapModel.add(subj,propIns.genPredicate,res.getString(prop.id),datatype)		
		  }
		  else				
		    tMapModel.add(subj,propIns.genPredicate,prop.objectMap.constant)
		  }
		}
      }
    }
    ds
  }
}