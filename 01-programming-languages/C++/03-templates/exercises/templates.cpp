/*
 * Exercise: Templates in C++
 * Difficulty: ★★★★☆ (4/5)
 * Learning Objectives:
 *   - Understand function templates
 *   - Master class templates
 *   - Learn about template specialization
 *   - Practice template metaprogramming basics
 */

#include <iostream>
#include <string>
#include <vector>
using namespace std;

/*
 * TODO 1: Create a function template to find the maximum of two values
 * Template parameter: typename T
 * Works with int, double, string, etc.
 */
template<typename T>
T find_max(T a, T b) {
    /* Your code here */
    return a; /* placeholder */
}

/*
 * TODO 2: Create a function template to swap two values
 */
template<typename T>
void my_swap(T &a, T &b) {
    /* Your code here */
}

/*
 * TODO 3: Create a function template to print any container
 * Should work with vector, array, etc.
 */
template<typename T>
void print_container(const T &container) {
    /* Your code here */
}

/*
 * TODO 4: Create a class template for a generic Stack
 * - Push, pop, peek, is_empty, size operations
 * - Use dynamic array internally
 */
template<typename T>
class Stack {
private:
    T *data;
    int top_index;
    int capacity;

public:
    Stack(int cap = 100);
    ~Stack();
    void push(T value);
    T pop();
    T peek() const;
    bool is_empty() const;
    int size() const;
};

/*
 * TODO 5: Create a class template for a Pair
 * - Two different types T1 and T2
 * - Methods: first(), second(), swap()
 */
template<typename T1, typename T2>
class Pair {
private:
    T1 first_val;
    T2 second_val;

public:
    Pair(T1 f, T2 s);
    T1 first() const;
    T2 second() const;
    void swap(Pair &other);
};

/*
 * TODO 6: Demonstrate template specialization
 * - Create a template function
 * - Specialize it for a specific type (e.g., const char*)
 */

/*
 * TODO 7: Demonstrate non-type template parameters
 * - Create a template with an int parameter
 * - Example: Array<T, N> where N is the size
 */
template<typename T, int N>
class FixedArray {
private:
    T data[N];

public:
    T& operator[](int index);
    int size() const { return N; }
};

/*
 * TODO 8: Create a variadic template function
 * - Print any number of arguments
 */
template<typename T>
void print_args(T value) {
    cout << value << endl;
}

template<typename T, typename... Args>
void print_args(T first, Args... rest) {
    cout << first << " ";
    print_args(rest...);
}

int main() {
    /* Test cases */
    /*
    cout << "=== Template Test Cases ===" << endl;

    // Test function templates
    cout << "Max of 5, 10: " << find_max(5, 10) << " (expected: 10)" << endl;
    cout << "Max of 3.14, 2.71: " << find_max(3.14, 2.71) << " (expected: 3.14)" << endl;
    cout << "Max of 'a', 'z': " << find_max('a', 'z') << " (expected: z)" << endl;

    // Test swap
    int a = 5, b = 10;
    my_swap(a, b);
    cout << "After swap: a=" << a << ", b=" << b << endl;

    // Test print_container
    vector<int> v = {1, 2, 3, 4, 5};
    cout << "Vector: ";
    print_container(v);

    // Test Stack
    Stack<int> stack;
    stack.push(10);
    stack.push(20);
    stack.push(30);
    cout << "Stack top: " << stack.peek() << " (expected: 30)" << endl;
    cout << "Stack size: " << stack.size() << " (expected: 3)" << endl;

    // Test Pair
    Pair<string, int> student("Alice", 95);
    cout << "Pair: " << student.first() << ", " << student.second() << endl;

    // Test FixedArray
    FixedArray<int, 5> arr;
    for (int i = 0; i < 5; i++) arr[i] = i * 10;
    cout << "FixedArray[3]: " << arr[3] << " (expected: 30)" << endl;

    // Test variadic template
    cout << "Variadic: ";
    print_args(1, 2.5, "hello", 'c');
    */

    return 0;
}
