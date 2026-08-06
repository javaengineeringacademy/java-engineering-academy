// Immutability in Scala

object Immutability {
  def main(args: Array[String]): Unit = {
    // Immutable list
    val list = List(1, 2, 3)
    val newList = list :+ 4
    println(s"original: $list")
    println(s"new: $newList")

    // Mutable list
    import scala.collection.mutable.ListBuffer
    val mutableList = ListBuffer(1, 2, 3)
    mutableList += 4
    println(s"mutable: $mutableList")

    // Immutable map
    val map = Map("a" -> 1, "b" -> 2)
    val newMap = map + ("c" -> 3)
    println(s"map: $map")
    println(s"new map: $newMap")

    // Immutable set
    val set = Set(1, 2, 3)
    val newSet = set + 4
    println(s"set: $set")
    println(s"new set: $newSet")

    // Case class copy
    case class Person(name: String, age: Int)
    val p1 = Person("Alice", 30)
    val p2 = p1.copy(age = 31)
    println(s"p1: $p1")
    println(s"p2: $p2")
  }
}
