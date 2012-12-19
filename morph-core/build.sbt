name := "morph-core"

organization := "es.upm.fi.oeg.morph"

version := "1.0.1"

scalaVersion := "2.9.1"

crossPaths := false

libraryDependencies ++= Seq(
  "com.hp.hpl.jena" % "jena" % "2.6.4",
  "com.hp.hpl.jena" % "arq" % "2.8.8",
  "junit" % "junit" % "4.7",
  "com.weiglewilczek.slf4s" % "slf4s_2.9.1" % "1.0.7",
  "org.slf4j" % "slf4j-log4j12" % "1.6.4",
  "org.slf4j" % "slf4j-api" % "1.6.4",
  "org.scalatest" % "scalatest_2.9.1" % "1.7.2" % "test",
  "org.scalacheck" % "scalacheck_2.9.1" % "1.9" % "test"
)

scalacOptions += "-deprecation"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_))

unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_))

//publishTo := Some(Resolver.file("jpc repo",new File(Path.userHome.absolutePath+"/git/jpc-repo/repo")))
publishTo := Some("Artifactory Realm" at "http://aldebaran.dia.fi.upm.es/artifactory/sstreams-releases-local")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishMavenStyle := true

