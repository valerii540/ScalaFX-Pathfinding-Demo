package graph

import views.Grid.Matrix

import scala.util.Try

case class Graph(paths: Map[Node, Set[Node]])

object Graph {
  var startNode: Option[Node]  = None
  var targetNode: Option[Node] = None

  def apply(grid: Matrix): Graph =
    Graph((for {
      row       <- grid.indices
      col       <- grid(row).indices if grid(row)(col).getState != NodeStates.Obstacle
      neighbours =
        Set(
          Try(grid(row)(col - 1)).toOption,
          Try(grid(row)(col + 1)).toOption,
          Try(grid(row - 1)(col)).toOption,
          Try(grid(row + 1)(col)).toOption
        ).flatten.filter(_.getState != NodeStates.Obstacle)
    } yield grid(row)(col) -> neighbours).toMap)

  def clear(): Unit = {
    startNode = None
    targetNode = None
  }
}
