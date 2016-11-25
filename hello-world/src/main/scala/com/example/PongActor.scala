package com.example

import akka.actor.ActorIdentity
import akka.actor.ActorRef
import akka.actor.Identify
import akka.actor.{Actor, ActorLogging, Props}

class PongActor extends Actor with ActorLogging {
  import PongActor._

  var forwardIdentifySender: Any = null
  
  def receive = {
    case PingActor.PingMessage(text) =>
      log.info("In PongActor - received message: {}", text)
      sender() ! PongMessage("pong")
    
    case ActorIdentity(_, refOpt) =>
      forwardIdentifySender.asInstanceOf[ActorRef] ! refOpt.toString
      
    case SelectMessage(path) =>
      forwardIdentifySender = sender()
      context.system.actorSelection(path) ! Identify
  }
  
  override def postStop() {
    log.info("PongActor: postStop")
  }
  
}

object PongActor {
  val props = Props[PongActor]
  case class PongMessage(text: String)
  case class SelectMessage(path: String)
  case class SelectResult(proof: String)
}
