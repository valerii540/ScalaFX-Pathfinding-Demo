package graph

import graph.NodeStates._
import javafx.scene.layout.{Region => JRegion}
import scalafx.Includes._
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Region, StackPane}
import scalafx.scene.text.Text

final class Node(val region: Region, val row: Int, val col: Int) {
  import Node._

  private[this] var level: Int = 0

  private var state: NodeState = NodeStates.Undiscovered

  def getLevel: Int = level

  def getState: NodeState = state

  def changeStateTo(newState: NodeState): Unit = {
    region.style = backgroundStyle(newState, level)
    state = newState
  }

  private def obstacleToolHandler(): Unit =
    state match {
      case Undiscovered => changeStateTo(Obstacle)
      case Obstacle     => changeStateTo(Undiscovered)
      case _            =>
    }

  private def setLevelHandler(level: Int): Unit = {
    this.level = level
    changeStateTo(state)
  }

  private def startAndTargetToolHandler(targetState: NodeState): Unit = {
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
  private[this] var dragSourceState: NodeState = Undiscovered

  private def backgroundStyle(state: NodeState, level: Int = 0) =
    s"-fx-background-color: ${state.color.atLevel(state match {
      case Undiscovered => level
      case _            => 0
    })}"

  def createNode(toolProp: ObjectProperty[NodeState], row: Int, col: Int, level: StringProperty): Node = {
    val nodeView = new StackPane {
      style = backgroundStyle(Undiscovered)
      children = new Text("")
    }

    val node = new Node(nodeView, row, col)

    nodeView.onDragDetected = (e: MouseEvent) => {
      toolProp.value match {
        case Obstacle     =>
          e.getSource.asInstanceOf[JRegion].startFullDrag()
          dragSourceState = node.state
          node.obstacleToolHandler()
        case Undiscovered =>
          e.getSource.asInstanceOf[JRegion].startFullDrag()
          dragSourceState = node.state
          node.setLevelHandler(level.value.toInt)
        case _            =>
      }
    }

    nodeView.onMouseDragEntered = _ =>
      (toolProp.value, node.state == dragSourceState) match {
        case (Obstacle, true)  => node.obstacleToolHandler()
        case (Undiscovered, _) => node.setLevelHandler(level.value.toInt)
        case _                 =>
      }

    nodeView.onMouseClicked = _ => {
      toolProp.value match {
        case NodeStates.Obstacle     =>
          dragSourceState = node.state
          node.obstacleToolHandler()
        case NodeStates.Undiscovered =>
          dragSourceState = node.state
          node.setLevelHandler(level.value.toInt)
        case NodeStates.Start        =>
          node.startAndTargetToolHandler(NodeStates.Start)
        case NodeStates.Target       =>
          node.startAndTargetToolHandler(NodeStates.Target)
      }
    }

    node
  }
}
