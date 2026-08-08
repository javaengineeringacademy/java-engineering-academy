// Module 08: Modern C++ — Exercises
// Complete each exercise by filling in the TODO sections.
// Compile: g++ -std=c++20 -Wall -Wextra -o exercises exercises.cpp

#include <iostream>
#include <vector>
#include <string>
#include <optional>
#include <variant>
#include <string_view>
#include <algorithm>
#include <numeric>
#include <functional>
#include <cassert>
#include <map>
#include <array>

// ============================================================================
// Exercise 1: Lambda Mastery
// Implement functions using lambdas of increasing complexity.
// ============================================================================

// TODO: Return a lambda that multiplies its argument by `factor`
// The lambda should capture `factor` by value
auto make_multiplier(int factor) {
    // Your code here
    return [](int) { return 0; };
}

// TODO: Given a vector of ints, return a vector of only positive numbers
// Use std::copy_if with a lambda
std::vector<int> positives_only(const std::vector<int>& nums) {
    // Your code here
    return {};
}

// TODO: Sort a vector of strings by their length (shortest first)
// Use std::sort with a lambda
void sort_by_length(std::vector<std::string>& words) {
    // Your code here
}

// TODO: Implement a counter lambda factory. Each call to the returned lambda increments an internal counter.
auto make_counter(int start = 0) {
    // Your code here — use a mutable lambda
    return []() { return 0; };
}

void exercise1() {
    std::cout << "\n=== Exercise 1: Lambda Mastery ===\n";

    auto triple = make_multiplier(3);
    assert(triple(5) == 15);
    assert(triple(0) == 0);
    assert(triple(-2) == -6);
    std::cout << "Multiplier: OK\n";

    std::vector<int> nums = {-3, -1, 0, 2, 5, -4, 7};
    auto pos = positives_only(nums);
    assert((pos == std::vector<int>{0, 2, 5, 7}));
    std::cout << "Positives only: OK\n";

    std::vector<std::string> words = {"elephant", "cat", "dog", "a", "bird"};
    sort_by_length(words);
    assert(words[0] == "a");
    assert(words[1] == "cat");
    assert(words[2] == "dog");
    std::cout << "Sort by length: OK\n";

    auto counter = make_counter(10);
    assert(counter() == 10);
    assert(counter() == 11);
    assert(counter() == 12);
    std::cout << "Counter: OK\n";
}

// ============================================================================
// Exercise 2: std::optional
// Use std::optional to handle potentially missing values safely.
// ============================================================================

// TODO: Find the first element in a vector that satisfies a predicate.
// Return std::nullopt if no element matches.
std::optional<int> find_first_if(const std::vector<int>& vec, std::function<bool(int)> pred) {
    // Your code here
    return std::nullopt;
}

// TODO: Parse an integer from a string. Return std::nullopt if parsing fails.
std::optional<int> safe_stoi(std::string_view sv) {
    // Your code here
    return std::nullopt;
}

// TODO: Divide two integers. Return std::nullopt if divisor is zero.
std::optional<double> safe_divide(int numerator, int divisor) {
    // Your code here
    return std::nullopt;
}

void exercise2() {
    std::cout << "\n=== Exercise 2: std::optional ===\n";

    std::vector<int> nums = {1, 4, 7, 10, 13};

    auto first_even = find_first_if(nums, [](int x) { return x % 2 == 0; });
    assert(first_even.has_value() && *first_even == 4);
    std::cout << "First even: " << *first_even << "\n";

    auto first_big = find_first_if(nums, [](int x) { return x > 100; });
    assert(!first_big.has_value());
    std::cout << "First > 100: nullopt (correct)\n";

    auto v1 = safe_stoi("42");
    assert(v1.has_value() && *v1 == 42);
    std::cout << "Parse '42': " << *v1 << "\n";

    auto v2 = safe_stoi("abc");
    assert(!v2.has_value());
    std::cout << "Parse 'abc': nullopt (correct)\n";

    auto d1 = safe_divide(10, 3);
    assert(d1.has_value());
    std::cout << "10 / 3 = " << *d1 << "\n";

    auto d2 = safe_divide(10, 0);
    assert(!d2.has_value());
    std::cout << "10 / 0 = nullopt (correct)\n";
}

// ============================================================================
// Exercise 3: Move Semantics
// Implement a move-aware resource wrapper.
// ============================================================================

// TODO: Implement a ResourceHolder class that owns dynamically allocated data.
// It should:
// - Have a constructor that allocates
// - Have a move constructor (noexcept)
// - Have a move assignment operator (noexcept)
// - Disable copy operations
// - Have a size() method
class ResourceHolder {
    int* data_;
    size_t size_;
public:
    // TODO: Constructor — allocate `size` ints, default-initialized to 0
    explicit ResourceHolder(size_t size)
        : data_(new int[size]()), size_(size) {
        // Allocate data_
    }

    // TODO: Move constructor
    ResourceHolder(ResourceHolder&& other) noexcept
        // Initialize from other, null out other
        : data_(nullptr), size_(0) {
    }

    // TODO: Move assignment operator
    ResourceHolder& operator=(ResourceHolder&& other) noexcept {
        // Swap/transfer resources
        return *this;
    }

    // Deleted copy operations
    ResourceHolder(const ResourceHolder&) = delete;
    ResourceHolder& operator=(const ResourceHolder&) = delete;

    ~ResourceHolder() { delete[] data_; }

    size_t size() const { return size_; }
    int* data() { return data_; }
    const int* data() const { return data_; }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Move Semantics ===\n";

    ResourceHolder a(10);
    assert(a.size() == 10);

    // Move construction
    ResourceHolder b = std::move(a);
    assert(b.size() == 10);
    assert(a.size() == 0);  // a is now empty
    std::cout << "Move construction: OK\n";

    // Move assignment
    ResourceHolder c(5);
    c = std::move(b);
    assert(c.size() == 10);
    assert(b.size() == 0);
    std::cout << "Move assignment: OK\n";

    // Chain move
    ResourceHolder d = std::move(ResourceHolder(3));
    assert(d.size() == 3);
    std::cout << "Chain move: OK\n";
}

// ============================================================================
// Exercise 4: std::variant and std::visit
// Build a simple calculator using std::variant for value types.
// ============================================================================

using Number = std::variant<int, double>;

// TODO: Add two Numbers. If both are int, return int result. If either is double, return double result.
Number add_numbers(const Number& a, const Number& b) {
    // Use std::visit with overloaded lambdas or std::holds_alternative
    return 0;
}

// TODO: Multiply two Numbers with the same rules as add_numbers.
Number multiply_numbers(const Number& a, const Number& b) {
    // Your code here
    return 0;
}

// TODO: Return a string representation of a Number
std::string number_to_string(const Number& n) {
    // Use std::visit
    return "";
}

void exercise4() {
    std::cout << "\n=== Exercise 4: std::variant ===\n";

    Number a = 3;       // int
    Number b = 2.5;     // double

    auto sum = add_numbers(a, b);
    assert(std::holds_alternative<double>(sum));
    assert(std::get<double>(sum) == 5.5);
    std::cout << "3 + 2.5 = " << number_to_string(sum) << "\n";

    auto prod = multiply_numbers(a, Number(4));
    assert(std::holds_alternative<int>(prod));
    assert(std::get<int>(prod) == 12);
    std::cout << "3 * 4 = " << number_to_string(prod) << "\n";

    auto dsum = add_numbers(Number(2.5), Number(3.5));
    assert(std::holds_alternative<double>(dsum));
    std::cout << "2.5 + 3.5 = " << number_to_string(dsum) << "\n";
}

// ============================================================================
// Exercise 5: Modern C++ Pipeline
// Combine multiple modern features to build a data processing pipeline.
// ============================================================================

struct Product {
    std::string name;
    double price;
    int quantity;
};

// TODO: Given a vector of Products, return only those with quantity > 0 and price < max_price.
// Use std::copy_if, std::vector, and a lambda.
std::vector<Product> filter_products(const std::vector<Product>& products,
                                     double max_price) {
    // Your code here
    return {};
}

// TODO: Given a vector of filtered Products, compute the total value (price * quantity).
// Use std::accumulate with a lambda.
double total_value(const std::vector<Product>& products) {
    // Your code here
    return 0.0;
}

// TODO: Find the product with the highest total value (price * quantity).
// Return std::optional — nullopt if the vector is empty.
std::optional<Product> best_value_product(const std::vector<Product>& products) {
    // Your code here
    return std::nullopt;
}

// TODO: Build a summary string like "Widget(10) | Gadget(5)" from a vector of products.
// Use a loop with structured bindings or std::accumulate.
std::string summarize(const std::vector<Product>& products) {
    // Your code here
    return "";
}

void exercise5() {
    std::cout << "\n=== Exercise 5: Modern C++ Pipeline ===\n";

    std::vector<Product> inventory = {
        {"Widget", 9.99, 10},
        {"Gadget", 24.99, 5},
        {"Doohickey", 4.99, 0},   // out of stock
        {"Thingamajig", 99.99, 2},
        {"Whatchamacallit", 14.99, 8}
    };

    auto affordable = filter_products(inventory, 20.0);
    assert(affordable.size() == 3);  // Widget, Doohickey (qty 0 filtered), Widget
    std::cout << "Affordable in-stock products: " << affordable.size() << "\n";

    double total = total_value(affordable);
    std::cout << "Total value: $" << total << "\n";
    assert(total > 0);

    auto best = best_value_product(affordable);
    assert(best.has_value());
    std::cout << "Best value: " << best->name << "\n";

    std::string summary = summarize(inventory);
    std::cout << "Summary: " << summary << "\n";
    assert(!summary.empty());
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 08: Modern C++ Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
