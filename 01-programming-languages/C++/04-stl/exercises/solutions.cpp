// Module 04: STL — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <numeric>
#include <string>
#include <sstream>
#include <cctype>
#include <cassert>

// ============================================================================
// Exercise 1 Solution: Vector Operations
// ============================================================================

std::vector<int> filter_evens(const std::vector<int>& input) {
    std::vector<int> result;
    std::copy_if(input.begin(), input.end(), std::back_inserter(result),
                 [](int x) { return x % 2 == 0; });
    return result;
}

int sum_vector(const std::vector<int>& vec) {
    return std::accumulate(vec.begin(), vec.end(), 0);
}

int index_of_max(const std::vector<int>& vec) {
    if (vec.empty()) return -1;
    auto it = std::max_element(vec.begin(), vec.end());
    return static_cast<int>(it - vec.begin());
}

std::vector<int> remove_duplicates(std::vector<int> sorted_vec) {
    auto last = std::unique(sorted_vec.begin(), sorted_vec.end());
    sorted_vec.erase(last, sorted_vec.end());
    return sorted_vec;
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
// Exercise 2 Solution: Map Operations
// ============================================================================

std::map<std::string, int> word_frequency(const std::string& text) {
    std::map<std::string, int> freq;
    std::istringstream iss(text);
    std::string word;

    while (iss >> word) {
        // Lowercase the word
        std::string lower;
        for (char c : word) {
            if (std::isalpha(static_cast<unsigned char>(c))) {
                lower += std::tolower(static_cast<unsigned char>(c));
            }
        }
        if (!lower.empty()) {
            freq[lower]++;
        }
    }
    return freq;
}

std::string most_frequent(const std::map<std::string, int>& freq) {
    if (freq.empty()) return "";

    std::string top_word;
    int top_count = 0;
    for (const auto& [word, count] : freq) {
        if (count > top_count) {
            top_count = count;
            top_word = word;
        }
    }
    return top_word;
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
// Exercise 3 Solution: Set Operations
// ============================================================================

std::set<int> set_intersection_op(const std::set<int>& a, const std::set<int>& b) {
    std::set<int> result;
    std::set_intersection(a.begin(), a.end(), b.begin(), b.end(),
                          std::inserter(result, result.begin()));
    return result;
}

std::set<int> set_union_op(const std::set<int>& a, const std::set<int>& b) {
    std::set<int> result;
    std::set_union(a.begin(), a.end(), b.begin(), b.end(),
                   std::inserter(result, result.begin()));
    return result;
}

void exercise3() {
    std::cout << "\n=== Exercise 3: Set Operations ===\n";

    std::set<int> a = {1, 2, 3, 4, 5};
    std::set<int> b = {3, 4, 5, 6, 7};

    auto inter = set_intersection_op(a, b);
    assert((inter == std::set<int>{3, 4, 5}));
    std::cout << "Intersection: ";
    for (int x : inter) std::cout << x << " ";
    std::cout << "\n";

    auto uni = set_union_op(a, b);
    assert((uni == std::set<int>{1, 2, 3, 4, 5, 6, 7}));
    std::cout << "Union: ";
    for (int x : uni) std::cout << x << " ";
    std::cout << "\n";
}

// ============================================================================
// Exercise 4 Solution: Algorithm — Student Grades
// ============================================================================

struct Student {
    std::string name;
    int grade;
};

std::vector<Student> sort_by_grade(const std::vector<Student>& students) {
    auto sorted = students;
    std::sort(sorted.begin(), sorted.end(),
              [](const Student& a, const Student& b) {
                  return a.grade > b.grade;  // Highest first
              });
    return sorted;
}

std::vector<std::string> honor_roll(const std::vector<Student>& students) {
    std::vector<std::string> names;
    std::copy_if(students.begin(), students.end(), std::back_inserter(names),
                 [](const Student& s) { return s.grade >= 90; });
    return names;
}

double average_grade(const std::vector<Student>& students) {
    if (students.empty()) return 0.0;
    int total = std::accumulate(students.begin(), students.end(), 0,
                                [](int sum, const Student& s) {
                                    return sum + s.grade;
                                });
    return static_cast<double>(total) / students.size();
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
    assert(sorted[0].name == "Diana");

    auto honors = honor_roll(students);
    std::cout << "Honor roll: ";
    for (const auto& name : honors) std::cout << name << " ";
    std::cout << "\n";

    double avg = average_grade(students);
    std::cout << "Average: " << avg << "\n";
    assert(avg == 87.6);
}

// ============================================================================
// Exercise 5 Solution: Data Pipeline
// ============================================================================

struct Record {
    std::string name;
    int score;
};

std::vector<Record> parse_records(const std::vector<std::string>& csv_lines) {
    std::vector<Record> records;
    for (const auto& line : csv_lines) {
        auto comma_pos = line.find(',');
        if (comma_pos != std::string::npos) {
            std::string name = line.substr(0, comma_pos);
            int score = std::stoi(line.substr(comma_pos + 1));
            records.push_back({name, score});
        }
    }
    return records;
}

std::vector<Record> top_n(const std::vector<Record>& records, int n) {
    auto sorted = records;
    std::sort(sorted.begin(), sorted.end(),
              [](const Record& a, const Record& b) {
                  return a.score > b.score;
              });
    if (static_cast<int>(sorted.size()) > n) {
        sorted.resize(n);
    }
    return sorted;
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
    assert(top3[0].name == "Eve");
    assert(top3[1].name == "Alice");
    assert(top3[2].name == "Charlie");
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 04: STL Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
