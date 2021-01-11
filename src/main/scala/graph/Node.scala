package graph

import graph.NodeStates._
import javafx.scene.layout.{Region => JRegion}
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Region, StackPane}
import scalafx.scene.text.Text

final class Node(val region: Region) {
  import Node._

  var state: NodeState = NodeStates.Undiscovered

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

  def createNode(toolProp: ObjectProperty[NodeState]): Node = {
    val nodeView = new StackPane {
      style = Node.backgroundStyle(Undiscovered.color)
      children = new Text {
        text = ""
      }
    }

    val node = new Node(nodeView)

    nodeView.onDragDetected = (e: MouseEvent) => {
      toolProp.value match {
        case Obstacle =>
          e.getSource.asInstanceOf[JRegion].startFullDrag()
          dragSourceState = node.state
          node.obstacleToolHandler()
        case _        =>
      }
    }

    nodeView.onMouseDragEntered = _ =>
      if (toolProp.value == NodeStates.Obstacle && node.state == dragSourceState)
        node.obstacleToolHandler()

    nodeView.onMouseClicked = _ => {
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

    node
  }
}
