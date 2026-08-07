/*
 * Exercise: Modern C++ Features
 * Difficulty: ★★★★☆ (4/5)
 * Learning Objectives:
 *   - Master lambda expressions
 *   - Understand move semantics
 *   - Practice structured bindings
 *   - Learn about optional, variant, and any
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <optional>
#include <variant>
#include <any>
#include <tuple>
#include <map>
#include <string>
using namespace std;

/*
 * TODO 1: Implement lambda expressions
 * - Basic lambda
 * - Lambda with capture (by value, by reference)
 * - Lambda with parameters and return type
 */

/*
 * TODO 2: Use lambdas with STL algorithms
 * - sort with custom comparator
 * - transform to transform elements
 * - accumulate with lambda
 * - find_if to search
 */

/*
 * TODO 3: Implement perfect forwarding with universal references
 */
template<typename T>
void wrapper(T &&arg) {
    /* Your code here - forward to another function */
}

/*
 * TODO 4: Demonstrate move semantics
 * - Move constructor
 * - Move assignment operator
 * - std::move usage
 */
class MoveDemo {
private:
    int *data;

public:
    MoveDemo(int value);
    ~MoveDemo();
    MoveDemo(MoveDemo &&other) noexcept;
    MoveDemo& operator=(MoveDemo &&other) noexcept;
    int get_value() const;
};

/*
 * TODO 5: Use structured bindings (C++17)
 * - Bind to pair
 * - Bind to tuple
 * - Bind to map elements
 */

/*
 * TODO 6: Demonstrate std::optional
 * - Function that may not return a value
 * - Check and extract value
 */
optional<int> find_even(const vector<int> &v) {
    /* Your code here - return first even number or nullopt */
    return nullopt;
}

/*
 * TODO 7: Demonstrate std::variant
 * - Create a variant of different types
 * - Use std::visit to handle different types
 */
using Value = variant<int, double, string>;

void print_variant(const Value &v) {
    /* Your code here */
}

/*
 * TODO 8: Demonstrate std::any
 * - Store different types
 * - Retrieve with type checking
 */

/*
 * TODO 9: Use fold expressions (C++17)
 * - Create a variadic template function
 * - Use fold to process all arguments
 */
template<typename... Args>
auto sum(Args... args) {
    /* Your code here */
    return (args + ...);
}

/*
 * TODO 10: Demonstrate if constexpr (C++17)
 * - Compile-time branching
 */

/*
 * TODO 11: Use std::invoke and std::apply
 */

/*
 * TODO 12: Implement a simple coroutine concept (preview)
 */

int main() {
    /* Test cases */
    /*
    cout << "=== Modern C++ Test Cases ===" << endl;

    // Test lambdas
    cout << "\n--- Lambdas ---" << endl;
    auto add = [](int a, int b) { return a + b; };
    cout << "Lambda add: " << add(3, 4) << " (expected: 7)" << endl;

    int x = 10;
    auto capture_by_value = [x]() { return x; };
    auto capture_by_ref = [&x]() { x += 5; };
    cout << "Capture by value: " << capture_by_value() << " (expected: 10)" << endl;
    capture_by_ref();
    cout << "Capture by ref: " << x << " (expected: 15)" << endl;

    // Test with STL
    cout << "\n--- Lambda with STL ---" << endl;
    vector<int> v = {5, 2, 8, 1, 9, 3};
    sort(v.begin(), v.end(), [](int a, int b) { return a > b; });
    cout << "Sorted descending: ";
    for (int n : v) cout << n << " ";
    cout << endl;

    vector<int> doubled(v.size());
    transform(v.begin(), v.end(), doubled.begin(), [](int n) { return n * 2; });
    cout << "Doubled: ";
    for (int n : doubled) cout << n << " ";
    cout << endl;

    // Test structured bindings
    cout << "\n--- Structured Bindings ---" << endl;
    pair<string, int> person = {"Alice", 25};
    auto [name, age] = person;
    cout << "Name: " << name << ", Age: " << age << endl;

    map<string, int> scores = {{"Bob", 90}, {"Charlie", 85}};
    for (const auto &[student, score] : scores) {
        cout << student << ": " << score << endl;
    }

    // Test optional
    cout << "\n--- Optional ---" << endl;
    vector<int> nums = {1, 3, 5, 8, 9};
    optional<int> even = find_even(nums);
    if (even) {
        cout << "Found even: " << *even << " (expected: 8)" << endl;
    }

    // Test variant
    cout << "\n--- Variant ---" << endl;
    Value v1 = 42;
    Value v2 = 3.14;
    Value v3 = "Hello";
    print_variant(v1);
    print_variant(v2);
    print_variant(v3);

    // Test fold expression
    cout << "\n--- Fold Expressions ---" << endl;
    cout << "Sum: " << sum(1, 2, 3, 4, 5) << " (expected: 15)" << endl;

    // Test move semantics
    cout << "\n--- Move Semantics ---" << endl;
    MoveDemo m1(100);
    MoveDemo m2 = move(m1);
    cout << "m2 value: " << m2.get_value() << " (expected: 100)" << endl;
    */

    return 0;
}
