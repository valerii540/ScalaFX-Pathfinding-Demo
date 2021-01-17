package views

import algorithms.{AStar, BFS, Pathfinder}
import graph.NodeStates.{Obstacle, Start, Target}
import graph.{Graph, NodeState, NodeStates}
import javafx.scene.control.{ToggleButton => JToggleButton}
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import utils.PathfindingExecutor

object ToolBarView {
  private[this] def executionAlert() =
    new Alert(AlertType.Information) {
      title = "Execution alert"
      headerText = "Unspecified start and target nodes!"
      contentText = "Please, specify start and target nodes with appropriate buttons"
    }.showAndWait()

  def createToolBar(gridProp: ObjectProperty[Grid], toolProp: ObjectProperty[NodeState]): ToolBar = {
    val toolsToggleGroup = new ToggleGroup

    toolsToggleGroup.selectedToggle.onChange { (_, _, newToggle) =>
      if (newToggle != null)
        toolProp.value = NodeStates.withName(newToggle.asInstanceOf[JToggleButton].text.value)
    }

    val choiceBox =
      new ChoiceBox[Pathfinder] {
        items = ObservableBuffer(BFS, AStar)
        selectionModel().selectFirst()
      }

    new ToolBar {
      content = Seq(
        new ToggleButton {
          text = Obstacle.entryName
          toggleGroup = toolsToggleGroup
          selected = true
        },
        new ToggleButton {
          text = Start.entryName
          toggleGroup = toolsToggleGroup
        },
        new ToggleButton {
          text = Target.entryName
          toggleGroup = toolsToggleGroup
        },
        new Separator,
        choiceBox,
        new Button       { bttn =>
          text = "▶"
          tooltip = new Tooltip("Execute/Stop pathfinding")
          style = "-fx-text-fill: green"
          onMouseClicked = _ => {
            if (bttn.getText == "▶" && Graph.startNode.isDefined && Graph.targetNode.isDefined) {
              val algorithm = choiceBox.selectionModel().getSelectedItem

              PathfindingExecutor.execute(gridProp, algorithm)

              bttn.text = "■"
            } else if (bttn.getText == "■") {
              PathfindingExecutor.cancelRunning()
              bttn.text = "▶"
            } else
              executionAlert()
          }
        },
        new Separator,
        new Button       {
          text = "⌫"
          tooltip = new Tooltip("Clear only pathfinding results")
          onMouseClicked = _ => {
            gridProp.value.clearResult()
          }
        },
        new Button       {
          text = "⌫"
          style = "-fx-text-fill: red"
          tooltip = new Tooltip("Clear scene completely")
          onMouseClicked = _ => {
            gridProp.value.clear()
          }
        }
      )
    }
  }
}
