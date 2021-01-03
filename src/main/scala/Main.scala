import javafx.scene.layout.{Region => jfxRegion}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.binding.NumberBinding
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

object Main extends JFXApp {

  stage = new JFXApp.PrimaryStage { rootScene =>
    title = ""
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

    val paintRed = "-fx-background-color: rgb(178,34,34)"

    def createTile: Region =
      new Region {
        style = "-fx-background-color: rgb(144,238,144)"
        onDragDetected = (e: MouseEvent) => e.getSource.asInstanceOf[jfxRegion].startFullDrag()

        onMouseDragEntered = _ => style = paintRed
        onMouseClicked = _ => style = paintRed
      }

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
