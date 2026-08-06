<?php
// PHP Arrays

// Indexed arrays
$colors = ["red", "green", "blue"];
echo "colors: " . implode(", ", $colors) . "\n";

// Associative arrays
$person = ["name" => "Alice", "age" => 30];
echo "name: {$person['name']}, age: {$person['age']}\n";

// Array functions
echo "count: " . count($colors) . "\n";
array_push($colors, "yellow");
echo "after push: " . implode(", ", $colors) . "\n";
array_pop($colors);
echo "after pop: " . implode(", ", $colors) . "\n";

// Multidimensional arrays
$matrix = [[1, 2], [3, 4]];
echo "matrix[1][0]: {$matrix[1][0]}\n";

// Loop through array
foreach ($colors as $color) {
    echo "color: $color\n";
}

// Loop with key
foreach ($person as $key => $value) {
    echo "$key: $value\n";
}
