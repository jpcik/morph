package es.upm.fi.oeg.morph.r2rml
//import es.upm.fi.oeg.morph.r2rml.SubjectMap

object R2rmlUtils {
  def extractTemplateVal(template:String)={
    val (i,j)=(template.indexOf('{'),template.indexOf('}'))
    template.substring(i+1,j)
  }
  
  
  def extractColumn(sm:TermMap)={
    val col=  if (sm.column!=null) sm.column
              else if (sm.template!=null) extractTemplateVal(sm.template)
              else null
    //col
    if (col!=null)
      col.replace("\"","")
    else null
  }
    
  def generateTemplateVal(template:String,value:String)={
    val s=template.split(Array('{','}'))
    if (s.size==2) s(0)+value
    else s(0)+value+s(2)
  }
    
}