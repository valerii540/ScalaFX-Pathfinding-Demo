package graph

import enumeratum._

case class Color(red: Int, green: Int, blue: Int) {
  def atLevel(level: Int): Color = this.copy(
    red = this.red - level * 12,
    green = this.green - level * 20,
    blue = this.blue - level * 12
  )

  override def toString: String = s"rgb($red,$green,$blue)"
}

sealed abstract class NodeState(val color: Color) extends EnumEntry

object NodeStates extends Enum[NodeState] {

  case object Undiscovered extends NodeState(color = Color(144, 238, 144))
  case object Obstacle     extends NodeState(color = Color(178, 34, 34))
  case object Start        extends NodeState(color = Color(255, 255, 153))
  case object Target       extends NodeState(color = Color(51, 102, 255))
  case object Visited      extends NodeState(color = Color(195, 195, 195))
  case object Path         extends NodeState(color = Color(80, 80, 80))

  val values: IndexedSeq[NodeState] = findValues
}
