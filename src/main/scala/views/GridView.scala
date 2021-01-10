package views

import graph.{Node, NodeState}
import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Insets
import scalafx.scene.layout._

import scala.util.Try

final class Grid(val mapRows: Int, val mapColumns: Int, toolProp: ObjectProperty[NodeState]) {
  private val matrix: IndexedSeq[IndexedSeq[Node]] = {

    val mMatrix: Array[Array[Node]] = Array.fill(mapRows)(new Array(mapColumns))

    for {
      row <- mMatrix.indices
      col <- mMatrix(row).indices
    } mMatrix(row)(col) = Node.createNode(toolProp)

    for {
      row <- mMatrix.indices
      col <- mMatrix(row).indices
    } {
      val neighbours =
        Set(
          Try(mMatrix(row)(col - 1)).toOption,
          Try(mMatrix(row)(col + 1)).toOption,
          Try(mMatrix(row - 1)(col)).toOption,
          Try(mMatrix(row + 1)(col)).toOption
        ).flatten

      mMatrix(row)(col) = new Node(mMatrix(row)(col).region, neighbours)
    }

    mMatrix.toIndexedSeq.map(_.toIndexedSeq)
  }

  for (row <- matrix)
    println(row.map(_.neighbours.size).mkString(", "))
  println()

  def apply(row: Int): IndexedSeq[Node] = matrix(row)
}

object GridView {

  def createGridPane(gridWidth: NumberBinding, grid: Grid): GridPane = {
    //FIXME: grid height must be equal grid width
    val gridPane = new GridPane {
      hgap = 1
      vgap = 1
      minWidth <== gridWidth

      padding = Insets(0, 2, 0, 0)

      columnConstraints = (0 until grid.mapColumns).map(_ =>
        new ColumnConstraints {
          hgrow = Priority.Always
        }
      )
      rowConstraints = (0 until grid.mapRows).map(_ =>
        new RowConstraints {
          vgrow = Priority.Always
        }
      )

      for {
        i <- 0 until grid.mapColumns
        j <- 0 until grid.mapRows
      } add(grid(j)(i).region, i, j)
    }

    gridPane
  }
}
