# PHP Monitoring

## New Relic APM

Monitor PHP applications with New Relic for performance insights.

```bash
# Install New Relic agent
sudo apt-get install newrelic-php5
sudo newrelic-install install

# Configure in php.ini
newrelic.enabled = true
newrelic.license_key = "YOUR_LICENSE_KEY"
newrelic.appname = "My PHP App"
```

Instrument custom transactions:
```php
newrelic_set_appname("Custom Transaction");
newrelic_record_custom_metric("Custom/UserCount", $userCount);
newrelic_add_custom_parameter("user_id", $userId);
```

## Sentry Error Tracking

Capture and track PHP errors in production.

```php
// Install via Composer
// composer require sentry/sentry

use Sentry\SentrySdk;
use Sentry\Severity;

SentrySdk::init([
    'dsn' => 'https://your-dsn@sentry.io/project-id',
    'traces_sample_rate' => 1.0,
]);

try {
    riskyOperation();
} catch (\Throwable $e) {
    SentrySdk::captureException($e);
}

// Add context
SentrySdk::configureScope(function ($scope) {
    $scope->setUser(['id' => $userId, 'email' => $email]);
    $scope->setTag('environment', 'production');
    $scope->setExtra('request_data', $_POST);
});
```

## Error Logging

Configure PHP error logging for production.

```ini
; php.ini
error_reporting = E_ALL
display_errors = Off
log_errors = On
error_log = /var/log/php/error.log
log_errors_max_len = 1024
```

Custom error handler:
```php
set_error_handler(function ($severity, $message, $file, $line) {
    throw newErrorException($message, 0, $severity, $file, $line);
});

set_exception_handler(function ($e) {
    error_log($e->getMessage() . ' in ' . $e->getFile() . ':' . $e->getLine());
    http_response_code(500);
    echo 'Internal Server Error';
});
```

## Application Performance Monitoring

```php
// Track request timing
$start = microtime(true);
// ... process request
$duration = microtime(true) - $start;
error_log("Request took {$duration}s");

// Track slow queries
$start = microtime(true);
$stmt->execute();
$duration = microtime(true) - $start;
if ($duration > 0.1) {
    error_log("Slow query ({$duration}s): $sql");
}
```

## Health Check Endpoint

```php
// public/health.php
header('Content-Type: application/json');

$checks = [
    'status' => 'ok',
    'php_version' => PHP_VERSION,
    'database' => checkDatabase(),
    'redis' => checkRedis(),
    'disk_space' => disk_free_space('/') > 1073741824,
];

http_response_code(200);
echo json_encode($checks);

function checkDatabase(): bool {
    try {
        $pdo = new PDO($dsn, $user, $pass);
        $pdo->query("SELECT 1");
        return true;
    } catch (Exception $e) {
        return false;
    }
}
```

## Log Analysis Tools

- **GoAccess**: Real-time web log analyzer
- **AWStats**: Advanced web statistics
- **Logsene**: Centralized log management
- **ELK Stack**: Elasticsearch, Logstash, Kibana for log aggregation
