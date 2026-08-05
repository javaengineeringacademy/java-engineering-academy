# Prototype Pattern in Scala

The Prototype pattern creates new objects by cloning existing instances. In Scala, case classes provide `copy` for shallow cloning, and custom methods for deep copying.

## When to Use

- Creating copies of expensive-to-build objects
- Template-based object creation
- Preserving object state without re-initialization
- Avoiding complex construction logic

## Implementation

### Case Class Copy

```scala
case class Document(
  title: String,
  content: String,
  metadata: Map[String, String] = Map.empty
)

// Usage
val template = Document("Template", "Default content", Map("draft" -> "true"))
val doc1 = template.copy()
val doc2 = template.copy(title = "Report", content = "Custom content")
```

### Deep Clone

```scala
case class DeepDocument(
  title: String,
  content: String,
  tags: List[String]
) {
  def clone(): DeepDocument = DeepDocument(
    title,
    content,
    tags.toList // Creates new list
  )
}
```

### Prototype Registry

```scala
import scala.collection.mutable

class PrototypeRegistry[T] {
  private val prototypes = mutable.Map[String, T]()

  def register(name: String, prototype: T): Unit =
    prototypes(name) = prototype

  def clone(name: String): Option[T] = prototypes.get(name) match {
    case Some(p: { def copy(): T }) => Some(p.copy())
    case _ => None
  }
}

// Usage
case class Shape(kind: String, color: String, size: Double)

val registry = new PrototypeRegistry[Shape]
registry.register("redCircle", Shape("circle", "red", 10.0))
val cloned = registry.clone("redCircle")
```

### Custom Clone

```scala
trait Clonable[T] {
  def clone(): T
}

case class Config(
  databaseUrl: String,
  maxConnections: Int,
  features: Set[String]
) extends Clonable[Config] {
  def clone(): Config = Config(
    databaseUrl,
    maxConnections,
    features.toSet // Creates new set
  )
}

// Usage
val original = Config("localhost:5432", 10, Set("feature1"))
val copy = original.clone()
```

## Best Practices

- Use case class `copy` for shallow cloning of immutable data
- Implement deep clone methods for mutable or nested structures
- Document whether clone is shallow or deep
- Consider using `immutable` collections for automatic deep copy semantics
- Use the `Cloneable` trait pattern for consistent cloning behavior

## Interview Questions

1. How does case class `copy` differ from a traditional clone?
2. When is shallow copy sufficient vs deep copy?
3. How do you handle cloning of circular references?
4. What are the performance implications of cloning in Scala?
5. How do you implement cloning for sealed trait hierarchies?

## References

- [Case Classes](https://docs.scala-lang.org/tour/case-classes.html)
- [Immutable Collections](https://docs.scala-lang.org/collections/)
- [Effective Scala](https://twitter.github.io/effectivescala/)
