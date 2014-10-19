name := "morph-r2rml-tc"

organization := "es.upm.fi.oeg.morph"

version := "1.0.8"

libraryDependencies ++= Seq(
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "ch.qos.logback" % "logback-classic" % "1.0.13" % "test",    
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.6")

parallelExecution in Test := false
