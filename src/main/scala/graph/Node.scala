package graph

import graph.NodeStates._
import javafx.scene.layout.{Region => JRegion}
import scalafx.Includes._
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Region, StackPane}

final case class Node(@transient region: Region, row: Int, col: Int, private var level: Int) {
  import Node._

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
    (targetState: @unchecked) match {
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

  private def withHandlers(toolProp: ObjectProperty[NodeState], levelProp: StringProperty): Node = {
    region.onDragDetected = (e: MouseEvent) => {
      toolProp.value match {
        case Obstacle     =>
          e.getSource.asInstanceOf[JRegion].startFullDrag()
          dragSourceState = state
          obstacleToolHandler()
        case Undiscovered =>
          e.getSource.asInstanceOf[JRegion].startFullDrag()
          dragSourceState = state
          setLevelHandler(levelProp.value.toInt)
        case _            =>
      }
    }

    region.onMouseDragEntered = _ =>
      (toolProp.value, state == dragSourceState) match {
        case (Obstacle, true)  => obstacleToolHandler()
        case (Undiscovered, _) => setLevelHandler(levelProp.value.toInt)
        case _                 =>
      }

    region.onMouseClicked = _ => {
      (toolProp.value: @unchecked) match {
        case NodeStates.Obstacle     =>
          dragSourceState = state
          obstacleToolHandler()
        case NodeStates.Undiscovered =>
          dragSourceState = state
          setLevelHandler(levelProp.value.toInt)
        case NodeStates.Start        =>
          startAndTargetToolHandler(NodeStates.Start)
        case NodeStates.Target       =>
          startAndTargetToolHandler(NodeStates.Target)
      }
    }

    this
  }
}

object Node {
  private var dragSourceState: NodeState = Undiscovered

  private def backgroundStyle(state: NodeState, level: Int = 0) =
    s"-fx-background-color: ${state.color.atLevel(state match {
      case Undiscovered => level
      case _            => 0
    })}"

  def enrichNode(partialNode: Node, toolProp: ObjectProperty[NodeState], levelProp: StringProperty): Node = {
    val nodeView = new StackPane {
      style = backgroundStyle(partialNode.state, partialNode.level)
    }

    val node = partialNode
      .copy(region = nodeView)
      .withHandlers(toolProp, levelProp)

    node.state = partialNode.state

    node
  }

  def createNode(toolProp: ObjectProperty[NodeState], row: Int, col: Int, levelProp: StringProperty): Node = {
    val nodeView = new StackPane {
      style = backgroundStyle(Undiscovered)
    }

    Node(nodeView, row, col, 0)
      .withHandlers(toolProp, levelProp)
  }
}
