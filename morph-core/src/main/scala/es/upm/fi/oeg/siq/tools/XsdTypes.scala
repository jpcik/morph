package es.upm.fi.oeg.siq.tools
import java.sql.Types
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype

object XsdTypes {
  def sqlType2XsdType(sqlType:Int)=sqlType match{
    case Types.NVARCHAR=>XSDDatatype.XSDstring
    case Types.BOOLEAN=>XSDDatatype.XSDboolean
    case Types.VARCHAR=> XSDDatatype.XSDstring
    case Types.BIGINT=> XSDDatatype.XSDinteger
    case Types.INTEGER=> XSDDatatype.XSDinteger
    case Types.DOUBLE=> XSDDatatype.XSDdouble
    case Types.FLOAT=> XSDDatatype.XSDfloat
    case Types.TIME=> XSDDatatype.XSDtime
    case Types.DATE=> XSDDatatype.XSDdate
    case Types.CHAR=> XSDDatatype.XSDstring
    case Types.TIMESTAMP=> XSDDatatype.XSDdateTime
    case _=>null
  }
}