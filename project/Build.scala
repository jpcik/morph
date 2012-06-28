import sbt._
import Keys._

object HelloBuild extends Build {
  lazy val root = Project(id = "morph-parent",
                          base = file(".")) aggregate(core, querygen,r2rmlTc)

  lazy val core = Project(id = "morph-core",
                          base = file("morph-core"))

  lazy val querygen = Project(id = "morph-querygen",
                              base = file("morph-querygen")) dependsOn(core)

  lazy val r2rmlTc = Project(id = "morph-r2rml-tc",
                             base = file("morph-r2rml-tc")) dependsOn(querygen)
}