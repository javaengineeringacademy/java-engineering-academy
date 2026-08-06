# PHP Strings

## Overview
PHP strings can be single-quoted, double-quoted, or heredoc.

## String Types
```php
$name = 'Alice';      // single-quoted
$name = "Alice";      // double-quoted
$name = <<<EOT
Alice
EOT;                 // heredoc
```

## String Functions
```php
strlen($str);
strtolower($str);
strtoupper($str);
substr($str, 0, 5);
strpos($str, "hello");
```

## Interpolation
```php
echo "Hello, $name";
echo "Hello, {$name}";
```

## Resources
- [PHP Strings](https://www.php.net/manual/en/language.types.string.php)
