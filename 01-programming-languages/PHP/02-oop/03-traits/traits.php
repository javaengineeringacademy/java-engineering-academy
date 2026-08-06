<?php
// PHP Traits

trait Logger {
    public function log(string $msg): void {
        echo "LOG: $msg\n";
    }
}

trait Timestamp {
    public function timestamp(): string {
        return date('Y-m-d H:i:s');
    }
}

class Service {
    use Logger, Timestamp;

    public function doWork(): void {
        $this->log("starting at " . $this->timestamp());
        echo "working\n";
        $this->log("done at " . $this->timestamp());
    }
}

$service = new Service();
$service->doWork();

// Trait conflict resolution
trait A {
    public function hello(): void {
        echo "A\n";
    }
}

trait B {
    public function hello(): void {
        echo "B\n";
    }
}

class C {
    use A, B {
        A::hello insteadof B;
        B::hello as world;
    }
}

$c = new C();
$c->hello();
$c->world();
