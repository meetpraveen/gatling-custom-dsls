package io.praveen.gatling.extension.dsl

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.praveen.gatling.extension.dsl.CheckExtension.Queues

/**
  * Extensions for executors
  * Created by praveesi on 05/06/16.
  */
object ExecutorExtension {

  /**
    * Execute the passed chain only if the attribute polled for is in the Queue [[io.praveen.gatling.extension.dsl.CheckExtension.Queues]]
    * bound to userId in the session.
    *
    * It will keep polling the queue until the value is offered to the queue by the producer.
    * @param paramName parameter key for queued element
    * @param chain chain to execute when queue is offered the polled record
    */
  def withPolledRecord(paramName: String)(chain: ChainBuilder) = {
    exec { session =>
      session.set(s"emptyQueueFlag_$paramName", true)
    }
      .asLongAs("${emptyQueueFlag_" + paramName + ".exists()}")(exec { session =>
        val props = Queues.pollForAgentAndProp(session.get("userId").as[String], paramName)
        props match {
          case None => session.set(s"emptyQueueFlag_$paramName", true)
          case Some(x) =>
            session.set(s"$paramName", x).remove(s"emptyQueueFlag_$paramName")
        }
      }).exec(chain)
  }
}
