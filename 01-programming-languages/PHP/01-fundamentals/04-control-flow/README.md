# PHP Control Flow

## Overview
PHP has standard control structures with some unique features.

## if/elseif/else
```php
if ($x > 0) {
    echo "positive";
} elseif ($x < 0) {
    echo "negative";
} else {
    echo "zero";
}
```

## switch
```php
switch ($day) {
    case "Monday":
        echo "Weekday";
        break;
    default:
        echo "Other";
}
```

## for Loop
```php
for ($i = 0; $i < 10; $i++) {
    echo $i;
}
```

## foreach
```php
foreach ($arr as $value) {
    echo $value;
}
```

## while/do-while
```php
while ($condition) { /* ... */ }
do { /* ... */ } while ($condition);
```

## match (PHP 8)
```php
$result = match($x) {
    1 => "one",
    2 => "two",
    default => "other",
};
```

## Resources
- [PHP Control Structures](https://www.php.net/manual/en/language.control-structures.php)
