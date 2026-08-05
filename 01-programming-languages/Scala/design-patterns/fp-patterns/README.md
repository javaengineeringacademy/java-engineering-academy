# Functional Programming Patterns in Scala

Scala supports functional programming patterns through its type system, higher-order functions, and libraries like Cats and Scalaz. These patterns enable composable, testable, and predictable code.

## Core Concepts

### Monad

A Monad is a design pattern that allows chaining operations while preserving context.

```scala
trait Monad[F[_]] {
  def pure[A](a: A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

// Option as a Monad
val result: Option[Int] = for {
  a <- Some(1)
  b <- Some(2)
} yield a + b
// Some(3)
```

### Functor

A Functor is a type that implements `map`, allowing transformation of wrapped values.

```scala
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

// Option as a Functor
val mapped: Option[Int] = Some(5).map(_ * 2) // Some(10)
```

### Applicative

An Applicative extends Functor with `ap`, allowing application of wrapped functions.

```scala
trait Applicative[F[_]] extends Functor[F] {
  def pure[A](a: A): F[A]
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
}

// Option as an Applicative
val f: Option[Int => Int] = Some(_ * 2)
val a: Option[Int] = Some(5)
val result: Option[Int] = a.ap(f) // Some(10)
```

## For Comprehensions

For comprehensions are syntactic sugar for monadic operations.

```scala
case class User(name: String, age: Int)
case class Order(userId: String, total: Double)

def getUser(id: String): Option[User] = Some(User("Alice", 30))
def getOrders(userId: String): Option[List[Order]] = Some(List(Order("1", 100.0)))

val result: Option[Double] = for {
  user <- getUser("1")
  orders <- getOrders(user.name)
  total = orders.map(_.total).sum
} yield total
```

## Category Theory Patterns

### Semigroup

```scala
trait Semigroup[A] {
  def combine(x: A, y: A): A
}

implicit val intSemigroup: Semigroup[Int] = new Semigroup[Int] {
  def combine(x: Int, y: Int): Int = x + y
}
```

### Monoid

```scala
trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
  def combine(x: Int, y: Int): Int = x + y
  def empty: Int = 0
}
```

### Foldable

```scala
trait Foldable[F[_]] {
  def foldLeft[A, B](fa: F[A], z: B)(f: (B, A) => B): B
  def foldRight[A, B](fa: F[A], z: B)(f: (A, B) => B): B
}
```

## Best Practices

- Use for comprehensions for readable monadic chains
- Prefer immutable data structures for pure functions
- Use type classes for ad-hoc polymorphism
- Consider using Cats or Scalaz for production FP patterns
- Document type class instances and their laws

## Interview Questions

1. What is the difference between a Functor and a Monad?
2. How do for comprehensions relate to flatMap and map?
3. What laws must a Monad satisfy?
4. When should you use type classes vs inheritance?
5. How do you handle errors in functional patterns?

## References

- [Cats Documentation](https://typelevel.org/cats/)
- [Functional Programming in Scala](https://www.manning.com/books/functional-programming-in-scala)
- [Scala Documentation](https://docs.scala-lang.org/)
