# Builder Pattern in PHP

The Builder pattern separates object construction from its representation. In PHP, this is implemented using fluent interfaces with method chaining.

## When to Use

- Complex objects with many optional fields
- Objects that require step-by-step construction
- Configuration objects with sensible defaults
- When construction order matters

## Implementation

### Basic Builder

```php
class Server
{
    public function __construct(
        public readonly string $host,
        public readonly int $port = 8080,
        public readonly int $maxConnections = 100,
        public readonly int $timeout = 30
    ) {}
}

class ServerBuilder
{
    private string $host;
    private int $port = 8080;
    private int $maxConnections = 100;
    private int $timeout = 30;

    public function __construct(string $host)
    {
        $this->host = $host;
    }

    public function port(int $port): self
    {
        $this->port = $port;
        return $this;
    }

    public function maxConnections(int $max): self
    {
        $this->maxConnections = $max;
        return $this;
    }

    public function timeout(int $seconds): self
    {
        $this->timeout = $seconds;
        return $this;
    }

    public function build(): Server
    {
        return new Server(
            $this->host,
            $this->port,
            $this->maxConnections,
            $this->timeout
        );
    }
}

// Usage
$server = (new ServerBuilder('localhost'))
    ->port(3000)
    ->maxConnections(200)
    ->timeout(60)
    ->build();
```

### Query Builder

```php
class QueryBuilder
{
    private string $table;
    private array $conditions = [];
    private ?string $orderBy = null;
    private ?int $limit = null;

    public function __construct(string $table)
    {
        $this->table = $table;
    }

    public function where(string $condition): self
    {
        $this->conditions[] = $condition;
        return $this;
    }

    public function orderBy(string $column): self
    {
        $this->orderBy = $column;
        return $this;
    }

    public function limit(int $limit): self
    {
        $this->limit = $limit;
        return $this;
    }

    public function build(): string
    {
        $sql = "SELECT * FROM {$this->table}";

        if (!empty($this->conditions)) {
            $sql .= " WHERE " . implode(" AND ", $this->conditions);
        }

        if ($this->orderBy !== null) {
            $sql .= " ORDER BY {$this->orderBy}";
        }

        if ($this->limit !== null) {
            $sql .= " LIMIT {$this->limit}";
        }

        return $sql;
    }
}

// Usage
$query = (new QueryBuilder('users'))
    ->where('age > 25')
    ->where('active = 1')
    ->orderBy('name')
    ->limit(10)
    ->build();
```

### Immutable Builder

```php
final class ImmutableUser
{
    private function __construct(
        public readonly string $name,
        public readonly string $email,
        public readonly int $age
    ) {}

    public static function builder(): UserBuilder
    {
        return new UserBuilder();
    }
}

class UserBuilder
{
    private string $name = '';
    private string $email = '';
    private int $age = 0;

    public function name(string $name): self
    {
        $this->name = $name;
        return $this;
    }

    public function email(string $email): self
    {
        $this->email = $email;
        return $this;
    }

    public function age(int $age): self
    {
        $this->age = $age;
        return $this;
    }

    public function build(): ImmutableUser
    {
        return new ImmutableUser($this->name, $this->email, $this->age);
    }
}
```

## Best Practices

- Use fluent interfaces with method chaining for readability
- Use readonly properties for immutable builders
- Implement validation in the `build()` method
- Document default values for all optional parameters
- Consider using named constructors for simple cases

## Interview Questions

1. What is the difference between a builder and a factory pattern?
2. How do you handle builder validation errors?
3. What are the advantages of builders over telescoping constructors?
4. How do you implement an immutable builder in PHP?
5. When should you use the builder pattern vs default parameters?

## References

- [PHP Named Arguments](https://www.php.net/language.oop5.basic)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
