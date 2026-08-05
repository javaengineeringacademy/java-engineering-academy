# PHP Pitfalls

## Type Juggling

PHP performs automatic type conversion which can cause unexpected behavior.

```php
// Loose comparison pitfalls
"0" == false    // true
"" == false     // true
"0" == ""       // false
null == false   // true
[] == false     // true

// Use strict comparison
"0" === false   // false
"" === false    // false
null === false  // false
```

Always use `===` and `!==` for comparisons.

## Array Functions Inconsistency

PHP array functions have inconsistent parameter ordering.

```php
// Inconsistent parameter order
array_map($callback, $array);        // callback first
array_filter($array, $callback);     // array first
array_reduce($array, $callback);     // array first

// Use consistent approach with arrow functions
$result = array_map(fn($x) => $x * 2, $array);
$result = array_filter($array, fn($x) => $x > 0);
```

## Silent Failures

Many PHP functions return null or false silently instead of throwing exceptions.

```php
// File operations return false on failure
$file = fopen("nonexistent.txt", "r"); // false + warning

// Array access returns null for missing keys
$arr = ["key" => "value"];
echo $arr["missing"]; // null + notice

// Use proper error handling
$result = @file_get_contents($file); // @ suppresses errors (avoid)
```

## Scope Issues

Variables from outer scopes are not automatically available in functions.

```php
$message = "hello";

function greet() {
    echo $message; // Undefined variable error
}

// Solutions: use global keyword or pass as parameter
function greetGlobal() {
    global $message;
    echo $message;
}

function greetParam(string $msg) {
    echo $msg;
}
```

## Array-to-String Conversion

Arrays converted to strings produce unexpected results.

```php
$arr = [1, 2, 3];
echo $arr;           // "Array" (notice)
echo implode("", $arr); // "123"

$single = ["key" => "value"];
echo $single;        // "Array" (notice)
```

## Null Coalescing Gotchas

```php
$data = ["name" => null];

// ?? returns the alternative if value is null OR not set
echo $data["name"] ?? "default";  // "default"

// isset returns false for null values
echo isset($data["name"]) ? $data["name"] : "default"; // "default"

// Both work similarly, but ?? is cleaner
```

## foreach Reference Variable

```php
$arr = [1, 2, 3];

// Reference variable retains value
foreach ($arr as &$value) {
    $value *= 2;
}
unset($value); // Important to unset reference

// Without unsetting, last value persists
echo $value; // 6 (still references last element)
```

## String Encoding Issues

```php
// Multibyte string functions
strlen("hello")           // 5
mb_strlen("hello")       // 5
mb_strlen("你好")         // 2

// Always use mb_* functions for UTF-8
mb_strtolower("HELLO");
mb_substr("hello", 0, 2);
```
