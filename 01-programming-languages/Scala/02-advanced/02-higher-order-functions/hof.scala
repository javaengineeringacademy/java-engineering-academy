// Higher-Order Functions in Scala

object HigherOrderFunctions {
  def main(args: Array[String]): Unit = {
    val numbers = List(1, 2, 3, 4, 5)

    // map
    val doubled = numbers.map(_ * 2)
    println(s"doubled: $doubled")

    // filter
    val evens = numbers.filter(_ % 2 == 0)
    println(s"evens: $evens")

    // flatMap
    val nested = List(List(1, 2), List(3, 4))
    val flat = nested.flatMap(identity)
    println(s"flat: $flat")

    // reduce
    val sum = numbers.reduce(_ + _)
    println(s"sum: $sum")

    // foldLeft
    val product = numbers.foldLeft(1)(_ * _)
    println(s"product: $product")

    // compose
    val addOne = (x: Int) => x + 1
    val timesTwo = (x: Int) => x * 2
    val composed = addOne andThen timesTwo
    println(s"composed(3): ${composed(3)}")
  }
}
