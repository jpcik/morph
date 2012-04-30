name := "morph-core"

version := "1.0"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "com.hp.hpl.jena" % "jena" % "2.6.4",
  "com.hp.hpl.jena" % "arq" % "2.8.8",
  "junit" % "junit" % "4.7",
  "com.weiglewilczek.slf4s" % "slf4s_2.9.1" % "1.0.7",
  "org.slf4j" % "slf4j-log4j12" % "1.6.4",
  "org.slf4j" % "slf4j-api" % "1.6.4",
  "org.scalatest" % "scalatest_2.9.2" % "1.7.2",
  "org.scalacheck" % "scalacheck_2.9.2" % "1.9"
)

