// Scala Control Flow

object ControlFlow {
  def main(args: Array[String]): Unit = {
    // if/else
    val x = 10
    val result = if (x > 5) "big" else "small"
    println(s"x is $result")

    // for loop
    println("for loop:")
    for (i <- 1 to 5) {
      print(s"$i ")
    }
    println()

    // for with yield
    val squares = for (i <- 1 to 5) yield i * i
    println(s"squares: $squares")

    // for with guard
    val evens = for (i <- 1 to 10 if i % 2 == 0) yield i
    println(s"evens: $evens")

    // while loop
    println("while loop:")
    var i = 0
    while (i < 5) {
      print(s"$i ")
      i += 1
    }
    println()

    // do-while
    println("do-while loop:")
    var j = 0
    do {
      print(s"$j ")
      j += 1
    } while (j < 5)
    println()

    // Pattern matching
    val day = "Monday"
    val dayType = day match {
      case "Monday" | "Tuesday" | "Wednesday" | "Thursday" | "Friday" => "Weekday"
      case "Saturday" | "Sunday" => "Weekend"
      case _ => "Unknown"
    }
    println(s"$day is a $dayType")

    // break and continue (via scala.util.control)
    import scala.util.control.Breaks._
    breakable {
      for (i <- 1 to 10) {
        if (i > 5) break
        print(s"$i ")
      }
    }
    println()
  }
}
