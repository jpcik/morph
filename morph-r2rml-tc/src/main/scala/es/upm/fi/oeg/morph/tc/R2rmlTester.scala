package es.upm.fi.oeg.morph.tc
import java.io.File
import java.io.InputStream
import java.util.Properties
import scala.collection.JavaConversions._
import scala.io.Source
import org.openjena.riot.Lang
import org.openjena.riot.RiotLoader
import org.openjena.riot.RiotWriter
import com.hp.hpl.jena.graph.Graph
import com.hp.hpl.jena.sparql.core.DatasetGraph
import es.upm.fi.oeg.morph.execute.RdfGenerator
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.relational.JDBCRelationalModel
import es.upm.fi.oeg.siq.sparql.Sparql
import java.io.FileWriter
import java.io.FileOutputStream

class SuiteTester(testPath:String,val name:String) extends Sparql{
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
    val suffix=tc.mappingDoc.replace("r2rml","").dropRight(4)
    RiotWriter.writeNQuads(new FileOutputStream(testPath+"/"+name+"/mapped"+suffix+"-morph.nq"),ds.asDatasetGraph)
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
    println(g1.getClass())
    g1.isIsomorphicWith(g2)
  }

  def compareValues(g1:Graph,g2:Graph)={
    val res=g1.find("s","p","o")
    res.foreach{a=>println(a.asTriple)
      println(g2.contains("s",a.getPredicate,a.getObject))}
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