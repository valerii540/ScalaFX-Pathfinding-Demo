package graph

import scala.collection.mutable
import scala.util.Try

case class Graph(paths: Map[Node, Set[Node]]) {

  def bfs(start: Node, end: Node): Map[Node, Node] = {
    val distance: mutable.Map[Node, Int] = mutable.Map[Node, Int]()
    val priorityQueue: mutable.PriorityQueue[(Node, Int)] = mutable.PriorityQueue[(Node, Int)]()(Ordering.by((_: (Node, Int))._2).reverse)
    val path: mutable.Map[Node, Node] = mutable.Map[Node, Node]()

    distance += (start -> 0)
    priorityQueue += (start -> distance(start))
    while (priorityQueue.nonEmpty) {
      val (current, dist) = priorityQueue.dequeue()
      if (end == current) priorityQueue.clear()
      else {
        for (neighbor <- paths(current)) {
          if (!distance.contains(neighbor) || distance(neighbor) > dist + 1) {
            distance += (neighbor -> (dist + 1))
            path += (neighbor -> current)
            priorityQueue += (neighbor -> distance(neighbor))
          }
        }
      }
    }
    path.toMap
  }

  def shortestPathNodes(path: Map[Node, Node], start: Node, end: Node): Set[Node] = {
    var current = end
    val nodes = mutable.Set.empty[Node]
    while (current != start) {
      nodes.add(current)
      current = path(current)
    }
    nodes.toSet
  }

}

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
