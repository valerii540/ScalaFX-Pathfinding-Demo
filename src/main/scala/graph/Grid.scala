package graph

object Grid {
  val mapColumns = 15
  val mapRows    = 11

  private val matrix: Array[Array[Node]] = Array.fill(mapRows)(new Array(mapColumns))

  def apply(col: Int): Array[Node] = matrix(col)
}
