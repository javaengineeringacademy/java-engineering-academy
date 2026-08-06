# Immutability in Scala

## Overview
Scala encourages immutable data structures for thread safety and predictability.

## Immutable Collections
```scala
val list = List(1, 2, 3)
val newList = list :+ 4
```

## Mutable vs Immutable
```scala
import scala.collection.mutable
val mutableList = mutable.ListBuffer(1, 2, 3)
mutableList += 4
```

## copy Method (Case Classes)
```scala
case class Point(x: Int, y: Int)
val p1 = Point(1, 2)
val p2 = p1.copy(x = 3)
```

## Resources
- [Scala Tour - Collections](https://docs.scala-lang.org/tour/collections.html)
