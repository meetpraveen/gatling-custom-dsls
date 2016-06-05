package io.praveen.gatling.extension.dsl

import io.gatling.core.controller.throttle._

import scala.concurrent.duration.FiniteDuration

/**
  * Created by praveesi on 05/06/16.
  */
object ThrottleExtension {

  /**
    * Add multi jump DSL to the Throttling DSL.
    */
  def multiJump(times: Int) = {
    MultiJumpStep(times)
  }

  case class MultiJumpStep(times: Int) {
    def initRps(initRps: Int) = MultiJumpInitRps(initRps, times)
  }

  case class MultiJumpInitRps(initRps: Int, times: Int) {
    def jumpBy(jumpBy: Int) = MultiJumpJumpBy(jumpBy, initRps, times)
  }

  case class MultiJumpJumpBy(jumpBy: Int, initRps: Int, times: Int) {
    def stabilizeFor(stabilizeFor: FiniteDuration) = mj(times, initRps, jumpBy, stabilizeFor)
  }

  private def mj(times: Int, startAt: Int, jumpByRps: Int, stabilizeFor: FiniteDuration): List[ThrottleStep] =
    (0 to times)
      .foldLeft(List.empty[ThrottleStep]) { (acc, currCount) =>
        Hold(stabilizeFor) :: Jump(startAt + currCount * jumpByRps) :: acc
      }
}
