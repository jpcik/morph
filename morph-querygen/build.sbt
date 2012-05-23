name := "morph-querygen"

organization := "es.upm.fi.oeg.morph"

version := "1.0.0"

scalaVersion := "2.9.1"

crossPaths := false

libraryDependencies ++= Seq(
  "es.upm.fi.oeg.morph" % "morph-core" % "1.0.0",
  "com.sun.jersey" % "jersey-client" % "1.8",
  "com.sun.jersey" % "jersey-core" % "1.8",
  "org.hsqldb" % "hsqldb" % "2.2.8",  
  "com.google.code.gson" % "gson" % "1.7.1"
)

scalacOptions += "-deprecation"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_))

unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_))