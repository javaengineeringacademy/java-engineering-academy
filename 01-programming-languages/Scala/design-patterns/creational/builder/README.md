# Builder Pattern in Scala

The Builder pattern separates object construction from its representation. In Scala, case classes and method chaining provide elegant builder implementations.

## When to Use

- Complex objects with many optional fields
- Configuration objects with sensible defaults
- Objects requiring step-by-step construction
- When construction order matters
- Immutable object creation

## Implementation

### Case Class Builder

```scala
case class Server(
  host: String,
  port: Int = 8080,
  maxConnections: Int = 100,
  timeout: Int = 30,
  ssl: Boolean = false
)

object Server {
  def builder(host: String): ServerBuilder = ServerBuilder(host)
}

case class ServerBuilder(
  host: String,
  port: Int = 8080,
  maxConnections: Int = 100,
  timeout: Int = 30,
  ssl: Boolean = false
) {
  def port(p: Int): ServerBuilder = copy(port = p)
  def maxConnections(m: Int): ServerBuilder = copy(maxConnections = m)
  def timeout(t: Int): ServerBuilder = copy(timeout = t)
  def ssl(s: Boolean): ServerBuilder = copy(ssl = s)
  def build(): Server = Server(host, port, maxConnections, timeout, ssl)
}
```

### Fluent Builder

```scala
class QueryBuilder private (
  private val table: String,
  private val conditions: List[String] = Nil,
  private val orderBy: Option[String] = None,
  private val limit: Option[Int] = None
) {
  def where(condition: String): QueryBuilder =
    new QueryBuilder(table, conditions :+ condition, orderBy, limit)

  def order(column: String): QueryBuilder =
    new QueryBuilder(table, conditions, Some(column), limit)

  def take(n: Int): QueryBuilder =
    new QueryBuilder(table, conditions, orderBy, Some(n))

  def build(): String = {
    val base = s"SELECT * FROM $table"
    val whereClause = if (conditions.nonEmpty) s" WHERE ${conditions.mkString(" AND ")}" else ""
    val orderClause = orderBy.map(o => s" ORDER BY $o").getOrElse("")
    val limitClause = limit.map(l => s" LIMIT $l").getOrElse("")
    s"$base$whereClause$orderClause$limitClause"
  }
}

object QueryBuilder {
  def apply(table: String): QueryBuilder = new QueryBuilder(table)
}
```

### Type-Safe Builder

```scala
sealed trait BuildState
case object NoHost extends BuildState
case object HasHost extends BuildState

class TypedBuilder[S <: BuildState](
  private val host: Option[String] = None,
  private val port: Int = 8080
) {
  def withHost(h: String): TypedBuilder[HasHost] =
    new TypedBuilder[HasHost](Some(h), port)

  def withPort(p: Int): TypedBuilder[S] =
    new TypedBuilder[S](host, p)

  def build()(implicit ev: S =:= HasHost): Server =
    Server(host.get, port)
}
```

## Best Practices

- Use case class copy for simple builders with defaults
- Implement method chaining with `copy` for immutable builders
- Use phantom types for compile-time validation of required fields
- Document default values for all optional parameters
- Consider using `implicit` parameters for builder configuration

## Interview Questions

1. How does case class `copy` simplify the builder pattern?
2. What is the typestate pattern and how does it work in Scala?
3. When should you use a builder vs case class defaults?
4. How do you handle builder validation errors?
5. What are the advantages of builders over telescoping constructors?

## References

- [Case Classes](https://docs.scala-lang.org/tour/case-classes.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
- [Scala Design Patterns](https://www.scala-lang.org/)
