# For Comprehensions in Scala

## Overview
For comprehensions provide a concise syntax for working with monads.

## Basic Syntax
```scala
for (i <- 1 to 10) yield i * 2
```

## With Guards
```scala
for (i <- 1 to 10 if i % 2 == 0) yield i
```

## Multiple Generators
```scala
for {
  x <- List(1, 2)
  y <- List("a", "b")
} yield (x, y)
```

## Resources
- [Scala Tour - For Comprehension](https://docs.scala-lang.org/tour/for-comprehensions.html)
