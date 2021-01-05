package graph

import graph.NodeState._
import javafx.scene.layout.{Region => JRegion}
import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Region

final class Node(region: Region, var state: NodeState)

object Graph {
  val mapColumns = 15
  val mapRows    = 11

  val graph: Array[Array[Node]] = Array.fill(mapRows)(new Array(mapColumns))

  private def backgroundStyle(color: String) = s"-fx-background-color: $color"

  private var dragSourceState: NodeState = Undiscovered

  def insertNode(row: Int, col: Int): Region = {
    val region = new Region {
      style = backgroundStyle(Undiscovered.color)
    }

    val node = new Node(region, Undiscovered)

    graph(col)(row) = node

    def handlePaint(): Unit =
      node.state match {
        case Undiscovered =>
          region.style = backgroundStyle(Wall.color)
          node.state = Wall
        case Wall         =>
          region.style = backgroundStyle(Undiscovered.color)
          node.state = Undiscovered
        case _            =>
      }

    region.onDragDetected = (e: MouseEvent) => {
      e.getSource.asInstanceOf[JRegion].startFullDrag()
      dragSourceState = node.state
      handlePaint()
    }
    region.onMouseDragEntered = _ => if (node.state == dragSourceState) handlePaint()
    region.onMouseClicked = _ => {
      dragSourceState = node.state
      handlePaint()

//      for (row <- graph)
//        println(row.map(_.state).mkString(","))
//      println()
    }

    region
  }
}
