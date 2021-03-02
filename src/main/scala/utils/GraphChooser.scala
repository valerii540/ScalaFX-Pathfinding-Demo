package utils

import cats.effect.IO
import graph.NodeStates.{Start, Target}
import graph.{Graph, Node, NodeState}
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.stage.{FileChooser, Stage}
import views.Grid
import views.Grid.Matrix

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

object GraphChooser {
  private[this] val fileChooser = new FileChooser

  def save(grid: Grid)(window: Stage): Unit = {
    fileChooser.title = "Save"
    val file = fileChooser.showSaveDialog(window)

    IO(new ObjectOutputStream(new FileOutputStream(file)))
      .bracket(oos => IO(oos.writeObject(grid.matrix)))(oos => IO(oos.close()))
      .unsafeRunSync()
  }

  def open(gridProp: ObjectProperty[Grid], toolProp: ObjectProperty[NodeState], levelProp: StringProperty)(
      window: Stage
  ): Unit = {
    fileChooser.title = "Open"
    val file = fileChooser.showOpenDialog(window)

    val matrix =
      IO(new ObjectInputStream(new FileInputStream(file)))
        .bracket(ois => IO(ois.readObject().asInstanceOf[Matrix]))(ois => IO(ois.close()))
        .unsafeRunSync()

    val updatedMatrix = matrix.map(row => row.map(Node.enrichNode(_, toolProp, levelProp)))

    Graph.startNode = updatedMatrix.flatten.find(_.getState == Start)
    Graph.targetNode = updatedMatrix.flatten.find(_.getState == Target)

    gridProp.value = gridProp.value.copy(matrix = updatedMatrix)
  }
}
