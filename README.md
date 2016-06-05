# gatling-custom-dsls

_Default_ DSLs are very powerful tools to build up a load scenario to match the requirements. However as we dig deeper into it, there will be need to extend it to match your requirement. Not everything can fold back to the OoB set. With this project, my aim is to showcase how to extend the existing ones, maybe over time with enough pulls it might fold back to the original.

## Extending checks - 
[Checks](http://gatling.io/docs/2.2.1/http/http_check.html) are the basic validation blocks in gatling scenarios. A lot can be done on the reponse using the [check extractors](http://gatling.io/docs/2.2.1/http/http_check.html#http-check-extracting) like find and transform (which is the best option if you don't want to extend the DSL and work around the existing DSL). The example [here](https://github.com/meetpraveen/gatling-custom-dsls/blob/master/src/main/scala/io/praveen/gatling/extension/dsl/CheckExtension.scala) shows how we use the checks and push the extracted response to a queue.

## Extending throttling - 
Throttling support has come as a boon for service charecterization. Benchmarking of the services is an important part to charecterize it. And one of the important parameters for benchmarking is the load a stateless service can take. For a stateless service the load is almost always measured in the RPS terms. Concurrent session is important, but the measure of how many request per second a service can handle is almost always more pertinent.

There can be various use cases to extend it, the example [here](https://github.com/meetpraveen/gatling-custom-dsls/blob/master/src/main/scala/io/praveen/gatling/extension/dsl/ThrottleExtension.scala) shows how we can have a continuous ramp up of the throttling RPS.

## Producer-consumer contract prallel scenarios - 
Scenarios in gatling by default run in parallel. This is good but, if one has a producer/consumer kind of service supported by multiple workers and we have to develop detached scenarios for producers and consumers we need some kind of blocked/waiting support across scenarios. A combination of [pushToQueue](https://github.com/meetpraveen/gatling-custom-dsls/blob/master/src/main/scala/io/praveen/gatling/extension/dsl/CheckExtension.scala#L30) and [withPolledRecord](https://github.com/meetpraveen/gatling-custom-dsls/blob/master/src/main/scala/io/praveen/gatling/extension/dsl/ExecutorExtension.scala#L21) gives this. This in its part describes how to create your own execute blocks.

