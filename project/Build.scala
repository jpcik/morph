import sbt._
import Keys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

object MainBuild extends Build {
  val scalaOnly = Seq (
    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_))
  )
  val projSettings = Seq (
    scalaVersion := "2.10.1",
    crossPaths := false,
    scalacOptions += "-deprecation",
    parallelExecution in Test := false,
    resolvers ++= Seq(
      DefaultMavenRepository,
      "Local ivy Repository" at "file://"+Path.userHome.absolutePath+"/.ivy2/local")
  )
  val ideSettings = Seq (
    EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
  )
  val publishSettings = Seq (
    publishTo := Some("Artifactory Realm" at "http://aldebaran.dia.fi.upm.es/artifactory/sstreams-releases-local"),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishMavenStyle := true,
    publishArtifact in (Compile, packageSrc) := false )

  val buildSettings = Defaults.defaultSettings ++ projSettings ++ ideSettings ++ publishSettings 

  scalaVersion := "2.10.1"
  autoScalaLibrary := false
  lazy val root = Project(id = "morph-parent",
                          base = file("."),settings = buildSettings) aggregate(core, querygen,r2rmlTc)

  lazy val core = Project(id = "morph-core",
                          base = file("morph-core"),settings = buildSettings ++ scalaOnly)

  lazy val querygen = Project(id = "morph-querygen",
                              base = file("morph-querygen"),settings = buildSettings ++ scalaOnly) dependsOn(core)

  lazy val r2rmlTc = Project(id = "morph-r2rml-tc",
                             base = file("morph-r2rml-tc"),settings = buildSettings ++ scalaOnly) dependsOn(querygen)

  lazy val morphstack = Project(id = "morph-stack",
                             base = file("morph-stack"),settings = buildSettings ++ scalaOnly) dependsOn(querygen)
}


