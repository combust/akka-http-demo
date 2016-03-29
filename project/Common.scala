import sbt._
import Keys._

object Common {
  val appVersion = "0.1-SNAPSHOT"

  var settings: Seq[Def.Setting[_]] = Seq(
    version := appVersion,
    scalaVersion := "2.11.7",
    crossScalaVersions := Seq("2.10.6", "2.11.7"),
    ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
    organization := "ml.bundle",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
  )
}