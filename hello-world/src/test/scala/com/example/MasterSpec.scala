package com.example

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import com.typesafe.config.ConfigFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike


class MasterSpec (_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
  
  def this() = this(ActorSystem("MasterSpec", ConfigFactory.parseString(
    """
      |akka {
      |  loggers = ["akka.testkit.TestEventListener"]
      |  loglevel = "WARNING"
      |  actor {
      |    debug {
      |      unhandled = off
      |    }
      |  }
      |}
    """.stripMargin)))
  
  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
  
  val master = system.actorOf(Props[Master], "master")
  
  "A master" should {
    "apply the chosen strategy for its child" in {
      master ! Props[Slave]
      val slave = expectMsgType[ActorRef]
      slave ! new ArithmeticException
      slave ! 20
      slave ! "get"
      expectMsg(20)
      // [akka://MasterSpec/user/master/$a] unhandled message from
      //     Actor[akka://MasterSpec/system/testActor-2#216760292]: notDefined
      slave ! "notDefined"
    }
  }
}
