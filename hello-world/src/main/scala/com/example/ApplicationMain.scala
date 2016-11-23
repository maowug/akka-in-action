package com.example

import akka.actor.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")
  val pingActor = system.actorOf(PingActor.props, "pingActor")
  
  pingActor ! PingActor.Initialize
  
  //  system.awaitTermination()
  Await.result(system.whenTerminated, Duration.Inf)
}
