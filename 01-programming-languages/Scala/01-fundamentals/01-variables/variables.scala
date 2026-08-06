// Scala Variables

object Variables {
  def main(args: Array[String]): Unit = {
    // Immutable variables (val)
    val x = 42
    val name = "Scala"
    println(s"x = $x, name = $name")

    // Mutable variables (var)
    var count = 0
    count += 1
    println(s"count = $count")

    // Type inference
    val a = 42
    val b = 3.14
    val c = "hello"
    val d = true
    println(s"a: ${a.getClass}, b: ${b.getClass}, c: ${c.getClass}, d: ${d.getClass}")

    // Explicit types
    val explicitInt: Int = 100
    val explicitDouble: Double = 2.718
    val explicitString: String = "Scala"
    println(s"explicitInt = $explicitInt, explicitDouble = $explicitDouble")

    // Lazy values
    lazy val expensive = {
      println("Computing...")
      42
    }
    println("Before accessing lazy val")
    println(s"expensive = $expensive")

    // Multiple assignment
    val (a1, b1) = (1, 2)
    println(s"a1 = $a1, b1 = $b1")

    // Tuple
    val tuple = (1, "hello", 3.14)
    println(s"tuple = $tuple")

    // Type aliases
    type Meter = Double
    type Kilometer = Double
    val distance: Meter = 100.0
    println(s"distance = $distance meters")
  }
}
