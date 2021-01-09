package views

import graph.NodeStates._
import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.StringProperty
import scalafx.geometry.Insets
import scalafx.scene.control.{Label, TextField, ToggleButton, ToggleGroup}
import scalafx.scene.layout.{HBox, Pane, Region, VBox}

object SettingsView {
  val toolsToggleGroup = new ToggleGroup

  def createSettingsView(widthProp: NumberBinding, rows: StringProperty, columns: StringProperty): Pane =
    new VBox {
      minWidth <== widthProp
      maxWidth <== minWidth

      style = "-fx-background-color: rgb(224,224,224)"
      padding = Insets(2, 4, 2, 2)

      children = Seq(
        new HBox { hBox =>
          maxWidth <== widthProp
          children = Seq(
            new Label("rows:") { minWidth = Region.USE_PREF_SIZE },
            new TextField      {
              text <==> rows
            }
          )
        },
        new HBox {
          children = Seq(
            new Label("columns:") { minWidth = Region.USE_PREF_SIZE },
            new TextField         { text <==> columns               }
          )
        },
        new VBox {
          children = Seq(
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
            }
          )
        }
      )
    }
}
