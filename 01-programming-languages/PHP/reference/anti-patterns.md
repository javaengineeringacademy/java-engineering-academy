# PHP Anti-Patterns

## SQL Injection
```php
// Bad
$query = "SELECT * FROM users WHERE id = $id";

// Good
$stmt = $pdo->prepare("SELECT * FROM users WHERE id = ?");
$stmt->execute([$id]);
```

## XSS Vulnerability
```php
// Bad
echo $_GET['name'];

// Good
echo htmlspecialchars($_GET['name'], ENT_QUOTES);
```

## Error Suppression
```php
// Bad
$file = @fopen("file.txt", "r");

// Good
$file = fopen("file.txt", "r") or die("Unable to open file");
```

## Loose Comparison
```php
// Bad
if ($var == null) { }

// Good
if ($var === null) { }
```

## God Class
```php
// Bad: Class with 1000+ lines

// Good: Split into multiple focused classes
```
