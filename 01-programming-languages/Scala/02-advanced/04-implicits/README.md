# Implicits in Scala

## Overview
Implicits provide implicit conversions and parameters. Scala 3 uses `given`/`using` instead.

## Implicit Parameters
```scala
def greet(implicit name: String): String = s"Hello, $name!"
```

## Implicit Conversions
```scala
implicit def stringToInt(s: String): Int = s.toInt
```

## Scala 3: given/using
```scala
given name: String = "Scala"
def greet(using name: String): String = s"Hello, $name!"
```

## Context Parameters
```scala
def process[T](data: T)(using ordering: Ordering[T]): T = data
```

## Resources
- [Scala 3 - Given/Using](https://docs.scala-lang.org/scala3/reference/contextual/givens.html)
