# Chain of Responsibility Pattern in Scala

The Chain of Responsibility pattern passes a request along a chain of handlers. In Scala, this is implemented using traits, function composition, or linked structures.

## When to Use

- Request processing pipelines
- Middleware stacks (web frameworks)
- Event handling chains
- Logging levels
- Approval workflows

## Implementation

### Trait-Based Chain

```scala
trait Handler {
  def handle(request: String): Option[String]
  def setNext(handler: Handler): Handler
}

class AuthHandler extends Handler {
  private var next: Option[Handler] = None

  def handle(request: String): Option[String] = {
    if (request.contains("auth")) Some("AuthHandler processed")
    else next.flatMap(_.handle(request))
  }

  def setNext(handler: Handler): Handler = {
    next = Some(handler)
    handler
  }
}

class ValidationHandler extends Handler {
  private var next: Option[Handler] = None

  def handle(request: String): Option[String] = {
    if (request.contains("valid")) Some("ValidationHandler processed")
    else next.flatMap(_.handle(request))
  }

  def setNext(handler: Handler): Handler = {
    next = Some(handler)
    handler
  }
}
```

### Function Chain

```scala
class MiddlewareChain {
  private var middlewares: List[String => Option[String]] = List()

  def add(middleware: String => Option[String]): Unit = {
    middlewares = middlewares :+ middleware
  }

  def execute(request: String): Option[String] = {
    middlewares.foldLeft(None: Option[String]) {
      case (Some(result), _) => Some(result)
      case (None, middleware) => middleware(request)
    }
  }
}

// Usage
val chain = new MiddlewareChain()
chain.add(req => if (req.contains("auth")) Some("Authenticated") else None)
chain.add(req => if (req.contains("valid")) Some("Validated") else None)
```

### Scala Idiomatic Chain

```scala
sealed trait LogHandler {
  def handle(level: String, message: String): Boolean
}

case class ConsoleHandler(minLevel: String) extends LogHandler {
  def handle(level: String, message: String): Boolean = {
    if (level >= minLevel) {
      println(s"[CONSOLE] $message")
      true
    } else false
  }
}

case class FileHandler(path: String, minLevel: String) extends LogHandler {
  def handle(level: String, message: String): Boolean = {
    if (level >= minLevel) {
      println(s"[FILE:$path] $message")
      true
    } else false
  }
}

def processChain(handlers: List[LogHandler], level: String, message: String): Boolean = {
  handlers.exists(_.handle(level, message))
}
```

### Middleware Pattern

```scala
type Middleware = String => String

def authMiddleware(next: String => String): String => String = {
  request => next(s"Auth: $request")
}

def loggingMiddleware(next: String => String): String => String = {
  request =>
    println(s"Logging: $request")
    next(request)
}

// Usage
val pipeline = authMiddleware(loggingMiddleware(identity))
println(pipeline("Hello"))
```

## Best Practices

- Keep handlers independent; avoid coupling between elements
- Use `Option` for chain results
- Document the chain order and handler responsibilities
- Use function composition for idiomatic Scala chains
- Consider using cats or scalaz for monadic chains

## Interview Questions

1. How does the chain of responsibility differ from the observer pattern?
2. When should you break the chain vs returning None?
3. How do you handle circular chains in Scala?
4. How do you test individual handlers in a chain?
5. What are the performance implications of long chains?

## References

- [Traits](https://docs.scala-lang.org/tour/traits.html)
- [Higher-Order Functions](https://docs.scala-lang.org/tour/higher-order-functions.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
