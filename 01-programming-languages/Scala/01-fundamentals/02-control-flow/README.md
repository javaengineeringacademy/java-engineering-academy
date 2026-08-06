# Scala Control Flow

## Overview
Scala has familiar control structures with functional twists like `for` comprehensions.

## if/else
```scala
val x = 10
val result = if (x > 5) "big" else "small"
```

## for Loop
```scala
for (i <- 1 to 10) {
  println(i)
}
```

## for Comprehension with yield
```scala
val squares = for (i <- 1 to 10) yield i * i
```

## while Loop
```scala
var i = 0
while (i < 10) {
  println(i)
  i += 1
}
```

## Pattern Matching in for
```scala
val pairs = List((1, "a"), (2, "b"))
for ((num, letter) <- pairs) {
  println(s"$num: $letter")
}
```

## Guards
```scala
for (i <- 1 to 10 if i % 2 == 0) {
  println(i)
}
```

## Resources
- [Scala Tour - Control Structure](https://docs.scala-lang.org/tour/control-structure-syntax.html)
