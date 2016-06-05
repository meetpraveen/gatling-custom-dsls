name := "custom-gatling-dsls"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.0" % "test",
  "io.gatling" % "gatling-test-framework" % "2.2.0" ,
  "io.gatling" % "gatling-bundle" % "2.2.0" artifacts (Artifact("gatling-bundle", "zip", "zip", "bundle"))
)

    