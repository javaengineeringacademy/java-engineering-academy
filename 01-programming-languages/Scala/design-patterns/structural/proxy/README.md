# Proxy Pattern in Scala

The Proxy pattern provides a surrogate for another object to control access. In Scala, this is implemented using traits, classes, or implicit conversions.

## When to Use

- Lazy initialization
- Access control and permissions
- Logging and monitoring
- Remote object access
- Caching

## Implementation

### Basic Proxy

```scala
trait Database {
  def query(sql: String): List[String]
}

class RealDatabase(connection: String) extends Database {
  def query(sql: String): List[String] = {
    println(s"Executing on $connection: $sql")
    List("result1", "result2")
  }
}

class DatabaseProxy(connection: String) extends Database {
  private lazy val realDb = {
    println(s"Connecting to $connection")
    new RealDatabase(connection)
  }

  def query(sql: String): List[String] = {
    println(s"Proxy: Forwarding query")
    realDb.query(sql)
  }
}
```

### Access Control Proxy

```scala
trait Service {
  def execute(command: String): String
}

class RealService extends Service {
  def execute(command: String): String = s"Executed: $command"
}

class AccessProxy(userRole: String) extends Service {
  private val realService = new RealService

  def execute(command: String): String = {
    if (userRole == "admin") {
      realService.execute(command)
    } else {
      "Access denied"
    }
  }
}
```

### Caching Proxy

```scala
trait DataFetcher {
  def fetch(key: String): String
}

class RealFetcher extends DataFetcher {
  def fetch(key: String): String = {
    println(s"Fetching from database: $key")
    s"data_for_$key"
  }
}

class CachingProxy extends DataFetcher {
  private val cache = scala.collection.mutable.Map[String, String]()
  private val realFetcher = new RealFetcher

  def fetch(key: String): String = {
    cache.getOrElseUpdate(key, realFetcher.fetch(key))
  }
}
```

### Virtual Proxy

```scala
trait Image {
  def display(): Unit
}

class RealImage(filename: String) extends Image {
  println(s"Loading image: $filename")
  def display(): Unit = println(s"Displaying: $filename")
}

class ImageProxy(filename: String) extends Image {
  private lazy val realImage = new RealImage(filename)
  def display(): Unit = realImage.display()
}
```

## Best Practices

- Keep the proxy interface identical to the real object
- Use `lazy val` for virtual proxy initialization
- Document proxy behavior and any additional logic
- Consider using implicit classes for transparent proxies
- Use `Option` to handle proxy state management

## Interview Questions

1. What are the different types of proxies?
2. How does a proxy differ from a decorator in Scala?
3. When would you use a virtual proxy vs a protection proxy?
4. How do you implement a transparent proxy?
5. How do proxies interact with Scala's immutability model?

## References

- [Lazy Val](https://docs.scala-lang.org/tour/basics.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
- [Scala Design Patterns](https://www.scala-lang.org/)
