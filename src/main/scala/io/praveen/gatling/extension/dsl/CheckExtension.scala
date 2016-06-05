package io.praveen.gatling.extension.dsl

import java.util.concurrent.ConcurrentLinkedQueue

import io.gatling.commons.validation.Validation
import io.gatling.core.Predef._
import io.gatling.core.check.extractor.Extractor
import io.gatling.core.check.{Check, ValidatorCheckBuilder}

import scala.collection.mutable

/**
  * Created by praveesi on 29/05/16.
  */
object CheckExtension {

  /**
    * Push to queue DSL for [[ValidatorCheckBuilder]]. Its similar to saveAs but pushes the item to queue. This should be used with the Queues [[Queues]]
    * object. The requirement for this builder is that the sesion should have a valid {{{userId}}} set.
    */
  implicit class PushToQueue[C <: Check[R], R, P, X](v: ValidatorCheckBuilder[C, R, P, X]) {
    /**
      * When used on the [[ValidatorCheckBuilder]] it helps in building a logic to extract the response string of the last builder step and
      * push it to a queue for the user executing the current execution. This along with [[ExecutorExtension.withPolledRecord()]] helps in
      * building a producer-consumer kind of load scenario
      *
      * @param keyName key for the parameter to be queued
      * @return builder [[ValidatorCheckBuilder]]
      */
    def pushToQueue(keyName: String): ValidatorCheckBuilder[C, R, P, Unit] = {
      ValidatorCheckBuilder(v.extender, v.preparer, session =>
        v.extractor(session).map(pushToQueueExtractor(pushToQueueOperation(_, keyName, session))))
    }

    private val pushToQueueErrorMapper: String => String = "pushToQueue crashed: " + _

    private def pushToQueueExtractor[X2](pushToQueue: X => X2)(extractor: Extractor[P, X]) =
      new Extractor[P, X2] {
        def name = extractor.name

        def arity = extractor.arity + ".pushToQueue"

        def apply(prepared: P): Validation[Option[X2]] =
          io.gatling.commons.validation.safely(pushToQueueErrorMapper) {
            extractor(prepared).map(_.map(pushToQueue))
          }
      }

    private def pushToQueueOperation(value: X, paramName: String, session: Session): Unit = {
      Queues.offerForAgent(session.get("userId").as[String], paramName, value.toString)
    }
  }

  object Queues {
    val queues = mutable.HashMap[String, mutable.HashMap[String, ConcurrentLinkedQueue[String]]]()

    def queue(agentId: String): mutable.HashMap[String, ConcurrentLinkedQueue[String]] = {
      queues.getOrElseUpdate(agentId, new mutable.HashMap[String, ConcurrentLinkedQueue[String]]())
    }

    def removeQueueForAgent(agentId: String): Option[mutable.HashMap[String, ConcurrentLinkedQueue[String]]] = {
      queues.remove(agentId)
    }

    def offerForAgent(agentId: String, propName: String, value: String): Boolean = {
      val mapOfQueues = queue(agentId)
      val paramQueue = mapOfQueues.getOrElseUpdate(propName, new ConcurrentLinkedQueue[String]())
      paramQueue.offer(value)
    }

    def pollForAgentAndProp(agentId: String, paramName: String): Option[String] = {
      for {
        map <- Some(queue(agentId))
        paramQueue <- map.get(paramName)
        polled <- Option(paramQueue.poll())
      } yield polled
    }
  }
}