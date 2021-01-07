import scalafx.application.JFXApp
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout._
import views.{Grid, GridView, SettingsView}

object Main extends JFXApp {
  private val gridView: ObjectProperty[Grid] = ObjectProperty(new Grid(11, 15))
  private val rowsView: StringProperty       = StringProperty(gridView.value.mapRows.toString)
  private val columnsView: StringProperty    = StringProperty(gridView.value.mapColumns.toString)

  rowsView.onChange((_, _, newRows) => gridView.value = new Grid(newRows.toInt, gridView.value.mapColumns))
  columnsView.onChange((_, _, newColumns) => gridView.value = new Grid(gridView.value.mapRows, newColumns.toInt))

  stage = new JFXApp.PrimaryStage {
    title = "ScalaFX Pathfinding Demo"
    width = 600
    height = 400

    scene = new Scene { mainScene =>
      content = new BorderPane {
        minHeight <== mainScene.height

        padding = Insets(2, 2, 2, 2)

        gridView.onChange((_, _, newGrid) => center = GridView.createGridPane(mainScene.width * 0.8, newGrid))

        center = GridView.createGridPane(mainScene.width * 0.8, gridView.value)

        right = SettingsView.createSettingsView(mainScene.width * 0.2, rowsView, columnsView)
      }
    }
  }
}
