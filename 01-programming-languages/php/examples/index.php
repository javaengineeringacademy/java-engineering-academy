<?php

// Variables
$name = "PHP";
$version = 8.3;
echo "Language: $name, Version: $version\n";

// Arrays
$numbers = [1, 2, 3, 4, 5];
$doubled = array_map(fn($x) => $x * 2, $numbers);
echo "Doubled: " . implode(", ", $doubled) . "\n";

// Classes
class Person {
    private string $name;
    private int $age;

    public function __construct(string $name, int $age) {
        $this->name = $name;
        $this->age = $age;
    }

    public function greet(): string {
        return "Hello, I'm {$this->name}!";
    }
}

$person = new Person("Alice", 30);
echo $person->greet() . "\n";

// Traits
trait Loggable {
    public function log(string $message): void {
        echo "LOG: $message\n";
    }
}

class App {
    use Loggable;
}

$app = new App();
$app->log("Application started");

// Named arguments
function createUser(string $name, int $age, string $email): array {
    return compact('name', 'age', 'email');
}

$user = createUser(name: "Bob", age: 25, email: "bob@example.com");
print_r($user);
