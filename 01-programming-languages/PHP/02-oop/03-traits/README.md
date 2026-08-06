# PHP Traits

## Overview
Traits provide code reuse without inheritance.

## Basic Trait
```php
trait Logger {
    public function log(string $msg): void {
        echo "LOG: $msg\n";
    }
}
```

## Using Trait
```php
class Service {
    use Logger;
    
    public function doWork(): void {
        $this->log("working");
    }
}
```

## Trait Conflicts
```php
trait A {
    public function hello() { echo "A"; }
}

trait B {
    public function hello() { echo "B"; }
}

class C {
    use A, B {
        A::hello insteadof B;
    }
}
```

## Resources
- [PHP Traits](https://www.php.net/manual/en/language.oop5.traits.php)
