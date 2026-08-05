# Visitor Pattern in Scala

The Visitor pattern represents an operation to be performed on elements of an object structure. In Scala, this is implemented using sealed traits and pattern matching.

## When to Use

- Operations over heterogeneous data structures
- AST traversal and compilation
- Serialization of complex structures
- Adding operations without modifying element classes
- File system traversal

## Implementation

### Sealed Trait Visitor

```scala
sealed trait Expr {
  def accept(visitor: ExprVisitor): Double
}

case class Number(value: Double) extends Expr {
  def accept(visitor: ExprVisitor): Double = visitor.visitNumber(this)
}

case class Add(left: Expr, right: Expr) extends Expr {
  def accept(visitor: ExprVisitor): Double = visitor.visitAdd(this)
}

case class Multiply(left: Expr, right: Expr) extends Expr {
  def accept(visitor: ExprVisitor): Double = visitor.visitMultiply(this)
}

trait ExprVisitor {
  def visitNumber(expr: Number): Double
  def visitAdd(expr: Add): Double
  def visitMultiply(expr: Multiply): Double
}

class Evaluator extends ExprVisitor {
  def visitNumber(expr: Number): Double = expr.value
  def visitAdd(expr: Add): Double = expr.left.accept(this) + expr.right.accept(this)
  def visitMultiply(expr: Multiply): Double = expr.left.accept(this) * expr.right.accept(this)
}

class Printer extends ExprVisitor {
  def visitNumber(expr: Number): Double = {
    println(expr.value)
    expr.value
  }
  def visitAdd(expr: Add): Double = {
    println("+")
    expr.left.accept(this)
    expr.right.accept(this)
  }
  def visitMultiply(expr: Multiply): Double = {
    println("*")
    expr.left.accept(this)
    expr.right.accept(this)
  }
}
```

### Pattern Matching Visitor

```scala
sealed trait Shape
case class Circle(radius: Double) extends Shape
case class Rectangle(width: Double, height: Double) extends Shape
case class Triangle(a: Double, b: Double, c: Double) extends Shape

def area(shape: Shape): Double = shape match {
  case Circle(r) => math.Pi * r * r
  case Rectangle(w, h) => w * h
  case Triangle(a, b, c) =>
    val s = (a + b + c) / 2
    math.sqrt(s * (s - a) * (s - b) * (s - c))
}

def perimeter(shape: Shape): Double = shape match {
  case Circle(r) => 2 * math.Pi * r
  case Rectangle(w, h) => 2 * (w + h)
  case Triangle(a, b, c) => a + b + c
}
```

### AST Visitor

```scala
sealed trait AST
case class Literal(value: Int) extends AST
case class BinaryOp(op: Char, left: AST, right: AST) extends AST

object ASTVisitor {
  def evaluate(node: AST): Int = node match {
    case Literal(value) => value
    case BinaryOp(op, left, right) =>
      val l = evaluate(left)
      val r = evaluate(right)
      op match {
        case '+' => l + r
        case '-' => l - r
        case '*' => l * r
        case '/' => l / r
        case _ => 0
      }
  }

  def countNodes(node: AST): Int = node match {
    case Literal(_) => 1
    case BinaryOp(_, left, right) =>
      1 + countNodes(left) + countNodes(right)
  }
}
```

### Generic Visitor

```scala
trait Visitor[T] {
  def visit(value: T): String
}

class StringVisitor extends Visitor[String] {
  def visit(value: String): String = s"String: $value"
}

class IntVisitor extends Visitor[Int] {
  def visit(value: Int): String = s"Int: $value"
}
```

## Best Practices

- Use sealed traits for exhaustive pattern matching
- Use pattern matching for simple visitors
- Use the accept method pattern for extensible hierarchies
- Document which operations each visitor performs
- Consider using `@tailrec` for recursive visitors

## Interview Questions

1. How does Scala's pattern matching simplify the visitor pattern?
2. What is the difference between pattern matching and accept/visit?
3. How do you add a new operation without modifying existing visitors?
4. How do you handle cyclic structures in visitors?
5. What are the performance implications of the visitor pattern?

## References

- [Sealed Traits](https://docs.scala-lang.org/tour/polymorphic-types.html)
- [Pattern Matching](https://docs.scala-lang.org/tour/pattern-matching.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
