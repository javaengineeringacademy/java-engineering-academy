<?php
// PHP Closures

// Basic closure
$greet = function(string $name) {
    return "Hello, $name";
};
echo $greet("Alice") . "\n";

// Closure with use
$factor = 2;
$multiply = function($x) use ($factor) {
    return $x * $factor;
};
echo "multiply(5): " . $multiply(5) . "\n";

// Closure by reference
$counter = 0;
$increment = function() use (&$counter) {
    $counter++;
};
$increment();
$increment();
echo "counter: $counter\n";

// Higher-order functions
$numbers = [1, 2, 3, 4, 5];
$sum = array_reduce($numbers, function($carry, $item) {
    return $carry + $item;
}, 0);
echo "sum: $sum\n";

// Closure as parameter
function apply($value, callable $transform) {
    return $transform($value);
}
$result = apply(5, function($x) { return $x * 2; });
echo "apply: $result\n";
