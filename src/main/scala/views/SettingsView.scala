package views

import algorithms.{AStar, BFS}
import graph.NodeStates._
import graph.{Graph, NodeStates}
import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.geometry.Insets
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, Pane, Region, VBox}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SettingsView {
  val toolsToggleGroup = new ToggleGroup

  private[this] def executionAlert() =
    new Alert(AlertType.Information) {
      title = "Execution alert"
      headerText = "Unspecified start and target nodes!"
      contentText = "Please, specify start and target nodes with appropriate buttons"
    }.showAndWait()

  def createSettingsView(
      widthProp: NumberBinding,
      rows: StringProperty,
      columns: StringProperty,
      gridProp: ObjectProperty[Grid]
  ): Pane =
    new VBox {
      prefWidth <== widthProp

      style = "-fx-background-color: rgb(224,224,224)"
      padding = Insets(2, 4, 2, 2)
      spacing = 5

      children = Seq(
        new HBox         { hBox =>
          children = Seq(
            new Label("rows:") { minWidth = Region.USE_PREF_SIZE },
            new TextField      {
              prefWidth <== widthProp
              text <==> rows
            }
          )
        },
        new HBox         {
          children = Seq(
            new Label("columns:") { minWidth = Region.USE_PREF_SIZE },
            new TextField         {
              prefWidth <== widthProp
              text <==> columns
            }
          )
        },
        new Separator,
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
        new Button       {
          text = "Run astar"
          onMouseClicked = _ => {
            if (Graph.startNode.isDefined && Graph.targetNode.isDefined)
              Future {
                gridProp.value.clearResult()
                val graph = Graph(gridProp.value.matrix)
                val start = Graph.startNode.get
                val end   = Graph.targetNode.get
                val path  = AStar.findPath(start, end, graph.paths)
                //            path.keys.filter(_.state != NodeStates.Target).foreach(_.changeStateTo(NodeStates.Visited))
                path.filter(_.state != NodeStates.Target).foreach(_.changeStateTo(NodeStates.Path))
              }
            else executionAlert()
          }
        },
        new Button       {
          text = "Run bsf"
          onMouseClicked = _ => {
            if (Graph.startNode.isDefined && Graph.targetNode.isDefined)
              Future {
                gridProp.value.clearResult()
                val graph = Graph(gridProp.value.matrix)
                val start = Graph.startNode.get
                val end   = Graph.targetNode.get
                val path  = BFS.findPath(start, end, graph.paths)
                path.filter(_.state != NodeStates.Target).foreach(_.changeStateTo(NodeStates.Path))
              }
            else executionAlert()
          }
        },
        new Separator,
        new Button       {
          text = "Clear results"
          onMouseClicked = _ => {
            gridProp.value.clearResult()
          }
        },
        new Button       {
          text = "Clear all"
          onMouseClicked = _ => {
            gridProp.value.clear()
          }
        }
      )
    }
}
