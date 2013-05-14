import AssemblyKeys._

name := "morph-core"

organization := "es.upm.fi.oeg.morph"

version := "1.0.3"

libraryDependencies ++= Seq(
  "org.apache.jena" % "jena-core" % "2.10.0" intransitive,
  "org.apache.jena" % "jena-arq" % "2.10.0" intransitive,
  "org.apache.jena" % "jena-iri" % "0.9.5" intransitive,
  "xerces" % "xercesImpl" % "2.10.0" ,
  "org.slf4j" % "slf4j-api" % "1.6.4",
  "junit" % "junit" % "4.7" % "test",
  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
  "org.scalacheck" % "scalacheck_2.10" % "1.10.0" % "test"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.6")

assemblySettings
