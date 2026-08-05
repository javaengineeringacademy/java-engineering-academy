# Singleton Pattern in Scala

The Singleton pattern ensures a class has only one instance. Scala provides native support through the `object` keyword, which creates a singleton instance automatically.

## When to Use

- Global configuration or settings
- Database connection pools
- Logging instances
- Service registries
- Thread-safe shared state

## Implementation

### Object Keyword

```scala
object AppConfig {
  private var _databaseUrl: String = ""
  private var _maxConnections: Int = 10

  def databaseUrl: String = _databaseUrl
  def databaseUrl_=(url: String): Unit = _databaseUrl = url

  def maxConnections: Int = _maxConnections
  def maxConnections_=(max: Int): Unit = _maxConnections = max
}

// Usage
AppConfig.databaseUrl = "localhost:5432"
println(AppConfig.databaseUrl)
```

### Companion Object Pattern

```scala
class Database private (val url: String) {
  def query(sql: String): List[String] = List(s"Result from $url")
}

object Database {
  private var instance: Option[Database] = None

  def apply(url: String): Database = instance match {
    case Some(db) if db.url == url => db
    case _ =>
      val db = new Database(url)
      instance = Some(db)
      db
  }

  def getInstance: Option[Database] = instance
}

// Usage
val db1 = Database("localhost:5432")
val db2 = Database("localhost:5432")
println(db1 eq db2) // true
```

### Lazy Initialization

```scala
object LazySingleton {
  lazy val expensiveResource: String = {
    println("Initializing expensive resource")
    "resource_data"
  }
}

// Usage
println("Before access")
LazySingleton.expensiveResource
LazySingleton.expensiveResource // Second access is instant
```

### Thread-Safe Singleton

```scala
import java.util.concurrent.atomic.AtomicReference

object ThreadSafeSingleton {
  private val instance = new AtomicReference[Option[MyClass]](None)

  def get: MyClass = instance.get() match {
    case Some(inst) => inst
    case None =>
      val newInst = new MyClass
      if (instance.compareAndSet(None, Some(newInst))) newInst
      else instance.get().get
  }
}

class MyClass
```

## Best Practices

- Use `object` for singletons; avoid `lazy val` when possible
- Use `lazy val` for expensive one-time initialization
- Make singleton state thread-safe when accessed from multiple threads
- Prefer dependency injection over singletons for testability
- Document singleton lifecycle and thread-safety guarantees

## Interview Questions

1. What is the difference between `object` and `class` in Scala?
2. How does Scala guarantee thread safety for `object` singletons?
3. When should you use a companion object vs a standalone object?
4. How do you test code that depends on a singleton?
5. What are the drawbacks of using singletons in Scala?

## References

- [Scala Objects](https://docs.scala-lang.org/tour/singleton-objects.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
- [Scala Documentation](https://docs.scala-lang.org/)
