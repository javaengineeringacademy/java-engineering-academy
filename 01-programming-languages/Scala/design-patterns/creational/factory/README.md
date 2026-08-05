# Factory Pattern in Scala

The Factory pattern creates objects without exposing instantiation logic. In Scala, companion object `apply` methods serve as factories, with pattern matching for type selection.

## When to Use

- Creating objects based on runtime parameters
- Encapsulating complex construction logic
- Supporting multiple concrete types
- Configuration-driven object creation
- Type-safe object creation

## Implementation

### Companion Object Apply

```scala
sealed trait Animal
case class Dog(name: String) extends Animal
case class Cat(name: String) extends Animal

object Animal {
  def apply(kind: String, name: String): Animal = kind match {
    case "dog" => Dog(name)
    case "cat" => Cat(name)
    case _ => throw new IllegalArgumentException(s"Unknown animal: $kind")
  }
}

// Usage
val dog = Animal("dog", "Buddy")
val cat = Animal("cat", "Whiskers")
```

### Parameterized Factory

```scala
sealed trait Shape {
  def area: Double
}

case class Circle(radius: Double) extends Shape {
  def area: Double = math.Pi * radius * radius
}

case class Rectangle(width: Double, height: Double) extends Shape {
  def area: Double = width * height
}

object Shape {
  def apply(kind: String, dims: Double*): Shape = kind match {
    case "circle" if dims.length == 1 => Circle(dims.head)
    case "rectangle" if dims.length == 2 => Rectangle(dims(0), dims(1))
    case _ => throw new IllegalArgumentException(s"Invalid shape: $kind")
  }
}
```

### Factory Method

```scala
trait Logger {
  def log(message: String): Unit
}

class ConsoleLogger extends Logger {
  def log(message: String): Unit = println(s"[CONSOLE] $message")
}

class FileLogger(path: String) extends Logger {
  def log(message: String): Unit = println(s"[FILE:$path] $message")
}

object Logger {
  def apply(kind: String, args: String*): Logger = kind match {
    case "console" => new ConsoleLogger
    case "file" => new FileLogger(args.headOption.getOrElse("default.log"))
    case _ => throw new IllegalArgumentException(s"Unknown logger: $kind")
  }
}
```

### Generic Factory

```scala
trait Creator[T] {
  def create(): T
}

class DogFactory extends Creator[Dog] {
  def create(): Dog = Dog("FactoryDog")
}

class CatFactory extends Creator[Cat] {
  def create(): Cat = Cat("FactoryCat")
}

object Creator {
  def apply[T](creator: Creator[T]): T = creator.create()
}
```

## Best Practices

- Use `apply` methods as primary factory constructors
- Use pattern matching for type selection in factories
- Return sealed trait types for compile-time exhaustiveness checking
- Document parameter requirements for each factory variant
- Consider using `Either` or `Try` for error-prone factory methods

## Interview Questions

1. How does Scala's `apply` method implement the factory pattern?
2. What is the difference between a factory and a companion object?
3. How do you handle factory errors in Scala?
4. When should you use a factory vs a builder pattern?
5. How do you extend a factory without modifying existing code?

## References

- [Scala Companion Objects](https://docs.scala-lang.org/tour/companion-objects.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
- [Design Patterns in Scala](https://www.scala-lang.org/)
