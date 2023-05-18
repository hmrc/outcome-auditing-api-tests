import sbt._

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"         %% "scalatest"               % "3.2.15" % Test,
    "com.vladsch.flexmark"   % "flexmark-all"            % "0.62.2" % Test,
    "com.typesafe"           % "config"                  % "1.4.2"  % Test,
    "com.typesafe.play"     %% "play-ahc-ws-standalone"  % "2.1.10" % Test,
    "org.slf4j"              % "slf4j-simple"            % "1.7.36" % Test,
    "com.typesafe.play"      %% "play-ws-standalone-json" % "2.1.2"   % Test,
    "com.github.tomakehurst" % "wiremock"                 % "2.27.2"  % Test,
    "org.assertj"            % "assertj-core"             % "3.23.1"  % Test,
    "uk.gov.hmrc"           %% "outcome-auditing"         % "0.+"     % Test
  )
}
