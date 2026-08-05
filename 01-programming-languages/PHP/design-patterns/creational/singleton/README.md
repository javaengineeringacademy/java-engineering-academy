# Singleton Pattern in PHP

The Singleton pattern ensures a class has only one instance and provides a global point of access. In PHP, this is implemented using private constructors and static methods.

## When to Use

- Database connection pools
- Configuration managers
- Logging instances
- Cache managers
- Session handlers

## Implementation

### Basic Singleton

```php
class Database
{
    private static ?Database $instance = null;
    private string $connection;

    private function __construct()
    {
        $this->connection = "Connected to database";
    }

    public static function getInstance(): Database
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }

    public function query(string $sql): string
    {
        return "Executing: {$sql}";
    }

    private function __clone() {}

    public function __wakeup()
    {
        throw new \Exception("Cannot unserialize singleton");
    }
}

// Usage
$db = Database::getInstance();
echo $db->query("SELECT * FROM users");
```

### Thread-Safe Singleton

```php
class Config
{
    private static ?Config $instance = null;
    private static bool $initialized = false;
    private array $settings = [];

    private function __construct() {}

    public static function getInstance(): Config
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }

    public function set(string $key, mixed $value): void
    {
        $this->settings[$key] = $value;
    }

    public function get(string $key, mixed $default = null): mixed
    {
        return $this->settings[$key] ?? $default;
    }
}
```

### Singleton with Lazy Initialization

```php
class Logger
{
    private static ?Logger $instance = null;
    private ?\PDO $pdo = null;

    private function __construct() {}

    public static function getInstance(): Logger
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }

    private function getConnection(): \PDO
    {
        if ($this->pdo === null) {
            $this->pdo = new \PDO('sqlite::memory:');
        }
        return $this->pdo;
    }

    public function log(string $message): void
    {
        echo "LOG: {$message}\n";
    }
}
```

## Best Practices

- Use `private` constructor to prevent external instantiation
- Implement `__clone` and `__wakeup` to prevent bypass
- Document thread-safety guarantees
- Consider dependency injection for testability
- Use `?Type` nullable syntax for instance initialization

## Interview Questions

1. Why is the singleton pattern controversial in PHP?
2. How do you test code that depends on a singleton?
3. What is the difference between a singleton and a static class?
4. How do you prevent singleton serialization issues?
5. When should you use dependency injection instead of a singleton?

## References

- [PHP Singleton Pattern](https://www.php.net/)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
