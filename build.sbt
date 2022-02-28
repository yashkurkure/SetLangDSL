ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val root = (project in file("."))
  .settings(
    name := "SetLangDSL2"
  )
libraryDependencies +=
  "org.scalatest" %% "scalatest" % "3.2.9"