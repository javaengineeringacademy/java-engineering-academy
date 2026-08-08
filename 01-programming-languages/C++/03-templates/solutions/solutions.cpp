// Module 03: Templates — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <string>
#include <vector>
#include <type_traits>
#include <algorithm>
#include <cctype>
#include <cassert>
#include <stdexcept>

// ============================================================================
// Exercise 1 Solution: Generic Swap and Min
// ============================================================================

template <typename T>
void swap_values(T& a, T& b) {
    T temp = std::move(a);
    a = std::move(b);
    b = std::move(temp);
}

template <typename T>
T min_value(T a, T b) {
    return (a < b) ? a : b;
}

void exercise1() {
    std::cout << "\n=== Exercise 1: Function Templates ===\n";

    int a = 5, b = 10;
    swap_values(a, b);
    assert(a == 10 && b == 5);
    std::cout << "Swapped ints: " << a << ", " << b << "\n";

    double x = 3.14, y = 2.71;
    swap_values(x, y);
    assert(x == 2.71 && y == 3.14);

    assert(min_value(3, 7) == 3);
    assert(min_value(3.14, 2.71) == 2.71);

    std::string s1 = "apple", s2 = "banana";
    swap_values(s1, s2);
    assert(s1 == "banana" && s2 == "apple");

    std::cout << "min(3,7) = " << min_value(3, 7) << "\n";
    std::cout << "min(3.14,2.71) = " << min_value(3.14, 2.71) << "\n";
    std::cout << "All swap and min tests passed!\n";
}

// ============================================================================
// Exercise 2 Solution: Generic Stack
// ============================================================================

template <typename T>
class Stack {
private:
    std::vector<T> elements_;

public:
    void push(const T& elem) {
        elements_.push_back(elem);
    }

    T pop() {
        if (elements_.empty()) {
            throw std::out_of_range("Stack::pop() called on empty stack");
        }
        T top = std::move(elements_.back());
        elements_.pop_back();
        return top;
    }

    const T& top() const {
        if (elements_.empty()) {
            throw std::out_of_range("Stack::top() called on empty stack");
        }
        return elements_.back();
    }

    bool empty() const { return elements_.empty(); }
    size_t size() const { return elements_.size(); }
};

void exercise2() {
    std::cout << "\n=== Exercise 2: Class Template ===\n";

    Stack<int> int_stack;
    int_stack.push(10);
    int_stack.push(20);
    assert(int_stack.size() == 2);
    assert(int_stack.top() == 20);
    assert(int_stack.pop() == 20);
    assert(int_stack.pop() == 10);
    assert(int_stack.empty());

    Stack<std::string> str_stack;
    str_stack.push("hello");
    str_stack.push("world");
    assert(str_stack.top() == "world");
    assert(str_stack.pop() == "world");
    assert(str_stack.pop() == "hello");

    bool caught = false;
    try {
        int_stack.pop();
    } catch (const std::out_of_range& e) {
        caught = true;
    }
    assert(caught);

    std::cout << "All stack tests passed!\n";
}

// ============================================================================
// Exercise 3 Solution: Template Specialization — Custom Printer
// ============================================================================

template <typename T>
class Printer {
public:
    void print(const T& val) {
        std::cout << val << "\n";
    }
};

template <>
class Printer<bool> {
public:
    void print(const bool& val) {
        std::cout << (val ? "TRUE" : "FALSE") << "\n";
    }
};

template <>
class Printer<std::string> {
public:
    void print(const std::string& val) {
        std::string upper = val;
        std::transform(upper.begin(), upper.end(), upper.begin(),
                       [](unsigned char c) { return std::toupper(c); });
        std::cout << upper << "\n";
    }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Template Specialization ===\n";

    Printer<int> pi;
    pi.print(42);

    Printer<bool> pb;
    pb.print(true);
    pb.print(false);

    Printer<std::string> ps;
    ps.print("hello");
    ps.print("World");

    std::cout << "All printer tests passed!\n";
}

// ============================================================================
// Exercise 4 Solution: Variadic Templates
// ============================================================================

void print_all() {
    std::cout << "\n";
}

template <typename T, typename... Args>
void print_all(const T& first, const Args&... rest) {
    std::cout << first;
    if constexpr (sizeof...(rest) > 0) {
        std::cout << " ";
        print_all(rest...);
    } else {
        std::cout << "\n";
    }
}

template <typename... Args>
void print_all_fold(const Args&... args) {
    ((std::cout << args << " "), ...);
    std::cout << "\n";
}

void exercise4() {
    std::cout << "\n=== Exercise 4: Variadic Templates ===\n";

    print_all();
    print_all(42);
    print_all(1, "hello", 3.14, 'x');
    print_all_fold(1, "hello", 3.14, 'x');

    std::cout << "All variadic tests passed!\n";
}

// ============================================================================
// Exercise 5 Solution: SFINAE / Concepts
// ============================================================================

template <typename T>
std::enable_if_t<std::is_arithmetic_v<T>, T>
safe_add(T a, T b) {
    return a + b;
}

void exercise5() {
    std::cout << "\n=== Exercise 5: SFINAE ===\n";

    std::cout << "safe_add(3, 4) = " << safe_add(3, 4) << "\n";
    std::cout << "safe_add(1.5, 2.5) = " << safe_add(1.5, 2.5) << "\n";

    std::cout << "All SFINAE tests passed!\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 03: Templates Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
