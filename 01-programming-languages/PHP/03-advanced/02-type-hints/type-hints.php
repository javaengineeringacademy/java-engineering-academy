<?php
// PHP Type Hints

// Scalar types
function add(int $a, int $b): int {
    return $a + $b;
}

// Nullable type
function find(int $id): ?string {
    return $id > 0 ? "found" : null;
}

// Union type (PHP 8)
function format(int|float|string $value): string {
    return (string) $value;
}

// Intersection type (PHP 8.1)
function count_items(Countable $items): int {
    return $items->count();
}

// Mixed type
function process(mixed $value): mixed {
    return $value;
}

echo add(2, 3) . "\n";
echo format(42) . "\n";
echo format(3.14) . "\n";
echo format("hello") . "\n";
