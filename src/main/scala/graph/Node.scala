package graph

import graph.NodeStates.NodeState
import javafx.scene.layout.{Region => jfxRegion}
import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Region

case class Node() extends Region {
  import Node._

  private[this] var state: NodeState = NodeStates.Undiscovered

  style = greenColor

  private[this] def handlePaint(): Unit =
    state match {
      case NodeStates.Undiscovered =>
        style = redColor
        state = NodeStates.Wall
      case _                       =>
        style = greenColor
        state = NodeStates.Undiscovered
    }

  onDragDetected = (e: MouseEvent) => e.getSource.asInstanceOf[jfxRegion].startFullDrag()
  onMouseDragEntered = _ => handlePaint()
  onMouseClicked = _ => handlePaint()
}

object Node {
  val redColor   = "-fx-background-color: rgb(178,34,34)"
  val greenColor = "-fx-background-color: rgb(144,238,144)"
}

object NodeStates extends Enumeration {
  type NodeState = Value

  val Undiscovered: NodeState = Value
  val Wall: NodeState         = Value
  val Discovered: NodeState   = Value
  val Starting: NodeState     = Value
  val Target: NodeState       = Value
}
