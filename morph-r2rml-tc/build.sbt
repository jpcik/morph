name := "morph-r2rml-tc"

organization := "es.upm.fi.oeg.morph"

version := "1.0.1"

scalaVersion := "2.9.1"

crossPaths := false

libraryDependencies ++= Seq(
  "es.upm.fi.oeg.morph" % "morph-core" % "1.0.0",
  "es.upm.fi.oeg.morph" % "morph-querygen" % "1.0.0",
  "org.hsqldb" % "hsqldb" % "2.2.8",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.scalatest" % "scalatest_2.9.1" % "1.7.2" % "test",
  "org.scalacheck" % "scalacheck_2.9.1" % "1.9" % "test"
)

scalacOptions += "-deprecation"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_))

unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_))

parallelExecution in Test := false

//publishTo := Some(Resolver.file("jpc repo",new File(Path.userHome.absolutePath+"/git/jpc-repo/repo")))
publishTo := Some("Artifactory Realm" at "http://aldebaran.dia.fi.upm.es/artifactory/sstreams-releases-local")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishMavenStyle := true