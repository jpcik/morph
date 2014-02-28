package es.upm.fi.oeg.morph.querygen

object SQLDialect {
  def apply(name: String): SQLDialect = {
    // TODO: autodetection of case classes
    name match {
      case "sqlserver" => new SQLServer
      case "oracle" => new Oracle
      case _ => new DefaultSQL
    }
  }
}

trait SQLDialect {
  val name: String = "default"
  val concatChar: String = "||"
  override def toString(): String = String.format("%s [concatChar=%s]", name, concatChar)
}

class DefaultSQL extends SQLDialect

case class SQLServer extends SQLDialect {
  override val name = "sqlserver"
  override val concatChar = "+"
}

case class Oracle extends SQLDialect {
  override val name: String = "oracle"
}

// NOTE: TESTING code, should actually go in src/test/...
object prova extends App {
  
  val dialects = List("sqlserver", "oracle", "something else", "other")
  dialects.foreach(name => {
    val d = SQLDialect(name)
    println(name + ": " + d)
  })
  
}