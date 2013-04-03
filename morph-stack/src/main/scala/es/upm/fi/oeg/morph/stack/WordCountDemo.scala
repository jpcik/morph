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

class WordCountDemo {

  def rdfize={
    val r2rml=R2rmlReader("mappings/data.ttl")
    val gen=new RdfGenerator(r2rml,null)
    val names=Array("timed","temp","stationid")
    val res=new DataResultSet(Stream(Array("tata","chipa","lupa"),
        Array("gota","per","lote")),
        names.map(a=>a->a).toMap,names)
    val ds=gen.generate(res)
    //RiotWriter.writeNQuads(System.out,ds.asDatasetGraph)
    
    val sw=new StringWriter
    
ds.getNamedModel(ds.listNames.toArray.head).write(sw,RDFFormat.TTL)    
     val update = "http://localhost:3030/ds/update";
     val query = "INSERT DATA  { "+ sw.toString +" }"
     println (query)
     val u = UpdateFactory.create
     
     u.add(query)
     val p = new UpdateProcessRemote(u, update,null)
     p.execute();
     


    //ds.getDefaultModel.write(System.out,RDFFormat.N3)

    
  }
  
  def wundTopology(ids:Array[String])={
    val builder=new TopologyBuilder
    val printer = new RdfExportBolt
    val b=builder.setBolt("print",printer,6)
    val idsp=ids.grouped(2).toArray
    idsp.zipWithIndex.foreach{id=>
      builder.setSpout("wund"+id._2,new WundergroundSpout(id._1),1)
      b.shuffleGrouping("wund"+id._2)  
    }
    
    builder.createTopology
  }
  
  def buildTopology(drpc:LocalDRPC):StormTopology = {
        
    val spout = new FixedBatchSpout(new Fields("sentence"), 3,
                new Values("the cow jumped over the moon"),
                new Values("the man went to the store and bought some candy"),
                new Values("four score and seven years ago"),
                new Values("how many apples can you eat"),
                new Values("to be or not to be the person"))
        
    spout.setCycle(true)
        
    val topology = new TridentTopology
    val wordCounts:TridentState =
           topology.newStream("spout1", spout)
                .parallelismHint(16)
                .each(new Fields("sentence"), new Split, new Fields("word"))
                .groupBy(new Fields("word"))
                .persistentAggregate(new MemoryMapState.Factory(),
                                     new Count, new Fields("count"))
                .parallelismHint(16)
                        
    topology.newDRPCStream("words", drpc)
                .each(new Fields("args"), new Split, new Fields("word"))
                .groupBy(new Fields("word"))
                .stateQuery(wordCounts, new Fields("word"), 
                    new MapGet().asInstanceOf[QueryFunction[State,_]], new Fields("count"))
                .each(new Fields("count"), new FilterNull)
                .aggregate(new Fields("count"), new Sum, new Fields("sum"))
                
    topology.build
  }
}


class Split extends BaseFunction {
  override def execute(tuple:TridentTuple,collector:TridentCollector) {
    val sentence = tuple.getString(0)
    sentence.split(" ").foreach {word=>
      collector.emit(new Values(word))
    }
  }
}

