package es.upm.fi.oeg.morph.tc

import scala.collection.JavaConversions._
import scala.io.Source
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.slf4j.LoggerFactory
import com.hp.hpl.jena.graph.Graph
import com.hp.hpl.jena.sparql.core.DatasetGraph
import com.typesafe.config.ConfigFactory
import es.upm.fi.oeg.morph.Morph
import es.upm.fi.oeg.morph.db.JDBCRelationalModel
import es.upm.fi.oeg.siq.sparql.Sparql
import java.io.FileOutputStream

class SuiteTester(testPath:String,val name:String) extends Sparql{
  
  val logger = LoggerFactory.getLogger(classOf[SuiteTester])  
  val conf=ConfigFactory.load.getConfig("morph")
  
  val manifestFile="manifest.ttl"
  val manifest=Manifest(testPath+"/"+name+"/"+manifestFile,name)
  if (manifest.database!=null) initDb
  
  private def initDb={
    val script=Source.fromFile(testPath+"/"+name+"/"+manifest.database.scriptFile).getLines.map(_+"\r\n")reduceLeft(_+_)
    val createSchema=conf.getBoolean("r2rmltc.createschema")
    val db=new DBManager(conf.getString("jdbc.driver"),conf.getString("jdbc.source.url")+name,
        conf.getString("jdbc.source.user"),conf.getString("jdbc.source.password"),createSchema)
    db.clearDB
    db.createDB(script)    
  }
  
  def getTc(id:String)=manifest.database.testcases.find(_.id.equals(id))
   
  def testTc(tc:TestCase)={
    val jdbcConf=conf.getConfig("jdbc")
    val jdbc= new JDBCRelationalModel(jdbcConf,jdbcConf.getString("source.url")+name)

    val ds = new Morph(conf,jdbc).generate(testPath+"/"+name+"/"+ tc.mappingDoc)
    RDFDataMgr.write(System.out,ds.asDatasetGraph, Lang.NQUADS)
    val suffix=tc.mappingDoc.replace("r2rml","").dropRight(4)
    RDFDataMgr.write(new FileOutputStream(testPath+"/"+name+"/mapped"+suffix+"-morph.nq"),ds.asDatasetGraph,Lang.NQUADS)
    logger.debug("output: "+tc.output)
    
    if (tc.output!=null) {
      val output=RDFDataMgr.loadDataset(testPath+"/"+name+"/"+tc.output, Lang.NQUADS)
      RDFDataMgr.write(System.out,output.asDatasetGraph,Lang.NQUADS)      
      //output.getReader("N-Quads").read(output,new FileInputStream(testPath+"/"+name+"/"+tc.output),"")
      //output.write(System.out,RDFFormat.N3)      
      val compareEquals=compare(output.asDatasetGraph(),ds.asDatasetGraph)
      logger.debug("comparing "+compareEquals)
      if (!compareEquals)
        throw new Exception("Test results do not match expected triple dataset.")
    }
    ds
  }
  
  def compare(d1:DatasetGraph,d2:DatasetGraph):Boolean={
    
    val c1=compare(d1.getDefaultGraph,d2.getDefaultGraph)
    if (d1.listGraphNodes.isEmpty && d2.listGraphNodes.isEmpty)
      c1
    else{
      val comparisons=d1.listGraphNodes.map{g=>
        val g2=d2.getGraph(g)
        if (g2!=null)
          compare(d1.getGraph(g),g2)
        else false
      }      
      comparisons.reduceLeft(_&&_) && c1
    }
  }
  
  def compare(g1:Graph,g2:Graph)={
    compareValues(g1,g2)
    logger.debug(g1.getClass.toString)
    g1.isIsomorphicWith(g2)
  }

  def compareValues(g1:Graph,g2:Graph)={
    val res=g1.find("s","p","o")
    res.foreach{
      a=>logger.debug(a.asTriple.toString)
      logger.debug(g2.contains("s",a.getPredicate,a.getObject).toString)
    }
  }
  
  def testAll{
    manifest.database.testcases.foreach{tc=>
      testTc(tc)
    }
  }
}
  
  
  
