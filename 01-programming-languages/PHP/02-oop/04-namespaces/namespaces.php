<?php
// PHP Namespaces

namespace App\Models;

class User {
    public function __construct(
        private string $name,
        private string $email
    ) {}

    public function getName(): string {
        return $this->name;
    }

    public function getEmail(): string {
        return $this->email;
    }
}

// Example usage
// In real code, you'd use: use App\Models\User;
$user = new User("Alice", "alice@example.com");
echo $user->getName() . "\n";
echo $user->getEmail() . "\n";
