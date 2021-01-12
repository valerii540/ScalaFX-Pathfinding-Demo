package views

import scalafx.beans.binding.NumberBinding
import scalafx.beans.property.StringProperty
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, Pane, Region, VBox}

object SettingsView {

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
        }
      )
    }
}
