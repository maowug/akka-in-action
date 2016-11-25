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

    case WatchDead(sb) =>
      context.watch(sb)
      log.info(s"PingActorWatcher watched dead $sb")
      
    case Terminated(tg) =>
      log.info(s"PingActorWatcher receive($count) Terminated of $tg ")
      
      val newWatcherAfterDead = context.system.actorOf(PingActorWatcher.props, s"newWatcherAfterDead$count")
      newWatcherAfterDead ! WatchDead(tg)
  
      count = count + 1
      
      if (count == 2) {
        context.system.terminate()
        log.info("PingActorWatcher system terminate")
      }
      
  }
}

object PingActorWatcher {
  
  var count = 0
  
  val props = Props[PingActorWatcher]
  
  case class Watch(sb: ActorRef)
  case class WatchDead(sb: ActorRef)
}
