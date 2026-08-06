<?php
// PHP Functions

function greet(string $name): string {
    return "Hello, $name";
}

function add(int $a, int $b = 0): int {
    return $a + $b;
}

function sum(int ...$numbers): int {
    return array_sum($numbers);
}

function divide(float $a, float $b): float {
    if ($b == 0) {
        throw new InvalidArgumentException("Division by zero");
    }
    return $a / $b;
}

// Arrow functions (PHP 7.4+)
$square = fn($x) => $x * $x;
echo "square(5): " . $square(5) . "\n";

// Higher-order functions
$numbers = [1, 2, 3, 4, 5];
$doubled = array_map(fn($n) => $n * 2, $numbers);
echo "doubled: " . implode(", ", $doubled) . "\n";

$evens = array_filter($numbers, fn($n) => $n % 2 == 0);
echo "evens: " . implode(", ", $evens) . "\n";

echo greet("Alice") . "\n";
echo add(2, 3) . "\n";
echo sum(1, 2, 3, 4, 5) . "\n";
echo divide(10, 2) . "\n";
