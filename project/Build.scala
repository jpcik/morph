import sbt._
import Keys._

object HelloBuild extends Build {
    lazy val root = Project(id = "morph-parent",
                            base = file(".")) aggregate(core, querygen)

    lazy val core = Project(id = "morph-core",
                           base = file("morph-core"))

    lazy val querygen = Project(id = "morph-querygen",
                           base = file("morph-querygen"))
}