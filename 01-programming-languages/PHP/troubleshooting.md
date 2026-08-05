# PHP Troubleshooting

## White Screen of Death (WSOD)

Blank page with no error output.

```php
// Enable error display temporarily
ini_set('display_errors', 1);
error_reporting(E_ALL);

// Check PHP error log
// /var/log/php/error.log

// Check syntax with CLI
php -l public/index.php

// Common causes:
// - Memory limit exceeded
// - Maximum execution time exceeded
// - Syntax error in code
// - Extension not loaded
```

## Memory Limit Errors

```php
// Error: Allowed memory size of X bytes exhausted

// Check current limit
echo ini_get('memory_limit'); // e.g., 128M

// Increase in code (temporary)
ini_set('memory_limit', '256M');

// Or set in php.ini
memory_limit = 512M

// Optimize memory usage
unset($largeArray);           // Free memory explicitly
$GLOBALS['__xdebug_profiler'] = null; // Clear Xdebug data
```

## Connection Timeouts

```php
// Database connection timeout
$pdo = new PDO($dsn, $user, $pass, [
    PDO::ATTR_TIMEOUT => 5,
    PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
]);

// HTTP request timeout
$ch = curl_init('https://api.example.com');
curl_setopt($ch, CURLOPT_TIMEOUT, 30);
curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 10);
curl_exec($ch);
```

## File Upload Issues

```ini
; Check php.ini settings
upload_max_filesize = 50M
post_max_size = 50M
max_file_uploads = 20

; Verify in PHP
echo ini_get('upload_max_filesize');
echo ini_get('post_max_size');

; Check upload directory permissions
ls -la /tmp/
chmod 755 /var/www/uploads/
```

## Permission Errors

```bash
# Common permission issues
ls -la storage/
ls -la bootstrap/cache/

# Fix permissions
chmod -R 775 storage/
chmod -R 775 bootstrap/cache/
chown -R www-data:www-data storage/
chown -R www-data:www-data bootstrap/cache/

# Check web server user
ps aux | grep nginx
ps aux | grep apache
```

## Extension Not Found

```php
// Error: Call to undefined function json_encode()

// Check loaded extensions
php -m | grep json
php -i | grep extension_dir

// Install missing extension
sudo apt install php8.2-mbstring

// Check extension is enabled in php.ini
grep extension=mbstring /etc/php/8.2/fpm/php.ini

// Restart PHP-FPM
sudo systemctl restart php8.2-fpm
```

## Composer Issues

```bash
# Permission denied
sudo chown -R $USER ~/.composer
composer install --no-dev

# Memory limit
COMPOSER_MEMORY_LIMIT=-1 composer update

# Script not found
php composer.phar install
```

## Debugging Checklist

1. Check PHP error logs first
2. Run syntax check with `php -l`
3. Verify extension loading with `php -m`
4. Check php.ini settings with `php -i`
5. Test with minimal reproduction case
6. Check file and directory permissions
7. Verify database connectivity
8. Monitor memory and execution time
