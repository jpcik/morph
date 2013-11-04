name := "morph-r2rml-tc"

organization := "es.upm.fi.oeg.morph"

version := "1.0.6"

libraryDependencies ++= Seq(
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "ch.qos.logback" % "logback-classic" % "1.0.9" % "test",    
  "org.scalatest" % "scalatest_2.10" % "2.0.RC1" % "test"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.6")

parallelExecution in Test := false
