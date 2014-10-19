import AssemblyKeys._

name := "morph-core"

organization := "es.upm.fi.oeg.morph"

version := "1.0.7"

libraryDependencies ++= Seq(
  "org.apache.jena" % "jena-core" % "2.11.0" exclude("log4j","log4j") exclude("org.slf4j","slf4j-log4j12"),
  "org.apache.jena" % "jena-arq" % "2.11.0" exclude("log4j","log4j") exclude("org.slf4j","slf4j-log4j12"),
  "org.slf4j" % "slf4j-api" % "1.7.6",
  "ch.qos.logback" % "logback-classic" % "1.1.1" % "test",      
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
 )

javacOptions ++= Seq("-source", "1.7", "-target", "1.6")

assemblySettings
