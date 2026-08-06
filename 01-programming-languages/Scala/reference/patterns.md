# Scala Patterns

## Builder Pattern
```scala
class QueryBuilder {
  private var table: String = ""
  private var conditions: List[String] = Nil
  
  def from(t: String): QueryBuilder = { table = t; this }
  def where(c: String): QueryBuilder = { conditions = c :: conditions; this }
  def build: String = s"SELECT * FROM $table WHERE ${conditions.mkString(" AND ")}"
}
```

## Typeclass Pattern
```scala
trait JsonWriter[T] {
  def write(value: T): String
}
```

## Abstract Factory
```scala
trait DatabaseFactory {
  def createConnection(): Connection
}
```

## Observer Pattern (via Observable)
```scala
import scala.collection.mutable
class Observable {
  private val observers = mutable.ListBuffer[Observer]()
  def subscribe(o: Observer): Unit = observers += o
  def notify(data: Any): Unit = observers.foreach(_.update(data))
}
```

## Decorator Pattern (via Traits)
```scala
trait Logger {
  def log(msg: String): Unit
}
class ConsoleLogger extends Logger {
  def log(msg: String): Unit = println(msg)
}
class TimestampLogger(inner: Logger) extends Logger {
  def log(msg: String): Unit = inner.log(s"${System.currentTimeMillis()}: $msg")
}
```
