/*
 * Exercise: Dynamic Memory in C++
 * Difficulty: ★★★☆☆ (3/5)
 * Learning Objectives:
 *   - Master new and delete operators
 *   - Understand dynamic array allocation
 *   - Learn about placement new
 *   - Practice RAII principles
 */

#include <iostream>
#include <string>
using namespace std;

/*
 * TODO 1: Demonstrate basic new and delete
 * - Allocate a single int
 * - Allocate a double
 * - Free both with delete
 */

/*
 * TODO 2: Demonstrate new[] and delete[]
 * - Allocate an array of 10 integers
 * - Initialize all elements
 * - Free with delete[]
 */

/*
 * TODO 3: Implement a simple DynamicArray class
 * - Allocate memory in constructor
 * - Free memory in destructor
 * - Implement Rule of Three
 */
class DynamicArray {
private:
    int *data;
    int size;

public:
    DynamicArray(int s);
    ~DynamicArray();
    DynamicArray(const DynamicArray &other);
    DynamicArray& operator=(const DynamicArray &other);
    int& operator[](int index);
    int get_size() const;
};

/*
 * TODO 4: Demonstrate placement new
 * - Allocate raw memory
 * - Construct object in that memory
 * - Manually call destructor
 */

/*
 * TODO 5: Implement a MemoryGuard class (RAII wrapper)
 * - Allocate memory in constructor
 * - Free memory in destructor
 * - Prevent copying
 */
class MemoryGuard {
private:
    void *ptr;
    size_t size;

public:
    MemoryGuard(size_t s);
    ~MemoryGuard();
    // Delete copy operations
    MemoryGuard(const MemoryGuard&) = delete;
    MemoryGuard& operator=(const MemoryGuard&) = delete;
    void* get();
};

/*
 * TODO 6: Demonstrate smart pointer usage (preview)
 * - unique_ptr
 * - shared_ptr
 * - weak_ptr
 */

/*
 * TODO 7: Implement a simple string class with dynamic memory
 * - Allocate memory for characters
 * - Implement copy constructor
 * - Implement move constructor
 * - Implement destructor
 */
class SimpleString {
private:
    char *data;
    int length;

public:
    SimpleString(const char *str = "");
    ~SimpleString();
    SimpleString(const SimpleString &other);
    SimpleString(SimpleString &&other) noexcept;
    SimpleString& operator=(const SimpleString &other);
    SimpleString& operator=(SimpleString &&other) noexcept;
    const char* c_str() const;
    int size() const;
};

int main() {
    /* Test cases */
    /*
    cout << "=== Dynamic Memory Test Cases ===" << endl;

    // Test basic new/delete
    int *p = new int(42);
    cout << "Allocated int: " << *p << " (expected: 42)" << endl;
    delete p;

    // Test new[]/delete[]
    int *arr = new int[5];
    for (int i = 0; i < 5; i++) arr[i] = i * 10;
    cout << "Array: ";
    for (int i = 0; i < 5; i++) cout << arr[i] << " ";
    cout << endl;
    delete[] arr;

    // Test DynamicArray
    DynamicArray da(5);
    for (int i = 0; i < 5; i++) da[i] = i + 1;
    cout << "DynamicArray: ";
    for (int i = 0; i < 5; i++) cout << da[i] << " ";
    cout << endl;

    // Test copy constructor
    DynamicArray da2 = da;
    cout << "Copied array: ";
    for (int i = 0; i < 5; i++) cout << da2[i] << " ";
    cout << endl;

    // Test placement new
    char buffer[sizeof(int)];
    int *placement_ptr = new(buffer) int(100);
    cout << "Placement new: " << *placement_ptr << " (expected: 100)" << endl;
    placement_ptr->~int();

    // Test MemoryGuard
    {
        MemoryGuard guard(100);
        cout << "MemoryGuard allocated" << endl;
    } // Memory freed here

    // Test smart pointers (preview)
    unique_ptr<int> uptr = make_unique<int>(50);
    shared_ptr<int> sptr = make_shared<int>(60);
    weak_ptr<int> wptr = sptr;
    cout << "unique_ptr: " << *uptr << endl;
    cout << "shared_ptr: " << *sptr << endl;
    cout << "weak_ptr lock: " << *wptr.lock() << endl;

    // Test SimpleString
    SimpleString s1("Hello");
    SimpleString s2 = s1;  // Copy
    SimpleString s3 = move(s1);  // Move
    cout << "s2: " << s2.c_str() << " (expected: Hello)" << endl;
    cout << "s3: " << s3.c_str() << " (expected: Hello)" << endl;
    */

    return 0;
}
