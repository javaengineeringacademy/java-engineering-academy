# Scala for Apache Spark

## Table of Contents

- [Overview](#overview)
- [Scala Basics](#scala-basics)
- [Object-Oriented Programming](#object-oriented-programming)
- [Functional Programming](#functional-programming)
- [Collections](#collections)
- [Pattern Matching](#pattern-matching)
- [Implicit Parameters](#implicit-parameters)
- [Scala and Spark](#scala-and-spark)
- [Performance Optimization](#performance-optimization)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

Scala is a modern multi-approach programming language designed to express
common programming patterns in a concise, elegant, and type-safe way.
It is the primary language for Apache Spark, providing seamless integration
with Spark's APIs.

### Key Characteristics

- **Object-oriented**: Everything is an object
- **Functional programming**: First-class functions, immutability
- **Statically typed**: Type safety with type inference
- **JVM-based**: Runs on Java Virtual Machine
- **Concurrent**: Actor model for concurrency

### Why Scala for Spark

- **Performance**: Native Spark APIs in Scala
- **Type safety**: Compile-time error detection
- **Functional programming**: Natural fit for distributed computing
- **Community**: Large ecosystem and community support
- **Interoperability**: Works seamlessly with Java

### Scala vs Python for Spark

| Feature | Scala | Python |
|---------|-------|--------|
| Performance | Faster | Slower |
| Type Safety | Static typing | Dynamic typing |
| Learning Curve | Steeper | Easier |
| API Coverage | Complete | Most features |
| Community | Smaller but dedicated | Larger |

---

## Scala Basics

### Variables and Types

```scala
// Immutable variable (val)
val name: String = "Alice"
val age: Int = 34
val salary: Double = 75000.0
val is_active: Boolean = true

// Mutable variable (var)
var counter: Int = 0
counter += 1

// Type inference
val inferred_name = "Bob"  // Type inferred as String
val inferred_age = 25      // Type inferred as Int

// Multiple assignment
val (x, y, z) = (1, 2, 3)

// String interpolation
val message = s"Hello, $name! You are $age years old."
val complex = s"Name: ${name.toUpperCase}, Age: ${age * 2}"
```

### Basic Operations

```scala
// Arithmetic operators
val sum = 10 + 5
val difference = 10 - 5
val product = 10 * 5
val quotient = 10 / 5
val remainder = 10 % 3

// Comparison operators
val equals = 10 == 5
val notEquals = 10 != 5
val greater = 10 > 5
val less = 10 < 5

// Logical operators
val and = true && false
val or = true || false
val not = !true

// String operations
val str1 = "Hello"
val str2 = "World"
val concatenated = str1 + " " + str2
val length = concatenated.length
val upper = concatenated.toUpperCase
val lower = concatenated.toLowerCase
```

### Control Flow

```scala
// If-else
val age = 25
if (age >= 18) {
  println("Adult")
} else if (age >= 13) {
  println("Teenager")
} else {
  println("Child")
}

// Expression-based if-else
val status = if (age >= 18) "Adult" else "Minor"

// For loop
for (i <- 1 to 10) {
  println(i)
}

// For loop with condition
for (i <- 1 to 10 if i % 2 == 0) {
  println(s"Even: $i")
}

// While loop
var count = 0
while (count < 5) {
  println(s"Count: $count")
  count += 1
}

// Do-while loop
var num = 0
do {
  println(s"Number: $num")
  num += 1
} while (num < 5)
```

---

## Object-Oriented Programming

### Classes

```scala
// Basic class
class Person(val name: String, val age: Int) {
  def greet(): String = s"Hello, my name is $name and I am $age years old."
}

// Create instance
val person = new Person("Alice", 34)
println(person.name)
println(person.greet())

// Class with default values
class Employee(
  name: String,
  age: Int,
  val department: String = "Engineering",
  val salary: Double = 50000.0
)

// Class with auxiliary constructor
class Circle(val radius: Double) {
  def this() = this(1.0)

  def area(): Double = Math.PI * radius * radius
  def circumference(): Double = 2 * Math.PI * radius
}
```

### Inheritance

```scala
// Base class
class Animal(val name: String, val sound: String) {
  def speak(): String = s"$name says $sound"
}

// Derived class
class Dog(name: String) extends Animal(name, "Woof") {
  def fetch(): String = s"$name is fetching!"
}

class Cat(name: String) extends Animal(name, "Meow") {
  def purr(): String = s"$name is purring!"
}

// Create instances
val dog = new Dog("Rex")
val cat = new Cat("Whiskers")
println(dog.speak())
println(cat.speak())
```

### Abstract Classes and Traits

```scala
// Abstract class
abstract class Shape {
  def area(): Double
  def perimeter(): Double
}

// Concrete class
class Circle(radius: Double) extends Shape {
  override def area(): Double = Math.PI * radius * radius
  override def perimeter(): Double = 2 * Math.PI * radius
}

class Rectangle(width: Double, height: Double) extends Shape {
  override def area(): Double = width * height
  override def perimeter(): Double = 2 * (width + height)
}

// Trait
trait Drawable {
  def draw(): String
}

trait Resizable {
  def resize(factor: Double): Unit
}

class Square(side: Double) extends Shape with Drawable with Resizable {
  override def area(): Double = side * side
  override def perimeter(): Double = 4 * side
  override def draw(): String = s"Drawing square with side $side"
  override def resize(factor: Double): Unit = println(s"Resizing by $factor")
}
```

### Case Classes

```scala
// Case class (immutable data carrier)
case class Person(name: String, age: Int)
case class Address(street: String, city: String, country: String)
case class Employee(name: String, age: Int, department: String)

// Create instances (no new keyword needed)
val person = Person("Alice", 34)
val address = Address("123 Main St", "New York", "USA")

// Automatic methods
println(person.name)
println(person.toString)
println(person.hashCode)

// Pattern matching
person match {
  case Person(name, age) if age > 30 => println(s"$name is over 30")
  case Person(name, _) => println(s"$name is under 30")
}

// Copy method
val updatedPerson = person.copy(age = 35)
```

### Objects

```scala
// Singleton object
object Database {
  private var connection: Connection = _

  def connect(): Connection = {
    if (connection == null) {
      connection = createConnection()
    }
    connection
  }

  private def createConnection(): Connection = {
    // Create database connection
    new Connection()
  }
}

// Companion object
class Person(val name: String, val age: Int)

object Person {
  def apply(name: String, age: Int): Person = new Person(name, age)
  def unapply(person: Person): Option[(String, Int)] = Some((person.name, person.age))
}

// Usage
val person = Person("Alice", 34)  // Uses apply method
```

---

## Functional Programming

### Functions

```scala
// Function definition
def add(a: Int, b: Int): Int = a + b

// Function with default parameter
def greet(name: String, greeting: String = "Hello"): String = {
  s"$greeting, $name!"
}

// Recursive function
def factorial(n: Int): Int = {
  if (n <= 1) 1
  else n * factorial(n - 1)
}

// Tail-recursive function
import scala.annotation.tailrec

@tailrec
def factorialTail(n: Int, accumulator: Int = 1): Int = {
  if (n <= 1) accumulator
  else factorialTail(n - 1, n * accumulator)
}

// Higher-order function
def applyFunction(f: Int => Int, x: Int): Int = f(x)

// Function types
val add: (Int, Int) => Int = (a, b) => a + b
val multiply: (Int, Int) => Int = _ * _  // Shorthand
```

### Anonymous Functions

```scala
// Anonymous function (lambda)
val double = (x: Int) => x * 2
val add = (a: Int, b: Int) => a + b

// Using anonymous functions
val numbers = List(1, 2, 3, 4, 5)
val doubled = numbers.map(x => x * 2)
val sum = numbers.reduce((a, b) => a + b)

// Multiple statements
val complex = (x: Int) => {
  val result = x * 2
  result + 1
}
```

### Closures

```scala
// Closure (captures variable from enclosing scope)
def multiplier(factor: Int): Int => Int = {
  (x: Int) => x * factor
}

val double = multiplier(2)
val triple = multiplier(3)
println(double(5))  // 10
println(triple(5))  // 15

// Mutable variable in closure
def counter(): () => Int = {
  var count = 0
  () => {
    count += 1
    count
  }
}

val increment = counter()
println(increment())  // 1
println(increment())  // 2
```

### Currying

```scala
// Curried function
def add(a: Int)(b: Int): Int = a + b

// Partial application
val add5 = add(5)_
println(add5(3))  // 8

// Curried function with multiple parameter groups
def calculate(operation: String)(a: Int, b: Int): Int = operation match {
  case "add" => a + b
  case "subtract" => a - b
  case "multiply" => a * b
  case _ => throw new IllegalArgumentException(s"Unknown operation: $operation")
}

val addFunc = calculate("add")_
println(addFunc(5, 3))  // 8
```

---

## Collections

### Lists

```scala
// Immutable list
val numbers = List(1, 2, 3, 4, 5)
val names = List("Alice", "Bob", "Charlie")

// Operations
val head = numbers.head          // 1
val tail = numbers.tail          // List(2, 3, 4, 5)
val isEmpty = numbers.isEmpty    // false
val length = numbers.length      // 5

// Transformations
val doubled = numbers.map(_ * 2)           // List(2, 4, 6, 8, 10)
val evens = numbers.filter(_ % 2 == 0)     // List(2, 4)
val sum = numbers.reduce(_ + _)            // 15
val product = numbers.foldLeft(1)(_ * _)   // 120

// Higher-order functions
numbers.foreach(println)
val filtered = numbers.withFilter(_ > 3)
val flatMapped = List("Hello World", "Foo Bar").flatMap(_.split(" "))
```

### Maps

```scala
// Immutable map
val ages = Map("Alice" -> 34, "Bob" -> 45, "Charlie" -> 29)

// Operations
val aliceAge = ages("Alice")              // 34
val bobAge = ages.getOrElse("Bob", 0)     // 45
val containsAlice = ages.contains("Alice") // true

// Transformations
val upperNames = ages.map { case (name, age) => (name.toUpperCase, age) }
val adults = ages.filter { case (_, age) => age >= 18 }
val totalAge = ages.values.sum

// Adding and removing
val updated = ages + ("Diana" -> 35)
val removed = ages - "Charlie"
```

### Sets

```scala
// Immutable set
val fruits = Set("apple", "banana", "cherry", "date")

// Operations
val hasApple = fruits.contains("apple")   // true
val size = fruits.size                    // 4

// Set operations
val vegetables = Set("carrot", "broccoli", "apple")
val intersection = fruits.intersect(vegetables)  // Set("apple")
val union = fruits.union(vegetables)              // Set("apple", "banana", ...)
val difference = fruits.diff(vegetables)          // Set("banana", "cherry", "date")

// Transformations
val upperFruits = fruits.map(_.toUpperCase)
val longFruits = fruits.filter(_.length > 5)
```

### Tuples

```scala
// Tuple creation
val person = ("Alice", 34, "Engineer")
val coordinates = (40.7128, -74.0060)

// Accessing elements
val name = person._1
val age = person._2
val job = person._3

// Pattern matching
val (n, a, j) = person

// Named tuple
val namedPerson = (name = "Alice", age = 34, job = "Engineer")

// Tuple operations
val swapped = coordinates.swap
```

### Arrays

```scala
// Mutable array
val numbers = Array(1, 2, 3, 4, 5)

// Operations
val length = numbers.length
val first = numbers(0)
numbers(0) = 10

// Transformations
val doubled = numbers.map(_ * 2)
val evens = numbers.filter(_ % 2 == 0)
val sum = numbers.sum
val sorted = numbers.sorted

// Conversions
val list = numbers.toList
val seq = numbers.toSeq
```

---

## Pattern Matching

### Basic Pattern Matching

```scala
// Value matching
val x = 5
x match {
  case 1 => println("One")
  case 2 => println("Two")
  case 3 => println("Three")
  case _ => println("Other")
}

// Type matching
def describe(value: Any): String = value match {
  case i: Int => s"Integer: $i"
  case s: String => s"String: $s"
  case d: Double => s"Double: $d"
  case b: Boolean => s"Boolean: $b"
  case _ => s"Unknown: $value"
}
```

### Pattern Matching with Case Classes

```scala
// Case class matching
sealed trait Shape
case class Circle(radius: Double) extends Shape
case class Rectangle(width: Double, height: Double) extends Shape
case class Triangle(base: Double, height: Double) extends Shape

def area(shape: Shape): Double = shape match {
  case Circle(r) => Math.PI * r * r
  case Rectangle(w, h) => w * h
  case Triangle(b, h) => 0.5 * b * h
}

// Nested pattern matching
sealed trait Tree
case class Leaf(value: Int) extends Tree
case class Node(left: Tree, right: Tree) extends Tree

def sumTree(tree: Tree): Int = tree match {
  case Leaf(value) => value
  case Node(left, right) => sumTree(left) + sumTree(right)
}
```

### Guards

```scala
// Pattern matching with guards
val x = 5
x match {
  case n if n > 0 => println("Positive")
  case n if n < 0 => println("Negative")
  case 0 => println("Zero")
}

// Complex guards
def classify(age: Int, name: String): String = (age, name) match {
  case (a, n) if a >= 18 && n.startsWith("A") => "Adult starting with A"
  case (a, _) if a >= 18 => "Adult"
  case (a, _) if a >= 13 => "Teenager"
  case _ => "Child"
}
```

### Extractors

```scala
// Custom extractor
object Email {
  def unapply(str: String): Option[(String, String)] = {
    val parts = str.split("@")
    if (parts.length == 2) Some((parts(0), parts(1)))
    else None
  }
}

// Usage
"alice@example.com" match {
  case Email(user, domain) => println(s"User: $user, Domain: $domain")
  case _ => println("Not an email")
}

// Multiple extractors
object Phone {
  def unapply(str: String): Option[(String, String)] = {
    val parts = str.split("-")
    if (parts.length == 2) Some((parts(0), parts(1)))
    else None
  }
}

"123-4567" match {
  case Phone(area, number) => println(s"Area: $area, Number: $number")
  case _ => println("Not a phone number")
}
```

---

## Implicit Parameters

### Basic Implicits

```scala
// Implicit parameter
def greet(implicit name: String): String = s"Hello, $name!"

// Implicit value
implicit val defaultName: String = "World"
println(greet())  // Uses implicit value

// Multiple implicit parameters
def calculate(implicit a: Int, b: Int): Int = a + b

implicit val num1: Int = 5
implicit val num2: Int = 10
println(calculate())  // 15
```

### Implicit Conversions

```scala
// Implicit conversion
implicit def intToString(x: Int): String = x.toString

val length: String = 123  // Implicitly converts Int to String
println(length.length)

// Implicit class
implicit class StringOps(val s: String) {
  def shout: String = s.toUpperCase + "!!!"
  def whisper: String = s.toLowerCase + "..."
}

println("hello".shout)   // HELLO!!!
println("WORLD".whisper) // world...
```

### Implicit Evidence

```scala
// Type class pattern
trait JsonWriter[T] {
  def write(value: T): String
}

implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
  def write(value: String): String = s""""$value""""
}

implicit val intWriter: JsonWriter[Int] = new JsonWriter[Int] {
  def write(value: Int): String = value.toString
}

// Using implicit evidence
def toJson[T](value: T)(implicit writer: JsonWriter[T]): String = {
  writer.write(value)
}

println(toJson("Hello"))  // "Hello"
println(toJson(42))       // 42
```

---

## Scala and Spark

### RDD Operations

```scala
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

val conf = new SparkConf().setAppName("SparkApp")
val sc = new SparkContext(conf)

// Create RDD
val data = sc.parallelize(List(1, 2, 3, 4, 5))

// Transformations
val doubled = data.map(_ * 2)
val evens = data.filter(_ % 2 == 0)

// Actions
val sum = data.reduce(_ + _)
val count = data.count()
val first = data.first()
```

### DataFrame Operations

```scala
import org.apache.spark.sql.SparkSession

val spark = SparkSession.builder()
  .appName("SparkSQL")
  .master("local[*]")
  .getOrCreate()

import spark.implicits._

// Create DataFrame
val df = List(
  ("Alice", 34),
  ("Bob", 45),
  ("Charlie", 29)
).toDF("name", "age")

// Operations
df.show()
df.printSchema()
df.select("name", "age").show()
df.filter($"age" > 30).show()
df.groupBy($"age").count().show()
```

### Dataset Operations

```scala
case class Person(name: String, age: Int)

val dataset = List(
  Person("Alice", 34),
  Person("Bob", 45),
  Person("Charlie", 29)
).toDS()

// Operations
dataset.show()
dataset.printSchema()
dataset.filter(_.age > 30).show()
dataset.map(p => Person(p.name, p.age + 1)).show()
dataset.groupByKey(_.age).count().show()
```

---

## Performance Optimization

### Immutable Collections

```scala
// Use immutable collections for thread safety
val immutableList = List(1, 2, 3)
val immutableMap = Map("a" -> 1, "b" -> 2)

// Use mutable collections when performance is critical
import scala.collection.mutable
val mutableList = mutable.ListBuffer(1, 2, 3)
val mutableMap = mutable.Map("a" -> 1, "b" -> 2)
```

### Tail Recursion

```scala
// Use tail recursion for large iterations
import scala.annotation.tailrec

@tailrec
def sumList(list: List[Int], accumulator: Int = 0): Int = list match {
  case Nil => accumulator
  case head :: tail => sumList(tail, accumulator + head)
}

// Non-tail recursive (stack overflow risk)
def sumListBad(list: List[Int]): Int = list match {
  case Nil => 0
  case head :: tail => head + sumListBad(tail)
}
```

### Lazy Evaluation

```scala
// Use lazy val for expensive computations
lazy val expensiveValue = {
  println("Computing expensive value...")
  42
}

// Value computed only when accessed
println(expensiveValue)

// Use Stream for lazy collections
def fibonacci(a: Int, b: Int): Stream[Int] = a #:: fibonacci(b, a + b)

val fibs = fibonacci(0, 1).take(10).toList
```

### Parallel Collections

```scala
// Use parallel collections for CPU-intensive operations
val data = (1 to 1000000).toList

// Sequential
val sequentialSum = data.reduce(_ + _)

// Parallel
val parallelSum = data.par.reduce(_ + _)
```

---

## Best Practices

### Code Style

1. **Use immutable by default**: Prefer `val` over `var`
2. **Use case classes**: For immutable data structures
3. **Use pattern matching**: For control flow
4. **Use higher-order functions**: For collection operations
5. **Use meaningful names**: Descriptive variable and function names

### Performance

1. **Use tail recursion**: For iterative algorithms
2. **Use lazy evaluation**: For expensive computations
3. **Use parallel collections**: For CPU-intensive operations
4. **Avoid mutable state**: When possible
5. **Use appropriate data structures**: Based on use case

### Error Handling

```scala
// Use Try for exception handling
import scala.util.{Try, Success, Failure}

def divide(a: Int, b: Int): Try[Int] = Try(a / b)

divide(10, 2) match {
  case Success(result) => println(s"Result: $result")
  case Failure(exception) => println(s"Error: ${exception.getMessage}")
}

// Use Option for nullable values
def findUser(id: Int): Option[User] = {
  // Return Some(user) or None
}

findUser(1) match {
  case Some(user) => println(s"Found user: ${user.name}")
  case None => println("User not found")
}
```

### Testing

```scala
// Use ScalaTest for testing
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CalculatorSpec extends AnyFlatSpec with Matchers {
  "Calculator" should "add two numbers" in {
    Calculator.add(2, 3) should be(5)
  }

  it should "subtract two numbers" in {
    Calculator.subtract(5, 3) should be(2)
  }
}
```

---

## Examples

### Complete Spark Application

```scala
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

case class Sales(date: String, product: String, amount: Double)

object SalesAnalysis {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("SalesAnalysis")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Read data
    val salesDF = spark.read
      .option("header", "true")
      .csv("sales.csv")
      .as[Sales]

    // Analysis
    val productSales = salesDF
      .groupBy("product")
      .agg(
        sum("amount").alias("total_sales"),
        count("*").alias("transaction_count")
      )
      .orderBy($"total_sales".desc)

    // Show results
    productSales.show()

    // Write results
    productSales.write
      .mode("overwrite")
      .parquet("output/product_sales")

    spark.stop()
  }
}
```

### Functional Data Processing

```scala
import scala.util.{Try, Success, Failure}

case class User(id: Int, name: String, email: String, age: Int)

object DataProcessor {
  def parseUser(line: String): Option[User] = {
    Try {
      val parts = line.split(",")
      User(
        parts(0).toInt,
        parts(1),
        parts(2),
        parts(3).toInt
      )
    } match {
      case Success(user) => Some(user)
      case Failure(_) => None
    }
  }

  def processUsers(lines: List[String]): List[User] = {
    lines.flatMap(parseUser)
      .filter(_.age >= 18)
      .sortBy(_.name)
  }

  def main(args: Array[String]): Unit = {
    val lines = List(
      "1,Alice,alice@example.com,34",
      "2,Bob,bob@example.com,45",
      "3,Charlie,charlie@example.com,29",
      "invalid,line",
      "4,Diana,diana@example.com,15"
    )

    val users = processUsers(lines)
    users.foreach(println)
  }
}
```

---

## References

- [Scala Documentation](https://docs.scala-lang.org/)
- [Scala School (Twitter)](https://twitter.github.io/scala_school/)
- [Programming in Scala](https://www.artima.com/shop/pinzou2)
- [Scala for the Impatient](https://www.horstmann.com/scala/)
- [Spark Scala API](https://spark.apache.org/docs/latest/api/scala/)
