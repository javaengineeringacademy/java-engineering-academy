# Iterator Pattern in Scala

The Iterator pattern provides a way to access elements sequentially. In Scala, iterators are built into the standard library with `Iterator` and `Iterable` traits.

## When to Use

- Traversing collections
- Lazy evaluation of sequences
- Custom data structure traversal
- Filtering and transforming collections
- Implementing range-based operations

## Implementation

### Custom Iterator

```scala
class Counter(max: Int) extends Iterator[Int] {
  private var count = 0

  def hasNext: Boolean = count < max

  def next(): Int = {
    count += 1
    count
  }
}

// Usage
val counter = new Counter(5)
counter.foreach(println)
```

### For Comprehension

```scala
case class Person(name: String, age: Int)

val people = List(
  Person("Alice", 30),
  Person("Bob", 25),
  Person("Charlie", 35)
)

val result = for {
  person <- people if person.age > 25
} yield person.name

println(result) // List(Alice, Charlie)
```

### Custom Collection Iterator

```scala
class Node[T](value: T, children: List[Node[T]] = List()) {
  def iterator: Iterator[T] = {
    Iterator.single(value) ++ children.flatMap(_.iterator)
  }
}

// Usage
val tree = Node(1, List(Node(2, List(Node(4))), Node(3)))
tree.iterator.foreach(println)
```

### Lazy Iterator

```scala
class FibonacciIterator extends Iterator[BigInt] {
  private var a = BigInt(0)
  private var b = BigInt(1)

  def hasNext: Boolean = true

  def next(): BigInt = {
    val result = a
    val newB = a + b
    a = b
    b = newB
    result
  }
}

// Usage
val fibs = new FibonacciIterator().take(10).toList
println(fibs)
```

### Filtered Iterator

```scala
class FilteredIterator[T](source: Iterator[T], predicate: T => Boolean) extends Iterator[T] {
  private var nextElement: Option[T] = None

  def hasNext: Boolean = {
    nextElement match {
      case Some(_) => true
      case None =>
        nextElement = source.find(predicate)
        nextElement.isDefined
    }
  }

  def next(): T = {
    nextElement match {
      case Some(value) =>
        nextElement = None
        value
      case None =>
        throw new NoSuchElementException("No more elements")
    }
  }
}
```

## Best Practices

- Use `for` comprehensions for readable collection processing
- Implement `Iterator` for custom iteration logic
- Use `Iterator.from` for infinite sequences
- Prefer `map`, `filter`, `flatMap` over manual iteration
- Document iterator element order and behavior

## Interview Questions

1. What is the difference between `Iterator` and `Iterable`?
2. How do you implement a bidirectional iterator in Scala?
3. What are iterator adapters vs consumer adapters?
4. How do you handle errors in iterators?
5. What are the performance characteristics of lazy vs eager iterators?

## References

- [Iterator](https://docs.scala-lang.org/overviews/collections/overview.html)
- [For Comprehensions](https://docs.scala-lang.org/tour/for-comprehensions.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
