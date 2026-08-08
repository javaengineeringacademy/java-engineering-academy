// Module 06: Smart Pointers — Exercises
// Compile: g++ -std=c++17 -fsanitize=address -g -o exercises exercises.cpp

#include <iostream>
#include <memory>
#include <string>
#include <vector>
#include <cassert>

// ============================================================================
// Exercise 1: unique_ptr — File Buffer
// Implement a FileBuffer using unique_ptr that manages a char array.
// ============================================================================

// TODO: Implement FileBuffer using std::unique_ptr<char[]>
// - Constructor: allocates buffer of given size
// - getBuffer(): returns raw pointer
// - getSize(): returns buffer size
// - Demonstrate ownership transfer with std::move

void exercise1() {
    std::cout << "\n=== Exercise 1: unique_ptr ===\n";

    // Create a FileBuffer with size 256
    // Transfer ownership to another unique_ptr
    // Verify original is nullptr after move
    // Verify new owner has valid buffer
}

// ============================================================================
// Exercise 2: shared_ptr — Shared Configuration
// Use shared_ptr to share a Configuration object between multiple components.
// ============================================================================

struct Config {
    std::string database_url;
    int max_connections;
    Config(const std::string& url, int max)
        : database_url(url), max_connections(max) {
        std::cout << "Config created: " << database_url << "\n";
    }
    ~Config() {
        std::cout << "Config destroyed: " << database_url << "\n";
    }
};

// TODO: Create a shared_ptr<Config>
// Share it with 3 different "components" (just local variables)
// Print use_count() at each step
// Reset one component, print use_count
// Reset all, verify Config is destroyed

void exercise2() {
    std::cout << "\n=== Exercise 2: shared_ptr ===\n";

    // TODO: Implement
}

// ============================================================================
// Exercise 3: weak_ptr — Breaking Circular References
// Fix a circular reference between Parent and Child nodes.
// ============================================================================

struct NodeFixed {
    std::string name;
    std::shared_ptr<NodeFixed> parent;
    std::weak_ptr<NodeFixed> child;  // Use weak_ptr to break cycle

    NodeFixed(const std::string& n) : name(n) {
        std::cout << "Created: " << name << "\n";
    }
    ~NodeFixed() {
        std::cout << "Destroyed: " << name << "\n";
    }
};

// TODO: Create a parent and child node
// Set parent->child = child and child->parent = parent
// Verify both are destroyed when main block exits
// Use weak_ptr::lock() to safely access the child

void exercise3() {
    std::cout << "\n=== Exercise 3: weak_ptr ===\n";

    // TODO: Implement
    // When done correctly, you should see both "Destroyed" messages
}

// ============================================================================
// Exercise 4: Factory Pattern with unique_ptr
// Implement a Shape factory that returns unique_ptr<Base>.
// ============================================================================

class ShapeBase {
public:
    virtual ~ShapeBase() = default;
    virtual double area() const = 0;
    virtual std::string type() const = 0;
};

// TODO: Implement Circle and Rectangle derived classes
// TODO: Implement a factory function:
// std::unique_ptr<ShapeBase> createShape(const std::string& type, ...);

void exercise4() {
    std::cout << "\n=== Exercise 4: Factory Pattern ===\n";

    // TODO: Use factory to create shapes
    // Store in a vector<unique_ptr<ShapeBase>>
    // Print area and type of each
}

// ============================================================================
// Exercise 5: Custom Deleter
// Use unique_ptr with a custom deleter for a FILE*.
// ============================================================================

// TODO: Create a unique_ptr<FILE> with a custom deleter that calls fclose
// Write a line to the file
// Read it back and print

void exercise5() {
    std::cout << "\n=== Exercise 5: Custom Deleter ===\n";

    // TODO: Implement
    // Auto-cleanup when unique_ptr goes out of scope
}

int main() {
    std::cout << "=== Module 06: Smart Pointers Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    return 0;
}
