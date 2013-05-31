/**
   Copyright 2010-2013 Ontology Engineering Group, Universidad Politécnica de Madrid, Spain

   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in 
   compliance with the License. You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software distributed under the License is 
   distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
   See the License for the specific language governing permissions and limitations under the License.
**/

package es.upm.fi.oeg.morph.stack
import backtype.storm.generated.StormTopology
import backtype.storm.tuple.Fields
import backtype.storm.tuple.Values
import backtype.storm.LocalDRPC
import storm.trident.operation.builtin.Count
import storm.trident.operation.builtin.MapGet
import storm.trident.operation.BaseFunction
import storm.trident.operation.TridentCollector
import storm.trident.testing.FixedBatchSpout
import storm.trident.testing.MemoryMapState
import storm.trident.tuple.TridentTuple
import storm.trident.TridentState
import storm.trident.TridentTopology
import storm.trident.planner.processor.StateQueryProcessor
import storm.trident.state.QueryFunction
import storm.trident.state.State
import storm.trident.operation.builtin.FilterNull
import storm.trident.operation.builtin.Sum
import backtype.storm.Config
import backtype.storm.LocalCluster
import backtype.storm.StormSubmitter
import backtype.storm.topology.TopologyBuilder
import es.upm.fi.oeg.morph.execute.RdfGenerator
import es.upm.fi.oeg.morph.r2rml.R2rmlReader
import es.upm.fi.oeg.morph.relational.DataResultSet
import es.upm.fi.oeg.morph.voc.RDFFormat
import org.openjena.riot.RiotWriter
import com.hp.hpl.jena.update.UpdateFactory
import com.hp.hpl.jena.update.UpdateExecutionFactory
import com.hp.hpl.jena.sparql.modify.UpdateProcessRemote
import java.io.StringWriter
import collection.JavaConversions._

object WordCountDemoTopo{
  def main(args:Array[String]){
    val wc=new WordCountDemo
    //wc.rdfize
    //exit
    val ids=Array("ICOMUNID113","ICOMUNID96","IMADRIDM50","IMADRIDM2","ICOMUNID56","IMADRIDM41")
    //new WundergroundSpout(ids).getData("ISANGALL2")
    //exit
    val conf = new Config
    conf.setMaxSpoutPending(20)
    //conf.setDebug(true)
    if (args.length==0) {
      val drpc = new LocalDRPC
      val cluster = new LocalCluster
            
      conf.setNumWorkers(2);

      cluster.submitTopology("wund-topo",conf,wc.wundTopology(ids))
      if (false){
      cluster.submitTopology("wordCounter", conf, wc.buildTopology(drpc))
      (1 to 3).foreach {i=>
         println("DRPC RESULT: " + drpc.execute("words", "cat the dog jumped"))
         Thread.sleep(1000)
      }}
    } else {
      //conf.setNumWorkers(3);
      //StormSubmitter.submitTopology(args(0), conf, wc.buildTopology(null))
    }
  }
}

