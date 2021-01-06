package graph

import graph.NodeState._
import javafx.scene.layout.{Region => JRegion}
import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Region

final class Node(val region: Region, var state: NodeState) {
  import Node._

  def handlePaint(): Unit =
    state match {
      case Undiscovered =>
        region.style = backgroundStyle(Wall.color)
        state = Wall
      case Wall         =>
        region.style = backgroundStyle(Undiscovered.color)
        state = Undiscovered
      case _            =>
    }
}

object Node {
  private var dragSourceState: NodeState = Undiscovered

  def backgroundStyle(color: String) = s"-fx-background-color: $color"

  def createNode(row: Int, col: Int): Node = {
    val region = new Region {
      style = Node.backgroundStyle(Undiscovered.color)
    }

    val node = new Node(region, Undiscovered)

    region.onDragDetected = (e: MouseEvent) => {
      e.getSource.asInstanceOf[JRegion].startFullDrag()
      dragSourceState = node.state
      node.handlePaint()
    }
    region.onMouseDragEntered = _ => if (node.state == dragSourceState) node.handlePaint()
    region.onMouseClicked = _ => {
      dragSourceState = node.state
      node.handlePaint()
    }

    Grid(col)(row) = node

    node
  }
}
