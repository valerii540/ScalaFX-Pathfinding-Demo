package graph

import scala.util.Try

case class Graph(paths: Map[Node, Set[Node]])

object Graph {
  def apply(grid: IndexedSeq[IndexedSeq[Node]]): Graph =
    Graph((for {
      row       <- grid.indices
      col       <- grid(row).indices if grid(row)(col).state != NodeStates.Obstacle
      neighbours =
        Set(
          Try(grid(row)(col - 1)).toOption,
          Try(grid(row)(col + 1)).toOption,
          Try(grid(row - 1)(col)).toOption,
          Try(grid(row + 1)(col)).toOption
        ).flatten.filter(_.state != NodeStates.Obstacle)
    } yield grid(row)(col) -> neighbours).toMap)
}
