// Traits in Scala

trait Drawable {
  def draw(): Unit
}

trait Resizable {
  def resize(factor: Double): Unit
}

class Circle(val radius: Double) extends Drawable with Resizable {
  def draw(): Unit = println(s"Drawing circle with radius $radius")
  def resize(factor: Double): Unit = println(s"Resizing circle by $factor")
}

class Square(val side: Double) extends Drawable {
  def draw(): Unit = println(s"Drawing square with side $side")
}

trait Logger {
  def log(msg: String): Unit = println(s"LOG: $msg")
}

class Service extends Logger {
  def doWork(): Unit = {
    log("starting work")
    println("doing work")
    log("work done")
  }
}

object Traits {
  def main(args: Array[String]): Unit = {
    val circle = Circle(5.0)
    circle.draw()
    circle.resize(2.0)

    val square = Square(3.0)
    square.draw()

    val service = new Service
    service.doWork()
  }
}
