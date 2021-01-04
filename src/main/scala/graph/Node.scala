package graph

import graph.NodeState._
import javafx.scene.layout.{Region => JRegion}
import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Region

case class Node() extends Region {
  import Node._

  private[this] var state: NodeState = Undiscovered

  style = backgroundStyle(Undiscovered.color)

  private[this] def handlePaint(): Unit =
    state match {
      case Undiscovered =>
        style = backgroundStyle(Wall.color)
        state = Wall
      case Wall         =>
        style = backgroundStyle(Undiscovered.color)
        state = Undiscovered
      case _            =>
    }

  onDragDetected = (e: MouseEvent) => {
    e.getSource.asInstanceOf[JRegion].startFullDrag()
    dragSourceState = state
    handlePaint()
  }
  onMouseDragEntered = _ => if (state == dragSourceState) handlePaint()
  onMouseClicked = _ => {
    dragSourceState = state
    handlePaint()
  }
}

object Node {
  private var dragSourceState: NodeState = Undiscovered

  private def backgroundStyle(color: String) = s"-fx-background-color: $color"
}
