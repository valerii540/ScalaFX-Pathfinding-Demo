package algorithms
import graph.{Node, NodeStates}

import scala.collection.mutable

object BFS extends Pathfinder {
  override val name: String = "BFS"

  override def findPath(from: Node, to: Node, paths: Map[Node, Set[Node]]): Set[Node] = {
    val distance: mutable.Map[Node, Int]                  = mutable.Map[Node, Int]()
    val priorityQueue: mutable.PriorityQueue[(Node, Int)] =
      mutable.PriorityQueue[(Node, Int)]()(Ordering.by((_: (Node, Int))._2).reverse)
    val path: mutable.Map[Node, Node]                     = mutable.Map[Node, Node]()

    distance += (from      -> 0)
    priorityQueue += (from -> distance(from))
    while (priorityQueue.nonEmpty) {
      Thread.sleep(200)
      val (current, dist) = priorityQueue.dequeue()
      if (current.state == NodeStates.Undiscovered) current.changeStateTo(NodeStates.Visited)
      if (to == current) priorityQueue.clear()
      else
        for (neighbor <- paths(current))
          if (!distance.contains(neighbor) || distance(neighbor) > dist + 1) {
            distance += (neighbor      -> (dist + 1))
            path += (neighbor          -> current)
            priorityQueue += (neighbor -> distance(neighbor))
          }
    }

    shortestPathNodes(path.toMap, from, to)
  }
}
