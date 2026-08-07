/*
 * Exercise: Map in C++ STL
 * Difficulty: ★★★☆☆ (3/5)
 * Learning Objectives:
 *   - Master std::map and std::unordered_map
 *   - Understand key-value pair operations
 *   - Practice insertion, deletion, and searching
 *   - Learn about iterator usage with maps
 */

#include <iostream>
#include <map>
#include <unordered_map>
#include <string>
#include <algorithm>
using namespace std;

/*
 * TODO 1: Demonstrate map creation and insertion
 * - Default constructor
 * - Insert with make_pair
 * - Insert with initializer list
 * - Insert with operator[]
 * - Insert with emplace
 */

/*
 * TODO 2: Demonstrate map access and search
 * - operator[] vs at()
 * - find()
 * - count()
 * - lower_bound(), upper_bound()
 */

/*
 * TODO 3: Demonstrate map modification
 * - erase()
 * - clear()
 * - swap()
 */

/*
 * TODO 4: Implement a function that counts word frequencies in a string
 * Return a map with words as keys and counts as values
 */
map<string, int> count_words(const string &text) {
    /* Your code here */
    return {};
}

/*
 * TODO 5: Implement a function that inverts a map
 * Swap keys and values (assume values are unique)
 */
map<int, string> invert_map(const map<string, int> &original) {
    /* Your code here */
    return {};
}

/*
 * TODO 6: Find the most frequent element in a map
 * Return the key with the highest value
 */
string most_frequent(const map<string, int> &freq) {
    /* Your code here */
    return "";
}

/*
 * TODO 7: Demonstrate unordered_map vs map
 * - Show the difference in ordering
 * - Show the difference in performance
 */

/*
 * TODO 8: Implement a function that merges two maps
 * If a key exists in both, sum the values
 */
map<string, int> merge_maps(const map<string, int> &a, const map<string, int> &b) {
    /* Your code here */
    return {};
}

/*
 * TODO 9: Use map with custom comparator
 * Create a map that sorts keys in descending order
 */

/*
 * TODO 10: Implement a function that groups elements by first character
 * Input: {"apple", "banana", "avocado", "blueberry"}
 * Output: {'a': ["apple", "avocado"], 'b': ["banana", "blueberry"]}
 */

int main() {
    /* Test cases */
    /*
    cout << "=== Map Test Cases ===" << endl;

    // Test map creation
    map<string, int> ages;
    ages["Alice"] = 25;
    ages["Bob"] = 30;
    ages.insert({"Charlie", 35});
    ages.emplace("Diana", 28);

    cout << "Alice's age: " << ages["Alice"] << " (expected: 25)" << endl;
    cout << "Size: " << ages.size() << " (expected: 4)" << endl;

    // Test search
    if (ages.find("Bob") != ages.end()) {
        cout << "Bob found" << endl;
    }
    cout << "Count of 'Eve': " << ages.count("Eve") << " (expected: 0)" << endl;

    // Test count_words
    string text = "the cat sat on the mat the cat";
    map<string, int> word_freq = count_words(text);
    cout << "Word frequencies:" << endl;
    for (const auto &pair : word_freq) {
        cout << "  " << pair.first << ": " << pair.second << endl;
    }

    // Test invert_map
    map<string, int> original = {{"a", 1}, {"b", 2}, {"c", 3}};
    map<int, string> inverted = invert_map(original);
    cout << "Inverted map:" << endl;
    for (const auto &pair : inverted) {
        cout << "  " << pair.first << " -> " << pair.second << endl;
    }

    // Test most_frequent
    map<string, int> freq = {{"apple", 5}, {"banana", 3}, {"cherry", 7}};
    cout << "Most frequent: " << most_frequent(freq) << " (expected: cherry)" << endl;

    // Test merge_maps
    map<string, int> m1 = {{"a", 1}, {"b", 2}};
    map<string, int> m2 = {{"b", 3}, {"c", 4}};
    map<string, int> merged = merge_maps(m1, m2);
    cout << "Merged: ";
    for (const auto &pair : merged) {
        cout << pair.first << ":" << pair.second << " ";
    }
    cout << "(expected: a:1 b:5 c:4)" << endl;

    // Test erase
    ages.erase("Bob");
    cout << "After erasing Bob, size: " << ages.size() << " (expected: 3)" << endl;

    // Demonstrate unordered_map
    unordered_map<string, int> umap;
    umap["x"] = 1;
    umap["y"] = 2;
    cout << "Unordered map (no guaranteed order): ";
    for (const auto &pair : umap) {
        cout << pair.first << ":" << pair.second << " ";
    }
    cout << endl;
    */

    return 0;
}
