<?php
// PHP Control Flow

$x = 10;

// if/elseif/else
if ($x > 0) {
    echo "positive\n";
} elseif ($x < 0) {
    echo "negative\n";
} else {
    echo "zero\n";
}

// switch
$day = "Monday";
switch ($day) {
    case "Monday":
    case "Tuesday":
    case "Wednesday":
    case "Thursday":
    case "Friday":
        echo "Weekday\n";
        break;
    case "Saturday":
    case "Sunday":
        echo "Weekend\n";
        break;
    default:
        echo "Unknown\n";
}

// for loop
echo "for: ";
for ($i = 0; $i < 5; $i++) {
    echo "$i ";
}
echo "\n";

// foreach
$colors = ["red", "green", "blue"];
foreach ($colors as $color) {
    echo "color: $color\n";
}

// while
echo "while: ";
$i = 0;
while ($i < 3) {
    echo "$i ";
    $i++;
}
echo "\n";

// match (PHP 8)
$result = match($x) {
    1 => "one",
    10 => "ten",
    default => "other",
};
echo "match: $result\n";
