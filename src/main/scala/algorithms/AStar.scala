package algorithms
import graph.{Node, NodeStates}
import views.enums.Tick

import scala.collection.mutable

object AStar extends Pathfinder {
  override val name: String = "A*"

  override def findPath(from: Node, to: Node, paths: Map[Node, Set[Node]], tick: Tick): Set[Node] = {
    val distance: mutable.Map[Node, Int]                  = mutable.Map[Node, Int]()
    val priorityQueue: mutable.PriorityQueue[(Node, Int)] =
      mutable.PriorityQueue[(Node, Int)]()(Ordering.by((in: (Node, Int)) => heuristic(in._1, to)).reverse)
    val path: mutable.Map[Node, Node]                     = mutable.Map[Node, Node]()

    distance += (from      -> 0)
    priorityQueue += (from -> distance(from))
    while (priorityQueue.nonEmpty) {
      tick.sleep()

      val (current, dist) = priorityQueue.dequeue()
      if (current.getState == NodeStates.Undiscovered) current.changeStateTo(NodeStates.Visited)
      if (to == current) priorityQueue.clear()
      else
        for (neighbor <- paths(current))
          if (!distance.contains(neighbor) || distance(neighbor) > dist + levelDifference(current, neighbor)) {
            distance += (neighbor      -> (dist + levelDifference(current, neighbor)))
            path += (neighbor          -> current)
            priorityQueue += (neighbor -> distance(neighbor))
          }
    }

    shortestPathNodes(path.toMap, from, to)
  }

  private def heuristic(start: Node, end: Node): Double =
    directDistance(start, end) + levelDifference(start, end)

  private[this] def directDistance(start: Node, end: Node): Double =
    Math.sqrt(Math.pow(start.row - end.row, 2) + Math.pow(start.col - end.col, 2))

  private def levelDifference(start: Node, end: Node): Int = Math.abs(start.getLevel - end.getLevel)
}
