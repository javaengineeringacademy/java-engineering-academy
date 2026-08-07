/*
 * Exercise: Vector in C++ STL
 * Difficulty: ★★★☆☆ (3/5)
 * Learning Objectives:
 *   - Master std::vector operations
 *   - Understand iterators and range-based for loops
 *   - Practice inserting, deleting, and searching
 *   - Learn about vector capacity and performance
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <numeric>
using namespace std;

/*
 * TODO 1: Demonstrate vector creation and initialization
 * - Default constructor
 * - Fill constructor
 * - Range constructor
 * - Initializer list
 */

/*
 * TODO 2: Demonstrate element access
 * - operator[], at(), front(), back(), data()
 * - Show the difference between [] and at() (bounds checking)
 */

/*
 * TODO 3: Demonstrate iterators
 * - begin(), end(), rbegin(), rend()
 * - const_iterator usage
 * - Iterator arithmetic
 */

/*
 * TODO 4: Demonstrate modification operations
 * - push_back, emplace_back
 * - pop_back
 * - insert, emplace
 * - erase, clear
 */

/*
 * TODO 5: Demonstrate capacity operations
 * - size(), capacity(), empty()
 * - reserve(), shrink_to_fit()
 * - resize()
 */

/*
 * TODO 6: Implement a function that removes duplicates from a sorted vector
 * Use std::unique and erase
 */
vector<int> remove_duplicates(vector<int> v) {
    /* Your code here */
    return v;
}

/*
 * TODO 7: Implement a function that merges two sorted vectors
 * Return a new sorted vector
 */
vector<int> merge_sorted(const vector<int> &a, const vector<int> &b) {
    /* Your code here */
    return {};
}

/*
 * TODO 8: Implement a function that rotates a vector
 * Rotate left by n positions
 */
void rotate_left(vector<int> &v, int n) {
    /* Your code here */
}

/*
 * TODO 9: Demonstrate emplace_back vs push_back
 * Show the difference in efficiency with a simple struct
 */

/*
 * TODO 10: Implement a function that finds all elements greater than a value
 * Return a new vector with those elements
 */
vector<int> find_greater(const vector<int> &v, int threshold) {
    /* Your code here */
    return {};
}

int main() {
    /* Test cases */
    /*
    cout << "=== Vector Test Cases ===" << endl;

    // Test creation
    vector<int> v1;                          // default
    vector<int> v2(5, 10);                   // fill: 5 elements of 10
    vector<int> v3 = {1, 2, 3, 4, 5};       // initializer list
    vector<int> v4(v3.begin(), v3.end());    // range

    cout << "v2 size: " << v2.size() << " (expected: 5)" << endl;
    cout << "v3: ";
    for (int x : v3) cout << x << " ";
    cout << endl;

    // Test element access
    cout << "v3[0]: " << v3[0] << " (expected: 1)" << endl;
    cout << "v3.at(2): " << v3.at(2) << " (expected: 3)" << endl;
    cout << "v3.front(): " << v3.front() << " (expected: 1)" << endl;
    cout << "v3.back(): " << v3.back() << " (expected: 5)" << endl;

    // Test modification
    v1.push_back(100);
    v1.push_back(200);
    v1.pop_back();
    cout << "After push/pop: " << v1[0] << " (expected: 100)" << endl;

    // Test remove_duplicates
    vector<int> sorted = {1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
    vector<int> unique = remove_duplicates(sorted);
    cout << "After removing duplicates: ";
    for (int x : unique) cout << x << " ";
    cout << "(expected: 1 2 3 4 5)" << endl;

    // Test merge_sorted
    vector<int> a = {1, 3, 5, 7};
    vector<int> b = {2, 4, 6, 8};
    vector<int> merged = merge_sorted(a, b);
    cout << "Merged: ";
    for (int x : merged) cout << x << " ";
    cout << "(expected: 1 2 3 4 5 6 7 8)" << endl;

    // Test rotate_left
    vector<int> rot = {1, 2, 3, 4, 5};
    rotate_left(rot, 2);
    cout << "Rotated left by 2: ";
    for (int x : rot) cout << x << " ";
    cout << "(expected: 3 4 5 1 2)" << endl;

    // Test find_greater
    vector<int> nums = {1, 5, 10, 15, 20, 25};
    vector<int> result = find_greater(nums, 12);
    cout << "Greater than 12: ";
    for (int x : result) cout << x << " ";
    cout << "(expected: 15 20 25)" << endl;

    // Test capacity
    vector<int> cap;
    cout << "Initial capacity: " << cap.capacity() << endl;
    cap.reserve(100);
    cout << "After reserve(100): " << cap.capacity() << endl;
    */

    return 0;
}
