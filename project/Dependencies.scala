import sbt._

object Dependencies {
  val akkaStreamVersion = "2.4.2"

  lazy val akkaDependencies = Seq("com.typesafe.akka" %% "akka-http-core" % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamVersion)

  lazy val coreDependencies = akkaDependencies
    .union(Seq("io.spray" %% "spray-json" % "1.3.2"))

  lazy val clientDependencies = coreDependencies

  lazy val serverDependencies = coreDependencies
}