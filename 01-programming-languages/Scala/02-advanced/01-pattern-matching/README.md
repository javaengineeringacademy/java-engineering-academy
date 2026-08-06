# Pattern Matching in Scala

## Overview
Pattern matching is a powerful feature for decomposing data and control flow.

## Basic Match
```scala
val x = 42
x match {
  case 1 => "one"
  case 42 => "answer"
  case _ => "other"
}
```

## Type Matching
```scala
def describe(x: Any): String = x match {
  case i: Int => s"integer: $i"
  case s: String => s"string: $s"
  case _ => "unknown"
}
```

## Extractors
```scala
case class Person(name: String, age: Int)
val Person(n, a) = Person("Alice", 30)
```

## Guards
```scala
x match {
  case i: Int if i > 0 => "positive"
  case i: Int if i < 0 => "negative"
  case _ => "zero"
}
```

## Resources
- [Scala Tour - Pattern Matching](https://docs.scala-lang.org/tour/pattern-matching.html)
