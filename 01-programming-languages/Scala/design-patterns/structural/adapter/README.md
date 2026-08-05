# Adapter Pattern in Scala

The Adapter pattern converts the interface of a class into another interface clients expect. In Scala, this is implemented using trait implementations and implicit classes.

## When to Use

- Integrating third-party libraries
- Making existing types work with new code
- Unifying multiple interfaces
- Legacy system integration
- Type conversion

## Implementation

### Trait-Based Adapter

```scala
trait MediaPlayer {
  def play(file: String): Unit
}

class VlcPlayer {
  def playVlc(path: String): Unit = println(s"Playing VLC: $path")
}

class VlcAdapter(player: VlcPlayer) extends MediaPlayer {
  def play(file: String): Unit = player.playVlc(file)
}
```

### Implicit Class Adapter

```scala
class OldFormatter {
  def formatOld(data: String): String = s"OLD:$data"
}

trait Formatter {
  def format(data: String): String
}

implicit class OldFormatterAdapter(formatter: OldFormatter) extends Formatter {
  def format(data: String): String = formatter.formatOld(data)
}

// Usage
val old = new OldFormatter()
val formatted: String = old.format("hello") // Uses implicit conversion
```

### Multiple Adapter Implementations

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

class NetworkLogger(url: String) extends Logger {
  def log(message: String): Unit = println(s"[NETWORK:$url] $message")
}
```

### Generic Adapter

```scala
trait Target[T] {
  def process(value: T): String
}

class LegacyService {
  def legacyProcess(input: Int): String = s"Legacy: $input"
}

class Adapter[T](service: LegacyService, converter: T => Int) extends Target[T] {
  def process(value: T): String = service.legacyProcess(converter(value))
}

// Usage
val adapter = new Adapter[String](new LegacyService(), _.length)
println(adapter.process("hello"))
```

## Best Practices

- Use implicit classes for transparent adapter conversions
- Keep adapters lightweight; prefer composition
- Document the mapping between old and new interfaces
- Use type parameters for generic adapters
- Test adapters with both source and target contracts

## Interview Questions

1. How do implicit classes simplify the adapter pattern in Scala?
2. What is the difference between an adapter and a facade?
3. When would you use a generic adapter vs a specific one?
4. How do you handle adapters that need to maintain state?
5. Can you combine the adapter pattern with the decorator pattern?

## References

- [Implicit Classes](https://docs.scala-lang.org/overviews/implicit-classes.html)
- [Scala Design Patterns](https://www.scala-lang.org/)
- [Effective Scala](https://twitter.github.io/effectivescala/)
