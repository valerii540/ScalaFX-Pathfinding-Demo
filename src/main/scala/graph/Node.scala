package graph

import javafx.scene.layout.{Region => JRegion}
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Region
import views.{Grid, Tool, Tools}

final class Node(val region: Region) {
  import Node._

  private var state: NodeState = NodeStates.Undiscovered

  private def changeStateTo(newState: NodeState): Unit = {
    region.style = backgroundStyle(newState.color)
    state = newState
  }

  def obstacleToolHandler(): Unit =
    state match {
      case NodeStates.Undiscovered => changeStateTo(NodeStates.Wall)
      case NodeStates.Wall         => changeStateTo(NodeStates.Undiscovered)
      case _                       =>
    }

  def startAndTargetToolHandler(targetState: NodeState): Unit = {
    targetState match {
      case NodeStates.Start  =>
        if (startNode.isDefined)
          startNode.get.changeStateTo(NodeStates.Undiscovered)

        startNode = Some(this)
      case NodeStates.Target =>
        if (targetNode.isDefined)
          targetNode.get.changeStateTo(NodeStates.Undiscovered)

        targetNode = Some(this)
    }

    changeStateTo(targetState)
  }
}

object Node {
  private var dragSourceState: NodeState = NodeStates.Undiscovered
  private var startNode: Option[Node]    = None
  private var targetNode: Option[Node]   = None

  def backgroundStyle(color: String) = s"-fx-background-color: $color"

  def createNode(row: Int, col: Int, grid: Grid, toolProp: ObjectProperty[Tool]): Node = {
    val region = new Region {
      style = Node.backgroundStyle(NodeStates.Undiscovered.color)
    }

    val node = new Node(region)

    region.onDragDetected = (e: MouseEvent) => {
      toolProp.value match {
        case Tools.Obstacle =>
          e.getSource.asInstanceOf[JRegion].startFullDrag()
          dragSourceState = node.state
          node.obstacleToolHandler()
        case _              =>
      }
    }

    region.onMouseDragEntered = _ =>
      if (toolProp.value == Tools.Obstacle && node.state == dragSourceState)
        node.obstacleToolHandler()

    region.onMouseClicked = _ => {
      toolProp.value match {
        case Tools.Obstacle =>
          dragSourceState = node.state
          node.obstacleToolHandler()
        case Tools.Start    =>
          node.startAndTargetToolHandler(NodeStates.Start)
        case Tools.Target   =>
          node.startAndTargetToolHandler(NodeStates.Target)
      }
    }

    grid(col)(row) = node

    node
  }
}
