# Composite Pattern in Scala

The Composite pattern composes objects into tree structures and treats individual and composite objects uniformly. Scala's sealed traits and case classes make this pattern natural.

## When to Use

- Hierarchical data structures (file systems, UI, ASTs)
- Treating single and composite objects uniformly
- Recursive data structures
- Tree traversal operations

## Implementation

### Sealed Trait Composite

```scala
sealed trait FileTree {
  def name: String
  def size: Long
}

case class File(name: String, size: Long) extends FileTree
case class Directory(name: String, children: List[FileTree]) extends FileTree {
  override def size: Long = children.map(_.size).sum
}

// Usage
val tree = Directory("root", List(
  File("file1.txt", 100),
  File("file2.txt", 200),
  Directory("subdir", List(
    File("file3.txt", 50)
  ))
))

println(s"Total size: ${tree.size}")
```

### Expression Tree

```scala
sealed trait Expr {
  def evaluate: Double
}

case class Number(value: Double) extends Expr {
  def evaluate: Double = value
}

case class Add(left: Expr, right: Expr) extends Expr {
  def evaluate: Double = left.evaluate + right.evaluate
}

case class Multiply(left: Expr, right: Expr) extends Expr {
  def evaluate: Double = left.evaluate * right.evaluate
}

// Usage
val expr = Add(Number(5), Multiply(Number(3), Number(2)))
println(s"Result: ${expr.evaluate}")
```

### UI Component Tree

```scala
sealed trait Component {
  def render(): String
}

case class TextComponent(text: String) extends Component {
  def render(): String = text
}

case class Container(components: List[Component]) extends Component {
  def render(): String = components.map(_.render()).mkString("<div>", "", "</div>")
}

// Usage
val ui = Container(List(
  TextComponent("Hello"),
  Container(List(
    TextComponent("World")
  ))
))
```

### Tree Traversal

```scala
sealed trait Tree[+A]
case object Leaf extends Tree[Nothing]
case class Node[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {
  def fold[A, B](tree: Tree[A])(f: A => B)(g: (B, B) => B): B = tree match {
    case Leaf => throw new NoSuchElementException("Empty tree")
    case Node(value, Leaf, Leaf) => f(value)
    case Node(value, left, right) =>
      g(f(value), g(fold(left)(f)(g), fold(right)(f)(g)))
  }
}
```

## Best Practices

- Use sealed traits for exhaustive pattern matching
- Implement `toString` for debugging composite structures
- Use `fold` or `map` for tree traversal
- Document tree depth constraints
- Consider using `@tailrec` for linear recursive traversals

## Interview Questions

1. How does Scala's pattern matching simplify the composite pattern?
2. What is the difference between a sealed trait and a regular trait?
3. How do you implement iteration over a composite tree?
4. How do you handle mutable trees in Scala?
5. When should you use the composite pattern vs a simple collection?

## References

- [Sealed Traits](https://docs.scala-lang.org/tour/polymorphic-types.html)
- [Pattern Matching](https://docs.scala-lang.org/tour/pattern-matching.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
