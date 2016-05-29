# gatling-custom-dsls

..._Default_ DSLs are very powerful tools to build up a load scenario to match the requirements. However as we dig deeper into it, there will be need to extend it to match your requirement. Not everything can fold back to the OoB set. With this project, my aim is to showcase how to extend the existing ones, maybe over time with enough pulls it might fold back to the original.

## Extending checks - 
...[Checks](http://gatling.io/docs/2.2.1/http/http_check.html) are the basic validation blocks in gatling scenarios. A lot can be done on the reponse using the [check extractors](http://gatling.io/docs/2.2.1/http/http_check.html#http-check-extracting) like find and transform (which is the best option if you don't want to extend the DSL and work around the existing DSL). The example [here]() shows how we use the checks and push the extracted response to a queue.

## Extending throttling -
## Producer-consumer contract prallel scenarios -

