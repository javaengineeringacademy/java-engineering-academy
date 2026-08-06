# PHP Namespaces

## Overview
Namespaces organize code and prevent naming conflicts.

## Defining Namespace
```php
namespace App\Models;

class User {
    // ...
}
```

## Using Namespace
```php
use App\Models\User;

$user = new User();
```

## Aliasing
```php
use App\Models\User as UserModel;
```

## Autoloading (PSR-4)
```json
{
    "autoload": {
        "psr-4": {
            "App\\": "src/"
        }
    }
}
```

## Resources
- [PHP Namespaces](https://www.php.net/manual/en/language.namespaces.php)
