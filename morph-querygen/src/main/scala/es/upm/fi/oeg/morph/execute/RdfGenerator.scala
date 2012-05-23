package es.upm.fi.oeg.morph.execute
import collection.JavaConversions._
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.query.DatasetFactory
import es.upm.fi.oeg.morph.voc.RDF
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.querygen.RdbQueryGenerator
import es.upm.fi.oeg.morph.relational.RelationalModel
import java.sql.Types
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype
import java.sql.SQLSyntaxErrorException
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.sparql.core.DatasetGraph

class RelationalQueryException(msg:String,e:Throwable) extends Exception(msg,e)

class RdfGenerator(model:R2rmlReader,relational:RelationalModel) {
  //def graph(name:String)
  def generate={
    val m = ModelFactory.createDefaultModel
	val ds = DatasetFactory.create(m) 
		
    val queries=model.tMaps.foreach{tMap=>
      val q=new RdbQueryGenerator(tMap).query
      println("query "+q)
      
      val res=try{
        relational.query(q)
      } catch {case ex:SQLSyntaxErrorException=>throw new RelationalQueryException("Invalid query: "+ex.getMessage,ex)} 
      
      val tgen=new TripleGenerator(ds,tMap)
      while (res.next){        
        val subj = tgen.genSubject(res)
        val tMapModel = if (tMap.subjectMap.graphMap==null) ds.getDefaultModel
          else if (tMap.subjectMap.graphMap.constant!=null) 
            if (ds.getNamedModel(tMap.subjectMap.graphMap.constant.asResource.getURI)!=null)
              ds.getNamedModel(tMap.subjectMap.graphMap.constant.asResource.getURI)
            else {
              ds.addNamedModel(tMap.subjectMap.graphMap.constant.asResource.getURI,ModelFactory.createDefaultModel)
              ds.getNamedModel(tMap.subjectMap.graphMap.constant.asResource.getURI)
            }
          else if (tMap.subjectMap.graphMap.template!=null){
            val gname=tgen.genGraph(res)
            if (ds.getNamedModel(gname)==null)
              ds.addNamedModel(gname,ModelFactory.createDefaultModel)
            ds.getNamedModel(gname)
                     
          }else null
            
          
				
        if (tgen.genRdfType!=null)
          tMapModel.add(subj,RDF.typeProp,tgen.genRdfType)
								
				
		tMap.poMaps.foreach{prop=>
		  val parentTmap=if (prop.refObjectMap!=null) model.triplesMaps(prop.refObjectMap.parentTriplesMap)
		    else null
		  val propIns = POGenerator(ds,prop,parentTmap)
		  val po=propIns.generate(null,res)
		  if (po._2!=null)
			tMapModel.add(subj,propIns.genPredicate,po._1.toString,po._2)
		  else if (po._1.isInstanceOf[Resource])
		    tMapModel.add(subj,propIns.genPredicate,po._1.asInstanceOf[Resource])
		  else
		    tMapModel.add(subj,propIns.genPredicate,po._1.toString)	
		}
      }
    }
    ds
  }
}