# Visitor Pattern (Kotlin)

## Overview

The Visitor pattern lets you separate algorithms from the objects on which they operate.
Kotlin's sealed classes and when expressions enable type-safe visitor implementations
without double dispatch.

## When to Use

- Object structure contains classes with many interfaces
- Need to perform operations on objects without changing classes
- Operations vary across object types
- Related operations should be grouped together

## Kotlin Implementation

### Sealed Class Visitor

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

### Interface Visitor

```kotlin
interface Visitor<T> {
    fun visit(item: T): Any
}

interface Visitable<T> {
    fun accept(visitor: Visitor<T>): Any
}

class Book(val title: String, val price: Double) : Visitable<Book> {
    override fun accept(visitor: Visitor<Book>): Any = visitor.visit(this)
}

class PriceVisitor : Visitor<Book> {
    override fun visit(item: Book): Double = item.price
}
```

### Pattern Matching

```kotlin
fun <T> match(item: T, handlers: Map<Class<*>, (Any) -> T>): T {
    return handlers[item.javaClass]?.invoke(item)
        ?: throw IllegalArgumentException("No handler for ${item.javaClass}")
}
```

### Generic Visitor

```kotlin
class CompositeVisitor<T> {
    private val visitors = mutableListOf<(T) -> Any>()

    fun add(visitor: (T) -> Any) {
        visitors.add(visitor)
    }

    fun visit(item: T): List<Any> = visitors.map { it(item) }
}
```

### Extension Function Visitor

```kotlin
sealed class Shape {
    data class Circle(val radius: Double) : Shape()
    data class Rectangle(val width: Double, val height: Double) : Shape()
}

fun Shape.area(): Double = when (this) {
    is Shape.Circle -> Math.PI * radius * radius
    is Shape.Rectangle -> width * height
}
```

## Best Practices

- Use sealed classes for type safety
- Leverage when expressions for exhaustive matching
- Keep visitor interface focused
- Document visitor responsibilities clearly
- Use extension functions for simple visitors

## Interview Questions

1. What is double dispatch in Visitor?
2. How does Kotlin avoid double dispatch with when?
3. Can you add new elements without changing visitor?
4. When should you use Visitor vs Strategy?
5. How do you handle null elements in Visitor?

## References

- Kotlin documentation: Sealed classes
- "Kotlin in Action" by Svetlana Isakova
- "Refactoring to Patterns" by Kerievsky
