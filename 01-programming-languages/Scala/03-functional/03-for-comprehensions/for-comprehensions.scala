// For Comprehensions in Scala

object ForComprehensions {
  def main(args: Array[String]): Unit = {
    // Basic for comprehension
    val squares = for (i <- 1 to 5) yield i * i
    println(s"squares: $squares")

    // With guard
    val evens = for (i <- 1 to 10 if i % 2 == 0) yield i
    println(s"evens: $evens")

    // Multiple generators
    val pairs = for {
      x <- List(1, 2, 3)
      y <- List("a", "b")
    } yield (x, y)
    println(s"pairs: $pairs")

    // Nested for
    for (i <- 1 to 3) {
      for (j <- 1 to 3) {
        print(s"($i,$j) ")
      }
    }
    println()

    // For with Option
    val result = for {
      a <- Some(1)
      b <- Some(2)
    } yield a + b
    println(s"option result: $result")

    // For with Either
    val eitherResult = for {
      a <- Right(10)
      b <- Right(20)
    } yield a + b
    println(s"either result: $eitherResult")
  }
}
