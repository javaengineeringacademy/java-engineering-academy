// Module 01: Fundamentals — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <cctype>
#include <cassert>
#include <cstring>

// ============================================================================
// Exercise 1 Solution: Variables in C++
// ============================================================================

void exercise1() {
    std::cout << "\n=== Exercise 1: Variables ===\n";

    // TODO 1: Variable declarations
    int intVar = 42;
    double doubleVar = 3.14;
    float floatVar = 2.71f;
    char charVar = 'A';
    bool boolVar = true;
    std::string stringVar = "Hello, C++!";

    // TODO 2: auto type deduction
    auto a = 42;
    auto b = 3.14;
    auto c = "Hello";
    std::cout << "Type of a (int): " << typeid(a).name() << "\n";
    std::cout << "Type of b (double): " << typeid(b).name() << "\n";
    std::cout << "Type of c (const char*): " << typeid(c).name() << "\n";

    // TODO 3: const and constexpr
    const int MAX = 100;
    constexpr double PI = 3.14159;
    std::cout << "MAX: " << MAX << "\n";
    std::cout << "PI: " << PI << "\n";

    // TODO 4: Reference variables
    int x = 10;
    int &ref = x;
    ref = 20;
    std::cout << "x: " << x << ", ref: " << ref << "\n";
    assert(x == 20 && ref == 20);

    // TODO 5: Different types of initialization
    int direct_init(10);
    int copy_init = 10;
    int list_init{10};
    int uniform_init = {10};
    std::cout << "direct_init: " << direct_init << "\n";
    std::cout << "copy_init: " << copy_init << "\n";
    std::cout << "list_init: " << list_init << "\n";
    std::cout << "uniform_init: " << uniform_init << "\n";

    // TODO 6: Scope and lifetime
    int global_like = 100;
    {
        int block_scope = 200;
        std::cout << "Block scope: " << block_scope << "\n";
    }
    std::cout << "Outer scope: " << global_like << "\n";

    // TODO 7: sizeof
    std::cout << "sizeof(int): " << sizeof(int) << "\n";
    std::cout << "sizeof(long): " << sizeof(long) << "\n";
    std::cout << "sizeof(float): " << sizeof(float) << "\n";
    std::cout << "sizeof(double): " << sizeof(double) << "\n";
    std::cout << "sizeof(char): " << sizeof(char) << "\n";
    std::cout << "sizeof(bool): " << sizeof(bool) << "\n";

    std::cout << "Exercise 1 passed!\n";
}

// ============================================================================
// Exercise 2 Solution: References and Pointers
// ============================================================================

void swap_refs(int &a, int &b) {
    int temp = a;
    a = b;
    b = temp;
}

void swap_ptrs(int *a, int *b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

void to_uppercase(std::string &str) {
    for (char &c : str) {
        c = std::toupper(static_cast<unsigned char>(c));
    }
}

void print_value(const int &val) {
    std::cout << "Value: " << val << "\n";
}

int& find_larger(int &a, int &b) {
    return (a > b) ? a : b;
}

void process_value(int &val) {
    std::cout << "Lvalue ref: " << val << "\n";
}

void process_value(int &&val) {
    std::cout << "Rvalue ref: " << val << "\n";
}

template<typename T>
void wrapper(T &&arg) {
    process_value(std::forward<T>(arg));
}

void exercise2() {
    std::cout << "\n=== Exercise 2: References and Pointers ===\n";

    // Test swap_refs
    int x = 5, y = 10;
    std::cout << "Before swap: x=" << x << ", y=" << y << "\n";
    swap_refs(x, y);
    assert(x == 10 && y == 5);
    std::cout << "After swap_refs: x=" << x << ", y=" << y << "\n";

    // Test swap_ptrs
    x = 5; y = 10;
    swap_ptrs(&x, &y);
    assert(x == 10 && y == 5);
    std::cout << "After swap_ptrs: x=" << x << ", y=" << y << "\n";

    // Test to_uppercase
    std::string str = "hello world";
    to_uppercase(str);
    assert(str == "HELLO WORLD");
    std::cout << "Uppercase: " << str << "\n";

    // Test print_value
    print_value(42);

    // Test find_larger
    int a = 15, b = 20;
    int &larger = find_larger(a, b);
    assert(larger == 20);
    std::cout << "Larger: " << larger << "\n";

    // Test lvalue/rvalue references
    int val = 100;
    int &lref = val;
    int &&rref = 200;
    std::cout << "lvalue ref: " << lref << ", rvalue ref: " << rref << "\n";

    // Test perfect forwarding
    int lval = 42;
    wrapper(lval);      // Calls lvalue version
    wrapper(100);       // Calls rvalue version

    std::cout << "Exercise 2 passed!\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 01: Fundamentals Solutions ===\n";

    exercise1();
    exercise2();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
