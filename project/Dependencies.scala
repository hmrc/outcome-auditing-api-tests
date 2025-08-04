import sbt._

object Dependencies {

  private val playWsVersion = "3.0.7"

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"                %% "scalatest"                % "3.2.19",
    "com.vladsch.flexmark"          % "flexmark-all"             % "0.64.8",
    "com.typesafe"                  % "config"                   % "1.4.3",
    "org.playframework"            %% "play-ahc-ws-standalone"   % playWsVersion,
    "org.playframework"            %% "play-ws-standalone-json"  % playWsVersion,
    "org.wiremock"                  % "wiremock"                 % "3.13.1",
    "com.fasterxml.jackson.module" %% "jackson-module-scala"     % "2.19.2",
    "io.swagger.parser.v3"          % "swagger-parser"           % "2.1.31",
    "org.openapi4j"                 % "openapi-schema-validator" % "1.0.7"
  ).map(_ % Test)
}
