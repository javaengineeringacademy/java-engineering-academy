# PHP Interfaces

## Overview
Interfaces define contracts that classes must implement.

## Basic Interface
```php
interface Drawable {
    public function draw(): void;
}
```

## Implementing Interface
```php
class Circle implements Drawable {
    public function draw(): void {
        echo "Drawing circle";
    }
}
```

## Multiple Interfaces
```php
class MyClass implements InterfaceA, InterfaceB {
    // must implement all methods
}
```

## Resources
- [PHP Interfaces](https://www.php.net/manual/en/language.oop5.interfaces.php)
