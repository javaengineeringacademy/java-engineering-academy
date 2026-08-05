# PHP Core Concepts

## Variables

Variables in PHP are prefixed with `$` and dynamically typed.

```php
$name = "Alice";        // string
$age = 30;              // integer
$price = 19.99;         // float
$isActive = true;       // boolean
$items = [1, 2, 3];    // array
```

Type juggling occurs automatically. Use `===` for strict comparison (type and value).

## Arrays

PHP arrays are ordered maps that function as lists, dictionaries, and stacks.

```php
$fruits = ["apple", "banana", "cherry"];
$person = ["name" => "Alice", "age" => 30];

array_push($fruits, "date");
unset($fruits[1]);
$count = count($fruits);
```

Functions: `array_map`, `array_filter`, `array_reduce`, `array_merge`, `array_keys`, `array_values`.

## Object-Oriented Programming

PHP supports classes, interfaces, abstract classes, and inheritance.

```php
class User {
    public string $name;
    protected int $age;

    public function __construct(string $name, int $age) {
        $this->name = $name;
        $this->age = $age;
    }

    public function greet(): string {
        return "Hello, {$this->name}";
    }
}
```

Visibility keywords: `public`, `protected`, `private`. Static methods and properties belong to the class, not instances.

## Traits

Traits provide reusable code without inheritance hierarchies.

```php
trait Loggable {
    public function log(string $message): void {
        echo date('Y-m-d H:i:s') . " - $message\n";
    }
}

class Order {
    use Loggable;

    public function process(): void {
        $this->log("Order processed");
    }
}
```

Traits can have constants, abstract methods, and properties. Multiple traits can be used in a single class.

## Namespaces

Namespaces organize code and prevent naming conflicts.

```php
namespace App\Models;

use App\Database\Connection;

class User {
    private Connection $db;

    public function __construct(Connection $db) {
        $this->db = $db;
    }
}
```

PSR-4 autoloading maps namespaces to directory structures automatically.

## Type System

PHP 7+ introduced scalar type declarations and return types.

```php
function add(int $a, int $b): int {
    return $a + $b;
}

function process(?string $data): string {
    return $data ?? "default";
}
```

Types: `int`, `float`, `string`, `bool`, `array`, `object`, `callable`, `iterable`, `?type` (nullable), `mixed`.

## Error Handling

PHP uses exceptions and the older error system.

```php
try {
    $result = riskyOperation();
} catch (RuntimeException $e) {
    error_log($e->getMessage());
} finally {
    cleanup();
}
```

Custom exceptions extend `Exception`. The `@` operator suppresses errors (avoid in production).
