# Scala Variables

## Overview
Scala is strongly typed with type inference. Use `val` for immutable and `var` for mutable variables.

## Immutable Variables
```scala
val x = 42
val name = "Scala"
```

## Mutable Variables
```scala
var count = 0
count += 1
```

## Type Inference
```scala
val a = 42        // Int
val b = 3.14      // Double
val c = "hello"   // String
val d = true      // Boolean
```

## Explicit Types
```scala
val a: Int = 42
val b: Double = 3.14
val c: String = "hello"
```

## Lazy Values
```scala
lazy val expensive = computeValue()
```

## Common Types
- `Int`, `Long`, `Short`, `Byte`
- `Double`, `Float`
- `String`, `Char`
- `Boolean`

## Resources
- [Scala Tour](https://docs.scala-lang.org/tour/basics.html)
