name := "morph-querygen"

organization := "es.upm.fi.oeg.morph"

version := "1.0.8"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "com.typesafe.play" %% "play" % "2.3.3",
  "com.typesafe.play" %% "play-json" % "2.3.3",
  "com.typesafe.play" %% "play-iteratees" % "2.3.3",
  //"com.fasterxml.jackson.core" % "jackson-core" % "2.2.2" intransitive,
  //"com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2" intransitive,   
  //"com.fasterxml.jackson.core" % "jackson-annotations" % "2.2.2" intransitive,   
  "org.joda" % "joda-convert" % "1.2",
  "joda-time" % "joda-time" % "2.1",  
  "org.hsqldb" % "hsqldb" % "2.2.8",  
  //"com.typesafe" % "config" % "1.0.2",  
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.6")

resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/releases/"
)