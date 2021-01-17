package views.enums

import enumeratum._

import scala.concurrent.duration.{DurationInt, FiniteDuration}

sealed abstract class Tick(duration: FiniteDuration) extends EnumEntry {
  def sleep(): Unit = Thread.sleep(duration.toMillis)
}

object Ticks extends Enum[Tick] {
  case object Instantly extends Tick(0.millis) {
    override def sleep(): Unit = ()
  }

  case object `1/5 second` extends Tick(200.millis)
  case object `1/2 second` extends Tick(500.millis)

  val values: IndexedSeq[Tick] = findValues
}
