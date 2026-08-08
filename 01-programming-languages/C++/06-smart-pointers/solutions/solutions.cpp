// Module 06: Smart Pointers — Solutions

#include <iostream>
#include <memory>
#include <string>
#include <vector>
#include <cstdio>
#include <cstring>
#include <cassert>

// ============================================================================
// Exercise 1 Solution: unique_ptr — File Buffer
// ============================================================================

class FileBuffer {
    std::unique_ptr<char[]> buffer_;
    size_t size_;
public:
    explicit FileBuffer(size_t size)
        : buffer_(std::make_unique<char[]>(size)), size_(size) {
        std::memset(buffer_.get(), 0, size);
    }

    char* getBuffer() { return buffer_.get(); }
    size_t getSize() const { return size_; }
    bool empty() const { return !buffer_; }
};

void exercise1() {
    std::cout << "\n=== Exercise 1: unique_ptr ===\n";

    FileBuffer fb(256);
    assert(!fb.empty());
    assert(fb.getSize() == 256);

    auto* raw = fb.getBuffer();
    std::cout << "Buffer created at: " << (void*)raw << "\n";

    auto uptr = std::make_unique<int[]>(10);
    uptr[0] = 42;
    assert(uptr[0] == 42);

    auto uptr2 = std::move(uptr);
    assert(uptr == nullptr);
    assert(uptr2[0] == 42);

    std::cout << "Exercise 1 passed!\n";
}

// ============================================================================
// Exercise 2 Solution: shared_ptr — Shared Configuration
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

void exercise2() {
    std::cout << "\n=== Exercise 2: shared_ptr ===\n";

    std::shared_ptr<Config> config = std::make_shared<Config>("postgres://localhost", 10);
    std::cout << "After creation: use_count = " << config.use_count() << "\n";
    assert(config.use_count() == 1);

    {
        std::shared_ptr<Config> component_b = config;
        std::cout << "After sharing with B: use_count = " << config.use_count() << "\n";
        assert(config.use_count() == 2);

        {
            std::shared_ptr<Config> component_c = config;
            std::cout << "After sharing with C: use_count = " << config.use_count() << "\n";
            assert(config.use_count() == 3);
        }
        std::cout << "After C destroyed: use_count = " << config.use_count() << "\n";
        assert(config.use_count() == 2);
    }

    std::cout << "After B destroyed: use_count = " << config.use_count() << "\n";
    assert(config.use_count() == 1);

    config.reset();
    std::cout << "After reset: Config should be destroyed (see output above)\n";
}

// ============================================================================
// Exercise 3 Solution: weak_ptr — Breaking Circular References
// ============================================================================

struct Node3 {
    std::string name;
    std::shared_ptr<Node3> parent;
    std::weak_ptr<Node3> child;

    Node3(const std::string& n) : name(n) {
        std::cout << "Created: " << name << "\n";
    }
    ~Node3() {
        std::cout << "Destroyed: " << name << "\n";
    }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: weak_ptr ===\n";

    auto parent = std::make_shared<Node3>("Parent");
    auto child = std::make_shared<Node3>("Child");

    parent->child = child;
    child->parent = parent;

    std::cout << "Parent use_count: " << parent.use_count() << "\n";
    std::cout << "Child use_count: " << child.use_count() << "\n";

    if (auto alive_child = parent->child.lock()) {
        std::cout << "Child is alive: " << alive_child->name << "\n";
    }

    parent.reset();
    child.reset();

    std::cout << "Both should be destroyed (see output above)\n";
}

// ============================================================================
// Exercise 4 Solution: Factory Pattern with unique_ptr
// ============================================================================

class Shape4 {
public:
    virtual ~Shape4() = default;
    virtual double area() const = 0;
    virtual std::string type() const = 0;
};

class Circle4 : public Shape4 {
    double radius_;
public:
    explicit Circle4(double r) : radius_(r) {}
    double area() const override { return 3.14159 * radius_ * radius_; }
    std::string type() const override { return "Circle"; }
};

class Rectangle4 : public Shape4 {
    double w_, h_;
public:
    Rectangle4(double w, double h) : w_(w), h_(h) {}
    double area() const override { return w_ * h_; }
    std::string type() const override { return "Rectangle"; }
};

std::unique_ptr<Shape4> createShape(const std::string& type, double a, double b = 0) {
    if (type == "circle") return std::make_unique<Circle4>(a);
    if (type == "rectangle") return std::make_unique<Rectangle4>(a, b);
    return nullptr;
}

void exercise4() {
    std::cout << "\n=== Exercise 4: Factory Pattern ===\n";

    std::vector<std::unique_ptr<Shape4>> shapes;
    shapes.push_back(createShape("circle", 5.0));
    shapes.push_back(createShape("rectangle", 3.0, 4.0));
    shapes.push_back(createShape("circle", 2.5));

    double total = 0;
    for (const auto& s : shapes) {
        std::cout << s->type() << ": area = " << s->area() << "\n";
        total += s->area();
    }
    std::cout << "Total area: " << total << "\n";
}

// ============================================================================
// Exercise 5 Solution: Custom Deleter
// ============================================================================

struct FILEDeleter {
    void operator()(FILE* fp) const {
        if (fp) {
            std::cout << "Closing file via custom deleter\n";
            std::fclose(fp);
        }
    }
};

void exercise5() {
    std::cout << "\n=== Exercise 5: Custom Deleter ===\n";

    using FilePtr = std::unique_ptr<FILE, FILEDeleter>;

    {
        FilePtr file(std::fopen("test_smart.txt", "w"));
        if (file) {
            std::fputs("Hello from smart pointer!\n", file.get());
            std::cout << "Written to file\n";
        }
    }

    {
        FilePtr file(std::fopen("test_smart.txt", "r"));
        if (file) {
            char buf[100];
            std::fgets(buf, sizeof(buf), file.get());
            std::cout << "Read: " << buf;
        }
    }

    std::remove("test_smart.txt");
    std::cout << "Exercise 5 passed!\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 06: Smart Pointers Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
