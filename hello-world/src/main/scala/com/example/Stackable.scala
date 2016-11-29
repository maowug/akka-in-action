package com.example

import akka.actor.Actor
import akka.actor.Props


trait ActorStack extends Actor {
  def wrappedReceive: Receive
  def receive: Receive = {
    case x => if(wrappedReceive.isDefinedAt(x)) wrappedReceive(x) else unhandled(x)
  }
}

trait HookA extends ActorStack {
  override def receive: Receive = {
    case x => println("before HookA") ;super.receive(x) ;println("after HookA")
  }
}

trait HookB extends ActorStack {
  override def receive: Receive = {
    case x => println("before HookB") ; super.receive(x) ; println("after HookB")
  }
}

/**
  *
    // HookB called before HookA!!!
    before HookB
    before HookA
    got something
    after HookA
    after HookB
  
  */
class DummyActor extends Actor with HookA with HookB {
  def wrappedReceive: Receive = {
    case x => println("got something")
  }
}

object DummyActor {
  val props = Props[DummyActor]
}
