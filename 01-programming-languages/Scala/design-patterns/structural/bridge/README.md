# Bridge Pattern in Scala

The Bridge pattern separates abstraction from implementation so both can vary independently. In Scala, this is achieved using traits and class hierarchies.

## When to Use

- Avoiding class explosion from multiple dimensions
- Separating platform-specific code from business logic
- When both abstraction and implementation need independent extension
- Cross-platform development

## Implementation

### Trait-Based Bridge

```scala
trait Renderer {
  def renderCircle(x: Double, y: Double, radius: Double): Unit
  def renderRectangle(x: Double, y: Double, width: Double, height: Double): Unit
}

class SVGRenderer extends Renderer {
  def renderCircle(x: Double, y: Double, radius: Double): Unit =
    println(s"SVG Circle at ($x, $y) radius $radius")
  def renderRectangle(x: Double, y: Double, width: Double, height: Double): Unit =
    println(s"SVG Rect at ($x, $y) ${width}x$height")
}

class CanvasRenderer extends Renderer {
  def renderCircle(x: Double, y: Double, radius: Double): Unit =
    println(s"Canvas Circle at ($x, $y) radius $radius")
  def renderRectangle(x: Double, y: Double, width: Double, height: Double): Unit =
    println(s"Canvas Rect at ($x, $y) ${width}x$height")
}

abstract class Shape(renderer: Renderer) {
  def draw(): Unit
}

class Circle(renderer: Renderer, x: Double, y: Double, radius: Double)
  extends Shape(renderer) {
  def draw(): Unit = renderer.renderCircle(x, y, radius)
}

class Rectangle(renderer: Renderer, x: Double, y: Double, width: Double, height: Double)
  extends Shape(renderer) {
  def draw(): Unit = renderer.renderRectangle(x, y, width, height)
}
```

### Generic Bridge

```scala
trait Implementor {
  def operationImpl(): String
}

class ConcreteImplementorA extends Implementor {
  def operationImpl(): String = "A"
}

class ConcreteImplementorB extends Implementor {
  def operationImpl(): String = "B"
}

class Abstraction(implementor: Implementor) {
  def operation(): String = s"Abstraction(${implementor.operationImpl()})"
}
```

### Platform Bridge

```scala
trait Platform {
  def drawLine(x1: Int, y1: Int, x2: Int, y2: Int): Unit
  def drawText(x: Int, y: Int, text: String): Unit
}

class WindowsPlatform extends Platform {
  def drawLine(x1: Int, y1: Int, x2: Int, y2: Int): Unit =
    println(s"Windows: Line from ($x1,$y1) to ($x2,$y2)")
  def drawText(x: Int, y: Int, text: String): Unit =
    println(s"Windows: Text '$text' at ($x,$y)")
}

class MacOSPlatform extends Platform {
  def drawLine(x1: Int, y1: Int, x2: Int, y2: Int): Unit =
    println(s"MacOS: Line from ($x1,$y1) to ($x2,$y2)")
  def drawText(x: Int, y: Int, text: String): Unit =
    println(s"MacOS: Text '$text' at ($x,$y)")
}

abstract class Graphic(platform: Platform) {
  def draw(): Unit
}

class LineGraphic(platform: Platform, x1: Int, y1: Int, x2: Int, y2: Int)
  extends Graphic(platform) {
  def draw(): Unit = platform.drawLine(x1, y1, x2, y2)
}
```

## Best Practices

- Use traits for both abstraction and implementation dimensions
- Document the relationship between dimensions
- Use constructor parameters to inject implementations
- Consider using type parameters for compile-time bridge resolution
- Keep abstractions stable; vary implementations freely

## Interview Questions

1. What is the difference between bridge and adapter patterns?
2. When should you use type parameters vs trait objects?
3. How does the bridge pattern reduce code duplication?
4. Can you combine bridge with factory pattern?
5. How do you test code using the bridge pattern?

## References

- [Traits](https://docs.scala-lang.org/tour/traits.html)
- [Effective Scala](https://twitter.github.io/effectivescala/)
- [Scala Design Patterns](https://www.scala-lang.org/)
