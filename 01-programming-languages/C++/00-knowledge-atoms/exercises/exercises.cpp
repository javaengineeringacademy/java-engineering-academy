// Module 00: Knowledge Atoms — Exercises
// Complete each exercise by filling in the TODO sections.
// Compile: g++ -std=c++17 -Wall -Wextra -o exercises exercises.cpp

#include <iostream>
#include <string>
#include <vector>
#include <cassert>

// ============================================================================
// Exercise 1: Hello World and Basic Output
// Master the basics of C++ output and program structure.
// ============================================================================

void exercise1() {
    std::cout << "\n=== Exercise 1: Hello World ===\n";

    // TODO 1: Print "Hello, World!" to the console

    // TODO 2: Print your name on a separate line

    // TODO 3: Print multiple values on one line using <<
    // Output: "The answer is 42"

    // TODO 4: Print a newline using std::endl

    // TODO 5: Print using escape sequences: tab, backslash, quotes
    // Output: Hello\tWorld\n"Quotes" \\Backslash
}

// ============================================================================
// Exercise 2: Basic Data Types
// Understand the fundamental data types in C++.
// ============================================================================

void exercise2() {
    std::cout << "\n=== Exercise 2: Data Types ===\n";

    // TODO 1: Declare an int, float, double, char, and bool
    // Give each a meaningful value

    // TODO 2: Print the size of each type using sizeof
    // Expected: int=4, float=4, double=8, char=1, bool=1

    // TODO 3: Show integer overflow
    // Declare a short, increment until it wraps around

    // TODO 4: Demonstrate implicit type conversion
    // int divided by int vs int divided by double
    int a = 7, b = 2;
    // TODO: Print a/b (integer division) and (double)a/b (floating point)

    // TODO 5: Use static_cast to convert between types
    // Convert int to double and back
}

// ============================================================================
// Exercise 3: Control Flow
// Master if/else, loops, and switch statements.
// ============================================================================

// TODO: Implement is_prime(n) that returns true if n is prime
bool is_prime(int n) {
    // Your code here
    return false;
}

// TODO: Implement fizzbuzz(n) that returns:
// "FizzBuzz" if divisible by 3 and 5
// "Fizz" if divisible by 3
// "Buzz" if divisible by 5
// The number as string otherwise
std::string fizzbuzz(int n) {
    // Your code here
    return "";
}

// TODO: Implement factorial(n) using a loop
int factorial(int n) {
    // Your code here
    return 0;
}

// TODO: Implement fibonacci(n) that returns the nth Fibonacci number
// fibonacci(0)=0, fibonacci(1)=1, fibonacci(2)=1, fibonacci(3)=2
int fibonacci(int n) {
    // Your code here
    return 0;
}

void exercise3() {
    std::cout << "\n=== Exercise 3: Control Flow ===\n";

    assert(is_prime(2) == true);
    assert(is_prime(7) == true);
    assert(is_prime(4) == false);
    assert(is_prime(1) == false);
    std::cout << "is_prime: OK\n";

    assert(fizzbuzz(15) == "FizzBuzz");
    assert(fizzbuzz(3) == "Fizz");
    assert(fizzbuzz(5) == "Buzz");
    assert(fizzbuzz(7) == "7");
    std::cout << "fizzbuzz: OK\n";

    assert(factorial(0) == 1);
    assert(factorial(5) == 120);
    assert(factorial(10) == 3628800);
    std::cout << "factorial: OK\n";

    assert(fibonacci(0) == 0);
    assert(fibonacci(1) == 1);
    assert(fibonacci(10) == 55);
    std::cout << "fibonacci: OK\n";
}

// ============================================================================
// Exercise 4: Functions
// Understand function declarations, parameters, and return values.
// ============================================================================

// TODO: Implement swap using pointers
void swap_ptrs(int *a, int *b) {
    // Your code here
}

// TODO: Implement swap using references
void swap_refs(int &a, int &b) {
    // Your code here
}

// TODO: Implement pass-by-value, pass-by-reference, pass-by-pointer demo
void modify_all(int val, int &ref, int *ptr) {
    // TODO: Modify each parameter and explain what happens
    // val: should not affect original
    // ref: should affect original
    // ptr: should affect original
}

// TODO: Implement a function with default parameters
// greet(name, greeting="Hello") returns "Hello, name!"
std::string greet(const std::string &name, const std::string &greeting = "Hello") {
    // Your code here
    return "";
}

// TODO: Implement function overloading
int add(int a, int b) { return a + b; }
double add(double a, double b) { return a + b; }
std::string add(const std::string &a, const std::string &b) { return a + b; }

void exercise4() {
    std::cout << "\n=== Exercise 4: Functions ===\n";

    int x = 5, y = 10;
    swap_ptrs(&x, &y);
    assert(x == 10 && y == 5);
    std::cout << "swap_ptrs: OK\n";

    x = 5; y = 10;
    swap_refs(x, y);
    assert(x == 10 && y == 5);
    std::cout << "swap_refs: OK\n";

    assert(greet("World") == "Hello, World!");
    assert(greet("C++", "Welcome") == "Welcome, C++!");
    std::cout << "greet: OK\n";

    assert(add(2, 3) == 5);
    assert(add(2.5, 3.5) == 6.0);
    assert(add(std::string("Hello"), std::string(" World")) == "Hello World");
    std::cout << "overloaded add: OK\n";
}

// ============================================================================
// Exercise 5: Arrays and Strings
// Work with C-style arrays and std::string.
// ============================================================================

// TODO: Find the maximum element in an array
int find_max(const int arr[], int size) {
    // Your code here
    return 0;
}

// TODO: Reverse an array in place
void reverse_array(int arr[], int size) {
    // Your code here
}

// TODO: Check if a string is a palindrome
bool is_palindrome(const std::string &s) {
    // Your code here
    return false;
}

// TODO: Count vowels in a string
int count_vowels(const std::string &s) {
    // Your code here
    return 0;
}

// TODO: Convert string to uppercase
std::string to_uppercase(const std::string &s) {
    // Your code here
    return "";
}

void exercise5() {
    std::cout << "\n=== Exercise 5: Arrays and Strings ===\n";

    int arr[] = {3, 7, 2, 9, 1};
    assert(find_max(arr, 5) == 9);
    std::cout << "find_max: OK\n";

    reverse_array(arr, 5);
    assert(arr[0] == 1 && arr[4] == 3);
    std::cout << "reverse_array: OK\n";

    assert(is_palindrome("racecar") == true);
    assert(is_palindrome("hello") == false);
    assert(is_palindrome("madam") == true);
    std::cout << "is_palindrome: OK\n";

    assert(count_vowels("hello") == 2);
    assert(count_vowels("AEIOU") == 5);
    assert(count_vowels("rhythm") == 0);
    std::cout << "count_vowels: OK\n";

    assert(to_uppercase("hello") == "HELLO");
    assert(to_uppercase("Hello World") == "HELLO WORLD");
    std::cout << "to_uppercase: OK\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 00: Knowledge Atoms Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
