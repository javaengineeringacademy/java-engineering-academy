<?php
// PHP Interfaces

interface Drawable {
    public function draw(): void;
}

interface Resizable {
    public function resize(float $factor): void;
}

class Circle implements Drawable, Resizable {
    private float $radius;

    public function __construct(float $radius) {
        $this->radius = $radius;
    }

    public function draw(): void {
        echo "Drawing circle with radius {$this->radius}\n";
    }

    public function resize(float $factor): void {
        $this->radius *= $factor;
        echo "Resized circle to radius {$this->radius}\n";
    }
}

$circle = new Circle(5.0);
$circle->draw();
$circle->resize(2.0);
