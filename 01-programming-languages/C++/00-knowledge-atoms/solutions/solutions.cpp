// Module 00: Knowledge Atoms — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <string>
#include <vector>
#include <cctype>
#include <cassert>

// ============================================================================
// Exercise 1 Solution: Hello World
// ============================================================================

void exercise1() {
    std::cout << "\n=== Exercise 1: Hello World ===\n";

    std::cout << "Hello, World!\n";
    std::cout << "Pooja\n";
    std::cout << "The answer is " << 42 << "\n";
    std::cout << "Line 1" << std::endl;
    std::cout << "Hello\tWorld\n\"Quotes\" \\Backslash\n";
}

// ============================================================================
// Exercise 2 Solution: Data Types
// ============================================================================

void exercise2() {
    std::cout << "\n=== Exercise 2: Data Types ===\n";

    int intVar = 42;
    float floatVar = 3.14f;
    double doubleVar = 2.71828;
    char charVar = 'A';
    bool boolVar = true;

    std::cout << "sizeof(int): " << sizeof(intVar) << "\n";
    std::cout << "sizeof(float): " << sizeof(floatVar) << "\n";
    std::cout << "sizeof(double): " << sizeof(doubleVar) << "\n";
    std::cout << "sizeof(char): " << sizeof(charVar) << "\n";
    std::cout << "sizeof(bool): " << sizeof(boolVar) << "\n";

    short s = 32767;
    s++;
    std::cout << "short overflow: " << s << "\n";

    int a = 7, b = 2;
    std::cout << "7/2 (int): " << a / b << "\n";
    std::cout << "7/2 (double): " << (double)a / b << "\n";

    int_to_double:
    int i = 42;
    double d = static_cast<double>(i);
    int back = static_cast<int>(d);
    std::cout << "int->double->int: " << i << " -> " << d << " -> " << back << "\n";
}

// ============================================================================
// Exercise 3 Solution: Control Flow
// ============================================================================

bool is_prime(int n) {
    if (n < 2) return false;
    for (int i = 2; i * i <= n; i++) {
        if (n % i == 0) return false;
    }
    return true;
}

std::string fizzbuzz(int n) {
    if (n % 3 == 0 && n % 5 == 0) return "FizzBuzz";
    if (n % 3 == 0) return "Fizz";
    if (n % 5 == 0) return "Buzz";
    return std::to_string(n);
}

int factorial(int n) {
    int result = 1;
    for (int i = 2; i <= n; i++) {
        result *= i;
    }
    return result;
}

int fibonacci(int n) {
    if (n <= 0) return 0;
    if (n == 1) return 1;
    int a = 0, b = 1;
    for (int i = 2; i <= n; i++) {
        int temp = a + b;
        a = b;
        b = temp;
    }
    return b;
}

void exercise3() {
    std::cout << "\n=== Exercise 3: Control Flow ===\n";

    assert(is_prime(2) == true);
    assert(is_prime(7) == true);
    assert(is_prime(4) == false);
    assert(is_prime(1) == false);

    assert(fizzbuzz(15) == "FizzBuzz");
    assert(fizzbuzz(3) == "Fizz");
    assert(fizzbuzz(5) == "Buzz");
    assert(fizzbuzz(7) == "7");

    assert(factorial(0) == 1);
    assert(factorial(5) == 120);
    assert(factorial(10) == 3628800);

    assert(fibonacci(0) == 0);
    assert(fibonacci(1) == 1);
    assert(fibonacci(10) == 55);

    std::cout << "All control flow tests passed!\n";
}

// ============================================================================
// Exercise 4 Solution: Functions
// ============================================================================

void swap_ptrs(int *a, int *b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

void swap_refs(int &a, int &b) {
    int temp = a;
    a = b;
    b = temp;
}

std::string greet(const std::string &name, const std::string &greeting) {
    return greeting + ", " + name + "!";
}

void exercise4() {
    std::cout << "\n=== Exercise 4: Functions ===\n";

    int x = 5, y = 10;
    swap_ptrs(&x, &y);
    assert(x == 10 && y == 5);

    x = 5; y = 10;
    swap_refs(x, y);
    assert(x == 10 && y == 5);

    assert(greet("World") == "Hello, World!");
    assert(greet("C++", "Welcome") == "Welcome, C++!");

    assert(add(2, 3) == 5);
    assert(add(2.5, 3.5) == 6.0);
    assert(add(std::string("Hello"), std::string(" World")) == "Hello World");

    std::cout << "All function tests passed!\n";
}

// ============================================================================
// Exercise 5 Solution: Arrays and Strings
// ============================================================================

int find_max(const int arr[], int size) {
    int max_val = arr[0];
    for (int i = 1; i < size; i++) {
        if (arr[i] > max_val) max_val = arr[i];
    }
    return max_val;
}

void reverse_array(int arr[], int size) {
    for (int i = 0; i < size / 2; i++) {
        int temp = arr[i];
        arr[i] = arr[size - 1 - i];
        arr[size - 1 - i] = temp;
    }
}

bool is_palindrome(const std::string &s) {
    int left = 0, right = s.size() - 1;
    while (left < right) {
        if (s[left] != s[right]) return false;
        left++;
        right--;
    }
    return true;
}

int count_vowels(const std::string &s) {
    int count = 0;
    for (char c : s) {
        char lower = std::tolower(c);
        if (lower == 'a' || lower == 'e' || lower == 'i' || lower == 'o' || lower == 'u') {
            count++;
        }
    }
    return count;
}

std::string to_uppercase(const std::string &s) {
    std::string result = s;
    for (char &c : result) {
        c = std::toupper(c);
    }
    return result;
}

void exercise5() {
    std::cout << "\n=== Exercise 5: Arrays and Strings ===\n";

    int arr[] = {3, 7, 2, 9, 1};
    assert(find_max(arr, 5) == 9);

    reverse_array(arr, 5);
    assert(arr[0] == 1 && arr[4] == 3);

    assert(is_palindrome("racecar") == true);
    assert(is_palindrome("hello") == false);
    assert(is_palindrome("madam") == true);

    assert(count_vowels("hello") == 2);
    assert(count_vowels("AEIOU") == 5);
    assert(count_vowels("rhythm") == 0);

    assert(to_uppercase("hello") == "HELLO");
    assert(to_uppercase("Hello World") == "HELLO WORLD");

    std::cout << "All array/string tests passed!\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 00: Knowledge Atoms Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
