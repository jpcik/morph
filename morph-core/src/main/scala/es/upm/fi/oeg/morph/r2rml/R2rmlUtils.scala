package es.upm.fi.oeg.morph.r2rml

object R2rmlUtils {
  
  def extractTemplateVal(template:String)={
    val (i,j)=(template.indexOf('{'),template.indexOf('}'))
    template.substring(i+1,j)
  }
  
  def extractTemplateVals(template:String)=
    template.split('{').filter(_.contains('}')).map{sub=>
      sub.takeWhile(c=>c!='}')   
    }
  
  def removeTemplateVars(template:String)={
    template.split('{').map{sub=>
      if (sub.contains('}')) sub.dropWhile(c=>c!='}')
      else sub
    }.reduce(_++_)
  }
  def extractColumn(sm:TermMap)={
    val col=  if (sm.column!=null) sm.column
              else if (sm.template!=null) extractTemplateVal(sm.template)
              else null

    if (col!=null) col
    else null
  }
    
  def generateTemplateVal(template:String,value:String)={
    val s=template.split(Array('{','}'))
    if (s.size==2) s(0)+value
    else s(0)+value+s(2)
  }
    
  def replaceTemplate(template:String,values:Array[String])={
    if (values.contains(null)) null
    else {
      val vars=extractTemplateVals(template) zip values
      var res=template
      vars.foreach{v=>res=res.replace("{"+v._1+"}",v._2)}
      res
    }
  }
}