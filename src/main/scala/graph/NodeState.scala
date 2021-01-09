package graph

import enumeratum._

sealed abstract class NodeState(val color: String) extends EnumEntry

object NodeStates extends Enum[NodeState] {

  case object Undiscovered extends NodeState(color = "rgb(144,238,144)")
  case object Wall         extends NodeState(color = "rgb(178,34,34)")
  case object Start        extends NodeState(color = "rgb(255,255,153)")
  case object Target       extends NodeState(color = "rgb(51,102,255)")

  val values: IndexedSeq[NodeState] = findValues
}
