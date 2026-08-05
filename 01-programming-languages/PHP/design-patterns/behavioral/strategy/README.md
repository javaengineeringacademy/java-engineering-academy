# Strategy Pattern in PHP

The Strategy pattern defines a family of algorithms and makes them interchangeable. In PHP, this is implemented using interfaces, callables, or closures.

## When to Use

- Multiple sorting or filtering algorithms
- Payment processing strategies
- Validation rules
- Compression algorithms
- Route planning algorithms

## Implementation

### Interface-Based Strategy

```php
interface CompressionStrategy
{
    public function compress(array $data): array;
}

class GzipCompression implements CompressionStrategy
{
    public function compress(array $data): array
    {
        echo "Compressing with Gzip\n";
        return $data;
    }
}

class Lz4Compression implements CompressionStrategy
{
    public function compress(array $data): array
    {
        echo "Compressing with LZ4\n";
        return $data;
    }
}

class FileProcessor
{
    public function __construct(private CompressionStrategy $strategy) {}

    public function process(array $data): array
    {
        return $this->strategy->compress($data);
    }
}
```

### Callable Strategy

```php
class Sorter
{
    public function __construct(
        private array $data,
        private callable $strategy
    ) {}

    public function sort(): array
    {
        $result = $this->data;
        usort($result, $this->strategy);
        return $result;
    }
}

// Usage
$sorter = new Sorter([5, 3, 1, 4, 2], fn($a, $b) => $a <=> $b);
echo implode(', ', $sorter->sort());
```

### Validation Strategy

```php
interface Validator
{
    public function validate(string $input): bool;
}

class EmailValidator implements Validator
{
    public function validate(string $input): bool
    {
        return filter_var($input, FILTER_VALIDATE_EMAIL) !== false;
    }
}

class PhoneValidator implements Validator
{
    public function validate(string $input): bool
    {
        return preg_match('/^\+?[0-9]+$/', $input) === 1;
    }
}

function validateAll(array $validators, string $input): bool
{
    return array_reduce($validators, fn($valid, $v) => $valid && $v->validate($input), true);
}
```

### Closure Strategy

```php
class Pipeline
{
    private array $steps = [];

    public function addStep(string $name, callable $step): self
    {
        $this->steps[$name] = $step;
        return $this;
    }

    public function execute(mixed $data): mixed
    {
        foreach ($this->steps as $step) {
            $data = $step($data);
        }
        return $data;
    }
}

// Usage
$pipeline = new Pipeline();
$pipeline
    ->addStep('trim', fn($s) => trim($s))
    ->addStep('lower', fn($s) => strtolower($s))
    ->addStep('slug', fn($s) => preg_replace('/\s+/', '-', $s));

echo $pipeline->execute("  Hello World  ");
```

## Best Practices

- Use interfaces for complex strategies with state
- Use callables/closures for simple, stateless strategies
- Document strategy selection criteria
- Use dependency injection for strategy selection
- Consider using enums for strategy type selection

## Interview Questions

1. What is the difference between strategy and command patterns?
2. When should you use interfaces vs callables for strategies?
3. How do you implement strategy selection at runtime?
4. How do you handle strategy state and configuration?
5. Can strategies be composed? How?

## References

- [PHP Closures](https://www.php.net/closure)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
