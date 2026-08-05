# Bridge Pattern in PHP

The Bridge pattern separates abstraction from implementation so both can vary independently. In PHP, this is achieved using interfaces and composition.

## When to Use

- Avoiding class explosion from multiple dimensions
- Separating platform-specific code from business logic
- When both abstraction and implementation need independent extension
- Cross-platform development

## Implementation

### Basic Bridge

```php
interface Renderer
{
    public function renderCircle(float $x, float $y, float $radius): string;
    public function renderRectangle(float $x, float $y, float $width, float $height): string;
}

class SVGRenderer implements Renderer
{
    public function renderCircle(float $x, float $y, float $radius): string
    {
        return "<circle cx='{$x}' cy='{$y}' r='{$radius}'/>";
    }

    public function renderRectangle(float $x, float $y, float $width, float $height): string
    {
        return "<rect x='{$x}' y='{$y}' width='{$width}' height='{$height}'/>";
    }
}

class CanvasRenderer implements Renderer
{
    public function renderCircle(float $x, float $y, float $radius): string
    {
        return "Canvas Circle at ({$x}, {$y}) radius {$radius}";
    }

    public function renderRectangle(float $x, float $y, float $width, float $height): string
    {
        return "Canvas Rect at ({$x}, {$y}) {$width}x{$height}";
    }
}

abstract class Shape
{
    public function __construct(protected Renderer $renderer) {}

    abstract public function draw(): string;
}

class Circle extends Shape
{
    public function __construct(
        Renderer $renderer,
        private float $x,
        private float $y,
        private float $radius
    ) {
        parent::__construct($renderer);
    }

    public function draw(): string
    {
        return $this->renderer->renderCircle($this->x, $this->y, $this->radius);
    }
}

class Rectangle extends Shape
{
    public function __construct(
        Renderer $renderer,
        private float $x,
        private float $y,
        private float $width,
        private float $height
    ) {
        parent::__construct($renderer);
    }

    public function draw(): string
    {
        return $this->renderer->renderRectangle($this->x, $this->y, $this->width, $this->height);
    }
}
```

### Platform Bridge

```php
interface Platform
{
    public function drawLine(int $x1, int $y1, int $x2, int $y2): string;
    public function drawText(int $x, int $y, string $text): string;
}

class WindowsPlatform implements Platform
{
    public function drawLine(int $x1, int $y1, int $x2, int $y2): string
    {
        return "Windows: Line from ({$x1},{$y1}) to ({$x2},{$y2})";
    }

    public function drawText(int $x, int $y, string $text): string
    {
        return "Windows: Text '{$text}' at ({$x},{$y})";
    }
}

class MacOSPlatform implements Platform
{
    public function drawLine(int $x1, int $y1, int $x2, int $y2): string
    {
        return "MacOS: Line from ({$x1},{$y1}) to ({$x2},{$y2})";
    }

    public function drawText(int $x, int $y, string $text): string
    {
        return "MacOS: Text '{$text}' at ({$x},{$y})";
    }
}

abstract class Graphic
{
    public function __construct(protected Platform $platform) {}

    abstract public function draw(): string;
}

class LineGraphic extends Graphic
{
    public function __construct(
        Platform $platform,
        private int $x1,
        private int $y1,
        private int $x2,
        private int $y2
    ) {
        parent::__construct($platform);
    }

    public function draw(): string
    {
        return $this->platform->drawLine($this->x1, $this->y1, $this->x2, $this->y2);
    }
}
```

## Best Practices

- Define interfaces for both abstraction and implementation
- Document the relationship between dimensions
- Use constructor injection for implementations
- Consider using dependency injection containers
- Keep abstractions stable; vary implementations freely

## Interview Questions

1. What is the difference between bridge and adapter patterns?
2. When should you use interfaces vs abstract classes?
3. How does the bridge pattern reduce code duplication?
4. Can you combine bridge with factory pattern?
5. How do you test code using the bridge pattern?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
