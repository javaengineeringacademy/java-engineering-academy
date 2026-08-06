# PHP Functions

## Overview
PHP functions support default parameters, type hints, and return types.

## Function Definition
```php
function greet(string $name): string {
    return "Hello, $name";
}
```

## Default Parameters
```php
function add(int $a, int $b = 0): int {
    return $a + $b;
}
```

## Nullable Types
```php
function find(int $id): ?User {
    return null;
}
```

## Variadic Functions
```php
function sum(int ...$numbers): int {
    return array_sum($numbers);
}
```

## Resources
- [PHP Functions](https://www.php.net/manual/en/language.functions.php)
