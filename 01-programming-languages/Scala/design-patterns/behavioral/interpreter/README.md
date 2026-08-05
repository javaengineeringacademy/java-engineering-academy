# Interpreter Pattern in Scala

The Interpreter pattern defines a grammar for a language and provides an interpreter for it. In Scala, this is implemented using sealed traits for AST nodes and pattern matching for evaluation.

## When to Use

- Simple language parsing
- Expression evaluation
- Configuration file parsing
- Query languages
- DSL design

## Implementation

### Expression Interpreter

```scala
sealed trait Expr {
  def evaluate: Double
  def toString: String
}

case class Number(value: Double) extends Expr {
  def evaluate: Double = value
  override def toString: String = value.toString
}

case class Add(left: Expr, right: Expr) extends Expr {
  def evaluate: Double = left.evaluate + right.evaluate
  override def toString: String = s"($left + $right)"
}

case class Multiply(left: Expr, right: Expr) extends Expr {
  def evaluate: Double = left.evaluate * right.evaluate
  override def toString: String = s"($left * $right)"
}

// Usage
val expr = Add(Number(5), Multiply(Number(3), Number(2)))
println(s"$expr = ${expr.evaluate}")
```

### Rule Interpreter

```scala
trait Rule {
  def evaluate(context: Context): Boolean
}

case class Context(temperature: Double, humidity: Double, windSpeed: Double)

case class AndRule(left: Rule, right: Rule) extends Rule {
  def evaluate(context: Context): Boolean =
    left.evaluate(context) && right.evaluate(context)
}

case class TemperatureRule(min: Double, max: Double) extends Rule {
  def evaluate(context: Context): Boolean =
    context.temperature >= min && context.temperature <= max
}

case class HumidityRule(threshold: Double) extends Rule {
  def evaluate(context: Context): Boolean =
    context.humidity >= threshold
}
```

### Simple Calculator

```scala
def parseAndEvaluate(input: String): Double = {
  val tokens = input.split("\\s+").toList
  tokens.foldLeft(List.empty[Double]) {
    case (stack, "+") =>
      val b = stack.head
      val a = stack.tail.head
      (a + b) :: stack.tail.tail
    case (stack, "-") =>
      val b = stack.head
      val a = stack.tail.head
      (a - b) :: stack.tail.tail
    case (stack, "*") =>
      val b = stack.head
      val a = stack.tail.head
      (a * b) :: stack.tail.tail
    case (stack, n) =>
      n.toDouble :: stack
  }.head
}

// Usage
println(parseAndEvaluate("3 4 + 2 *")) // 14.0
```

### DSL Interpreter

```scala
sealed trait Query
case class Select(fields: List[String], from: String) extends Query
case class Where(condition: String, query: Query) extends Query

object QueryInterpreter {
  def interpret(query: Query): String = query match {
    case Select(fields, from) =>
      s"SELECT ${fields.mkString(", ")} FROM $from"
    case Where(condition, inner) =>
      s"${interpret(inner)} WHERE $condition"
  }
}

// Usage
val query = Where("age > 25", Select(List("name", "age"), "users"))
println(QueryInterpreter.interpret(query))
```

## Best Practices

- Use sealed traits for AST node types
- Use pattern matching for evaluation logic
- Document the grammar syntax and supported operations
- Consider using parser combinator libraries for complex grammars
- Add error handling for malformed expressions

## Interview Questions

1. When should you use the interpreter pattern vs a parser library?
2. How do you handle operator precedence?
3. How do you implement error recovery in interpreters?
4. What are the limitations of the interpreter pattern?
5. How do you optimize interpreter performance?

## References

- [Sealed Traits](https://docs.scala-lang.org/tour/polymorphic-types.html)
- [Pattern Matching](https://docs.scala-lang.org/tour/pattern-matching.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
