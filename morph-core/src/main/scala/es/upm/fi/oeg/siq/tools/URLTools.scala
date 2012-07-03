package es.upm.fi.oeg.siq.tools

object URLTools {
  def stripSpecial(input:String)={
    val resultStr = new StringBuilder
    input.toCharArray.foreach{ch=>
      if (!isSpecial(ch)) 
        resultStr.append(ch)            
    }
    resultStr.toString
  }
  
  def encode(input:String)={
    val resultStr = new StringBuilder
    input.toCharArray.foreach{ch=>
      if (isUnsafe(ch)) {
        resultStr.append('%')
        resultStr.append(toHex(ch / 16))
        resultStr.append(toHex(ch % 16))
      } 
      else resultStr.append(ch)            
    }
    resultStr.toString
  }

  def encodeAll(input:String)={
    val resultStr = new StringBuilder
    input.toCharArray.foreach{ch=>
      if (isSpecial(ch)) {
        resultStr.append('%')
        resultStr.append(toHex(ch / 16))
        resultStr.append(toHex(ch % 16))
      } 
      else resultStr.append(ch)            
    }
    resultStr.toString
  }

  private def toHex(ch:Int):Char=
    (if (ch<10) '0'+ch else 'A'+ch -10).toChar

  private def isUnsafe(ch:Char):Boolean= 
    if (ch > 128 || ch < 0) true
    else " %$&+,;=?@<>%".indexOf(ch) >= 0
  
  private def isSpecial(ch:Char):Boolean= {
    if (ch > 128 || ch < 0) return true
    return " \"/%$:&+,;=?@<>#%{}\\".indexOf(ch) >= 0;
  }
}