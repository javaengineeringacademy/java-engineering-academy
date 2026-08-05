# Abstract Factory Pattern in PHP

The Abstract Factory pattern provides an interface for creating families of related objects without specifying their concrete classes. In PHP, this is implemented using interfaces and concrete factory classes.

## When to Use

- Creating families of related objects
- Ensuring objects from the same family are used together
- Cross-platform UI components
- Database driver families
- Theme systems

## Implementation

### Basic Abstract Factory

```php
interface Button
{
    public function render(): string;
}

interface Checkbox
{
    public function render(): string;
}

class WindowsButton implements Button
{
    public function render(): string { return "Windows Button"; }
}

class WindowsCheckbox implements Checkbox
{
    public function render(): string { return "Windows Checkbox"; }
}

class MacOSButton implements Button
{
    public function render(): string { return "MacOS Button"; }
}

class MacOSCheckbox implements Checkbox
{
    public function render(): string { return "MacOS Checkbox"; }
}

interface GUIFactory
{
    public function createButton(): Button;
    public function createCheckbox(): Checkbox;
}

class WindowsFactory implements GUIFactory
{
    public function createButton(): Button { return new WindowsButton(); }
    public function createCheckbox(): Checkbox { return new WindowsCheckbox(); }
}

class MacOSFactory implements GUIFactory
{
    public function createButton(): Button { return new MacOSButton(); }
    public function createCheckbox(): Checkbox { return new MacOSCheckbox(); }
}
```

### Database Abstract Factory

```php
interface Connection
{
    public function connect(): string;
}

interface QueryBuilder
{
    public function select(string $table): string;
}

class MySQLConnection implements Connection
{
    public function connect(): string { return "Connected to MySQL"; }
}

class MySQLQueryBuilder implements QueryBuilder
{
    public function select(string $table): string
    {
        return "SELECT * FROM {$table}";
    }
}

class PostgreSQLConnection implements Connection
{
    public function connect(): string { return "Connected to PostgreSQL"; }
}

class PostgreSQLQueryBuilder implements QueryBuilder
{
    public function select(string $table): string
    {
        return "SELECT * FROM {$table} LIMIT ALL";
    }
}

interface DatabaseFactory
{
    public function createConnection(): Connection;
    public function createQueryBuilder(): QueryBuilder;
}

class MySQLFactory implements DatabaseFactory
{
    public function createConnection(): Connection { return new MySQLConnection(); }
    public function createQueryBuilder(): QueryBuilder { return new MySQLQueryBuilder(); }
}

class PostgreSQLFactory implements DatabaseFactory
{
    public function createConnection(): Connection { return new PostgreSQLConnection(); }
    public function createQueryBuilder(): QueryBuilder { return new PostgreSQLQueryBuilder(); }
}
```

### Factory Registry

```php
class FactoryRegistry
{
    private static array $factories = [];

    public static function register(string $name, callable $factory): void
    {
        self::$factories[$name] = $factory;
    }

    public static function get(string $name): object
    {
        if (!isset(self::$factories[$name])) {
            throw new \InvalidArgumentException("Unknown factory: {$name}");
        }
        return (self::$factories[$name])();
    }
}
```

## Best Practices

- Define interfaces for all product types
- Ensure factories produce compatible product families
- Document which products each factory creates
- Use dependency injection to supply factories
- Consider using enums for factory selection

## Interview Questions

1. What is the difference between Abstract Factory and Factory Method?
2. When should you use Abstract Factory vs Simple Factory?
3. How do you add a new product family without modifying existing code?
4. How do you handle factory configuration?
5. What are the drawbacks of the Abstract Factory pattern?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
