// Module 10: Testing — Solutions
// Study these after attempting the exercises yourself.

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
#include <cctype>

// ============================================================================
// Exercise 1 Solution: Calculator
// ============================================================================

class Calculator {
    double result_;
public:
    Calculator() : result_(0.0) {}

    Calculator& add(double value) {
        result_ += value;
        return *this;
    }

    Calculator& subtract(double value) {
        result_ -= value;
        return *this;
    }

    Calculator& multiply(double value) {
        result_ *= value;
        return *this;
    }

    Calculator& divide(double value) {
        if (value == 0.0) throw std::invalid_argument("Division by zero");
        result_ /= value;
        return *this;
    }

    double getResult() const { return result_; }

    Calculator& reset() {
        result_ = 0.0;
        return *this;
    }
};

void exercise1() {
    std::cout << "\n=== Exercise 1: Calculator (TDD) ===\n";

    Calculator calc;
    assert(calc.add(10).getResult() == 10.0);
    assert(calc.subtract(3).getResult() == 7.0);
    assert(calc.multiply(2).getResult() == 14.0);
    assert(calc.divide(7).getResult() == 2.0);
    std::cout << "Basic operations: OK\n";

    Calculator calc2;
    double result = calc2.add(5).multiply(3).subtract(1).divide(2).getResult();
    assert(result == 7.0);
    std::cout << "Chaining: OK\n";

    Calculator calc3;
    bool threw = false;
    try { calc3.divide(0); }
    catch (const std::invalid_argument&) { threw = true; }
    assert(threw);
    std::cout << "Division by zero: OK\n";

    Calculator calc4;
    calc4.add(100);
    calc4.reset();
    assert(calc4.getResult() == 0.0);
    std::cout << "Reset: OK\n";
}

// ============================================================================
// Exercise 2 Solution: String Processor
// ============================================================================

std::string reverse_string(const std::string& s) {
    return std::string(s.rbegin(), s.rend());
}

bool is_palindrome(const std::string& s) {
    std::string cleaned;
    for (char c : s) {
        if (std::isalpha(static_cast<unsigned char>(c))) {
            cleaned += std::tolower(static_cast<unsigned char>(c));
        }
    }
    std::string rev(cleaned.rbegin(), cleaned.rend());
    return cleaned == rev;
}

int count_char(const std::string& s, char c) {
    return static_cast<int>(std::count(s.begin(), s.end(), c));
}

std::string capitalize_words(const std::string& s) {
    std::string result = s;
    bool new_word = true;
    for (char& c : result) {
        if (std::isspace(static_cast<unsigned char>(c))) {
            new_word = true;
        } else if (new_word) {
            c = std::toupper(static_cast<unsigned char>(c));
            new_word = false;
        }
    }
    return result;
}

void exercise2() {
    std::cout << "\n=== Exercise 2: String Processor ===\n";

    assert(reverse_string("hello") == "olleh");
    assert(reverse_string("") == "");
    assert(reverse_string("a") == "a");
    assert(reverse_string("ab") == "ba");
    std::cout << "reverse_string: OK\n";

    assert(is_palindrome("racecar") == true);
    assert(is_palindrome("RaceCar") == true);
    assert(is_palindrome("") == true);
    assert(is_palindrome("a") == true);
    assert(is_palindrome("ab") == false);
    std::cout << "is_palindrome: OK\n";

    assert(count_char("hello", 'l') == 2);
    assert(count_char("hello", 'z') == 0);
    assert(count_char("", 'a') == 0);
    assert(count_char("aaa", 'a') == 3);
    std::cout << "count_char: OK\n";

    assert(capitalize_words("hello world") == "Hello World");
    assert(capitalize_words("") == "");
    assert(capitalize_words("  hello  ") == "  Hello  ");
    assert(capitalize_words("a") == "A");
    std::cout << "capitalize_words: OK\n";
}

// ============================================================================
// Exercise 3 Solution: Bank Account
// ============================================================================

class BankAccount {
    double balance_;
    int transaction_count_;
public:
    explicit BankAccount(double initial_balance)
        : balance_(initial_balance), transaction_count_(1) {
        if (initial_balance < 0) throw std::invalid_argument("Initial balance must be >= 0");
    }

    BankAccount& deposit(double amount) {
        if (amount <= 0) throw std::invalid_argument("Deposit amount must be > 0");
        balance_ += amount;
        transaction_count_++;
        return *this;
    }

    BankAccount& withdraw(double amount) {
        if (amount <= 0) throw std::invalid_argument("Withdrawal amount must be > 0");
        if (amount > balance_) throw std::invalid_argument("Insufficient funds");
        balance_ -= amount;
        transaction_count_++;
        return *this;
    }

    double getBalance() const { return balance_; }
    int getTransactionCount() const { return transaction_count_; }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Bank Account ===\n";

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
// Exercise 4 Solution: Data Validator
// ============================================================================

struct ValidationRule {
    std::function<bool(const std::string&)> check;
    std::string message;
};

std::vector<ValidationRule> create_email_rules() {
    return {
        {[](const std::string& s) { return !s.empty(); }, "Email cannot be empty"},
        {[](const std::string& s) {
            return std::count(s.begin(), s.end(), '@') == 1;
        }, "Email must contain exactly one @"},
        {[](const std::string& s) {
            auto at = s.find('@');
            return at != std::string::npos && at > 0;
        }, "Email must have characters before @"},
        {[](const std::string& s) {
            auto at = s.find('@');
            return at != std::string::npos && at < s.size() - 1;
        }, "Email must have characters after @"},
        {[](const std::string& s) {
            auto at = s.find('@');
            return s.find('.', at + 1) != std::string::npos;
        }, "Email must have a domain extension"}
    };
}

std::vector<std::string> validate(const std::string& value,
                                   const std::vector<ValidationRule>& rules) {
    std::vector<std::string> errors;
    for (const auto& rule : rules) {
        if (!rule.check(value)) {
            errors.push_back(rule.message);
        }
    }
    return errors;
}

void exercise4() {
    std::cout << "\n=== Exercise 4: Data Validator ===\n";

    auto email_rules = create_email_rules();

    assert(validate("user@example.com", email_rules).empty());
    assert(validate("test.name@domain.co", email_rules).empty());
    std::cout << "Valid emails: OK\n";

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
// Exercise 5 Solution: FizzBuzz Test Generator
// ============================================================================

std::string fizzbuzz(int n) {
    if (n % 3 == 0 && n % 5 == 0) return "FizzBuzz";
    if (n % 3 == 0) return "Fizz";
    if (n % 5 == 0) return "Buzz";
    return std::to_string(n);
}

struct TestCase {
    int input;
    std::string expected;
};

std::vector<TestCase> generate_test_cases() {
    return {
        {1, "1"}, {2, "2"}, {3, "Fizz"}, {4, "4"}, {5, "Buzz"},
        {6, "Fizz"}, {7, "7"}, {8, "8"}, {9, "Fizz"}, {10, "Buzz"},
        {11, "11"}, {12, "Fizz"}, {13, "13"}, {14, "14"}, {15, "FizzBuzz"},
        {0, "FizzBuzz"}, {-3, "Fizz"}, {-5, "Buzz"}, {-15, "FizzBuzz"}, {30, "FizzBuzz"}
    };
}

void exercise5() {
    std::cout << "\n=== Exercise 5: FizzBuzz Test Generator ===\n";

    assert(fizzbuzz(1) == "1");
    assert(fizzbuzz(3) == "Fizz");
    assert(fizzbuzz(5) == "Buzz");
    assert(fizzbuzz(15) == "FizzBuzz");
    assert(fizzbuzz(7) == "7");
    std::cout << "Direct tests: OK\n";

    auto test_cases = generate_test_cases();
    assert(test_cases.size() >= 10);
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
    std::cout << "=== Module 10: Testing Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
