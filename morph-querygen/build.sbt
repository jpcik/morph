name := "morph-querygen"

organization := "es.upm.fi.oeg.morph"

version := "1.0.2"

libraryDependencies ++= Seq(
  "es.upm.fi.oeg.morph" % "morph-core" % "1.0.2",
  "net.databinder.dispatch" %% "dispatch-core" % "0.9.5",
  "play" %% "play" % "2.1.1" intransitive,
  "play" %% "play-iteratees" % "2.1.1" intransitive,
  "org.codehaus.jackson" % "jackson-core-asl" % "1.9.10" intransitive,
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.10" intransitive,
  "org.joda" % "joda-convert" % "1.2",
  "joda-time" % "joda-time" % "2.1",  
  "com.sun.jersey" % "jersey-client" % "1.8",
  "com.sun.jersey" % "jersey-core" % "1.8",
  "org.hsqldb" % "hsqldb" % "2.2.8",  
  "com.google.code.gson" % "gson" % "1.7.1",
  "junit" % "junit" % "4.7" % "test",  
  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
  "org.scalacheck" % "scalacheck_2.10" % "1.10.0" % "test"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.6")

resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/releases/"
)