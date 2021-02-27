package views

import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, Pane, Region, VBox}
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
      rows: StringProperty,
      columns: StringProperty
  ): Pane =
    new VBox {
      prefWidth <== widthProp

      style = "-fx-background-color: rgb(224,224,224)"
      padding = Insets(2, 4, 2, 2)
      spacing = 5

      children = Seq(
        new HBox { hBox =>
          children = Seq(
            new Label("rows:") { minWidth = Region.USE_PREF_SIZE },
            new TextField      {
              prefWidth <== widthProp
              text <==> rows
            }
          )
        },
        new HBox {
          children = Seq(
            new Label("columns:") { minWidth = Region.USE_PREF_SIZE },
            new TextField         {
              prefWidth <== widthProp
              text <==> columns
            }
          )
        },
        new Separator,
        tickChoiceBox
      )
    }
}
