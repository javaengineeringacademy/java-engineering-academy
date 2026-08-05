# Proxy Pattern in PHP

The Proxy pattern provides a surrogate for another object to control access. In PHP, this is implemented using classes that implement the same interface as the real object.

## When to Use

- Lazy initialization
- Access control and permissions
- Logging and monitoring
- Remote object access
- Caching

## Implementation

### Basic Proxy

```php
interface Database
{
    public function query(string $sql): array;
}

class RealDatabase implements Database
{
    public function __construct(private string $connection) {}

    public function query(string $sql): array
    {
        echo "Executing on {$this->connection}: {$sql}\n";
        return [['id' => 1, 'name' => 'result']];
    }
}

class DatabaseProxy implements Database
{
    private ?RealDatabase $realDb = null;

    public function __construct(private string $connection) {}

    public function query(string $sql): array
    {
        if ($this->realDb === null) {
            echo "Connecting to {$this->connection}\n";
            $this->realDb = new RealDatabase($this->connection);
        }
        return $this->realDb->query($sql);
    }
}
```

### Access Control Proxy

```php
interface Service
{
    public function execute(string $command): string;
}

class RealService implements Service
{
    public function execute(string $command): string
    {
        return "Executed: {$command}";
    }
}

class AccessProxy implements Service
{
    public function __construct(
        private string $userRole,
        private Service $service
    ) {}

    public function execute(string $command): string
    {
        if ($this->userRole === 'admin') {
            return $this->service->execute($command);
        }
        return "Access denied";
    }
}
```

### Caching Proxy

```php
interface DataFetcher
{
    public function fetch(string $key): string;
}

class RealFetcher implements DataFetcher
{
    public function fetch(string $key): string
    {
        echo "Fetching from database: {$key}\n";
        return "data_for_{$key}";
    }
}

class CachingProxy implements DataFetcher
{
    private array $cache = [];

    public function __construct(private DataFetcher $fetcher) {}

    public function fetch(string $key): string
    {
        if (!isset($this->cache[$key])) {
            $this->cache[$key] = $this->fetcher->fetch($key);
        }
        return $this->cache[$key];
    }
}
```

### Virtual Proxy

```php
interface Image
{
    public function display(): string;
}

class RealImage implements Image
{
    public function __construct(private string $filename)
    {
        echo "Loading image: {$filename}\n";
    }

    public function display(): string
    {
        return "Displaying: {$this->filename}";
    }
}

class ImageProxy implements Image
{
    private ?RealImage $realImage = null;

    public function __construct(private string $filename) {}

    public function display(): string
    {
        if ($this->realImage === null) {
            $this->realImage = new RealImage($this->filename);
        }
        return $this->realImage->display();
    }
}
```

## Best Practices

- Keep the proxy interface identical to the real object
- Use lazy initialization for virtual proxies
- Document proxy behavior and additional logic
- Use dependency injection for proxy configuration
- Consider using traits for common proxy behavior

## Interview Questions

1. What are the different types of proxies?
2. How does a proxy differ from a decorator in PHP?
3. When would you use a virtual proxy vs a protection proxy?
4. How do you implement a transparent proxy?
5. How do proxies interact with PHP's garbage collection?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
