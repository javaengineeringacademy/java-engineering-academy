# Strategy Pattern in Scala

The Strategy pattern defines a family of algorithms and makes them interchangeable. In Scala, this is implemented using function types, traits, or higher-order functions.

## When to Use

- Multiple sorting or filtering algorithms
- Payment processing strategies
- Validation rules
- Compression algorithms
- Route planning algorithms

## Implementation

### Function Type Strategy

```scala
class Sorter[T](data: List[T], strategy: (List[T]) => List[T]) {
  def sort(): List[T] = strategy(data)
}

def bubbleSort[T](data: List[T])(implicit ord: Ordering[T]): List[T] = {
  val arr = data.toArray
  val len = arr.length
  for (i <- 0 until len; j <- 0 until len - 1 - i) {
    if (ord.gt(arr(j), arr(j + 1))) {
      val temp = arr(j)
      arr(j) = arr(j + 1)
      arr(j + 1) = temp
    }
  }
  arr.toList
}

// Usage
val sorter = Sorter(List(5, 3, 1, 4, 2), bubbleSort _)
println(sorter.sort())
```

### Trait-Based Strategy

```scala
trait CompressionStrategy {
  def compress(data: Array[Byte]): Array[Byte]
}

class GzipCompression extends CompressionStrategy {
  def compress(data: Array[Byte]): Array[Byte] = {
    println("Compressing with Gzip")
    data
  }
}

class Lz4Compression extends CompressionStrategy {
  def compress(data: Array[Byte]): Array[Byte] = {
    println("Compressing with LZ4")
    data
  }
}

class FileProcessor(strategy: CompressionStrategy) {
  def process(data: Array[Byte]): Array[Byte] = strategy.compress(data)
}
```

### Generic Strategy

```scala
trait Validator {
  def validate(input: String): Boolean
}

class EmailValidator extends Validator {
  def validate(input: String): Boolean = input.contains("@")
}

class PhoneValidator extends Validator {
  def validate(input: String): Boolean = input.forall(c => c.isDigit || c == '+')
}

def validateAll(validators: List[Validator], input: String): Boolean =
  validators.forall(_.validate(input))
```

### Higher-Order Function Strategy

```scala
def applyStrategy[T](data: List[T], strategy: T => Boolean): List[T] =
  data.filter(strategy)

val numbers = List(1, 2, 3, 4, 5, 6)
val evens = applyStrategy(numbers, _ % 2 == 0)
val large = applyStrategy(numbers, _ > 3)

println(s"Evens: $evens")
println(s"Large: $large")
```

## Best Practices

- Use function types for simple, stateless strategies
- Use traits when strategies need state or complex behavior
- Use implicit parameters for implicit strategy selection
- Document strategy selection criteria
- Consider using pattern matching for strategy dispatch

## Interview Questions

1. What is the difference between strategy and command patterns?
2. When should you use function types vs traits for strategies?
3. How do you implement strategy selection at runtime?
4. How do you handle strategy state and configuration?
5. Can strategies be composed? How?

## References

- [Function Types](https://docs.scala-lang.org/tour/higher-order-functions.html)
- [Traits](https://docs.scala-lang.org/tour/traits.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
