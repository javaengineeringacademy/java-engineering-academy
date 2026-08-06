# Scala Anti-Patterns

## Null Abuse
```scala
// Bad
val name: String = null

// Good
val name: Option[String] = None
```

## Var Overuse
```scala
// Bad
var x = 0
x += 1

// Good
val x = 1
```

## Imperative Loops
```scala
// Bad
var result = List[Int]()
for (i <- list) {
  if (i > 0) result = result :+ i
}

// Good
val result = list.filter(_ > 0)
```

## Type Erasure
```scala
// Bad
def process[T](list: List[T]) = list.head

// Good
def process[T](list: List[T])(implicit m: ClassTag[T]) = list.head
```

## Overly Complex Pattern Matching
```scala
// Bad
x match {
  case 1 => "one"
  case 2 => "two"
  case _ => "other"
}

// Good
val map = Map(1 -> "one", 2 -> "two")
map.getOrElse(x, "other")
```
