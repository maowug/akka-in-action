package com.example

import akka.actor.{Actor, ActorLogging, Props}

class PingActor extends Actor with ActorLogging {
  //  ActorLogging: val log = Logging(context.system, this)
  
  import PingActor._
  
  var counter = 0
  val pongActor = context.actorOf(PongActor.props, "pongActor")
  
  /**
    * ...
    * [akka://MyActorSystem/user/pingActor/pongActor] PongActor: postStop
    * [akka://MyActorSystem/user/pingActor] PingActor: postStop
    */
  override def postStop() {
    log.info("PingActor: postStop")
  }

  def receive = {
    case Initialize =>
      log.info("In PingActor - starting ping-pong")
      pongActor ! PingMessage("ping")
    case PongActor.PongMessage(text) =>
      log.info("In PingActor - received message: {}", text)
      counter += 1
      if (counter == 3) context.system.terminate()
      else sender() ! PingMessage("ping")
  }
}

object PingActor {
  val props = Props[PingActor]
  case object Initialize
  case class PingMessage(text: String)
}
