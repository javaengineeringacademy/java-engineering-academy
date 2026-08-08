// Module 14: Best Practices — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <string>
#include <vector>
#include <memory>
#include <cassert>
#include <cmath>
#include <cstdio>
#include <algorithm>

// ============================================================================
// Exercise 1 Solution: Const Correctness
// ============================================================================

class Vector2D {
    double x_, y_;
public:
    Vector2D(double x = 0, double y = 0) : x_(x), y_(y) {}

    double get_x() const { return x_; }
    double get_y() const { return y_; }
    double magnitude() const { return std::sqrt(x_ * x_ + y_ * y); }
    std::string to_string() const {
        return "(" + std::to_string(x_) + ", " + std::to_string(y_) + ")";
    }

    Vector2D& set_x(double x) { x_ = x; return *this; }
    Vector2D& set_y(double y) { y_ = y; return *this; }
    Vector2D& add(const Vector2D& other) {
        x_ += other.x_;
        y_ += other.y_;
        return *this;
    }
};

Vector2D normalized(const Vector2D& v) {
    double mag = v.magnitude();
    if (mag == 0) return Vector2D(0, 0);
    return Vector2D(v.get_x() / mag, v.get_y() / mag);
}

void exercise1() {
    std::cout << "\n=== Exercise 1: Const Correctness ===\n";

    const Vector2D cv(3.0, 4.0);
    assert(cv.get_x() == 3.0);
    assert(cv.get_y() == 4.0);
    assert(cv.magnitude() == 5.0);
    std::cout << "Const vector: " << cv.to_string() << "\n";

    Vector2D v(1.0, 0.0);
    v.set_x(3.0).set_y(4.0);
    assert(v.magnitude() == 5.0);

    Vector2D n = normalized(cv);
    assert(n.get_x() == 0.6);
    assert(n.get_y() == 0.8);
    std::cout << "Normalized: " << n.to_string() << "\n";

    std::cout << "Exercise 1 passed!\n";
}

// ============================================================================
// Exercise 2 Solution: RAII
// ============================================================================

class FileGuard {
    FILE* file_;
    std::string filename_;
public:
    FileGuard(const std::string& filename, const char* mode)
        : file_(nullptr), filename_(filename) {
        file_ = fopen(filename.c_str(), mode);
    }

    ~FileGuard() {
        if (file_) fclose(file_);
    }

    FileGuard(const FileGuard&) = delete;
    FileGuard& operator=(const FileGuard&) = delete;

    FileGuard(FileGuard&& other) noexcept
        : file_(other.file_), filename_(std::move(other.filename_)) {
        other.file_ = nullptr;
    }

    FileGuard& operator=(FileGuard&& other) noexcept {
        if (this != &other) {
            if (file_) fclose(file_);
            file_ = other.file_;
            filename_ = std::move(other.filename_);
            other.file_ = nullptr;
        }
        return *this;
    }

    bool is_open() const { return file_ != nullptr; }
    const std::string& filename() const { return filename_; }

    bool write(const std::string& data) {
        if (!file_) return false;
        return fputs(data.c_str(), file_) != EOF;
    }

    std::string read_line() {
        if (!file_) return "";
        char buf[256];
        if (fgets(buf, sizeof(buf), file_)) {
            return std::string(buf);
        }
        return "";
    }
};

void exercise2() {
    std::cout << "\n=== Exercise 2: RAII ===\n";

    {
        FileGuard writer("test_raii.txt", "w");
        assert(writer.is_open());
        writer.write("Hello RAII!\n");
        writer.write("Line 2\n");
    }

    {
        FileGuard reader("test_raii.txt", "r");
        assert(reader.is_open());
        std::string line = reader.read_line();
        assert(line.find("Hello RAII!") != std::string::npos);
        std::cout << "Read: " << line;
    }

    remove("test_raii.txt");
    std::cout << "Exercise 2 passed!\n";
}

// ============================================================================
// Exercise 3 Solution: Rule of Zero/Five
// ============================================================================

class Person {
    std::string name_;
    int age_;
    std::vector<std::string> hobbies_;
public:
    Person(const std::string& name, int age, const std::vector<std::string>& hobbies = {})
        : name_(name), age_(age), hobbies_(hobbies) {}

    const std::string& name() const { return name_; }
    int age() const { return age_; }
    const std::vector<std::string>& hobbies() const { return hobbies_; }
};

class Buffer {
    char* data_;
    size_t size_;
public:
    explicit Buffer(size_t size) : data_(new char[size]()), size_(size) {}
    ~Buffer() { delete[] data_; }

    Buffer(const Buffer& other) : data_(new char[other.size_]), size_(other.size_) {
        std::copy(other.data_, other.data_ + other.size_, data_);
    }

    Buffer& operator=(const Buffer& other) {
        if (this != &other) {
            delete[] data_;
            size_ = other.size_;
            data_ = new char[size_];
            std::copy(other.data_, other.data_ + other.size_, data_);
        }
        return *this;
    }

    Buffer(Buffer&& other) noexcept : data_(other.data_), size_(other.size_) {
        other.data_ = nullptr;
        other.size_ = 0;
    }

    Buffer& operator=(Buffer&& other) noexcept {
        if (this != &other) {
            delete[] data_;
            data_ = other.data_;
            size_ = other.size_;
            other.data_ = nullptr;
            other.size_ = 0;
        }
        return *this;
    }

    size_t size() const { return size_; }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Rule of Zero/Five ===\n";

    Person p1("Alice", 30, {"reading", "coding"});
    Person p2 = p1;
    Person p3 = std::move(p1);
    assert(p2.name() == "Alice");
    assert(p3.name() == "Alice");

    Buffer b1(100);
    Buffer b2 = b1;
    Buffer b3 = std::move(b1);
    assert(b2.size() == 100);
    assert(b3.size() == 100);
    assert(b1.size() == 0);
    std::cout << "Exercise 3 passed!\n";
}

// ============================================================================
// Exercise 4 Solution: Smart Pointers
// ============================================================================

struct Widget {
    std::string name;
    int value;
    Widget(const std::string& n, int v) : name(n), value(v) {}
    ~Widget() { std::cout << "Widget destroyed: " << name << "\n"; }
};

void exercise4_unique() {
    auto w = std::make_unique<Widget>("Solo", 42);
    assert(w->value == 42);
    auto w2 = std::move(w);
    assert(w == nullptr);
    assert(w2->value == 42);
}

void exercise4_shared() {
    auto w1 = std::make_shared<Widget>("Shared", 100);
    {
        auto w2 = w1;
        auto w3 = w1;
        assert(w1.use_count() == 3);
    }
    assert(w1.use_count() == 1);
}

struct Node {
    std::string name;
    std::shared_ptr<Node> next;
    std::weak_ptr<Node> prev;
    Node(const std::string& n) : name(n) {}
};

void exercise4_weak() {
    auto n1 = std::make_shared<Node>("A");
    auto n2 = std::make_shared<Node>("B");
    n1->next = n2;
    n2->prev = n1;

    assert(n1.use_count() == 2);
    assert(n2.use_count() == 2);

    if (auto locked = n2->prev.lock()) {
        assert(locked->name == "A");
    }
}

void exercise4() {
    std::cout << "\n=== Exercise 4: Smart Pointers ===\n";
    exercise4_unique();
    exercise4_shared();
    exercise4_weak();
    std::cout << "Exercise 4 passed!\n";
}

// ============================================================================
// Exercise 5 Solution: Code Review
// ============================================================================

class GoodWidget {
    std::unique_ptr<int> data_;
public:
    GoodWidget(int value) : data_(std::make_unique<int>(value)) {}
    int get() const { return *data_; }
};

int good_function() { int x = 42; return x; }

void exercise5() {
    std::cout << "\n=== Exercise 5: Code Review ===\n";

    GoodWidget gw(100);
    assert(gw.get() == 100);

    int result = good_function();
    assert(result == 42);
    std::cout << "Exercise 5 passed!\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 14: Best Practices Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
