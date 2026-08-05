# PHP Configuration

## php.ini

The primary configuration file controlling PHP behavior.

Key sections:
- **[PHP]**: Core settings (memory_limit, error_reporting, date.timezone)
- **[Session]**: Session handling (session.save_handler, session.cookie_lifetime)
- **[MySQLi]**: Database settings (mysqli.default_port, mysqli.default_host)
- **[OPcache]**: Bytecode cache (opcache.enable, opcache.memory_consumption)

Common directives:
```ini
memory_limit = 256M
max_execution_time = 30
upload_max_filesize = 50M
post_max_size = 50M
error_reporting = E_ALL
display_errors = Off
log_errors = On
```

Runtime changes with `ini_set()`:
```php
ini_set('memory_limit', '512M');
ini_set('display_errors', '1');
```

## Composer

Dependency manager for PHP packages.

```bash
composer init                    # Create composer.json
composer require vendor/package  # Add dependency
composer update                  # Update dependencies
composer dump-autoload           # Regenerate autoloader
composer install                 # Install from lock file
```

Project structure with Composer:
```
project/
  composer.json
  composer.lock
  vendor/
    autoload.php
    composer/
    vendor-name/
      package-name/
```

## Autoloading

PSR-4 autoloading maps namespaces to directories.

```json
{
    "autoload": {
        "psr-4": {
            "App\\": "src/"
        }
    },
    "autoload-dev": {
        "psr-4": {
            "Tests\\": "tests/"
        }
    }
}
```

## Environment Variables

Store configuration outside code using environment variables.

```php
$dbHost = getenv('DB_HOST') ?: 'localhost';
$apiKey = $_ENV['API_KEY'] ?? null;
```

Use `vlucas/phpdotenv` for `.env` files:
```php
$dotenv = Dotenv\Dotenv::createImmutable(__DIR__);
$dotenv->load();
```

## php.ini Directives

### Error Handling
- `error_reporting`: Level of errors to report
- `display_errors`: Show errors on screen (Off in production)
- `log_errors`: Write errors to log file
- `error_log`: Path to error log file

### File Uploads
- `upload_max_filesize`: Maximum upload file size
- `post_max_size`: Maximum POST data size
- `file_uploads`: Enable file uploads
- `upload_tmp_dir`: Temporary directory for uploads

### Session Configuration
- `session.save_handler`: Where sessions are stored (files, redis, memcached)
- `session.save_path`: Path to session storage
- `session.cookie_lifetime`: Session cookie lifetime in seconds
- `session.gc_maxlifetime**: Maximum session data lifetime

## Extension Loading

```ini
extension=pdo_mysql
extension=mbstring
extension=curl
extension=openssl
extension=json
```

Check loaded extensions:
```php
phpinfo();
get_loaded_extensions();
extension_loaded('curl');
```
