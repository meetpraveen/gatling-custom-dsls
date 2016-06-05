package io.praveen.gatling.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.praveen.gatling.extension.dsl.ThrottleExtension._

/**
  * Created by praveesi on 05/06/16.
  */
class Throttle extends Simulation {
  val httpConf = http
    .baseURL("localhosr:8080")
    .contentTypeHeader("application/json")
    .acceptHeader("application/json;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/35.0")

  val execution = exec(http("do something").get("index.html").check(status.is(200)))
  setUp(scenario("").during(60)(exec(execution)).inject(atOnceUsers(10))
    .throttle(multiJump(5) initRps(12) jumpBy(10) stabilizeFor(10)))
    .protocols(httpConf)
}
