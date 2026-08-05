# PHP Debugging

## Xdebug Setup

Install and configure Xdebug for step debugging.

```ini
; php.ini
zend_extension=xdebug
xdebug.mode=debug
xdebug.client_host=127.0.0.1
xdebug.client_port=9003
xdebug.start_with_request=yes
xdebug.idekey=VSCODE
```

Usage in code:
```php
xdebug_break();          // Set breakpoint
xdebug_dump($variable); // Dump variable
xdebug_print_function_stack(); // Print call stack
```

## var_dump and print_r

Quick debugging with built-in functions.

```php
$user = ["name" => "Alice", "age" => 30];

// var_dump shows type and value
var_dump($user);
// array(2) { ["name"]=> string(5) "Alice" ["age"]=> int(30) }

// print_r for readable output
print_r($user);

// Formatted output with pre tags
echo '<pre>' . print_r($user, true) . '</pre>';
```

## Error Logging

Configure comprehensive error logging.

```php
// Set error reporting level
error_reporting(E_ALL);
ini_set('display_errors', 0);
ini_set('log_errors', 1);
ini_set('error_log', '/var/log/php/error.log');

// Custom error handler
set_error_handler(function ($errno, $errstr, $errfile, $errline) {
    $message = sprintf(
        "[%s] %s in %s on line %d",
        date('Y-m-d H:i:s'),
        $errstr,
        $errfile,
        $errline
    );
    error_log($message);
});

// Exception handler
set_exception_handler(function ($e) {
    error_log(sprintf(
        "Uncaught %s: %s in %s:%d",
        get_class($e),
        $e->getMessage(),
        $e->getFile(),
        $e->getLine()
    ));
});
```

## Debug Helpers

```php
// Debug function with context
function debug($var, $label = '') {
    $trace = debug_backtrace(DEBUG_BACKTRACE_IGNORE_ARGS, 1);
    $file = $trace[0]['file'] ?? 'unknown';
    $line = $trace[0]['line'] ?? 0;

    echo "<pre>";
    if ($label) echo "$label: ";
    echo basename($file) . ":$line\n";
    print_r($var);
    echo "</pre>";
}

// Memory usage
echo "Peak memory: " . formatBytes(memory_get_peak_usage(true));
echo "Current memory: " . formatBytes(memory_get_usage(true));

function formatBytes($bytes) {
    $units = ['B', 'KB', 'MB', 'GB'];
    $i = 0;
    while ($bytes >= 1024 && $i < count($units) - 1) {
        $bytes /= 1024;
        $i++;
    }
    return round($bytes, 2) . ' ' . $units[$i];
}
```

## Common Debugging Commands

```bash
# Check PHP configuration
php -i | grep "error_log"
php -i | grep "xdebug"

# Watch error logs in real time
tail -f /var/log/php/error.log

# Test code quickly
php -r "echo 2 + 2;"

# Lint check for syntax errors
php -l script.php

# Check for common issues
php -d display_errors=1 -d error_reporting=E_ALL script.php
```
