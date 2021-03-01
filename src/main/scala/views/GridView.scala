package views

import graph.{Graph, Node, NodeState, NodeStates}
import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.geometry.Insets
import scalafx.scene.layout._
import views.Grid.Matrix

final case class Grid(matrix: Matrix) {
  def apply(row: Int): IndexedSeq[Node] = matrix(row)

  def rows: Int = matrix.length

  def columns: Int = matrix.head.length

  def reset(): Unit = {
    matrix.flatten.foreach(_.changeStateTo(NodeStates.Undiscovered))
    Graph.clear()
  }

  def clearResult(): Unit = matrix.flatten
    .filter(n => n.getState == NodeStates.Visited || n.getState == NodeStates.Path)
    .foreach(_.changeStateTo(NodeStates.Undiscovered))
}

object Grid {
  type Matrix = IndexedSeq[IndexedSeq[Node]]

  def apply(rows: Int, columns: Int, toolProp: ObjectProperty[NodeState], levelProp: StringProperty): Grid = {
    val matrix: Matrix = IndexedSeq.tabulate(rows, columns)((row, col) => Node.createNode(toolProp, row, col, levelProp))
    Grid(matrix)
  }
}

object GridView {

  def createGridPane(gridWidth: NumberBinding, grid: Grid): GridPane = {
    //FIXME: grid height must be equal grid width
    val gridPane = new GridPane {
      hgap = 1
      vgap = 1
      minWidth <== gridWidth

      padding = Insets(0, 2, 0, 0)

      columnConstraints = (0 until grid.columns).map(_ =>
        new ColumnConstraints {
          hgrow = Priority.Always
        }
      )
      rowConstraints = (0 until grid.rows).map(_ =>
        new RowConstraints {
          vgrow = Priority.Always
        }
      )

      for {
        i <- 0 until grid.columns
        j <- 0 until grid.rows
      } add(grid(j)(i).region, i, j)
    }

    gridPane
  }
}
