# Visitor Pattern in PHP

The Visitor pattern represents an operation to be performed on elements of an object structure. In PHP, this is implemented using interfaces and method dispatch.

## When to Use

- Operations over heterogeneous data structures
- AST traversal and compilation
- Serialization of complex structures
- Adding operations without modifying element classes
- File system traversal

## Implementation

### Interface-Based Visitor

```php
interface Visitor
{
    public function visitNumber(Number $node): float;
    public function visitAdd(Add $node): float;
    public function visitMultiply(Multiply $node): float;
}

interface Expression
{
    public function accept(Visitor $visitor): float;
}

class Number implements Expression
{
    public function __construct(public readonly float $value) {}

    public function accept(Visitor $visitor): float
    {
        return $visitor->visitNumber($this);
    }
}

class Add implements Expression
{
    public function __construct(
        public readonly Expression $left,
        public readonly Expression $right
    ) {}

    public function accept(Visitor $visitor): float
    {
        return $visitor->visitAdd($this);
    }
}

class Multiply implements Expression
{
    public function __construct(
        public readonly Expression $left,
        public readonly Expression $right
    ) {}

    public function accept(Visitor $visitor): float
    {
        return $visitor->visitMultiply($this);
    }
}

class Evaluator implements Visitor
{
    public function visitNumber(Number $node): float { return $node->value; }
    public function visitAdd(Add $node): float
    {
        return $node->left->accept($this) + $node->right->accept($this);
    }
    public function visitMultiply(Multiply $node): float
    {
        return $node->left->accept($this) * $node->right->accept($this);
    }
}

class Printer implements Visitor
{
    public function visitNumber(Number $node): float
    {
        echo $node->value;
        return $node->value;
    }
    public function visitAdd(Add $node): float
    {
        echo "(";
        $node->left->accept($this);
        echo " + ";
        $node->right->accept($this);
        echo ")";
        return 0;
    }
    public function visitMultiply(Multiply $node): float
    {
        echo "(";
        $node->left->accept($this);
        echo " * ";
        $node->right->accept($this);
        echo ")";
        return 0;
    }
}
```

### Shape Visitor

```php
interface ShapeVisitor
{
    public function visitCircle(Circle $circle): string;
    public function visitRectangle(Rectangle $rectangle): string;
}

interface Shape
{
    public function accept(ShapeVisitor $visitor): string;
}

class Circle implements Shape
{
    public function __construct(public readonly float $radius) {}

    public function accept(ShapeVisitor $visitor): string
    {
        return $visitor->visitCircle($this);
    }
}

class Rectangle implements Shape
{
    public function __construct(
        public readonly float $width,
        public readonly float $height
    ) {}

    public function accept(ShapeVisitor $visitor): string
    {
        return $visitor->visitRectangle($this);
    }
}

class AreaCalculator implements ShapeVisitor
{
    public function visitCircle(Circle $circle): string
    {
        return "Circle area: " . (M_PI * $circle->radius ** 2);
    }

    public function visitRectangle(Rectangle $rectangle): string
    {
        return "Rectangle area: " . ($rectangle->width * $rectangle->height);
    }
}
```

### Generic Visitor

```php
class CompositeVisitor
{
    private array $visitors = [];

    public function addVisitor(string $type, callable $visitor): void
    {
        $this->visitors[$type] = $visitor;
    }

    public function visit(object $element): mixed
    {
        $type = get_class($element);
        if (isset($this->visitors[$type])) {
            return $this->visitors[$type]($element);
        }
        throw new \InvalidArgumentException("No visitor for type: {$type}");
    }
}
```

## Best Practices

- Use interfaces for visitor contracts
- Document which operations each visitor performs
- Use `get_class` for type-based dispatch
- Consider using `match` for enum-based visitors
- Implement double dispatch for extensible hierarchies

## Interview Questions

1. How does PHP's type system benefit the visitor pattern?
2. What is double dispatch and why is it needed?
3. How do you add a new operation without modifying existing visitors?
4. How do you handle cyclic structures in visitors?
5. What are the performance implications of the visitor pattern?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
