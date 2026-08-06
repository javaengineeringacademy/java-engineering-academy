# Scala Classes

## Overview
Scala classes are blueprints for objects. Case classes provide immutable data structures with pattern matching.

## Basic Class
```scala
class Person(val name: String, var age: Int)
```

## Constructor
```scala
class Person(val name: String, var age: Int) {
  def greet(): String = s"Hello, $name"
}
```

## Case Classes
```scala
case class Point(x: Int, y: Int)
val p = Point(1, 2)
val p2 = p.copy(x = 3)
```

## Companion Objects
```scala
class Circle(val radius: Double)
object Circle {
  def apply(radius: Double): Circle = new Circle(radius)
}
```

## Resources
- [Scala Tour - Classes](https://docs.scala-lang.org/tour/classes.html)
