package graph

import graph.NodeStates._
import javafx.scene.layout.{Region => JRegion}
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Region
import views.Grid

final class Node(val region: Region) {
  import Node._

  private var state: NodeState = NodeStates.Undiscovered

  private def changeStateTo(newState: NodeState): Unit = {
    region.style = backgroundStyle(newState.color)
    state = newState
  }

  def obstacleToolHandler(): Unit =
    state match {
      case Undiscovered => changeStateTo(Obstacle)
      case Obstacle     => changeStateTo(Undiscovered)
      case _            =>
    }

  def startAndTargetToolHandler(targetState: NodeState): Unit = {
    targetState match {
      case Start  =>
        if (startNode.isDefined)
          startNode.get.changeStateTo(Undiscovered)

        startNode = Some(this)
      case Target =>
        if (targetNode.isDefined)
          targetNode.get.changeStateTo(Undiscovered)

        targetNode = Some(this)
    }

    changeStateTo(targetState)
  }
}

object Node {
  private var dragSourceState: NodeState = Undiscovered
  private var startNode: Option[Node]    = None
  private var targetNode: Option[Node]   = None

  def backgroundStyle(color: String) = s"-fx-background-color: $color"

  def createNode(row: Int, col: Int, grid: Grid, toolProp: ObjectProperty[NodeState]): Node = {
    val region = new Region {
      style = Node.backgroundStyle(Undiscovered.color)
    }

    val node = new Node(region)

    region.onDragDetected = (e: MouseEvent) => {
      toolProp.value match {
        case Obstacle =>
          e.getSource.asInstanceOf[JRegion].startFullDrag()
          dragSourceState = node.state
          node.obstacleToolHandler()
        case _        =>
      }
    }

    region.onMouseDragEntered = _ =>
      if (toolProp.value == NodeStates.Obstacle && node.state == dragSourceState)
        node.obstacleToolHandler()

    region.onMouseClicked = _ => {
      toolProp.value match {
        case NodeStates.Obstacle =>
          dragSourceState = node.state
          node.obstacleToolHandler()
        case NodeStates.Start    =>
          node.startAndTargetToolHandler(NodeStates.Start)
        case NodeStates.Target   =>
          node.startAndTargetToolHandler(NodeStates.Target)
      }
    }

    grid(col)(row) = node

    node
  }
}
