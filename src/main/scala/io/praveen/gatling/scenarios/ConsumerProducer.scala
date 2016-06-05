package io.praveen.gatling.scenarios

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.praveen.gatling.extension.dsl.CheckExtension._
import io.praveen.gatling.extension.dsl.ExecutorExtension._

/**
  * Created by praveesi on 05/06/16.
  */
class ConsumerProducer extends Simulation {
  val httpConf = http
    .baseURL("localhosr:8080")
    .contentTypeHeader("application/json")
    .acceptHeader("application/json;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/35.0")

  val producer = exec(http("create article").post("service.json/article").check(status.is(201), jsonPath("$.articleId").transform(string => string).pushToQueue("articleId")))
  //This execution will make the scenario block until there is an articleId created in the queue for the user
  val consumer = withPolledRecord("articleId")(exec(http("create article").put("service.json/article${articleId}").body(StringBody("""{"consumed":true}""")).check(status.is(200))))

  setUp(
    scenario("Create").during(60)(exec(producer)).inject(atOnceUsers(10)),
    scenario("Consume").during(70)(exec(consumer)).inject(nothingFor(10), atOnceUsers(10))
  ).protocols(httpConf)
}

