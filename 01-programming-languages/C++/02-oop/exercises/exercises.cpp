// Module 02: Object-Oriented Programming — Exercises
// Complete each exercise by filling in the TODO sections.
// Compile: g++ -std=c++17 -Wall -Wextra -o exercises exercises.cpp

#include <iostream>
#include <string>
#include <vector>
#include <memory>
#include <cmath>
#include <cassert>

// ============================================================================
// Exercise 1: BankAccount with Rule of Three
// Implement a BankAccount class that properly manages a dynamically allocated
// transaction log. Apply the Rule of Three.
// ============================================================================

class BankAccount {
private:
    std::string owner_;
    double balance_;
    // TODO: Add a dynamically allocated array of transaction descriptions
    // and a count of transactions

public:
    BankAccount(const std::string& owner, double initial_balance)
        : owner_(owner), balance_(initial_balance) {
        // TODO: Initialize the transaction log array (empty initially)
    }

    // TODO: Implement destructor (Rule of Three)
    ~BankAccount() {
        // TODO: Free the transaction log
    }

    // TODO: Implement copy constructor (Rule of Three)
    BankAccount(const BankAccount& other)
        : owner_(other.owner_), balance_(other.balance_) {
        // TODO: Deep copy the transaction log
    }

    // TODO: Implement copy assignment operator (Rule of Three)
    BankAccount& operator=(const BankAccount& other) {
        // TODO: Handle self-assignment, free old data, deep copy
        return *this;
    }

    void deposit(double amount) {
        if (amount > 0) {
            balance_ += amount;
            // TODO: Record this deposit in the transaction log
        }
    }

    void withdraw(double amount) {
        if (amount > 0 && amount <= balance_) {
            balance_ -= amount;
            // TODO: Record this withdrawal in the transaction log
        }
    }

    double getBalance() const { return balance_; }
    const std::string& getOwner() const { return owner_; }

    // TODO: Add a method to print all transactions
    void printTransactions() const {
        // TODO: Implement
    }
};

// ============================================================================
// Exercise 2: Shape Hierarchy with Polymorphism
// Create a Shape base class and derived Circle, Rectangle, and Triangle.
// All shapes must be drawable and have calculable areas.
// ============================================================================

class Shape {
public:
    // TODO: Make this a proper abstract base class
    // - Virtual destructor
    // - Pure virtual area() method
    // - Pure virtual describe() method
    // - Pure virtual clone() method (returns unique_ptr<Shape>)

    virtual ~Shape() = default;
};

class Circle : public Shape {
    double radius_;
public:
    explicit Circle(double radius) : radius_(radius) {}

    // TODO: Implement area() — π * r²
    // TODO: Implement describe() — prints "Circle with radius X"
    // TODO: Implement clone() — returns deep copy
};

class Rectangle : public Shape {
    double width_, height_;
public:
    Rectangle(double w, double h) : width_(w), height_(h) {}

    // TODO: Implement area() — width * height
    // TODO: Implement describe() — prints "Rectangle WxH"
    // TODO: Implement clone() — returns deep copy
};

class Triangle : public Shape {
    double base_, height_;
public:
    Triangle(double base, double height) : base_(base), height_(height) {}

    // TODO: Implement area() — 0.5 * base * height
    // TODO: Implement describe() — prints "Triangle base X height Y"
    // TODO: Implement clone() — returns deep copy
};

// ============================================================================
// Exercise 3: Composition — Car and Engine
// Build a Car class that uses composition (has-a Engine, has-a Transmission).
// Demonstrate that composition is often better than inheritance.
// ============================================================================

class Engine {
    int horsepower_;
    bool running_;
public:
    explicit Engine(int hp) : horsepower_(hp), running_(false) {}

    void start() {
        if (!running_) {
            running_ = true;
            std::cout << "Engine started (" << horsepower_ << " HP)\n";
        }
    }

    void stop() {
        if (running_) {
            running_ = false;
            std::cout << "Engine stopped\n";
        }
    }

    bool isRunning() const { return running_; }
    int getHorsepower() const { return horsepower_; }
};

class Transmission {
    int current_gear_;
    int max_gears_;
public:
    explicit Transmission(int max_gears = 6)
        : current_gear_(0), max_gears_(max_gears) {}

    // TODO: Implement shift_up() — increase gear, max is max_gears_
    // TODO: Implement shift_down() — decrease gear, min is 0 (neutral)
    // TODO: Implement get_gear() — return current gear
    // Print status after each shift
};

class Car {
    std::string model_;
    Engine engine_;
    Transmission transmission_;
    bool moving_;
public:
    // TODO: Constructor that initializes engine and transmission
    // TODO: start() — starts engine
    // TODO: stop() — stops engine, resets gear to neutral
    // TODO: accelerate() — if engine running, shift up and set moving
    // TODO: brake() — if moving, shift down, stop if gear 0
    // TODO: status() — print car status (model, engine, gear, moving)
};

// ============================================================================
// Exercise 4: Encapsulation — Temperature Class
// Create a Temperature class that encapsulates a celsius value and provides
// conversion methods. Enforce invariants (temperature >= -273.15).
// ============================================================================

class Temperature {
    double celsius_;
    // TODO: Private helper for validation

public:
    // TODO: Constructor — validate temperature >= -273.15
    // TODO: getCelsius() and getFahrenheit()
    // TODO: setCelsius() and setFahrenheit() — with validation
    // TODO: toString() — returns "XX.X°C (YY.Y°F)"
};

// ============================================================================
// Exercise 5: Polymorphic Container — Drawing Program
// Store different shapes in a vector of unique_ptr<Shape> and perform
// polymorphic operations on them.
// ============================================================================

void exercise5() {
    std::cout << "\n=== Exercise 5: Polymorphic Container ===\n";

    std::vector<std::unique_ptr<Shape>> shapes;

    // TODO: Create a Circle(r=5), Rectangle(3,4), Triangle(6,2)
    // and push them into the vector using clone() or make_unique

    // TODO: Loop through shapes and:
    // 1. Call describe() on each
    // 2. Print the area of each
    // 3. Print total area of all shapes

    // TODO: Clone one shape and add it to the vector
    // Print the new total number of shapes
}

int main() {
    std::cout << "=== Module 02: OOP Exercises ===\n";

    // Uncomment exercises as you implement them:
    // exercise1();
    // exercise2();
    // exercise3();
    // exercise4();
    exercise5();

    return 0;
}
