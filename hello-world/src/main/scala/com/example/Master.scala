package com.example

import akka.actor.Actor
import akka.actor.OneForOneStrategy
import akka.actor.Props
import akka.actor.SupervisorStrategy.Escalate
import akka.actor.SupervisorStrategy.Restart
import akka.actor.SupervisorStrategy.Resume

class Master extends Actor {
  
  import scala.concurrent.duration._
  
  override val supervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 5, withinTimeRange =  1 minute
  ) {
    case _: ArithmeticException => Resume
    case _: NullPointerException => Restart
    case _: Exception            => Escalate
  }
  
  override def receive: Receive = {
    case p: Props => sender() ! context.actorOf(p)
  }
}

object Master {
  val props = Props[Master]
}


class Slave extends Actor {
  var state = 0
  def receive = {
    case ex: Exception => throw ex
    case x: Int        => state = x
    case "get"         => sender() ! state
  }
}
