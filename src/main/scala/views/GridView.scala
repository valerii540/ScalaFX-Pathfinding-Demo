package views

import graph.{Node, NodeState}
import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Insets
import scalafx.scene.layout._

final class Grid(val mapRows: Int, val mapColumns: Int) {
  private[this] val matrix: Array[Array[Node]] = Array.fill(mapRows)(new Array(mapColumns))

  def apply(col: Int): Array[Node] = matrix(col)
}

object GridView {

  def createGridPane(gridWidth: NumberBinding, grid: Grid, toolProp: ObjectProperty[NodeState]): GridPane = {
    def createTile(i: Int, j: Int): Region = Node.createNode(i, j, grid, toolProp).region

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
      } add(createTile(i, j), i, j)
    }

    gridPane
  }
}
