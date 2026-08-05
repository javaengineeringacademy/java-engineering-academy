# Interpreter Pattern (Kotlin)

## Overview

The Interpreter pattern defines a grammatical representation for a language and provides
an interpreter to work with this grammar. Kotlin's DSL capabilities and sealed classes
enable type-safe interpreter implementations.

## When to Use

- Simple grammar representation needed
- Efficiency is not critical concern
- Grammar is simple and simple interpreter suffices
- Similar patterns of expressions occur frequently

## Kotlin Implementation

### Sealed Class Interpreter

```kotlin
sealed class Expression {
    abstract fun interpret(): Double

    data class Number(val value: Double) : Expression() {
        override fun interpret(): Double = value
    }

    data class Add(val left: Expression, val right: Expression) : Expression() {
        override fun interpret(): Double = left.interpret() + right.interpret()
    }

    data class Multiply(val left: Expression, val right: Expression) : Expression() {
        override fun interpret(): Double = left.interpret() * right.interpret()
    }
}
```

### DSL Interpreter

```kotlin
class Interpreter {
    fun number(value: Double): () -> Double = { value }

    fun add(left: () -> Double, right: () -> Double): () -> Double =
        { left() + right() }

    fun multiply(left: () -> Double, right: () -> Double): () -> Double =
        { left() * right() }
}

val interp = Interpreter()
val expr = interp.add(interp.number(5.0), interp.multiply(interp.number(3.0), interp.number(2.0)))
```

### Variable Interpreter

```kotlin
typealias VariableMap = Map<String, Double>

fun createInterpreter(variables: VariableMap) = object {
    fun number(value: Double): () -> Double = { value }
    fun variable(name: String): () -> Double = { variables[name] ?: 0.0 }
    fun add(left: () -> Double, right: () -> Double): () -> Double = { left() + right() }
}

val variables = mapOf("x" to 10.0, "y" to 5.0)
val interp = createInterpreter(variables)
val expr = interp.add(interp.variable("x"), interp.variable("y"))
```

### AST Interpreter

```kotlin
sealed class AST {
    data class Number(val value: Double) : AST()
    data class Add(val left: AST, val right: AST) : AST()
    data class Multiply(val left: AST, val right: AST) : AST()
}

fun evaluate(ast: AST): Double = when (ast) {
    is AST.Number -> ast.value
    is AST.Add -> evaluate(ast.left) + evaluate(ast.right)
    is AST.Multiply -> evaluate(ast.left) * evaluate(ast.right)
}
```

### Kotlin DSL

```kotlin
class ExpressionBuilder {
    private var expression: () -> Double = { 0.0 }

    fun number(value: Double) {
        expression = { value }
    }

    fun add(left: () -> Double, right: () -> Double) {
        expression = { left() + right() }
    }

    fun build(): () -> Double = expression
}
```

## Best Practices

- Use sealed classes for type safety
- Leverage Kotlin DSL capabilities
- Document grammar rules clearly
- Consider caching for repeated interpretations
- Use closures for lazy evaluation

## Interview Questions

1. When should you use Interpreter pattern?
2. How do you implement expression trees?
3. Can Interpreter be used for SQL parsing?
4. What are alternatives to Interpreter for complex grammars?
5. How do you handle operator precedence?

## References

- Kotlin documentation: DSLs
- "Kotlin in Action" by Svetlana Isakova
- "Compilers: Principles, Techniques, and Tools"
