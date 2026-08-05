# PHP Project Structure

## PSR-4 Standard Layout

```
project/
  composer.json
  composer.lock
  phpunit.xml
  src/
    Controllers/
      HomeController.php
      UserController.php
    Models/
      User.php
      Product.php
    Services/
      UserService.php
      PaymentService.php
    Repositories/
      UserRepository.php
    Database/
      Connection.php
      Migrations/
  public/
    index.php
    css/
    js/
    images/
  config/
    app.php
    database.php
    routes.php
  routes/
    web.php
    api.php
  templates/
    layouts/
      base.php
    home/
      index.php
    users/
      index.php
  storage/
    logs/
    cache/
  tests/
    Unit/
      UserServiceTest.php
    Feature/
      UserControllerTest.php
  vendor/
    autoload.php
```

## composer.json Example

```json
{
    "name": "vendor/project",
    "description": "PHP project description",
    "type": "project",
    "require": {
        "php": ">=8.1",
        "vlucas/phpdotenv": "^5.5"
    },
    "require-dev": {
        "phpunit/phpunit": "^10.0",
        "phpstan/phpstan": "^1.9"
    },
    "autoload": {
        "psr-4": {
            "App\\": "src/"
        }
    },
    "autoload-dev": {
        "psr-4": {
            "Tests\\": "tests/"
        }
    },
    "scripts": {
        "test": "phpunit",
        "analyse": "phpstan analyse"
    }
}
```

## Entry Point

```php
// public/index.php
require_once __DIR__ . '/../vendor/autoload.php';

use Dotenv\Dotenv;

$dotenv = Dotenv::createImmutable(dirname(__DIR__));
$dotenv->load();

$router = require dirname(__DIR__) . '/routes/web.php';
$router->dispatch($_SERVER['REQUEST_URI'], $_SERVER['REQUEST_METHOD']);
```

## Configuration Pattern

```php
// config/app.php
return [
    'name' => $_ENV['APP_NAME'] ?? 'My App',
    'debug' => filter_var($_ENV['APP_DEBUG'], FILTER_VALIDATE_BOOLEAN),
    'timezone' => $_ENV['APP_TIMEZONE'] ?? 'UTC',
];
```

## Namespace Conventions

- `App\Controllers` - HTTP request handlers
- `App\Models` - Data structures and business logic
- `App\Services` - Business logic and external integrations
- `App\Repositories` - Data access layer
- `App\Database` - Connection and migration management
- `Tests\Unit` - Unit tests
- `Tests\Feature` - Integration tests
