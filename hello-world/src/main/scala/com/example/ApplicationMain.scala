package com.example

import akka.actor.ActorSystem
import akka.actor.Inbox
import akka.actor.Props
import com.example.PingActorWatcher.Watch
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")
  val pingActor = system.actorOf(PingActor.props, "pingActor")
  
  
//  val watcherSystem = ActorSystem("MyWatcherActorSystem")
//  val pingActorWatcher = watcherSystem.actorOf(PingActorWatcher.props, "pingActorWatcher")
//
//
//  pingActorWatcher ! Watch(pingActor)
//
//  pingActor ! PingActor.Initialize
  
//  //  system.awaitTermination()
//  Await.result(system.whenTerminated, Duration.Inf)
//  Await.result(watcherSystem.whenTerminated, Duration.Inf)
  
  
  val  dummyActor = system.actorOf(DummyActor.props, "dummyActor")
  dummyActor ! "something"
  
  Await.result(system.whenTerminated, Duration.Inf)
}
