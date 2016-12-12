package aia.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.ActorMaterializerSettings
import akka.stream.Supervision
import akka.stream.ThrottleMode
import akka.stream.scaladsl.Source
import scala.concurrent.Await

object TimeBased extends App {
  import scala.concurrent.duration._
  implicit val system = ActorSystem()
  
  implicit val ec = system.dispatcher
  
  val decider : Supervision.Decider = {
    case _: LogStreamProcessor.LogParseException => Supervision.Stop
    case _                    => Supervision.Stop
  }
  
  implicit val materializer = ActorMaterializer(
    ActorMaterializerSettings(system)
      .withSupervisionStrategy(decider)
  )
  
  // maximumBurst: 20
  Source.fromIterator(() => (1 to 30).toIterator)
    .throttle(1, 1.second, 20, ThrottleMode.shaping)
    .runForeach(println)
      .foreach( _ => {
          system.terminate()
          Await.result(system.whenTerminated, Duration.Inf)
      })
  
}
