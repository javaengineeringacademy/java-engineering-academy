// Module 03: Templates — Exercises
// Complete each exercise by filling in the TODO sections.
// Compile: g++ -std=c++20 -Wall -Wextra -o exercises exercises.cpp

#include <iostream>
#include <string>
#include <vector>
#include <type_traits>
#include <cassert>

// ============================================================================
// Exercise 1: Function Template — Generic Swap and Min
// Implement a function template that swaps two values and another that
// returns the minimum of two values. Both must work for any comparable type.
// ============================================================================

// TODO: Implement swap_values — takes two references of type T and swaps them
// template <typename T>
// void swap_values(T& a, T& b) {
//     // Use a temporary variable or std::move
// }

// TODO: Implement min_value — takes two values of type T, returns the smaller
// template <typename T>
// T min_value(T a, T b) {
//     // Return the smaller value
// }

void exercise1() {
    std::cout << "\n=== Exercise 1: Function Templates ===\n";

    int a = 5, b = 10;
    // swap_values(a, b);
    // assert(a == 10 && b == 5);
    // std::cout << "Swapped ints: " << a << ", " << b << "\n";

    double x = 3.14, y = 2.71;
    // swap_values(x, y);
    // assert(x == 2.71 && y == 3.14);

    // assert(min_value(3, 7) == 3);
    // assert(min_value(3.14, 2.71) == 2.71);
    // std::cout << "min(3,7) = " << min_value(3, 7) << "\n";
    // std::cout << "min(3.14,2.71) = " << min_value(3.14, 2.71) << "\n";
}

// ============================================================================
// Exercise 2: Class Template — Generic Stack
// Implement a Stack<T> class template with push, pop, top, empty, and size.
// ============================================================================

// TODO: Implement the Stack class template
// template <typename T>
// class Stack {
// private:
//     std::vector<T> elements_;
// public:
//     void push(const T& elem);
//     T pop();
//     const T& top() const;
//     bool empty() const;
//     size_t size() const;
// };

void exercise2() {
    std::cout << "\n=== Exercise 2: Class Template ===\n";

    // Stack<int> int_stack;
    // int_stack.push(10);
    // int_stack.push(20);
    // assert(int_stack.size() == 2);
    // assert(int_stack.top() == 20);
    // assert(int_stack.pop() == 20);
    // assert(int_stack.pop() == 10);
    // assert(int_stack.empty());

    // Stack<std::string> str_stack;
    // str_stack.push("hello");
    // str_stack.push("world");
    // assert(str_stack.top() == "world");
}

// ============================================================================
// Exercise 3: Template Specialization — Custom Printer
// Create a generic Printer<T> that prints any type. Specialize it for:
// - bool: prints "TRUE" or "FALSE"
// - std::string: prints in uppercase
// ============================================================================

// TODO: Generic Printer
// template <typename T>
// class Printer {
// public:
//     void print(const T& val) {
//         std::cout << val << "\n";
//     }
// };

// TODO: Specialize for bool
// template <>
// class Printer<bool> {
// public:
//     void print(const bool& val) {
//         // Print "TRUE" or "FALSE"
//     }
// };

// TODO: Specialize for std::string (print in uppercase)
// template <>
// class Printer<std::string> {
// public:
//     void print(const std::string& val) {
//         // Convert to uppercase and print
//     }
// };

void exercise3() {
    std::cout << "\n=== Exercise 3: Template Specialization ===\n";

    // Printer<int> pi;
    // pi.print(42);           // Should print: 42

    // Printer<bool> pb;
    // pb.print(true);         // Should print: TRUE
    // pb.print(false);        // Should print: FALSE

    // Printer<std::string> ps;
    // ps.print("hello");      // Should print: HELLO
}

// ============================================================================
// Exercise 4: Variadic Templates — Print All Arguments
// Implement a function template that accepts any number of arguments
// and prints each one separated by a space.
// ============================================================================

// TODO: Base case
// void print_all() {
//     std::cout << "\n";
// }

// TODO: Recursive case
// template <typename T, typename... Args>
// void print_all(const T& first, const Args&... rest) {
//     std::cout << first;
//     if constexpr (sizeof...(rest) > 0) {
//         std::cout << " ";
//         print_all(rest...);
//     } else {
//         std::cout << "\n";
//     }
// }

void exercise4() {
    std::cout << "\n=== Exercise 4: Variadic Templates ===\n";

    // print_all();                         // Output: (newline)
    // print_all(42);                        // Output: 42
    // print_all(1, "hello", 3.14, 'x');    // Output: 1 hello 3.14 x
}

// ============================================================================
// Exercise 5: SFINAE / Concepts — Type-Safe Add
// Implement a function template `safe_add` that only compiles for types
// that support operator+. Use either SFINAE or C++20 concepts.
// ============================================================================

// TODO: Implement using SFINAE (enable_if)
// template <typename T>
// std::enable_if_t<std::is_arithmetic_v<T>, T>
// safe_add(T a, T b) {
//     return a + b;
// }

// TODO: Or implement using C++20 concepts
// template <typename T>
//     requires requires(T a, T b) { { a + b } -> std::convertible_to<T>; }
// T safe_add(T a, T b) {
//     return a + b;
// }

void exercise5() {
    std::cout << "\n=== Exercise 5: SFINAE / Concepts ===\n";

    // std::cout << safe_add(3, 4) << "\n";         // 7
    // std::cout << safe_add(1.5, 2.5) << "\n";     // 4.0
    // safe_add(std::string("a"), std::string("b")); // If using concepts: OK
    // safe_add("hello", "world");                    // Should fail with SFINAE
}

int main() {
    std::cout << "=== Module 03: Templates Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    return 0;
}
