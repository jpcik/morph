name := "morph-r2rml-tc"

organization := "es.upm.fi.oeg.morph"

version := "1.0.2"

libraryDependencies ++= Seq(
  "es.upm.fi.oeg.morph" % "morph-core" % "1.0.3",
  "es.upm.fi.oeg.morph" % "morph-querygen" % "1.0.3",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "junit" % "junit" % "4.7" % "test",  
  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
  "org.scalacheck" % "scalacheck_2.10" % "1.10.0" % "test"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.6")

parallelExecution in Test := false
