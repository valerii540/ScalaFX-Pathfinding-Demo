package graph

import graph.NodeStates._
import javafx.scene.layout.{Region => JRegion}
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Region, StackPane}
import scalafx.scene.text.Text

final class Node(val region: Region, val row: Int, val col: Int) {
  import Node._

  var state: NodeState = NodeStates.Undiscovered

  def changeStateTo(newState: NodeState): Unit = {
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
        if (Graph.startNode.isDefined)
          Graph.startNode.get.changeStateTo(Undiscovered)

        Graph.startNode = Some(this)
      case Target =>
        if (Graph.targetNode.isDefined)
          Graph.targetNode.get.changeStateTo(Undiscovered)

        Graph.targetNode = Some(this)
    }

    changeStateTo(targetState)
  }
}

object Node {
  private var dragSourceState: NodeState = Undiscovered

  def backgroundStyle(color: String) = s"-fx-background-color: $color"

  def createNode(toolProp: ObjectProperty[NodeState], row: Int, col: Int): Node = {
    val nodeView = new StackPane {
      style = Node.backgroundStyle(Undiscovered.color)
      children = new Text {
        text = ""
      }
    }

    val node = new Node(nodeView, row, col)

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
