<?php
// PHP Variables

$name = "Alice";
$age = 30;
$pi = 3.14;
$active = true;

echo "name: $name\n";
echo "age: $age\n";
echo "pi: $pi\n";
echo "active: " . ($active ? 'true' : 'false') . "\n";

// Type juggling
$var = "42";
$int = (int) $var;
echo "int: $int\n";

// Type checking
var_dump($name);
echo "type: " . gettype($name) . "\n";
echo "is_int: " . (is_int($age) ? 'true' : 'false') . "\n";

// Constants
define('MAX_SIZE', 100);
echo "MAX_SIZE: " . MAX_SIZE . "\n";

// Null
$null_var = null;
echo "null_var: " . var_export($null_var, true) . "\n";

// Arrays
$arr = [1, 2, 3];
echo "array: ";
print_r($arr);
