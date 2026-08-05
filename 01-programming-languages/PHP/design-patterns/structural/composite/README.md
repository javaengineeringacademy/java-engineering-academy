# Composite Pattern in PHP

The Composite pattern composes objects into tree structures and treats individual and composite objects uniformly. In PHP, this is implemented using interfaces for component types.

## When to Use

- Hierarchical data structures (file systems, UI, menus)
- Treating single and composite objects uniformly
- Tree traversal operations
- Recursive data structures

## Implementation

### File System Composite

```php
interface FilesystemItem
{
    public function getName(): string;
    public function getSize(): int;
    public function render(int $indent = 0): string;
}

class File implements FilesystemItem
{
    public function __construct(
        private string $name,
        private int $size
    ) {}

    public function getName(): string { return $this->name; }
    public function getSize(): int { return $this->size; }

    public function render(int $indent = 0): string
    {
        return str_repeat('  ', $indent) . $this->name . " ({$this->size} bytes)\n";
    }
}

class Directory implements FilesystemItem
{
    private array $children = [];

    public function __construct(private string $name) {}

    public function getName(): string { return $this->name; }

    public function getSize(): int
    {
        return array_reduce($this->children, fn($sum, $item) => $sum + $item->getSize(), 0);
    }

    public function add(FilesystemItem $item): void
    {
        $this->children[] = $item;
    }

    public function render(int $indent = 0): string
    {
        $result = str_repeat('  ', $indent) . $this->name . "/\n";
        foreach ($this->children as $child) {
            $result .= $child->render($indent + 1);
        }
        return $result;
    }
}
```

### Menu Composite

```php
interface MenuItemInterface
{
    public function getPrice(): float;
    public function render(): string;
}

class MenuItem implements MenuItemInterface
{
    public function __construct(
        private string $name,
        private float $price
    ) {}

    public function getPrice(): float { return $this->price; }

    public function render(): string
    {
        return "{$this->name}: \${$this->price}";
    }
}

class Menu implements MenuItemInterface
{
    private array $items = [];

    public function __construct(private string $name) {}

    public function getPrice(): float
    {
        return array_reduce($this->items, fn($sum, $item) => $sum + $item->getPrice(), 0);
    }

    public function add(MenuItemInterface $item): void
    {
        $this->items[] = $item;
    }

    public function render(): string
    {
        $result = "{$this->name}:\n";
        foreach ($this->items as $item) {
            $result .= "  - " . $item->render() . "\n";
        }
        return $result;
    }
}
```

### Expression Tree

```php
interface Expression
{
    public function evaluate(): float;
    public function toString(): string;
}

class Number implements Expression
{
    public function __construct(private float $value) {}

    public function evaluate(): float { return $this->value; }
    public function toString(): string { return (string) $this->value; }
}

class Add implements Expression
{
    public function __construct(
        private Expression $left,
        private Expression $right
    ) {}

    public function evaluate(): float
    {
        return $this->left->evaluate() + $this->right->evaluate();
    }

    public function toString(): string
    {
        return "({$this->left->toString()} + {$this->right->toString()})";
    }
}

class Multiply implements Expression
{
    public function __construct(
        private Expression $left,
        private Expression $right
    ) {}

    public function evaluate(): float
    {
        return $this->left->evaluate() * $this->right->evaluate();
    }

    public function toString(): string
    {
        return "({$this->left->toString()} * {$this->right->toString()})";
    }
}
```

## Best Practices

- Define a common interface for all component types
- Implement `add`, `remove`, and `getChild` methods on composites
- Document tree depth constraints
- Use type hints for component parameters
- Consider using iterators for tree traversal

## Interview Questions

1. What is the difference between composite and decorator patterns?
2. How do you handle leaf nodes in a composite?
3. How do you implement iteration over a composite tree?
4. How do you prevent adding children to leaf nodes?
5. When should you use composite vs a simple collection?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
