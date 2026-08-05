# Composite Pattern (Kotlin)

## Overview

The Composite pattern lets you compose objects into tree structures to represent
part-whole hierarchies. Kotlin's sealed classes and recursion make composites
particularly clean.

## When to Use

- Representing part-whole hierarchies
- Treating individual and composite objects uniformly
- Building tree structures like menus or file systems
- Creating UI component hierarchies

## Kotlin Implementation

### Sealed Class Composite

```kotlin
sealed class Component {
    abstract fun display(depth: Int = 0)
}

class Leaf(private val name: String) : Component() {
    override fun display(depth: Int) {
        println("-".repeat(depth) + name)
    }
}

class Composite(private val name: String) : Component() {
    private val children = mutableListOf<Component>()

    fun add(component: Component) {
        children.add(component)
    }

    fun remove(component: Component) {
        children.remove(component)
    }

    override fun display(depth: Int) {
        println("-".repeat(depth) + "+" + name)
        children.forEach { it.display(depth + 2) }
    }
}
```

### Generic Composite

```kotlin
interface Tree<T> {
    val value: T
    val children: List<Tree<T>>
}

data class TreeNode<T>(
    override val value: T,
    override val children: List<Tree<T>> = emptyList()
) : Tree<T>
```

### Functional Composite

```kotlin
fun <T> composite(
    value: T,
    init: MutableList<Tree<T>>.() -> Unit
): Tree<T> {
    val children = mutableListOf<Tree<T>>()
    children.init()
    return TreeNode(value, children)
}

fun <T> leaf(value: T): Tree<T> = TreeNode(value)
```

### Visitor Integration

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

## Best Practices

- Use sealed classes for type safety
- Define uniform interface for all components
- Use recursion for traversal
- Consider visitor for complex operations
- Document component lifecycle

## Interview Questions

1. How does Composite differ from Decorator?
2. What is the difference between Composite and Chain of Responsibility?
3. Can composite operations fail on leaves?
4. How do you traverse a composite tree?
5. When should you avoid using Composite?

## References

- Kotlin documentation: Sealed classes
- "Kotlin in Action" by Svetlana Isakova
- "Head First Design Patterns" by Freeman
