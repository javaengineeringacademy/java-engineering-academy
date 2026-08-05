# PHP Cheat Sheet

## Variables

```php
$name = "Alice";         // string
$age = 30;               // integer
$price = 19.99;          // float
$isActive = true;        // boolean
$items = [1, 2, 3];     // array
$null = null;            // null
```

## Arrays

```php
$arr = [1, 2, 3];
$arr = ["key" => "value", "key2" => "value2"];
array_push($arr, 4);
count($arr);
array_map(fn($x) => $x * 2, $arr);
array_filter($arr, fn($x) => $x > 0);
array_merge($arr1, $arr2);
```

## Strings

```php
$str = "Hello World";
strlen($str);
substr($str, 0, 5);
strpos($str, "World");
str_replace("World", "PHP", $str);
strtoupper($str);
strtolower($str);
explode(",", $csv);
implode(",", $arr);
```

## Functions

```php
function add(int $a, int $b): int {
    return $a + $b;
}

$add = fn($a, $b) => $a + $b;

function divide(float $a, float $b): float {
    if ($b == 0) throw new InvalidArgumentException("Division by zero");
    return $a / $b;
}
```

## Classes

```php
class User {
    public string $name;
    private int $age;

    public function __construct(string $name, int $age) {
        $this->name = $name;
        $this->age = $age;
    }

    public function greet(): string {
        return "Hello, {$this->name}";
    }
}

$user = new User("Alice", 30);
echo $user->greet();
```

## Error Handling

```php
try {
    riskyOperation();
} catch (RuntimeException $e) {
    error_log($e->getMessage());
} finally {
    cleanup();
}
```

## Database (PDO)

```php
$pdo = new PDO('mysql:host=localhost;dbname=test', $user, $pass);
$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$stmt = $pdo->prepare("SELECT * FROM users WHERE id = :id");
$stmt->execute(['id' => $id]);
$user = $stmt->fetch(PDO::FETCH_ASSOC);
```

## Useful Functions

```php
isset($var);        // Check if variable is set and not null
empty($var);        // Check if variable is empty
unset($var);        // Unset a variable
var_dump($var);     // Debug output with types
print_r($var);      // Readable debug output
json_encode($data); // Convert to JSON
json_decode($json); // Convert from JSON
filter_var($email, FILTER_VALIDATE_EMAIL);
date('Y-m-d H:i:s');
```

## Common Patterns

```php
// Null coalescing
$value = $data['key'] ?? 'default';

// Null safe operator (PHP 8+)
$country = $user?->address?->country;

// Match expression (PHP 8+)
$result = match($status) {
    'active' => 'Active',
    'inactive' => 'Inactive',
    default => 'Unknown',
};

// Named arguments (PHP 8+)
array_slice(array: $arr, offset: 1, length: 3);
```
