import graph.Node
import scalafx.application.JFXApp
import scalafx.beans.binding.NumberBinding
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

object Main extends JFXApp {

  stage = new JFXApp.PrimaryStage { rootScene =>
    title = "ScalaFX Pathfinding Demo"
    width = 600
    height = 400

    scene = new Scene {
      content = new BorderPane {
        center = createGrid(rootScene.width * 0.8)

        right = new Rectangle {
          fill = Color.Orange
          width <== rootScene.width * 0.2
          height <== rootScene.height
        }
      }
    }
  }

  def createGrid(gridWidth: NumberBinding): GridPane = {
    val mapColumns = 15
    val mapRows    = 11

    def createTile: Region = Node()

    //FIXME: grid height must be equal grid width
    val grid = new GridPane {
      hgap = 1
      vgap = 1
      minWidth <== gridWidth

      //FIXME: weird bottom offset
      padding = Insets(2, 2, 26, 2)

      columnConstraints = (0 until mapColumns).map(_ =>
        new ColumnConstraints {
          hgrow = Priority.Always
        }
      )
      rowConstraints = (0 until mapRows).map(_ =>
        new RowConstraints {
          vgrow = Priority.Always
        }
      )

      for {
        i <- 0 until mapColumns
        j <- 0 until mapRows
      } add(createTile, i, j, 1, 1)
    }

    grid
  }
}
