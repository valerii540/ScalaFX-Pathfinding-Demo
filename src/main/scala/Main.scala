import graph.{NodeState, NodeStates}
import scalafx.application.JFXApp
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout._
import views._

object Main extends JFXApp {
  private val toolProp: ObjectProperty[NodeState] = ObjectProperty(NodeStates.Obstacle)
  private val levelProp: StringProperty           = StringProperty("0")
  private val gridProp: ObjectProperty[Grid]      = ObjectProperty(new Grid(11, 15, toolProp, levelProp))
  private val rowsProp: StringProperty            = StringProperty(gridProp.value.mapRows.toString)
  private val columnsProp: StringProperty         = StringProperty(gridProp.value.mapColumns.toString)

  rowsProp.onChange((_, _, newRows) =>
    gridProp.value = new Grid(newRows.toInt, gridProp.value.mapColumns, toolProp, levelProp)
  )
  columnsProp.onChange((_, _, newColumns) =>
    gridProp.value = new Grid(gridProp.value.mapRows, newColumns.toInt, toolProp, levelProp)
  )

  stage = new JFXApp.PrimaryStage { s =>
    title = "ScalaFX Pathfinding Demo"
    width = 800
    height = 500

    scene = new Scene { mainScene =>
      content = new BorderPane {
        minHeight <== mainScene.height

        padding = Insets(2, 2, 2, 2)

        gridProp.onChange((_, _, newGrid) => center = GridView.createGridPane(mainScene.width * 0.8, newGrid))

        top = ToolBarView.createToolBar(gridProp, toolProp, levelProp)

        right = SettingsView.createSettingsView(mainScene.width * 0.2, rowsProp, columnsProp, gridProp)(s)

        center = GridView.createGridPane(mainScene.width * 0.8, gridProp.value)
      }
    }
  }
}
