package es.upm.fi.oeg.morph.stack

abstract class PeriodicStreamer {

  def getNames:Array[String]
  def getData:Seq[Array[Any]]
  def execute{
   
  }
}