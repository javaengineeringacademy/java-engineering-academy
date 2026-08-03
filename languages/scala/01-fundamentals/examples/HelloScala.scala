object HelloScala {
  def main(args: Array[String]): Unit = {
    // Variables
    val name: String = "Scala"  // immutable
    var version = 3.3          // mutable, type inferred
    println(s"Language: $name, Version: $version")

    // Pattern matching
    val language = "Scala"
    language match {
      case "Scala" => println("JVM Language")
      case "Java"  => println("JVM Language")
      case _       => println("Other")
    }

    // Case classes
    case class Person(name: String, age: Int)
    val p = Person("Alice", 30)
    println(s"Person: ${p.name}, Age: ${p.age}")

    // Higher-order functions
    val numbers = List(1, 2, 3, 4, 5)
    val doubled = numbers.map(_ * 2)
    val sum = numbers.reduce(_ + _)
    println(s"Doubled: $doubled, Sum: $sum")

    // For comprehension
    val result = for {
      x <- 1 to 10
      if x % 2 == 0
    } yield x * x
    println(s"Even squares: $result")
  }
}
