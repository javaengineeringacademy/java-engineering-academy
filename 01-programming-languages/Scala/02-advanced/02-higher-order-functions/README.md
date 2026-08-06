# Higher-Order Functions in Scala

## Overview
Higher-order functions take functions as parameters or return them.

## map
Transforms each element:
```scala
val doubled = List(1, 2, 3).map(_ * 2)
```

## filter
Selects elements:
```scala
val evens = List(1, 2, 3, 4).filter(_ % 2 == 0)
```

## flatMap
Maps then flattens:
```scala
val words = List("hello world", "foo bar").flatMap(_.split(" "))
```

## reduce
Combines elements:
```scala
val sum = List(1, 2, 3, 4).reduce(_ + _)
```

## foldLeft
Fold with initial value:
```scala
val sum = List(1, 2, 3).foldLeft(0)(_ + _)
```

## Resources
- [Scala Tour - Higher-Order Functions](https://docs.scala-lang.org/tour/higher-order-functions.html)
