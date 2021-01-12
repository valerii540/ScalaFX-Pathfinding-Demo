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

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
        new Button       {
          text = "▶️"
          tooltip = new Tooltip("Execute pathfinding")
          style = "-fx-text-fill: green"
          onMouseClicked = _ => {
            if (Graph.startNode.isDefined && Graph.targetNode.isDefined)
              Future {
                gridProp.value.clearResult()

                val algo  = choiceBox.selectionModel().getSelectedItem
                val graph = Graph(gridProp.value.matrix)
                val start = Graph.startNode.get
                val end   = Graph.targetNode.get

                val path = algo.findPath(start, end, graph.paths)
                path.filter(_.state != NodeStates.Target).foreach(_.changeStateTo(NodeStates.Path))
              }
            else executionAlert()
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
