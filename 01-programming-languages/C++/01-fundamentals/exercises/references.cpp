/*
 * Exercise: References and Pointers in C++
 * Difficulty: ★★☆☆☆ (2/5)
 * Learning Objectives:
 *   - Understand references vs pointers
 *   - Practice pass-by-reference and pass-by-pointer
 *   - Learn about const references
   *   - Master reference collapsing rules
 */

#include <iostream>
#include <string>
using namespace std;

/*
 * TODO 1: Implement a function that swaps two integers using references
 */
void swap_refs(int &a, int &b) {
    /* Your code here */
}

/*
 * TODO 2: Implement a function that swaps two integers using pointers
 */
void swap_ptrs(int *a, int *b) {
    /* Your code here */
}

/*
 * TODO 3: Implement a function that modifies a string through a reference
 * Convert the string to uppercase
 */
void to_uppercase(string &str) {
    /* Your code here */
}

/*
 * TODO 4: Implement a function that takes a const reference
 * Print the value without modifying it
 */
void print_value(const int &val) {
    /* Your code here */
}

/*
 * TODO 5: Implement a function that returns a reference
 * Find and return the larger of two integers
 */
int& find_larger(int &a, int &b) {
    /* Your code here */
    return a; /* placeholder */
}

/*
 * TODO 6: Demonstrate lvalue and rvalue references
 * Show the difference between:
 * int &lref = x;      // lvalue reference
 * int &&rref = 10;    // rvalue reference
 */

/*
 * TODO 7: Implement perfect forwarding
 * Create a wrapper function that forwards arguments
 */
template<typename T>
void wrapper(T &&arg) {
    /* Your code here - forward to another function */
}

int main() {
    /*
     * TODO 8: Test all functions above
     * Test swap_refs
     * Test swap_ptrs
     * Test to_uppercase
     * Test print_value
     * Test find_larger
     */

    /* Test cases */
    /*
    cout << "=== Test Cases ===" << endl;

    // Test swap_refs
    int x = 5, y = 10;
    cout << "Before swap: x=" << x << ", y=" << y << endl;
    swap_refs(x, y);
    cout << "After swap_refs: x=" << x << ", y=" << y << endl;

    // Test swap_ptrs
    x = 5; y = 10;
    swap_ptrs(&x, &y);
    cout << "After swap_ptrs: x=" << x << ", y=" << y << endl;

    // Test to_uppercase
    string str = "hello world";
    to_uppercase(str);
    cout << "Uppercase: " << str << " (expected: HELLO WORLD)" << endl;

    // Test print_value
    print_value(42);

    // Test find_larger
    int a = 15, b = 20;
    int &larger = find_larger(a, b);
    cout << "Larger: " << larger << " (expected: 20)" << endl;

    // Test lvalue/rvalue references
    int val = 100;
    int &lref = val;
    int &&rref = 200;
    cout << "lvalue ref: " << lref << ", rvalue ref: " << rref << endl;
    */

    return 0;
}
