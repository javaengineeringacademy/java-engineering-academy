<?php
// PHP Strings

$name = "Alice";
echo "Hello, $name\n";

// String functions
$str = "Hello World";
echo "length: " . strlen($str) . "\n";
echo "lower: " . strtolower($str) . "\n";
echo "upper: " . strtoupper($str) . "\n";
echo "substr: " . substr($str, 0, 5) . "\n";
echo "strpos: " . strpos($str, "World") . "\n";

// Heredoc
$multiline = <<<EOT
This is a
multiline string
EOT;
echo $multiline . "\n";

// Nowdoc (single-quoted heredoc)
$nowdoc = <<<'EOT'
This is a
nowdoc string
EOT;
echo $nowdoc . "\n";
