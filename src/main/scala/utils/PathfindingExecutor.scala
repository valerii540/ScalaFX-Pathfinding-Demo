package utils

import algorithms.Pathfinder
import cats.effect.{CancelToken, IO}
import graph.{Graph, NodeStates}
import scalafx.beans.property.ObjectProperty
import views.Grid

import java.util.concurrent.{ExecutorService, Executors, ThreadFactory}

object PathfindingExecutor {

  private[this] var cancelPathfinding: Option[CancelToken[IO]] = None

  /** Daemonic thread executor. Daemonic behavior provides correct application shutdown.
    */
  private[this] val daemonicExecutor: ExecutorService =
    Executors.newSingleThreadExecutor(
      new ThreadFactory() {
        override def newThread(r: Runnable): Thread = {
          val t = Executors.defaultThreadFactory.newThread(r)
          t.setDaemon(true)
          t
        }
      }
    )

  def cancelRunning(): Unit =
    if (cancelPathfinding.isDefined) {
      cancelPathfinding.get.unsafeRunSync()
      cancelPathfinding = None
    }

  def execute(gridProp: ObjectProperty[Grid], algorithm: Pathfinder)(callBack: => Unit): Unit = {
    val pathfinding: IO[Unit] = IO.cancelable { cb =>
      gridProp.value.clearResult()

      val graph = Graph(gridProp.value.matrix)
      val start = Graph.startNode.get
      val end   = Graph.targetNode.get

      val pathfinding = daemonicExecutor.submit {
        new Runnable {
          override def run(): Unit = {
            val path = algorithm.findPath(start, end, graph.paths)
            path.filter(_.state != NodeStates.Target).foreach(_.changeStateTo(NodeStates.Path))

            cb(Right(callBack))
          }
        }
      }

      IO(pathfinding.cancel(true))
    }

    cancelPathfinding = Some(pathfinding.unsafeRunCancelable {
      case Right(_)        => ()
      case Left(throwable) => throw throwable
    })
  }
}
