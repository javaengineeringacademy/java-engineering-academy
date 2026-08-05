# Prototype Pattern in PHP

The Prototype pattern creates new objects by cloning existing instances. In PHP, this is implemented using the `clone` keyword and the `__clone` magic method.

## When to Use

- Creating copies of expensive-to-build objects
- Template-based object creation
- Preserving object state without re-initialization
- Avoiding complex construction logic

## Implementation

### Basic Clone

```php
class Document
{
    public string $title;
    public string $content;
    public array $metadata;

    public function __construct(string $title, string $content, array $metadata = [])
    {
        $this->title = $title;
        $this->content = $content;
        $this->metadata = $metadata;
    }

    public function __clone()
    {
        $this->metadata = clone (object) $this->metadata;
    }
}

// Usage
$template = new Document('Template', 'Default content', ['draft' => 'true']);
$doc1 = clone $template;
$doc2 = clone $template;
$doc2->title = 'Report';
```

### Deep Clone

```php
class DeepCopy
{
    public array $data;

    public function __construct(array $data)
    {
        $this->data = $data;
    }

    public function deepClone(): self
    {
        return unserialize(serialize($this));
    }
}

// Usage
$original = new DeepCopy(['key' => ['nested' => 'value']]);
$clone = $original->deepClone();
```

### Prototype Registry

```php
class PrototypeRegistry
{
    private array $prototypes = [];

    public function register(string $name, object $prototype): void
    {
        $this->prototypes[$name] = $prototype;
    }

    public function clone(string $name): ?object
    {
        if (!isset($this->prototypes[$name])) {
            return null;
        }
        return clone $this->prototypes[$name];
    }
}

// Usage
$registry = new PrototypeRegistry();
$registry->register('template', new Document('Template', 'Content'));
$clone = $registry->clone('template');
```

### Custom Clone

```php
class Config
{
    private array $settings;

    public function __construct(array $settings)
    {
        $this->settings = $settings;
    }

    public function __clone()
    {
        $this->settings = $this->settings;
    }

    public function withSetting(string $key, mixed $value): self
    {
        $clone = clone $this;
        $clone->settings[$key] = $value;
        return $clone;
    }
}

// Usage
$original = new Config(['theme' => 'dark', 'lang' => 'en']);
$modified = $original->withSetting('theme', 'light');
```

## Best Practices

- Implement `__clone` for custom cloning behavior
- Use `clone` for shallow copies
- Use `unserialize(serialize($obj))` for deep copies
- Document whether clone is shallow or deep
- Consider using immutable objects to avoid cloning issues

## Interview Questions

1. What is the difference between shallow and deep clone in PHP?
2. How do you handle cloning of objects with circular references?
3. What are the performance implications of cloning?
4. How do you implement cloning for objects with resources?
5. When should you use the prototype pattern vs the factory pattern?

## References

- [PHP Clone](https://www.php.net/language.oop5.basic)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
