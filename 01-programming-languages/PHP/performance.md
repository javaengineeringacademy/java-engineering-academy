# PHP Performance

## OPcache Tuning

```ini
opcache.enable=1
opcache.memory_consumption=256
opcache.interned_strings_buffer=16
opcache.max_accelerated_files=20000
opcache.revalidate_freq=0
opcache.validate_timestamps=0
opcache.save_comments=1
opcache.enable_file_override=1
```

Disable `validate_timestamps` in production to skip file stat checks.

## Profiling with Xdebug

```ini
zend_extension=xdebug
xdebug.mode=profile
xdebug.output_dir=/tmp/xdebug
xdebug.start_with_request=trigger
```

Analyze with:
- **KCachegrind** (Linux)
- **QCachegrind** (macOS)
- **Xdebug Profiler** web interface

## Profiling with Blackfire

```bash
# Install Blackfire agent
curl -sS https://get.blackfire.io | bash

# Profile a script
blackfire run php script.php

# Profile HTTP request
blackfire curl http://localhost:8080/endpoint
```

## Database Optimization

- Use PDO persistent connections for high-traffic applications
- Enable query caching for read-heavy workloads
- Use prepared statements to avoid SQL parsing overhead
- Implement connection pooling with external tools

```php
$pdo = new PDO($dsn, $user, $pass, [
    PDO::ATTR_PERSISTENT => true,
    PDO::ATTR_EMULATE_PREPARES => false,
]);
```

## Memory Management

```php
// Free memory explicitly
unset($largeArray);

// Use generators for large datasets
function getRecords(): Generator {
    $result = $pdo->query("SELECT * FROM large_table");
    while ($row = $result->fetch()) {
        yield $row;
    }
}

// Monitor memory usage
$peak = memory_get_peak_usage(true);
$current = memory_get_usage(true);
```

## Caching Strategies

```php
// APCu for in-memory caching
apcu_store('key', $data, 300);
$value = apcu_fetch('key', $success);

// Redis for distributed caching
$redis = new Redis();
$redis->connect('127.0.0.1');
$redis->setex('key', 300, serialize($data));
$value = unserialize($redis->get('key'));

// File caching for simple cases
file_put_contents($cacheFile, serialize($data));
```

## Built-in Server

```bash
# Development only
php -S localhost:8000 -t public/

# With router script
php -S localhost:8000 public/router.php
```

## Performance Monitoring

- **New Relic APM**: Transaction tracing and slow query analysis
- **Tideways**: Continuous profiling for production
- **phptrace**: Trace PHP execution in real time
- **Swoole**: Async PHP runtime for high-performance applications
