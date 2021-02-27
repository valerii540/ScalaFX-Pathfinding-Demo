package utils

import scalafx.stage.{FileChooser, Stage}
import views.Grid

import java.io.File

object GraphChooser {
  private[this] val fileChooser = new FileChooser

  def save(grid: Grid)(implicit window: Stage): Unit = {
    fileChooser.title = "Save"
    fileChooser.showSaveDialog(window)
  }

  def open()(implicit window: Stage): File = {
    fileChooser.title = "Open"
    fileChooser.showOpenDialog(window)
  }
}
