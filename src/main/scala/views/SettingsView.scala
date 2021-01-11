package views

import graph.Graph
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
            val graph = Graph(gridProp.value.matrix)
          }
        }
      )
    }
}
