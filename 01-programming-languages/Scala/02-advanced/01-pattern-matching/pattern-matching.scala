// Pattern Matching in Scala

object PatternMatching {
  def main(args: Array[String]): Unit = {
    // Basic match
    val x = 42
    val result = x match {
      case 1 => "one"
      case 42 => "answer"
      case _ => "other"
    }
    println(s"x is $result")

    // Type matching
    def describe(x: Any): String = x match {
      case i: Int => s"integer: $i"
      case s: String => s"string: $s"
      case b: Boolean => s"boolean: $b"
      case _ => "unknown"
    }
    println(describe(42))
    println(describe("hello"))

    // Guards
    val number = 15
    val classification = number match {
      case n if n > 0 => "positive"
      case n if n < 0 => "negative"
      case _ => "zero"
    }
    println(s"$number is $classification")

    // Tuple pattern
    val pair = (1, "hello")
    pair match {
      case (n, s) => println(s"number: $n, string: $s")
      case _ => println("other")
    }

    // Case class pattern
    case class Person(name: String, age: Int)
    val person = Person("Alice", 30)
    person match {
      case Person(name, age) if age > 25 => println(s"$name is over 25")
      case Person(name, _) => println(s"$name is young")
      case _ => println("unknown")
    }

    // List pattern
    val list = List(1, 2, 3)
    list match {
      case List(1, 2, 3) => println("exact match")
      case List(_, _, _) => println("three elements")
      case _ => println("other")
    }

    // Nested pattern
    val nested = (1, (2, 3))
    nested match {
      case (1, (a, b)) => println(s"nested: $a, $b")
      case _ => println("other")
    }
  }
}
