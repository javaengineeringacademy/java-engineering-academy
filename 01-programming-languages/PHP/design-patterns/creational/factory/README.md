# Factory Pattern in PHP

The Factory pattern creates objects without exposing instantiation logic. In PHP, this is implemented using interfaces, static methods, and named constructors.

## When to Use

- Creating objects based on runtime configuration
- Decoupling creation logic from usage
- Supporting multiple concrete types
- Plugin or driver architectures

## Implementation

### Basic Factory

```php
interface Animal
{
    public function speak(): string;
}

class Dog implements Animal
{
    public function speak(): string { return "Woof"; }
}

class Cat implements Animal
{
    public function speak(): string { return "Meow"; }
}

class AnimalFactory
{
    public static function create(string $type): Animal
    {
        return match ($type) {
            'dog' => new Dog(),
            'cat' => new Cat(),
            default => throw new \InvalidArgumentException("Unknown animal: {$type}"),
        };
    }
}

// Usage
$animal = AnimalFactory::create('dog');
echo $animal->speak();
```

### Parameterized Factory

```php
interface Shape
{
    public function area(): float;
}

class Circle implements Shape
{
    public function __construct(private float $radius) {}

    public function area(): float
    {
        return M_PI * $this->radius ** 2;
    }
}

class Rectangle implements Shape
{
    public function __construct(
        private float $width,
        private float $height
    ) {}

    public function area(): float
    {
        return $this->width * $this->height;
    }
}

class ShapeFactory
{
    public static function create(string $type, float ...$dims): Shape
    {
        return match ($type) {
            'circle' => new Circle($dims[0]),
            'rectangle' => new Rectangle($dims[0], $dims[1]),
            default => throw new \InvalidArgumentException("Unknown shape: {$type}"),
        };
    }
}
```

### Factory Method

```php
interface Logger
{
    public function log(string $message): void;
}

class ConsoleLogger implements Logger
{
    public function log(string $message): void
    {
        echo "[CONSOLE] {$message}\n";
    }
}

class FileLogger implements Logger
{
    public function __construct(private string $path) {}

    public function log(string $message): void
    {
        echo "[FILE:{$this->path}] {$message}\n";
    }
}

class LoggerFactory
{
    public static function create(string $type, string $path = ''): Logger
    {
        return match ($type) {
            'console' => new ConsoleLogger(),
            'file' => new FileLogger($path),
            default => throw new \InvalidArgumentException("Unknown logger: {$type}"),
        };
    }
}
```

## Best Practices

- Use interfaces for factory return types
- Use `match` expressions for type selection (PHP 8.0+)
- Document parameter requirements for each type
- Use named constructors for complex object creation
- Consider using enums for type selection

## Interview Questions

1. What is the difference between a factory and a builder pattern?
2. How do you extend a factory without modifying existing code?
3. When would you use a factory method vs a simple constructor?
4. How do you handle factory errors in PHP?
5. What are the benefits of using interfaces in factories?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
