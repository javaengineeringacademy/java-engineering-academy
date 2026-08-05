# Flyweight Pattern in Scala

The Flyweight pattern minimizes memory usage by sharing data across similar objects. In Scala, this is implemented using caching, interning, or shared state.

## When to Use

- Large numbers of similar objects
- Memory-constrained environments
- Text editors with character objects
- Game objects with shared textures
- String deduplication

## Implementation

### String Interning

```scala
class StringInterner {
  private val pool = scala.collection.mutable.Map[String, String]()

  def intern(s: String): String = pool.getOrElseUpdate(s, s)
}

// Usage
val interner = new StringInterner()
val s1 = interner.intern("hello")
val s2 = interner.intern("hello")
println(s1 eq s2) // true
```

### Flyweight Factory

```scala
case class FlyweightData(sharedState: String)

class FlyweightFactory {
  private val pool = scala.collection.mutable.Map[String, FlyweightData]()

  def getData(state: String): FlyweightData =
    pool.getOrElseUpdate(state, FlyweightData(state))
}

case class Flyweight(uniqueState: (Double, Double), data: FlyweightData)

// Usage
val factory = new FlyweightFactory()
val fw1 = Flyweight((1.0, 2.0), factory.getData("common"))
val fw2 = Flyweight((3.0, 4.0), factory.getData("common"))
println(fw1.data eq fw2.data) // true
```

### Character Flyweight

```scala
case class CharacterGlyph(font: String, size: Int)

class TextEditor {
  private val glyphCache = scala.collection.mutable.Map[String, CharacterGlyph]()
  private val characters = scala.collection.mutable.ListBuffer[(Char, CharacterGlyph, (Int, Int))]()

  def insertChar(ch: Char, font: String, size: Int, pos: (Int, Int)): Unit = {
    val key = s"$font:$size"
    val glyph = glyphCache.getOrElseUpdate(key, CharacterGlyph(font, size))
    characters += ((ch, glyph, pos))
  }
}
```

### Thread-Safe Flyweight

```scala
import java.util.concurrent.ConcurrentHashMap

class ThreadSafeInterner {
  private val pool = new ConcurrentHashMap[String, String]()

  def intern(s: String): String =
    pool.computeIfAbsent(s, identity)
}

// Usage
val interner = new ThreadSafeInterner()
// Safe to use from multiple threads
```

## Best Practices

- Use mutable maps for thread-safe caching with `ConcurrentHashMap`
- Document the shared state boundaries
- Consider using `lazy val` for expensive shared resources
- Use `Option` to handle missing flyweight states
- Monitor memory usage with flyweight implementations

## Interview Questions

1. What is the difference between flyweight and prototype patterns?
2. When does the flyweight pattern become counterproductive?
3. How do you handle thread safety for flyweight caches?
4. How do you implement flyweight cleanup for unused entries?
5. What are the performance trade-offs of flyweight sharing?

## References

- [Mutable Collections](https://docs.scala-lang.org/collections/mutable-collections.html)
- [ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
