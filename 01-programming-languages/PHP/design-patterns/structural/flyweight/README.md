# Flyweight Pattern in PHP

The Flyweight pattern minimizes memory usage by sharing data across similar objects. In PHP, this is implemented using caching and object pools.

## When to Use

- Large numbers of similar objects
- Memory-constrained environments
- Text editors with character objects
- Game objects with shared textures
- String deduplication

## Implementation

### String Interning

```php
class StringInterner
{
    private array $pool = [];

    public function intern(string $s): string
    {
        if (!isset($this->pool[$s])) {
            $this->pool[$s] = $s;
        }
        return $this->pool[$s];
    }
}

// Usage
$interner = new StringInterner();
$s1 = $interner->intern("hello");
$s2 = $interner->intern("hello");
echo $s1 === $s2 ? "Same" : "Different";
```

### Flyweight Factory

```php
class FlyweightData
{
    public function __construct(public readonly string $sharedState) {}
}

class FlyweightFactory
{
    private array $pool = [];

    public function getData(string $state): FlyweightData
    {
        if (!isset($this->pool[$state])) {
            $this->pool[$state] = new FlyweightData($state);
        }
        return $this->pool[$state];
    }
}

class Flyweight
{
    public function __construct(
        public readonly array $uniqueState,
        public readonly FlyweightData $data
    ) {}
}
```

### Character Flyweight

```php
class CharacterGlyph
{
    public function __construct(
        public readonly string $font,
        public readonly int $size
    ) {}
}

class TextEditor
{
    private array $glyphCache = [];
    private array $characters = [];

    public function insertChar(string $ch, string $font, int $size, array $pos): void
    {
        $key = "{$font}:{$size}";
        if (!isset($this->glyphCache[$key])) {
            $this->glyphCache[$key] = new CharacterGlyph($font, $size);
        }
        $this->characters[] = [
            'char' => $ch,
            'glyph' => $this->glyphCache[$key],
            'position' => $pos
        ];
    }

    public function getGlyphCount(): int
    {
        return count($this->glyphCache);
    }
}
```

### Pool Pattern

```php
class ObjectPool
{
    private array $available = [];
    private int $size;

    public function __construct(private callable $factory, int $size = 10)
    {
        $this->size = $size;
        for ($i = 0; $i < $size; $i++) {
            $this->available[] = ($this->factory)();
        }
    }

    public function acquire(): object
    {
        if (empty($this->available)) {
            return ($this->factory)();
        }
        return array_pop($this->available);
    }

    public function release(object $obj): void
    {
        $this->available[] = $obj;
    }
}
```

## Best Practices

- Use arrays for fast key-based caching
- Implement `__destruct` for cleanup if needed
- Monitor memory usage with flyweight implementations
- Document shared state boundaries
- Consider using weak references for automatic cleanup

## Interview Questions

1. What is the difference between flyweight and prototype patterns?
2. When does the flyweight pattern become counterproductive?
3. How do you handle flyweight cleanup for unused entries?
4. What are the memory implications of flyweight sharing?
5. How do you implement thread-safe flyweights in PHP?

## References

- [PHP Memory Management](https://www.php.net/memory)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
