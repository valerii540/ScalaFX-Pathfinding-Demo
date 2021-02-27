package views

import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, Pane, Region, VBox}
import scalafx.stage.Stage
import utils.GraphChooser
import views.enums.{Tick, Ticks}

object SettingsView {
  private[this] val tickChoiceBox =
    new ChoiceBox[Tick] {
      items = ObservableBuffer(Ticks.values)
      selectionModel().select(Ticks.`200 ms`)
    }

  def getTickParameter: Tick = tickChoiceBox.selectionModel().getSelectedItem

  def createSettingsView(
      widthProp: NumberBinding,
      rowsProp: StringProperty,
      columnsProp: StringProperty,
      gridProp: ObjectProperty[Grid]
  )(implicit stage: Stage): Pane =
    new VBox { v =>
      prefWidth <== widthProp

      style = "-fx-background-color: rgb(224,224,224)"
      padding = Insets(2, 4, 2, 2)
      spacing = 5

      children = Seq(
        new HBox {
          children = Seq(
            new Button {
              text = "Open"
              tooltip = new Tooltip("Load field from file")
              onMouseClicked = _ => {
                val file = GraphChooser.open()
              }
            },
            new Button {
              text = "Save"
              tooltip = new Tooltip("Save current field as file")
              onMouseClicked = _ => {
                GraphChooser.save(gridProp.value)
              }
            }
          )
        },
        new HBox { hBox =>
          children = Seq(
            new Label("rows:") { minWidth = Region.USE_PREF_SIZE },
            new TextField      {
              prefWidth <== widthProp
              text <==> rowsProp
            }
          )
        },
        new HBox {
          children = Seq(
            new Label("columns:") { minWidth = Region.USE_PREF_SIZE },
            new TextField         {
              prefWidth <== widthProp
              text <==> columnsProp
            }
          )
        },
        new Separator,
        tickChoiceBox
      )
    }
}
