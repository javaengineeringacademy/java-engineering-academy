<?php
// PHP Attributes

#[Attribute]
class Route {
    public function __construct(
        public string $path,
        public string $method = 'GET'
    ) {}
}

#[Attribute]
class Deprecated {
    public function __construct(
        public string $replacement = ''
    ) {}
}

class UserService {
    #[Route(path: '/users', method: 'GET')]
    public function getUsers(): void {
        echo "GET /users\n";
    }

    #[Route(path: '/users', method: 'POST')]
    public function createUser(): void {
        echo "POST /users\n";
    }

    #[Deprecated(replacement: 'newMethod')]
    public function oldMethod(): void {
        echo "old method\n";
    }
}

$service = new UserService();

// Read attributes
$reflection = new ReflectionClass($service);
foreach ($reflection->getMethods() as $method) {
    $attrs = $method->getAttributes(Route::class);
    foreach ($attrs as $attr) {
        $route = $attr->newInstance();
        echo "{$route->method} {$route->path}\n";
    }
}
