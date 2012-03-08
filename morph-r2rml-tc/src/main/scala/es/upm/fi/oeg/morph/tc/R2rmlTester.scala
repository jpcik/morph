package es.upm.fi.oeg.morph.tc
import scala.io.Source
import es.upm.fi.dia.oeg.morph.R2RProcessor
import es.upm.fi.oeg.morph.execute.RdfGenerator
import java.io.File
import java.util.Properties
import java.io.InputStream

object R2rmlTester {
  val manifestFile="manifest.ttl"
  var testPath=""
  val db=new DBManager
  def test(suite:String){
    val manifest=Manifest(testPath+"/"+suite+"/"+manifestFile,suite)
    val script=Source.fromFile(testPath+"/"+suite+"/"+manifest.database.scriptFile).getLines.map(_+"\r\n")reduceLeft(_+_)
    db.clearDB
    db.createDB(script)
    
    val props=load(getClass.getClassLoader().getResourceAsStream("config.properties"))
    manifest.database.testcases.foreach{tc=>
      val r2r=new R2RProcessor
      props.setProperty(R2RProcessor.R2R_MAPPING_URL,testPath+"/"+suite+"/"+ tc.mappingDoc);
      r2r.configure(props);
      val ds=new RdfGenerator(r2r.model,r2r.relational).generate
      ds.getDefaultModel.write(System.out,RDFFormat.TTL)
    }
	
  }
  
def load(fis:InputStream)=
	{
        val props = new Properties();
        props.load(fis);    
        fis.close();
        props;
	}  
  
  def main(args:Array[String]){
    testPath=args(0)
    //"file:///c:/users/jpc/workspace-morph/morph-parent/morph-r2rml-tc"
    new File(testPath).list.take(2).foreach{dir=>
      test(dir)      
    }
  }
}