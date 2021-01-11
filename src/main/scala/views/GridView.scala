package views

import graph.{Node, NodeState}
import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Insets
import scalafx.scene.layout._

final class Grid(val mapRows: Int, val mapColumns: Int, toolProp: ObjectProperty[NodeState]) {
  val matrix: IndexedSeq[IndexedSeq[Node]] =
    IndexedSeq.fill(mapRows)(IndexedSeq.fill(mapColumns)(Node.createNode(toolProp)))

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
