import AssemblyKeys._

name := "morph-core"

organization := "es.upm.fi.oeg.morph"

version := "1.0.6"

libraryDependencies ++= Seq(
  "org.apache.jena" % "jena-core" % "2.11.0" intransitive,
  "org.apache.jena" % "jena-arq" % "2.11.0" intransitive,
  "org.apache.jena" % "jena-iri" % "1.0.0" intransitive,
  "xerces" % "xercesImpl" % "2.11.0" ,
  "org.slf4j" % "slf4j-api" % "1.6.4",
  "ch.qos.logback" % "logback-classic" % "1.0.9" % "test",      
  "org.scalatest" % "scalatest_2.10" % "2.0.RC1" % "test"
 )

javacOptions ++= Seq("-source", "1.7", "-target", "1.6")

assemblySettings
