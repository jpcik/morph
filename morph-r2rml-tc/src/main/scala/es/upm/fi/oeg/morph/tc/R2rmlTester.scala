package es.upm.fi.oeg.morph.tc
import scala.io.Source
import es.upm.fi.oeg.morph.execute.RdfGenerator
import java.io.File
import java.util.Properties
import java.io.InputStream
import com.hp.hpl.jena.rdf.model.ModelFactory
import java.io.FileInputStream
import com.hp.hpl.jena.query.DataSource
import collection.JavaConversions._
import com.hp.hpl.jena.sparql.core.DatasetGraph
import com.hp.hpl.jena.graph.Graph
import com.hp.hpl.jena.query.DatasetFactory
import es.upm.fi.oeg.morph.voc.RDFFormat
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.relational.JDBCRelationalModel
import org.openjena.riot.RiotLoader
import org.openjena.riot.Lang
import org.openjena.riot.RiotWriter

class SuiteTester(testPath:String,val name:String){
  val props=load(getClass.getClassLoader().getResourceAsStream("config.properties"))
  
  val manifestFile="manifest.ttl"
  val manifest=Manifest(testPath+"/"+name+"/"+manifestFile,name)
  val script=Source.fromFile(testPath+"/"+name+"/"+manifest.database.scriptFile).getLines.map(_+"\r\n")reduceLeft(_+_)
  val db=new DBManager
  db.clearDB
  db.createDB(script)
 
  private def load(fis:InputStream)={
    val props = new Properties
    props.load(fis)    
    fis.close
    props
  }  
  
  
 def getTc(id:String)=manifest.database.testcases.find(_.id.equals(id))
 
  
  def testTc(tc:TestCase)={
    val reader=R2rmlReader(testPath+"/"+name+"/"+ tc.mappingDoc)
    val relat=new JDBCRelationalModel(props)
    
    val ds=new RdfGenerator(reader,relat).generate
    RiotWriter.writeNQuads(System.out,ds.asDatasetGraph)
    println("output: "+tc.output)
    
    if (tc.output!=null) {
      val output=RiotLoader.load(testPath+"/"+name+"/"+tc.output, Lang.NQUADS)
      RiotWriter.writeNQuads(System.out,output)
      //output.getReader("N-Quads").read(output,new FileInputStream(testPath+"/"+name+"/"+tc.output),"")
      //output.write(System.out,RDFFormat.N3)      
      val compareEquals=compare(output,ds.asDatasetGraph)
      println("comparing "+compareEquals)
      if (!compareEquals)
        throw new Exception("Test results do not match expected triple dataset.")
    }
    ds
  }
  
  def compare(d1:DatasetGraph,d2:DatasetGraph):Boolean={
    val c1=d1.getDefaultGraph().isIsomorphicWith(d2.getDefaultGraph)
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
    g1.isIsomorphicWith(g2)
  }

  def testAll{
    manifest.database.testcases.foreach{tc=>
      //try{
      testTc(tc)
      //}
      //catch {case e:Exception => if (tc.hasOutput) throw new Exception(e) else e.printStackTrace}
    }
  }
}

object R2rmlTester {
  var testPath=""
  val db=new DBManager
  def test(suite:String){
    db.clearDB
    //val suite=new SuiteTester(testPath,)
    
	
  }
  

  def main(args:Array[String]){
    testPath=args(0)
    val db=new DBManager
    //"file:///c:/users/jpc/workspace-morph/morph-parent/morph-r2rml-tc"
    new File(testPath).list.take(3).foreach{dir=>
      db.clearDB
      val suite=new SuiteTester(testPath,dir)
      suite.testAll
      //test(dir)      
    }
  }
  
}