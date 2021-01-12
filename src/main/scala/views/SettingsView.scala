package views

import graph.{Graph, NodeStates}
import graph.NodeStates._
import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, Pane, Region, VBox}

object SettingsView {
  val toolsToggleGroup = new ToggleGroup

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
          text = "Run"
          onMouseClicked = _ => {
            gridProp.value.clearResult()
            val graph = Graph(gridProp.value.matrix)
            val start = gridProp.value.matrix.flatten.find(_.state == NodeStates.Start).get
            val end = gridProp.value.matrix.flatten.find(_.state == NodeStates.Target).get
            val path = graph.bfs(start, end)
            path.keys.filter(_.state != NodeStates.Target).foreach(_.changeStateTo(NodeStates.Visited))
            graph.shortestPathNodes(path, start, end).filter(_.state != NodeStates.Target).foreach(_.changeStateTo(NodeStates.Path))
          }
        },
        new Separator,
        new Button {
          text = "Clear results"
          onMouseClicked = _ => {
            gridProp.value.clearResult()
          }
        },
        new Button {
          text = "Clear all"
          onMouseClicked = _ => {
            gridProp.value.clear()
          }
        }
      )
    }
}
