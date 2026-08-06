# PHP Type Hints

## Overview
PHP 8 introduced union types, intersection types, and other type features.

## Scalar Types
```php
function add(int $a, int $b): int {
    return $a + $b;
}
```

## Class Types
```php
function process(User $user): void {
    // ...
}
```

## Union Types (PHP 8)
```php
function process(int|float $value): string {
    return (string) $value;
}
```

## Intersection Types (PHP 8.1)
```php
function process(Countable&Iterator $collection): void {
    // ...
}
```

## Resources
- [PHP Type Declarations](https://www.php.net/manual/en/language.types.declarations.php)
