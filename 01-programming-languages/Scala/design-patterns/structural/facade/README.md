# Facade Pattern in Scala

The Facade pattern provides a simplified interface to a complex subsystem. In Scala, classes or objects can wrap multiple subsystems behind a clean API.

## When to Use

- Simplifying complex library APIs
- Providing unified interface to subsystems
- Reducing coupling between client code and subsystems
- Creating layer architectures

## Implementation

### Basic Facade

```scala
class CPU {
  def freeze(): Unit = println("CPU: Freezing")
  def jump(address: Int): Unit = println(s"CPU: Jumping to $address")
  def execute(): Unit = println("CPU: Executing")
}

class Memory {
  def load(address: Int, data: String): Unit =
    println(s"Memory: Loading $data at $address")
}

class HardDrive {
  def read(sector: Int, size: Int): String = {
    println(s"HardDrive: Reading $size bytes from sector $sector")
    "boot_data"
  }
}

class ComputerFacade {
  private val cpu = new CPU
  private val memory = new Memory
  private val hardDrive = new HardDrive

  def start(): Unit = {
    cpu.freeze()
    val data = hardDrive.read(0, 1024)
    memory.load(0, data)
    cpu.jump(0)
    cpu.execute()
  }
}

// Usage
val computer = new ComputerFacade
computer.start()
```

### Generic Facade

```scala
trait SubsystemA {
  def operationA(): String
}

trait SubsystemB {
  def operationB(): String
}

class Facade[A <: SubsystemA, B <: SubsystemB](a: A, b: B) {
  def simplifiedOperation(): String =
    s"${a.operationA()} + ${b.operationB()}"
}

// Usage
class ConcreteA extends SubsystemA {
  def operationA(): String = "A"
}

class ConcreteB extends SubsystemB {
  def operationB(): String = "B"
}

val facade = new Facade(new ConcreteA, new ConcreteB)
println(facade.simplifiedOperation())
```

### Facade with Configuration

```scala
class DatabaseFacade(config: Map[String, String]) {
  private val host = config.getOrElse("host", "localhost")
  private val port = config.getOrElse("port", "5432")

  def query(sql: String): List[String] = {
    println(s"Querying $host:$port: $sql")
    List("result1", "result2")
  }

  def insert(table: String, data: Map[String, Any]): Boolean = {
    println(s"Inserting into $table: $data")
    true
  }

  def close(): Unit = println("Closing connection")
}
```

## Best Practices

- Keep the facade lightweight; delegate to subsystems
- Name facade methods to reflect operations, not subsystems
- Allow direct subsystem access for advanced use cases
- Document which subsystems the facade coordinates
- Use case classes for facade configuration

## Interview Questions

1. What is the difference between a facade and an adapter?
2. When should you expose subsystem internals through the facade?
3. How do you handle facade method failures?
4. Can a facade be used as a decorator? When?
5. How do you test code that depends on a facade?

## References

- [Scala Classes](https://docs.scala-lang.org/tour/classes.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
- [Scala Design Patterns](https://www.scala-lang.org/)
