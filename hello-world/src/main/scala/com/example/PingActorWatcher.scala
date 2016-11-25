package com.example

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Terminated


class PingActorWatcher extends Actor with ActorLogging {
  
  import PingActorWatcher._
  
  override def receive: Receive = {
    case Watch(sb) =>
      context.watch(sb)
      log.info(s"PingActorWatcher watched $sb")
    case Terminated(_) =>
      log.info("PingActorWatcher receive Terminated")
      context.system.terminate()
      log.info("PingActorWatcher system terminate")
      
  }
}

object PingActorWatcher {
  val props = Props[PingActorWatcher]
  
  case class Watch(sb: ActorRef)
}
