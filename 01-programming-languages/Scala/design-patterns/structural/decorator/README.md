# Decorator Pattern in Scala

The Decorator pattern adds responsibilities to objects dynamically. In Scala, this is implemented using stackable traits and mixins for composable behavior.

## When to Use

- Adding behavior without modifying original code
- Layering cross-cutting concerns
- Composition over inheritance
- Runtime behavior modification
- Middleware patterns

## Implementation

### Stackable Traits

```scala
trait DataSource {
  def write(data: String): Unit
  def read(): String
}

class FileDataSource(filename: String) extends DataSource {
  def write(data: String): Unit = println(s"Writing to $filename: $data")
  def read(): String = s"Data from $filename"
}

trait EncryptionDecorator extends DataSource {
  abstract override def write(data: String): Unit = {
    super.write(s"ENCRYPTED($data)")
  }
  abstract override def read(): String = {
    super.read().stripPrefix("ENCRYPTED(").stripSuffix(")")
  }
}

trait CompressionDecorator extends DataSource {
  abstract override def write(data: String): Unit = {
    super.write(s"COMPRESSED[$data]")
  }
  abstract override def read(): String = {
    super.read().stripPrefix("COMPRESSED[").stripSuffix("]")
  }
}

// Usage
val source = new FileDataSource("data.txt") with EncryptionDecorator with CompressionDecorator
source.write("Hello, World!")
println(source.read())
```

### Mixin Decorator

```scala
trait Greeting {
  def greet(): String = "Hello"
}

trait FormalGreeting extends Greeting {
  abstract override def greet(): String = s"Dear Guest, ${super.greet()}"
}

trait ExclamationGreeting extends Greeting {
  abstract override def greet(): String = s"${super.greet()}!"
}

class FormalExclamation extends Greeting with FormalGreeting with ExclamationGreeting

// Usage
val greeting = new FormalExclamation
println(greeting.greet()) // "Dear Guest, Hello!"
```

### Transparent Decorator

```scala
trait Component {
  def operation(): String
}

class BaseComponent extends Component {
  def operation(): String = "Base"
}

trait LoggingDecorator(component: Component) extends Component {
  abstract override def operation(): String = {
    println("Before operation")
    val result = super.operation()
    println("After operation")
    result
  }
}

trait TimingDecorator(component: Component) extends Component {
  abstract override def operation(): String = {
    val start = System.currentTimeMillis()
    val result = super.operation()
    val elapsed = System.currentTimeMillis() - start
    println(s"Operation took ${elapsed}ms")
    result
  }
}

// Usage
val component = new BaseComponent with LoggingDecorator with TimingDecorator
component.operation()
```

## Best Practices

- Use `abstract override` for stackable trait decorators
- Keep decorators single-responsibility
- Document the order of trait mixing
- Use constructor parameters for decorator configuration
- Prefer traits over classes for flexible composition

## Interview Questions

1. What is the difference between a decorator and a proxy?
2. How does `abstract override` work in Scala?
3. When should you use stackable traits vs inheritance?
4. How do you handle decorator ordering?
5. Can decorators be applied dynamically at runtime?

## References

- [Stackable Traits](https://docs.scala-lang.org/tour/traits.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
- [Scala Design Patterns](https://www.scala-lang.org/)
