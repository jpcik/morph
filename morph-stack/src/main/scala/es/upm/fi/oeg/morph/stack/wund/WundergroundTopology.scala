package es.upm.fi.oeg.morph.stack.wund

import backtype.storm.topology.TopologyBuilder
import es.upm.fi.oeg.morph.stack.ExportRdfBolt
import backtype.storm.Config
import backtype.storm.LocalDRPC
import backtype.storm.LocalCluster

class WundergroundTopology {
  def wundTopology(ids:Array[String])={
    val builder=new TopologyBuilder
    val printer = new ExportRdfBolt("",null)
    val b=builder.setBolt("print",printer,6)
    val idsp=ids.grouped(2).toArray
    idsp.zipWithIndex.foreach{id=>
      builder.setSpout("wund"+id._2,new WundergroundSpout(id._1),1)
      b.shuffleGrouping("wund"+id._2)  
    }
    
    builder.createTopology
  }
    
}

object  WundergroundTopology{
  def main(args:Array[String]){
    val wc=new WundergroundTopology
    val ids=Array("ICOMUNID113","ICOMUNID96","IMADRIDM50","IMADRIDM2","ICOMUNID56","IMADRIDM41")
    val conf = new Config
    conf.setMaxSpoutPending(20)
    //conf.setDebug(true)
    if (args.length==0) {
      val drpc = new LocalDRPC
      val cluster = new LocalCluster
            
      conf.setNumWorkers(2);
      cluster.submitTopology("wund-topo",conf,wc.wundTopology(ids))
    } else {
      //conf.setNumWorkers(3);
      //StormSubmitter.submitTopology(args(0), conf, wc.buildTopology(null))
    }
  }
}


