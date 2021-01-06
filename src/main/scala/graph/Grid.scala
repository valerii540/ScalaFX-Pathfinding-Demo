package graph

class Grid(val mapRows: Int, val mapColumns: Int) {
  private val matrix: Array[Array[Node]] = Array.fill(mapRows)(new Array(mapColumns))

  def apply(col: Int): Array[Node] = matrix(col)
}
