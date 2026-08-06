// Scala Classes

class Person(val name: String, var age: Int) {
  def greet(): String = s"Hello, $name"
  override def toString: String = s"Person($name, $age)"
}

case class Point(x: Int, y: Int)

class Circle(val radius: Double) {
  def area: Double = math.Pi * radius * radius
}

object Circle {
  def apply(radius: Double): Circle = new Circle(radius)
}

object Classes {
  def main(args: Array[String]): Unit = {
    val person = Person("Alice", 30)
    println(person.greet())
    person.age = 31
    println(s"age: ${person.age}")

    val p1 = Point(1, 2)
    val p2 = p1.copy(x = 3)
    println(s"p1: $p1, p2: $p2")

    val circle = Circle(5.0)
    println(s"area: ${circle.area}")

    val list = List(Point(1, 2), Point(3, 4))
    list match {
      case List(Point(x1, y1), Point(x2, y2)) =>
        println(s"two points: ($x1,$y1) and ($x2,$y2)")
      case _ => println("other")
    }
  }
}
