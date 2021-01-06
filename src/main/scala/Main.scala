import graph.{Grid, Node}
import scalafx.application.JFXApp
import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Label, TextField}
import scalafx.scene.layout._

object Main extends JFXApp {
  private val gridView: ObjectProperty[Grid] = ObjectProperty(new Grid(11, 15))
  private val rowsView: StringProperty       = StringProperty(gridView.value.mapRows.toString)
  private val columnsView: StringProperty    = StringProperty(gridView.value.mapColumns.toString)

  rowsView.onChange((_, _, newRows) => gridView.value = new Grid(newRows.toInt, gridView.value.mapColumns))
  columnsView.onChange((_, _, newColumns) => gridView.value = new Grid(gridView.value.mapRows, newColumns.toInt))

  stage = new JFXApp.PrimaryStage { rootScene =>
    title = "ScalaFX Pathfinding Demo"
    width = 600
    height = 400

    scene = new Scene {
      content = new BorderPane {
        gridView.onChange((_, _, newGrid) => center = createGrid(rootScene.width * 0.8, newGrid))

        center = createGrid(rootScene.width * 0.8, gridView.value)

        right = new VBox {
          minHeight <== rootScene.height
          maxWidth <== rootScene.width * 0.2
          children = Seq(
            new HBox {
              children = Seq(
                new Label("rows:"),
                new TextField {
                  prefWidth = 50
                  text <==> rowsView
                }
              )
            },
            new HBox {
              alignment = Pos.Center
              children = Seq(
                new Label("columns:"),
                new TextField {
                  prefWidth = 50
                  text <==> columnsView
                }
              )
            }
          )
        }
      }
    }
  }

  def createGrid(gridWidth: NumberBinding, grid: Grid): GridPane = {
    def createTile(i: Int, j: Int): Region = Node.createNode(i, j, grid).region

    //FIXME: grid height must be equal grid width
    val gridPane = new GridPane {
      hgap = 1
      vgap = 1
      minWidth <== gridWidth

      //FIXME: weird bottom offset
      padding = Insets(2, 2, 26, 2)

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
