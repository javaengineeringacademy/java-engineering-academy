# Decorator Pattern in PHP

The Decorator pattern adds responsibilities to objects dynamically. In PHP, this is implemented using classes that implement the same interface as the wrapped object.

## When to Use

- Adding behavior without modifying original code
- Layering cross-cutting concerns (logging, caching)
- Composition over inheritance
- Runtime behavior modification

## Implementation

### Basic Decorator

```php
interface DataSource
{
    public function writeData(string $data): void;
    public function readData(): string;
}

class FileDataSource implements DataSource
{
    public function __construct(private string $filename) {}

    public function writeData(string $data): void
    {
        echo "Writing to {$this->filename}: {$data}\n";
    }

    public function readData(): string
    {
        return "Data from {$this->filename}";
    }
}

class EncryptionDecorator implements DataSource
{
    public function __construct(private DataSource $wrapped) {}

    public function writeData(string $data): void
    {
        $this->wrapped->writeData("ENCRYPTED({$data})");
    }

    public function readData(): string
    {
        $data = $this->wrapped->readData();
        return preg_replace('/^ENCRYPTED\((.*)\)$/', '$1', $data);
    }
}

class CompressionDecorator implements DataSource
{
    public function __construct(private DataSource $wrapped) {}

    public function writeData(string $data): void
    {
        $this->wrapped->writeData("COMPRESSED[{$data}]");
    }

    public function readData(): string
    {
        $data = $this->wrapped->readData();
        return preg_replace('/^COMPRESSED\[(.*)\]$/', '$1', $data);
    }
}
```

### Stacking Decorators

```php
$source = new FileDataSource('data.txt');
$decorated = new CompressionDecorator(
    new EncryptionDecorator($source)
);

$decorated->writeData("Hello, World!");
echo $decorated->readData();
```

### Logging Decorator

```php
interface Logger
{
    public function log(string $message): void;
}

class FileLogger implements Logger
{
    public function __construct(private string $path) {}

    public function log(string $message): void
    {
        echo "[FILE:{$this->path}] {$message}\n";
    }
}

class LoggingDecorator implements Logger
{
    public function __construct(private Logger $wrapped) {}

    public function log(string $message): void
    {
        $timestamp = date('Y-m-d H:i:s');
        $this->wrapped->log("[{$timestamp}] {$message}");
    }
}
```

### Middleware Decorator

```php
interface RequestHandler
{
    public function handle(string $request): string;
}

class BaseHandler implements RequestHandler
{
    public function handle(string $request): string
    {
        return "Response to: {$request}";
    }
}

class AuthMiddleware implements RequestHandler
{
    public function __construct(private RequestHandler $next) {}

    public function handle(string $request): string
    {
        echo "Auth check\n";
        return $this->next->handle($request);
    }
}

class CacheMiddleware implements RequestHandler
{
    public function __construct(private RequestHandler $next) {}

    public function handle(string $request): string
    {
        echo "Cache check\n";
        return $this->next->handle($request);
    }
}
```

## Best Practices

- Ensure decorators implement the same interface as the wrapped object
- Keep decorators single-responsibility
- Use composition over inheritance
- Document which decorators are applied
- Consider using traits for common decorator behavior

## Interview Questions

1. What is the difference between a decorator and a proxy?
2. How do you prevent decorators from being applied multiple times?
3. When should you use decorators vs inheritance?
4. How do you handle decorator ordering?
5. Can decorators modify the return type of methods?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
