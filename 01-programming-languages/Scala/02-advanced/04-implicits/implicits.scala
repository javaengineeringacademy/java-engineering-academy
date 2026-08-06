// Implicits in Scala

object Implicits {
  implicit val name: String = "Scala"

  def greet()(implicit name: String): String = s"Hello, $name!"

  def add(a: Int, b: Int): Int = a + b

  def main(args: Array[String]): Unit = {
    // Implicit parameter
    val greeting = greet()
    println(greeting)

    // Implicit class
    implicit class StringOps(s: String) {
      def shout: String = s.toUpperCase + "!"
    }
    println("hello".shout)

    // Multiple implicits
    implicit val num: Int = 42
    def show()(implicit n: Int, nm: String): String = s"$nm: $n"
    println(show())

    // Implicit conversion
    implicit def intToString(i: Int): String = i.toString
    val str: String = 42
    println(s"converted: $str")
  }
}
