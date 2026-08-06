# PHP Attributes

## Overview
PHP 8 introduced attributes as a native way to add metadata to code.

## Basic Attribute
```php
#[Attribute]
class Route {
    public function __construct(
        public string $path,
        public string $method = 'GET'
    ) {}
}
```

## Using Attribute
```php
#[Route(path: '/users', method: 'GET')]
public function getUsers(): void {
    // ...
}
```

## Resources
- [PHP Attributes](https://www.php.net/manual/en/language.attributes.php)
