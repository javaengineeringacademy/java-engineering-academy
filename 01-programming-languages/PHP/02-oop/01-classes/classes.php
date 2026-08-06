<?php
// PHP Classes

class Person {
    public string $name;
    protected int $age;
    private string $email;

    public function __construct(string $name, int $age, string $email) {
        $this->name = $name;
        $this->age = $age;
        $this->email = $email;
    }

    public function greet(): string {
        return "Hello, {$this->name}";
    }

    public function getAge(): int {
        return $this->age;
    }

    private function getEmail(): string {
        return $this->email;
    }
}

// Constructor promotion (PHP 8)
class Point {
    public function __construct(
        public float $x,
        public float $y
    ) {}

    public function __toString(): string {
        return "({$this->x}, {$this->y})";
    }
}

$person = new Person("Alice", 30, "alice@example.com");
echo $person->greet() . "\n";
echo "age: " . $person->getAge() . "\n";

$point = new Point(1.0, 2.5);
echo "point: $point\n";
