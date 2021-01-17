package algorithms
import graph.{Node, NodeStates}
import views.enums.Tick

import scala.collection.mutable

object AStar extends Pathfinder {
  override val name: String = "A*"

  override def findPath(from: Node, to: Node, paths: Map[Node, Set[Node]], tick: Tick): Set[Node] = {
    val distance: mutable.Map[Node, Int]                  = mutable.Map[Node, Int]()
    val priorityQueue: mutable.PriorityQueue[(Node, Int)] =
      mutable.PriorityQueue[(Node, Int)]()(Ordering.by((in: (Node, Int)) => directDistance(in._1, to)).reverse)
    val path: mutable.Map[Node, Node]                     = mutable.Map[Node, Node]()

    distance += (from      -> 0)
    priorityQueue += (from -> distance(from))
    while (priorityQueue.nonEmpty) {
      tick.sleep()

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

  private[this] def directDistance(start: Node, end: Node): Double =
    Math.sqrt(Math.pow(start.row - end.row, 2) + Math.pow(start.col - end.col, 2))
}
