name := "akka-http-client-demo"

lazy val `root` = project.in(file("."))
  .settings(Common.settings)
  .aggregate(`core`, `client`, `server`)

lazy val `core` = project.in(file("core"))
  .settings(Common.settings)
  .settings(libraryDependencies ++= Dependencies.coreDependencies)

lazy val `client` = project.in(file("client"))
  .settings(Common.settings)
  .settings(libraryDependencies ++= Dependencies.clientDependencies)
  .dependsOn(`core`)

lazy val `server` = project.in(file("server"))
  .settings(Common.settings)
  .settings(libraryDependencies ++= Dependencies.serverDependencies)
  .dependsOn(`core`)
