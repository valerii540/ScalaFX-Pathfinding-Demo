import graph.{NodeState, NodeStates}
import javafx.scene.control.{ToggleButton => JToggleButton}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout._
import views._

object Main extends JFXApp {
  private val toolProp: ObjectProperty[NodeState] = ObjectProperty(NodeStates.Obstacle)
  private val gridProp: ObjectProperty[Grid]      = ObjectProperty(new Grid(11, 15, toolProp))
  private val rowsProp: StringProperty            = StringProperty(gridProp.value.mapRows.toString)
  private val columnsProp: StringProperty         = StringProperty(gridProp.value.mapColumns.toString)

  rowsProp.onChange((_, _, newRows) => gridProp.value = new Grid(newRows.toInt, gridProp.value.mapColumns, toolProp))
  columnsProp.onChange((_, _, newColumns) => gridProp.value = new Grid(gridProp.value.mapRows, newColumns.toInt, toolProp))

  SettingsView.toolsToggleGroup.selectedToggle.onChange { (_, _, newToggle) =>
    toolProp.value = NodeStates.withName(newToggle.asInstanceOf[JToggleButton].text.value)
  }

  stage = new JFXApp.PrimaryStage {
    title = "ScalaFX Pathfinding Demo"
    width = 600
    height = 400

    scene = new Scene { mainScene =>
      content = new BorderPane {
        minHeight <== mainScene.height

        padding = Insets(2, 2, 2, 2)

        gridProp.onChange((_, _, newGrid) => center = GridView.createGridPane(mainScene.width * 0.8, newGrid))

        right = SettingsView.createSettingsView(mainScene.width * 0.2, rowsProp, columnsProp)

        center = GridView.createGridPane(mainScene.width * 0.8, gridProp.value)
      }
    }
  }
}
