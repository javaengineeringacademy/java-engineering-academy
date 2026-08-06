// Scala Functions

object Functions {
  def main(args: Array[String]): Unit = {
    // Basic method
    def add(a: Int, b: Int): Int = a + b
    println(s"add(2, 3) = ${add(2, 3)}")

    // Lambda
    val double = (x: Int) => x * 2
    println(s"double(5) = ${double(5)}")

    // Currying
    def multiply(a: Int)(b: Int): Int = a * b
    val triple = multiply(3) _
    println(s"triple(4) = ${triple(4)}")

    // Higher-order function
    val numbers = List(1, 2, 3, 4, 5)
    val doubled = numbers.map(double)
    println(s"doubled: $doubled")

    // Function composition
    val addOne = (x: Int) => x + 1
    val timesTwo = (x: Int) => x * 2
    val composed = addOne andThen timesTwo
    println(s"composed(3) = ${composed(3)}")

    // Partial application
    def greet(greeting: String)(name: String): String = s"$greeting, $name!"
    val hello = greet("Hello") _
    println(hello("Alice"))
    println(hello("Bob"))
  }
}
