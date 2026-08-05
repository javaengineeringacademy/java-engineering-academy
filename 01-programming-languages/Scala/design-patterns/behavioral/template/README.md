# Template Method Pattern in Scala

The Template Method pattern defines the skeleton of an algorithm in a base class, allowing subclasses to override specific steps. In Scala, this is implemented using abstract classes and traits.

## When to Use

- Algorithms with invariant structure but variant steps
- Framework design with customizable hooks
- Code reuse across similar operations
- Building parsers or processors
- Reducing code duplication

## Implementation

### Abstract Class Template

```scala
abstract class DataProcessor {
  def readData(): List[String]
  def processItem(item: String): String
  def writeData(data: List[String]): Unit

  def run(): Unit = {
    val raw = readData()
    val processed = raw.map(processItem)
    writeData(processed)
  }
}

class CSVProcessor extends DataProcessor {
  def readData(): List[String] = List("a,b", "c,d")
  def processItem(item: String): String = item.replace(",", " | ")
  def writeData(data: List[String]): Unit = data.foreach(d => println(s"CSV: $d"))
}

class JSONProcessor extends DataProcessor {
  def readData(): List[String] = List("{\"a\":1}", "{\"b\":2}")
  def processItem(item: String): String = item.toUpperCase
  def writeData(data: List[String]): Unit = data.foreach(d => println(s"JSON: $d"))
}
```

### Trait Template with Hooks

```scala
trait Game {
  def initialize(): Unit = println("Default initialization")
  def playTurn(): Unit
  def checkWin(): Boolean
  def end(): Unit = println("Default ending")

  final def play(): Unit = {
    initialize()
    var won = false
    while (!won) {
      playTurn()
      won = checkWin()
    }
    end()
  }
}

class Chess extends Game {
  def playTurn(): Unit = println("Chess turn")
  def checkWin(): Boolean = false
}

class TicTacToe extends Game {
  override def initialize(): Unit = println("TicTacToe initialized")
  def playTurn(): Unit = println("TicTacToe turn")
  def checkWin(): Boolean = true
}
```

### Template with Defaults

```scala
abstract class Logger {
  def format(message: String): String = s"[${java.time.Instant.now()}] $message"
  def write(formatted: String): Unit

  def log(message: String): Unit = {
    write(format(message))
  }
}

class ConsoleLogger extends Logger {
  def write(formatted: String): Unit = println(formatted)
}

class FileLogger(path: String) extends Logger {
  def write(formatted: String): Unit = println(s"Writing to $path: $formatted")
}
```

### Builder Template

```scala
abstract class Builder[T] {
  def buildStep1(): Unit
  def buildStep2(): Unit
  def getResult(): T

  final def build(): T = {
    buildStep1()
    buildStep2()
    getResult()
  }
}

class ServerBuilder extends Builder[Map[String, Any]] {
  private var config = Map[String, Any]()

  def buildStep1(): Unit = config += ("host" -> "localhost")
  def buildStep2(): Unit = config += ("port" -> 8080)
  def getResult(): Map[String, Any] = config
}
```

## Best Practices

- Use `final` on the template method to prevent overriding
- Use abstract methods for required steps
- Provide default implementations for optional hooks
- Document which methods are hooks vs required
- Consider using traits for multiple inheritance of templates

## Interview Questions

1. What is the difference between template method and strategy pattern?
2. How does Scala's trait system implement template methods?
3. When should you use abstract methods vs default implementations?
4. How do you handle template method error handling?
5. Can template methods call other template methods?

## References

- [Abstract Classes](https://docs.scala-lang.org/tour/abstract-types.html)
- [Traits](https://docs.scala-lang.org/tour/traits.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
