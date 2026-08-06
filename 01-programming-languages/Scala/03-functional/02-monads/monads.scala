// Monads in Scala

import scala.util.Try

object Monads {
  def main(args: Array[String]): Unit = {
    // Option
    val someValue: Option[Int] = Some(42)
    val noValue: Option[Int] = None
    println(someValue.map(_ * 2))
    println(noValue.map(_ * 2))

    // Either
    val right: Either[String, Int] = Right(42)
    val left: Either[String, Int] = Left("error")
    println(right.map(_ * 2))
    println(left.map(_ * 2))

    // Try
    val safeParse = Try("42".toInt)
    println(safeParse)
    println(safeParse.map(_ * 2))

    // for comprehension with Option
    val result = for {
      a <- Some(1)
      b <- Some(2)
    } yield a + b
    println(s"result: $result")

    // for comprehension with Either
    val eitherResult = for {
      a <- Right(10)
      b <- Right(20)
    } yield a + b
    println(s"either result: $eitherResult")
  }
}
