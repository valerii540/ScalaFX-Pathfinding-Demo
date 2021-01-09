package views

import enumeratum._

sealed trait Tool extends EnumEntry

object Tools extends Enum[Tool] {

  case object Obstacle extends Tool
  case object Start    extends Tool
  case object Target   extends Tool

  val values: IndexedSeq[Tool] = findValues
}
