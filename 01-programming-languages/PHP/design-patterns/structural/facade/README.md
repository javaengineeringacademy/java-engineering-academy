# Facade Pattern in PHP

The Facade pattern provides a simplified interface to a complex subsystem. In PHP, this is implemented as a class that wraps multiple subsystems behind a clean API.

## When to Use

- Simplifying complex library APIs
- Providing unified interface to subsystems
- Reducing coupling between client code and subsystems
- Creating layer architectures

## Implementation

### Basic Facade

```php
class CPU
{
    public function freeze(): void { echo "CPU: Freezing\n"; }
    public function jump(int $address): void { echo "CPU: Jumping to {$address}\n"; }
    public function execute(): void { echo "CPU: Executing\n"; }
}

class Memory
{
    public function load(int $address, string $data): void
    {
        echo "Memory: Loading {$data} at {$address}\n";
    }
}

class HardDrive
{
    public function read(int $sector, int $size): string
    {
        echo "HardDrive: Reading {$size} bytes from sector {$sector}\n";
        return "boot_data";
    }
}

class ComputerFacade
{
    private CPU $cpu;
    private Memory $memory;
    private HardDrive $hardDrive;

    public function __construct()
    {
        $this->cpu = new CPU();
        $this->memory = new Memory();
        $this->hardDrive = new HardDrive();
    }

    public function start(): void
    {
        $this->cpu->freeze();
        $data = $this->hardDrive->read(0, 1024);
        $this->memory->load(0, $data);
        $this->cpu->jump(0);
        $this->cpu->execute();
    }
}
```

### Database Facade

```php
class DatabaseFacade
{
    private \PDO $pdo;

    public function __construct(string $dsn, string $user = '', string $pass = '')
    {
        $this->pdo = new \PDO($dsn, $user, $pass);
    }

    public function query(string $sql): array
    {
        $stmt = $this->pdo->query($sql);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public function insert(string $table, array $data): bool
    {
        $columns = implode(', ', array_keys($data));
        $placeholders = implode(', ', array_fill(0, count($data), '?'));
        $sql = "INSERT INTO {$table} ({$columns}) VALUES ({$placeholders})";
        $stmt = $this->pdo->prepare($sql);
        return $stmt->execute(array_values($data));
    }
}
```

### Service Facade

```php
class UserServiceFacade
{
    public function __construct(
        private UserRepository $users,
        private EmailService $email,
        private LoggerService $logger
    ) {}

    public function register(string $name, string $email): array
    {
        $user = $this->users->create(['name' => $name, 'email' => $email]);
        $this->email->sendWelcome($email, $name);
        $this->logger->info("User registered: {$name}");
        return $user;
    }
}
```

## Best Practices

- Keep the facade lightweight; delegate to subsystems
- Name facade methods to reflect operations
- Allow direct subsystem access for advanced use cases
- Document which subsystems the facade coordinates
- Use dependency injection for subsystem dependencies

## Interview Questions

1. What is the difference between a facade and an adapter?
2. When should you expose subsystem internals?
3. How do you handle facade method failures?
4. Can a facade be used as a decorator?
5. How do you test code that depends on a facade?

## References

- [PHP Classes](https://www.php.net/language.oop5.basic)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
