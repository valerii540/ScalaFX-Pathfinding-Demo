import sbt._

object Dependencies {
  private lazy val scalaFX = "org.scalafx" %% "scalafx" % "15.0.1-R20"

  private lazy val javaFXModules = {
    val osName = System.getProperty("os.name") match {
      case n if n.startsWith("Linux")   => "linux"
      case n if n.startsWith("Mac")     => "mac"
      case n if n.startsWith("Windows") => "win"
      case _                            => throw new Exception("Unknown platform!")
    }

    Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
      .map(m => "org.openjfx" % s"javafx-$m" % "15.0.1" classifier osName)
  }

  lazy val allScalaFXDependencies: Seq[ModuleID] = scalaFX +: javaFXModules
}
