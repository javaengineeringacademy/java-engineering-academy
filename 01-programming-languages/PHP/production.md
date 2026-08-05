# PHP Production Deployment

## PHP-FPM with Nginx

```nginx
server {
    listen 80;
    server_name example.com;
    root /var/www/project/public;

    location / {
        try_files $uri $uri/ /index.php?$query_string;
    }

    location ~ \.php$ {
        fastcgi_pass unix:/run/php/php8.2-fpm.sock;
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        include fastcgi_params;
        fastcgi_read_timeout 300;
        fastcgi_buffering on;
        fastcgi_buffer_size 16k;
        fastcgi_buffers 16 16k;
    }

    location ~ /\.ht {
        deny all;
    }

    access_log /var/log/nginx/project.access.log;
    error_log /var/log/nginx/project.error.log;
}
```

## Docker Deployment

```dockerfile
FROM php:8.2-fpm-alpine

RUN apk add --no-cache \
    icu-dev \
    libzip-dev \
    && docker-php-ext-install pdo_mysql intl zip opcache

COPY php.ini /usr/local/etc/php/conf.d/custom.ini
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

WORKDIR /var/www/html
COPY composer.json composer.lock ./
RUN composer install --no-dev --optimize-autoloader --no-scripts

COPY . .

RUN chown -R www-data:www-data storage bootstrap/cache
RUN chmod -R 775 storage bootstrap/cache

CMD ["php-fpm"]
```

## Health Checks

```php
// healthcheck.php
header('Content-Type: application/json');

$checks = [];
$healthy = true;

// Database check
try {
    $pdo->query("SELECT 1");
    $checks['database'] = 'ok';
} catch (Exception $e) {
    $checks['database'] = 'error';
    $healthy = false;
}

// Memory check
$memUsage = memory_get_usage(true) / 1024 / 1024;
$checks['memory_mb'] = round($memUsage, 2);
if ($memUsage > 256) {
    $healthy = false;
}

http_response_code($healthy ? 200 : 503);
echo json_encode(['status' => $healthy ? 'healthy' : 'unhealthy', 'checks' => $checks]);
```

## Graceful Shutdown

```php
pcntl_signal(SIGTERM, function () {
    // Close database connections
    $pdo = null;

    // Stop accepting new requests
    // Finish current request
    // Exit cleanly
    exit(0);
});

pcntl_signal_dispatch();
```

## Configuration Management

```php
// Environment-based configuration
return [
    'database' => [
        'host' => $_ENV['DB_HOST'] ?? 'localhost',
        'port' => $_ENV['DB_PORT'] ?? 3306,
        'name' => $_ENV['DB_NAME'],
        'user' => $_ENV['DB_USER'],
        'pass' => $_ENV['DB_PASS'],
    ],
    'cache' => [
        'driver' => $_ENV['CACHE_DRIVER'] ?? 'apcu',
        'ttl' => $_ENV['CACHE_TTL'] ?? 300,
    ],
];
```

## Log Rotation

```bash
# /etc/logrotate.d/php
/var/log/php/*.log {
    daily
    missingok
    rotate 14
    compress
    delaycompress
    notifempty
    create 0640 www-data adm
    sharedscripts
    postrotate
        /usr/sbin/service php8.2-fpm reload > /dev/null 2>&1 || true
    endscript
}
```

## Monitoring Commands

```bash
# Check PHP-FPM status
php-fpm -t                    # Test configuration
php-fpm -i                    # Display info

# Monitor processes
ps aux | grep php-fpm
watch -n 1 'ps aux | grep php-fpm | wc -l'

# Check error logs
tail -f /var/log/php/error.log
journalctl -u php8.2-fpm -f
```
