package views

import algorithms.{AStar, BFS, Pathfinder}
import graph.NodeStates.{Obstacle, Start, Target}
import graph.{Graph, NodeState, NodeStates}
import javafx.scene.control.{ToggleButton => JToggleButton}
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import utils.PathfindingExecutor

object ToolBarView {
  private[this] val startSign = "▶"
  private[this] val stopSign  = "■"

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

    val algoChoiceBox =
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
        algoChoiceBox,
        new Button       { bttn =>
          text = startSign
          tooltip = new Tooltip("Execute/Stop pathfinding")
          style = "-fx-text-fill: green"
          onMouseClicked = _ => {
            if (bttn.getText == startSign && Graph.startNode.isDefined && Graph.targetNode.isDefined) {
              val algorithm = algoChoiceBox.selectionModel().getSelectedItem
              val tick      = SettingsView.getTickParameter

              PathfindingExecutor.execute(gridProp, algorithm, tick) {
                Platform.runLater { bttn.text = startSign }
              }

              bttn.text = stopSign
            } else if (bttn.getText == stopSign) {
              PathfindingExecutor.cancelRunning()
              bttn.text = startSign
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
