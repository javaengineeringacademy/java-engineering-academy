# PHP Classes

## Overview
PHP classes support constructors, visibility, and properties.

## Basic Class
```php
class Person {
    public string $name;
    public int $age;
    
    public function __construct(string $name, int $age) {
        $this->name = $name;
        $this->age = $age;
    }
}
```

## Visibility
- `public`: Accessible from anywhere
- `protected`: Accessible within class and subclasses
- `private`: Accessible only within class

## Constructor Promotion (PHP 8)
```php
class Person {
    public function __construct(
        public string $name,
        public int $age
    ) {}
}
```

## Resources
- [PHP Classes](https://www.php.net/manual/en/language.oop5.php)
