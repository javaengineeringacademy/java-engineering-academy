// Module 10: Testing — Exercises
// Complete each exercise by implementing the production code and writing
// test assertions. These exercises simulate test-driven development.
// Compile: g++ -std=c++17 -Wall -Wextra -o exercises exercises.cpp

#include <iostream>
#include <vector>
#include <string>
#include <optional>
#include <algorithm>
#include <numeric>
#include <functional>
#include <cassert>
#include <stdexcept>
#include <cmath>
#include <sstream>

// ============================================================================
// Exercise 1: Test-Driven Development
// Implement a Calculator class that passes all the test assertions below.
// ============================================================================

class Calculator {
    double result_;
public:
    Calculator() : result_(0.0) {}

    // TODO: Implement add — adds value to result
    Calculator& add(double value) {
        // Your code here
        return *this;
    }

    // TODO: Implement subtract — subtracts value from result
    Calculator& subtract(double value) {
        // Your code here
        return *this;
    }

    // TODO: Implement multiply — multiplies result by value
    Calculator& multiply(double value) {
        // Your code here
        return *this;
    }

    // TODO: Implement divide — divides result by value
    // Throws std::invalid_argument if value is 0
    Calculator& divide(double value) {
        // Your code here
        return *this;
    }

    // TODO: Implement getResult — returns current result
    double getResult() const {
        return result_;
    }

    // TODO: Implement reset — resets result to 0
    Calculator& reset() {
        // Your code here
        return *this;
    }
};

void exercise1() {
    std::cout << "\n=== Exercise 1: Calculator (TDD) ===\n";

    // Basic operations
    Calculator calc;
    assert(calc.add(10).getResult() == 10.0);
    assert(calc.subtract(3).getResult() == 7.0);
    assert(calc.multiply(2).getResult() == 14.0);
    assert(calc.divide(7).getResult() == 2.0);
    std::cout << "Basic operations: OK\n";

    // Chaining
    Calculator calc2;
    double result = calc2.add(5).multiply(3).subtract(1).divide(2).getResult();
    assert(result == 7.0);
    std::cout << "Chaining: OK\n";

    // Division by zero
    Calculator calc3;
    bool threw = false;
    try {
        calc3.divide(0);
    } catch (const std::invalid_argument&) {
        threw = true;
    }
    assert(threw);
    std::cout << "Division by zero: OK\n";

    // Reset
    Calculator calc4;
    calc4.add(100);
    calc4.reset();
    assert(calc4.getResult() == 0.0);
    std::cout << "Reset: OK\n";
}

// ============================================================================
// Exercise 2: String Processor with Edge Cases
// Implement a string processor and verify it handles all edge cases.
// ============================================================================

// TODO: Reverse a string. Handle empty string.
std::string reverse_string(const std::string& s) {
    // Your code here
    return "";
}

// TODO: Check if a string is a palindrome (case-insensitive).
// Handle empty string (return true) and single character (return true).
bool is_palindrome(const std::string& s) {
    // Your code here
    return false;
}

// TODO: Count occurrences of a character in a string.
// Handle empty string (return 0).
int count_char(const std::string& s, char c) {
    // Your code here
    return 0;
}

// TODO: Capitalize the first letter of each word.
// Handle empty string, multiple spaces, leading/trailing spaces.
std::string capitalize_words(const std::string& s) {
    // Your code here
    return "";
}

void exercise2() {
    std::cout << "\n=== Exercise 2: String Processor ===\n";

    // reverse_string
    assert(reverse_string("hello") == "olleh");
    assert(reverse_string("") == "");
    assert(reverse_string("a") == "a");
    assert(reverse_string("ab") == "ba");
    std::cout << "reverse_string: OK\n";

    // is_palindrome
    assert(is_palindrome("racecar") == true);
    assert(is_palindrome("RaceCar") == true);
    assert(is_palindrome("") == true);
    assert(is_palindrome("a") == true);
    assert(is_palindrome("ab") == false);
    assert(is_palindrome("A man a plan a canal Panama") == false);  // spaces not removed
    std::cout << "is_palindrome: OK\n";

    // count_char
    assert(count_char("hello", 'l') == 2);
    assert(count_char("hello", 'z') == 0);
    assert(count_char("", 'a') == 0);
    assert(count_char("aaa", 'a') == 3);
    std::cout << "count_char: OK\n";

    // capitalize_words
    assert(capitalize_words("hello world") == "Hello World");
    assert(capitalize_words("") == "");
    assert(capitalize_words("  hello  ") == "  Hello  ");
    assert(capitalize_words("a") == "A");
    std::cout << "capitalize_words: OK\n";
}

// ============================================================================
// Exercise 3: Bank Account with Validation
// Implement a bank account that validates operations and handles errors.
// ============================================================================

// TODO: Implement a BankAccount class with:
// - Constructor takes initial balance (must be >= 0, else throw)
// - deposit(amount) — adds amount (must be > 0, else throw)
// - withdraw(amount) — subtracts amount (must be > 0 and <= balance, else throw)
// - getBalance() — returns current balance
// - getTransactionCount() — returns number of operations
class BankAccount {
    double balance_;
    int transaction_count_;
public:
    // TODO: Constructor
    explicit BankAccount(double initial_balance)
        : balance_(0.0), transaction_count_(0) {
        // Your code here
    }

    // TODO: Deposit
    BankAccount& deposit(double amount) {
        // Your code here
        return *this;
    }

    // TODO: Withdraw
    BankAccount& withdraw(double amount) {
        // Your code here
        return *this;
    }

    double getBalance() const { return balance_; }
    int getTransactionCount() const { return transaction_count_; }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Bank Account ===\n";

    // Normal operations
    BankAccount account(100.0);
    assert(account.getBalance() == 100.0);
    assert(account.getTransactionCount() == 1);

    account.deposit(50.0);
    assert(account.getBalance() == 150.0);
    assert(account.getTransactionCount() == 2);

    account.withdraw(30.0);
    assert(account.getBalance() == 120.0);
    assert(account.getTransactionCount() == 3);
    std::cout << "Normal operations: OK\n";

    // Invalid operations
    bool threw = false;

    try { BankAccount bad(-10.0); }
    catch (const std::invalid_argument&) { threw = true; }
    assert(threw);

    threw = false;
    try { account.deposit(-5.0); }
    catch (const std::invalid_argument&) { threw = true; }
    assert(threw);

    threw = false;
    try { account.deposit(0); }
    catch (const std::invalid_argument&) { threw = true; }
    assert(threw);

    threw = false;
    try { account.withdraw(1000.0); }
    catch (const std::invalid_argument&) { threw = true; }
    assert(threw);

    threw = false;
    try { account.withdraw(-10.0); }
    catch (const std::invalid_argument&) { threw = true; }
    assert(threw);
    std::cout << "Invalid operations rejected: OK\n";
}

// ============================================================================
// Exercise 4: Data Validator
// Implement a validation system that checks data against multiple rules.
// ============================================================================

struct ValidationRule {
    std::function<bool(const std::string&)> check;
    std::string message;
};

// TODO: Create validators for:
// - Not empty
// - Minimum length
// - Maximum length
// - Contains only alphanumeric characters
// - Contains at least one digit

std::vector<ValidationRule> create_email_rules() {
    // Your code here — create rules for basic email validation:
    // 1. Not empty
    // 2. Contains exactly one @
    // 3. Has characters before @
    // 4. Has characters after @
    // 5. Has a dot after @
    return {};
}

// TODO: Validate a value against a set of rules. Return all failing messages.
std::vector<std::string> validate(const std::string& value,
                                   const std::vector<ValidationRule>& rules) {
    std::vector<std::string> errors;
    // Your code here
    return errors;
}

void exercise4() {
    std::cout << "\n=== Exercise 4: Data Validator ===\n";

    auto email_rules = create_email_rules();

    // Valid emails
    assert(validate("user@example.com", email_rules).empty());
    assert(validate("test.name@domain.co", email_rules).empty());
    std::cout << "Valid emails: OK\n";

    // Invalid emails
    auto errors1 = validate("", email_rules);
    assert(!errors1.empty());
    std::cout << "Empty email rejected: OK\n";

    auto errors2 = validate("noatsign.com", email_rules);
    assert(!errors2.empty());
    std::cout << "No @ rejected: OK\n";

    auto errors3 = validate("@domain.com", email_rules);
    assert(!errors3.empty());
    std::cout << "No local part rejected: OK\n";

    auto errors4 = validate("user@", email_rules);
    assert(!errors4.empty());
    std::cout << "No domain rejected: OK\n";
}

// ============================================================================
// Exercise 5: Test-Case Generator
// Implement a function that generates test cases for a fizzbuzz-like function.
// ============================================================================

// TODO: Implement fizzbuzz-like function
// - divisible by 3 and 5: "FizzBuzz"
// - divisible by 3: "Fizz"
// - divisible by 5: "Buzz"
// - otherwise: the number as a string
std::string fizzbuzz(int n) {
    // Your code here
    return "";
}

// TODO: Generate a set of test cases including:
// - Normal cases (1-15)
// - Edge cases (0, negative numbers, multiples of 3 and 5)
struct TestCase {
    int input;
    std::string expected;
};

std::vector<TestCase> generate_test_cases() {
    // Your code here
    return {};
}

void exercise5() {
    std::cout << "\n=== Exercise 5: FizzBuzz Test Generator ===\n";

    // Direct tests
    assert(fizzbuzz(1) == "1");
    assert(fizzbuzz(3) == "Fizz");
    assert(fizzbuzz(5) == "Buzz");
    assert(fizzbuzz(15) == "FizzBuzz");
    assert(fizzbuzz(7) == "7");
    std::cout << "Direct tests: OK\n";

    // Generated test cases
    auto test_cases = generate_test_cases();
    assert(test_cases.size() >= 10);  // At least 10 test cases
    std::cout << "Generated " << test_cases.size() << " test cases\n";

    for (const auto& tc : test_cases) {
        assert(fizzbuzz(tc.input) == tc.expected);
    }
    std::cout << "All generated tests pass: OK\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 10: Testing Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
