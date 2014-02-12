name := "morph-querygen"

organization := "es.upm.fi.oeg.morph"

version := "1.0.6"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.9.5",
  "com.typesafe.play" %% "play" % "2.2.1" intransitive,
  "com.typesafe.play" %% "play-json" % "2.2.1" intransitive,
  "com.typesafe.play" %% "play-iteratees" % "2.2.1" intransitive,
  "com.typesafe.play" %% "templates" % "2.2.1" intransitive,
  "com.fasterxml.jackson.core" % "jackson-core" % "2.2.2" intransitive,
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2" intransitive,   
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.2.2" intransitive,   
  "org.joda" % "joda-convert" % "1.2",
  "joda-time" % "joda-time" % "2.1",  
  "org.hsqldb" % "hsqldb" % "2.2.8",  
  "com.typesafe" % "config" % "1.0.2",  
  "org.scalatest" % "scalatest_2.10" % "2.0.RC1" % "test"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.6")

resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/releases/"
)