package algorithms

import graph.Node
import views.enums.Tick

import scala.collection.mutable

trait Pathfinder {
  val name: String

  def findPath(from: Node, to: Node, paths: Map[Node, Set[Node]], tick: Tick): Set[Node]

  protected final def shortestPathNodes(path: Map[Node, Node], start: Node, end: Node): Set[Node] = {
    var current = end
    val nodes   = mutable.Set.empty[Node]
    while (current != start) {
      nodes.add(current)
      current = path(current)
    }
    nodes.toSet
  }

  final override def toString: String = name
}
