// Module 04: STL — Exercises
// Complete each exercise by filling in the TODO sections.
// Compile: g++ -std=c++17 -Wall -Wextra -o exercises exercises.cpp

#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <numeric>
#include <string>
#include <cassert>

// ============================================================================
// Exercise 1: Vector Operations
// Implement functions that work with std::vector.
// ============================================================================

// TODO: Return a new vector with only the even numbers from the input
std::vector<int> filter_evens(const std::vector<int>& input) {
    // Use std::copy_if or a loop
    return {};
}

// TODO: Return the sum of all elements in the vector
int sum_vector(const std::vector<int>& vec) {
    // Use std::accumulate
    return 0;
}

// TODO: Return the index of the largest element (-1 if empty)
int index_of_max(const std::vector<int>& vec) {
    // Use std::max_element
    return -1;
}

// TODO: Remove all duplicates from a SORTED vector and return the result
std::vector<int> remove_duplicates(std::vector<int> sorted_vec) {
    // Use std::unique and erase
    return {};
}

void exercise1() {
    std::cout << "\n=== Exercise 1: Vector Operations ===\n";

    std::vector<int> v = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    auto evens = filter_evens(v);
    assert((evens == std::vector<int>{2, 4, 6, 8, 10}));
    std::cout << "Evens: ";
    for (int x : evens) std::cout << x << " ";
    std::cout << "\n";

    assert(sum_vector(v) == 55);
    std::cout << "Sum: " << sum_vector(v) << "\n";

    assert(index_of_max(v) == 9);
    std::cout << "Index of max: " << index_of_max(v) << "\n";

    std::vector<int> dups = {1, 1, 2, 3, 3, 3, 4};
    auto unique = remove_duplicates(dups);
    assert((unique == std::vector<int>{1, 2, 3, 4}));
    std::cout << "Unique: ";
    for (int x : unique) std::cout << x << " ";
    std::cout << "\n";
}

// ============================================================================
// Exercise 2: Map Operations
// Work with std::map to build a word frequency counter.
// ============================================================================

// TODO: Given a string, return a map of word -> frequency
// Words should be lowercased (ignore punctuation)
std::map<std::string, int> word_frequency(const std::string& text) {
    std::map<std::string, int> freq;
    // Split text into words, lowercase, count
    return freq;
}

// TODO: Return the most frequent word from the map
std::string most_frequent(const std::map<std::string, int>& freq) {
    // Find the entry with highest count
    return "";
}

void exercise2() {
    std::cout << "\n=== Exercise 2: Map Operations ===\n";

    std::string text = "the cat sat on the mat the cat";
    auto freq = word_frequency(text);

    std::cout << "Word frequencies:\n";
    for (const auto& [word, count] : freq) {
        std::cout << "  " << word << ": " << count << "\n";
    }

    assert(freq["the"] == 3);
    assert(freq["cat"] == 2);
    assert(freq["sat"] == 1);

    std::string top = most_frequent(freq);
    std::cout << "Most frequent: " << top << "\n";
    assert(top == "the");
}

// ============================================================================
// Exercise 3: Set Operations
// Implement set intersection and union using std::set.
// ============================================================================

// TODO: Return the intersection of two sets (elements in both)
std::set<int> set_intersection(const std::set<int>& a, const std::set<int>& b) {
    // Use std::set_intersection or manual approach
    return {};
}

// TODO: Return the union of two sets (elements in either)
std::set<int> set_union(const std::set<int>& a, const std::set<int>& b) {
    // Use std::set_union or manual approach
    return {};
}

void exercise3() {
    std::cout << "\n=== Exercise 3: Set Operations ===\n";

    std::set<int> a = {1, 2, 3, 4, 5};
    std::set<int> b = {3, 4, 5, 6, 7};

    auto inter = set_intersection(a, b);
    assert((inter == std::set<int>{3, 4, 5}));
    std::cout << "Intersection: ";
    for (int x : inter) std::cout << x << " ";
    std::cout << "\n";

    auto uni = set_union(a, b);
    assert((uni == std::set<int>{1, 2, 3, 4, 5, 6, 7}));
    std::cout << "Union: ";
    for (int x : uni) std::cout << x << " ";
    std::cout << "\n";
}

// ============================================================================
// Exercise 4: Algorithm — Student Grades
// Use STL algorithms to process a vector of student records.
// ============================================================================

struct Student {
    std::string name;
    int grade;
};

// TODO: Sort students by grade (highest first)
std::vector<Student> sort_by_grade(const std::vector<Student>& students) {
    // Use std::sort with a lambda comparator
    return {};
}

// TODO: Return names of students with grade >= 90
std::vector<std::string> honor_roll(const std::vector<Student>& students) {
    // Use std::copy_if and std::transform
    return {};
}

// TODO: Calculate the average grade
double average_grade(const std::vector<Student>& students) {
    // Use std::accumulate
    return 0.0;
}

void exercise4() {
    std::cout << "\n=== Exercise 4: Algorithm — Student Grades ===\n";

    std::vector<Student> students = {
        {"Alice", 85}, {"Bob", 92}, {"Charlie", 78},
        {"Diana", 95}, {"Eve", 88}
    };

    auto sorted = sort_by_grade(students);
    std::cout << "Sorted by grade:\n";
    for (const auto& s : sorted) {
        std::cout << "  " << s.name << ": " << s.grade << "\n";
    }
    assert(sorted[0].name == "Diana");  // 95

    auto honors = honor_roll(students);
    std::cout << "Honor roll: ";
    for (const auto& name : honors) std::cout << name << " ";
    std::cout << "\n";

    double avg = average_grade(students);
    std::cout << "Average: " << avg << "\n";
    assert(avg == 87.6);
}

// ============================================================================
// Exercise 5: Data Transformation Pipeline
// Build a mini data pipeline using STL algorithms and containers.
// ============================================================================

// Given a vector of strings representing CSV rows like "Alice,95",
// parse them, sort by score, and return top N students.

struct Record {
    std::string name;
    int score;
};

// TODO: Parse CSV lines into Record structs
std::vector<Record> parse_records(const std::vector<std::string>& csv_lines) {
    // Split each line on comma, convert score to int
    return {};
}

// TODO: Return top N records by score
std::vector<Record> top_n(const std::vector<Record>& records, int n) {
    // Sort and take first n
    return {};
}

void exercise5() {
    std::cout << "\n=== Exercise 5: Data Pipeline ===\n";

    std::vector<std::string> csv = {
        "Alice,95", "Bob,87", "Charlie,92",
        "Diana,78", "Eve,99", "Frank,85"
    };

    auto records = parse_records(csv);
    assert(records.size() == 6);

    auto top3 = top_n(records, 3);
    assert(top3.size() == 3);
    std::cout << "Top 3 students:\n";
    for (const auto& r : top3) {
        std::cout << "  " << r.name << ": " << r.score << "\n";
    }
    assert(top3[0].name == "Eve");    // 99
    assert(top3[1].name == "Alice");  // 95
    assert(top3[2].name == "Charlie"); // 92
}

int main() {
    std::cout << "=== Module 04: STL Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
