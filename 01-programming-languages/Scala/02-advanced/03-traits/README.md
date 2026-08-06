# Traits in Scala

## Overview
Traits are reusable components that can be mixed into classes.

## Basic Trait
```scala
trait Drawable {
  def draw(): Unit
}
```

## Implementing Traits
```scala
class Circle extends Drawable {
  def draw(): Unit = println("Drawing circle")
}
```

## Mixins
```scala
class MyClass with TraitA with TraitB
```

## Abstract Classes vs Traits
- Abstract classes can have constructors
- Traits cannot have constructors (Scala 2)
- Classes can extend only one class, but multiple traits

## Resources
- [Scala Tour - Traits](https://docs.scala-lang.org/tour/traits.html)
