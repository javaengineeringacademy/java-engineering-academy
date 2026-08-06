# PHP Closures

## Overview
Closures are anonymous functions that can capture variables from their enclosing scope.

## Basic Closure
```php
$greet = function(string $name) {
    return "Hello, $name";
};
echo $greet("Alice");
```

## Closures with `use`
```php
$factor = 2;
$multiply = function($x) use ($factor) {
    return $x * $factor;
};
```

## Binding
```php
$closure = function() {
    return $this->name;
};
$bound = $closure->bindTo($person);
```

## Resources
- [PHP Closures](https://www.php.net/manual/en/class.closure.php)
