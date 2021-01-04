package graph

import enumeratum._

sealed abstract class NodeState(val color: String) extends EnumEntry

object NodeState extends Enum[NodeState] {

  case object Undiscovered extends NodeState(color = "rgb(144,238,144)")
  case object Wall         extends NodeState(color = "rgb(178,34,34)")

  val values: IndexedSeq[NodeState] = findValues
}
