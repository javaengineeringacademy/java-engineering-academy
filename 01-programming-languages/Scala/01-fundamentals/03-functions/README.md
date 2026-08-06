# Scala Functions

## Overview
Scala supports methods, functions, higher-order functions, and currying.

## Defining Methods
```scala
def add(a: Int, b: Int): Int = a + b
```

## Anonymous Functions (Lambdas)
```scala
val double = (x: Int) => x * 2
```

## Currying
```scala
def add(a: Int)(b: Int): Int = a + b
val add5 = add(5) _
```

## Higher-Order Functions
```scala
def applyToAll(list: List[Int], f: Int => Int): List[Int] = list.map(f)
```

## Multiple Parameter Lists
```scala
def foldLeft(list: List[Int])(zero: Int)(f: (Int, Int) => Int): Int = list.foldLeft(zero)(f)
```

## Resources
- [Scala Tour - Functions](https://docs.scala-lang.org/tour/basics.html)
