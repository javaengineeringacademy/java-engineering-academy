# Monads in Scala

## Overview
Monads provide a way to chain operations while handling context like failure or absence.

## Option Monad
```scala
val someValue: Option[Int] = Some(42)
val noValue: Option[Int] = None
```

## Either Monad
```scala
val right: Either[String, Int] = Right(42)
val left: Either[String, Int] = Left("error")
```

## Try Monad
```scala
import scala.util.Try
val safeParse = Try("42".toInt)
```

## Resources
- [Scala Tour - Monad](https://docs.scala-lang.org/tour/monads.html)
