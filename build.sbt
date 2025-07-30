import Dependencies.*
import _root_.caliban.tools.Codegen

val globalScalaVersion = "3.3.6"
ThisBuild / scalaVersion := globalScalaVersion
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := "4.9.9"
ThisBuild / scalacOptions := Seq("-Xmax-inlines", "50")

lazy val root = project
  .in(file("."))
  .enablePlugins(CalibanPlugin)
  .settings(
    libraryDependencies ++= (infrastructureDependencies ++ applicationDependencies ++ domainDependencies),
    Compile / caliban / calibanSettings ++= Seq(
      calibanSetting(file("petlovefam.graphql"))(
        _.genType(Codegen.GenType.Schema)
          .packageName("com.petlovefam.graphql")
          .effect("Task")
          .imports("zio.Task")
          .scalarMapping(("ID", "String"), ("Date", "java.time.LocalDateTime"))
      )
    )
  )

Global / onChangedBuildSource := ReloadOnSourceChanges
Compile / run / fork := true
