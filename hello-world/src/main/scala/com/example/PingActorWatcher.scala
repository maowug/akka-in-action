package com.example

import akka.actor.Actor
import akka.actor.ActorIdentity
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.Identify
import akka.actor.Props
import akka.actor.Terminated
import com.example.PongActor.SelectResult


class PingActorWatcher extends Actor with ActorLogging {
  
  import PingActorWatcher._
  
  override def receive: Receive = {
    case Watch(sb) =>
      context.watch(sb)
      log.info(s"PingActorWatcher watched $sb")

    case WatchDead(sb) =>
      context.watch(sb)
      log.info(s"PingActorWatcher watched dead $sb")


//      // in different actorSystem
//    case ActorIdentity(_, refOpt) =>
//      log.info(s"PingActorWatcher identify ??? $refOpt")

    case SelectResult(proof) =>
      log.info(s"PingActorWatcher SelectResult !!! $proof")
      
    case Terminated(tg) =>
      log.info(s"PingActorWatcher receive($count) Terminated of $tg ")
  
      context.system.actorSelection("pingActor") ! Identify(1)
      
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
