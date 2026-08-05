# Template Method Pattern in PHP

The Template Method pattern defines the skeleton of an algorithm in a base class, allowing subclasses to override specific steps. In PHP, this is implemented using abstract classes.

## When to Use

- Algorithms with invariant structure but variant steps
- Framework design with customizable hooks
- Code reuse across similar operations
- Building parsers or processors
- Reducing code duplication

## Implementation

### Basic Template Method

```php
abstract class DataProcessor
{
    abstract protected function readData(): array;
    abstract protected function processItem(string $item): string;
    abstract protected function writeData(array $data): void;

    public function run(): void
    {
        $raw = $this->readData();
        $processed = array_map([$this, 'processItem'], $raw);
        $this->writeData($processed);
    }
}

class CSVProcessor extends DataProcessor
{
    protected function readData(): array
    {
        return ['a,b', 'c,d'];
    }

    protected function processItem(string $item): string
    {
        return str_replace(',', ' | ', $item);
    }

    protected function writeData(array $data): void
    {
        foreach ($data as $item) {
            echo "CSV: {$item}\n";
        }
    }
}

class JSONProcessor extends DataProcessor
{
    protected function readData(): array
    {
        return ['{"a":1}', '{"b":2}'];
    }

    protected function processItem(string $item): string
    {
        return strtoupper($item);
    }

    protected function writeData(array $data): void
    {
        foreach ($data as $item) {
            echo "JSON: {$item}\n";
        }
    }
}
```

### Template with Hooks

```php
abstract class Game
{
    protected function initialize(): void
    {
        echo "Default initialization\n";
    }

    abstract protected function playTurn(): void;
    abstract protected function checkWin(): bool;

    protected function end(): void
    {
        echo "Default ending\n";
    }

    final public function play(): void
    {
        $this->initialize();
        do {
            $this->playTurn();
        } while (!$this->checkWin());
        $this->end();
    }
}

class Chess extends Game
{
    protected function playTurn(): void { echo "Chess turn\n"; }
    protected function checkWin(): bool { return false; }
}

class TicTacToe extends Game
{
    protected function initialize(): void { echo "TicTacToe initialized\n"; }
    protected function playTurn(): void { echo "TicTacToe turn\n"; }
    protected function checkWin(): bool { return true; }
}
```

### Builder Template

```php
abstract class Builder
{
    abstract protected function buildStep1(): void;
    abstract protected function buildStep2(): void;
    abstract protected function getResult(): mixed;

    final public function build(): mixed
    {
        $this->buildStep1();
        $this->buildStep2();
        return $this->getResult();
    }
}

class ServerBuilder extends Builder
{
    private array $config = [];

    protected function buildStep1(): void { $this->config['host'] = 'localhost'; }
    protected function buildStep2(): void { $this->config['port'] = 8080; }
    protected function getResult(): array { return $this->config; }
}
```

### Logger Template

```php
abstract class Logger
{
    protected function format(string $message): string
    {
        return "[" . date('Y-m-d H:i:s') . "] {$message}";
    }

    abstract protected function write(string $formatted): void;

    public function log(string $message): void
    {
        $this->write($this->format($message));
    }
}

class ConsoleLogger extends Logger
{
    protected function write(string $formatted): void
    {
        echo "{$formatted}\n";
    }
}

class FileLogger extends Logger
{
    public function __construct(private string $path) {}

    protected function write(string $formatted): void
    {
        echo "Writing to {$this->path}: {$formatted}\n";
    }
}
```

## Best Practices

- Use `final` on the template method to prevent overriding
- Use abstract methods for required steps
- Provide default implementations for optional hooks
- Document which methods are hooks vs required
- Use type declarations for all methods

## Interview Questions

1. What is the difference between template method and strategy pattern?
2. How do you prevent subclass override of the template method?
3. When should you use abstract methods vs default implementations?
4. How do you handle template method error handling?
5. Can template methods call other template methods?

## References

- [Abstract Classes](https://www.php.net/language.oop5.abstract)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
