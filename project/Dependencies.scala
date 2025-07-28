import sbt._
import Keys._

object Dependencies {

  val domainDependencies: Seq[ModuleID] = Seq(
    "io.github.arainko" %% "ducktape" % "0.2.9"
  )

  val applicationDependencies: Seq[ModuleID] = Seq(
    "dev.zio" %% "zio" % "2.1.20",
    "dev.zio" %% "zio-logging" % "2.5.1"
  )

  val infrastructureDependencies: Seq[ModuleID] = Seq(
    /* Database */
    "org.xerial" % "sqlite-jdbc" % "3.50.3.0",
    "io.getquill" %% "quill-jdbc-zio" % "4.8.6",

    /* Migration */
    "org.flywaydb" % "flyway-core" % "11.10.4",

    /* GraphQL */
    "com.github.ghostdogpr" %% "caliban" % "2.11.1",
    "com.github.ghostdogpr" %% "caliban-quick" % "2.11.1"
  )

  val backendDependencies: Seq[ModuleID] = Seq()
}
