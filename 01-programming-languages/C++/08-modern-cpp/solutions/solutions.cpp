// Module 08: Modern C++ — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <vector>
#include <string>
#include <optional>
#include <variant>
#include <string_view>
#include <algorithm>
#include <numeric>
#include <functional>
#include <cassert>
#include <map>
#include <array>
#include <charconv>

// ============================================================================
// Exercise 1 Solution: Lambda Mastery
// ============================================================================

auto make_multiplier(int factor) {
    return [factor](int x) { return x * factor; };
}

std::vector<int> positives_only(const std::vector<int>& nums) {
    std::vector<int> result;
    std::copy_if(nums.begin(), nums.end(), std::back_inserter(result),
                 [](int x) { return x >= 0; });
    return result;
}

void sort_by_length(std::vector<std::string>& words) {
    std::sort(words.begin(), words.end(),
              [](const std::string& a, const std::string& b) {
                  return a.size() < b.size();
              });
}

auto make_counter(int start) {
    return [count = start]() mutable { return count++; };
}

void exercise1() {
    std::cout << "\n=== Exercise 1: Lambda Mastery ===\n";

    auto triple = make_multiplier(3);
    assert(triple(5) == 15);
    assert(triple(0) == 0);
    assert(triple(-2) == -6);
    std::cout << "Multiplier: OK\n";

    std::vector<int> nums = {-3, -1, 0, 2, 5, -4, 7};
    auto pos = positives_only(nums);
    assert((pos == std::vector<int>{0, 2, 5, 7}));
    std::cout << "Positives only: OK\n";

    std::vector<std::string> words = {"elephant", "cat", "dog", "a", "bird"};
    sort_by_length(words);
    assert(words[0] == "a");
    assert(words[1] == "cat");
    assert(words[2] == "dog");
    std::cout << "Sort by length: OK\n";

    auto counter = make_counter(10);
    assert(counter() == 10);
    assert(counter() == 11);
    assert(counter() == 12);
    std::cout << "Counter: OK\n";
}

// ============================================================================
// Exercise 2 Solution: std::optional
// ============================================================================

std::optional<int> find_first_if(const std::vector<int>& vec, std::function<bool(int)> pred) {
    for (const auto& x : vec) {
        if (pred(x)) return x;
    }
    return std::nullopt;
}

std::optional<int> safe_stoi(std::string_view sv) {
    int result = 0;
    auto [ptr, ec] = std::from_chars(sv.data(), sv.data() + sv.size(), result);
    if (ec == std::errc{}) {
        return result;
    }
    return std::nullopt;
}

std::optional<double> safe_divide(int numerator, int divisor) {
    if (divisor == 0) return std::nullopt;
    return static_cast<double>(numerator) / divisor;
}

void exercise2() {
    std::cout << "\n=== Exercise 2: std::optional ===\n";

    std::vector<int> nums = {1, 4, 7, 10, 13};

    auto first_even = find_first_if(nums, [](int x) { return x % 2 == 0; });
    assert(first_even.has_value() && *first_even == 4);
    std::cout << "First even: " << *first_even << "\n";

    auto first_big = find_first_if(nums, [](int x) { return x > 100; });
    assert(!first_big.has_value());
    std::cout << "First > 100: nullopt (correct)\n";

    auto v1 = safe_stoi("42");
    assert(v1.has_value() && *v1 == 42);
    std::cout << "Parse '42': " << *v1 << "\n";

    auto v2 = safe_stoi("abc");
    assert(!v2.has_value());
    std::cout << "Parse 'abc': nullopt (correct)\n";

    auto d1 = safe_divide(10, 3);
    assert(d1.has_value());
    std::cout << "10 / 3 = " << *d1 << "\n";

    auto d2 = safe_divide(10, 0);
    assert(!d2.has_value());
    std::cout << "10 / 0 = nullopt (correct)\n";
}

// ============================================================================
// Exercise 3 Solution: Move Semantics
// ============================================================================

class ResourceHolder {
    int* data_;
    size_t size_;
public:
    explicit ResourceHolder(size_t size)
        : data_(new int[size]()), size_(size) {}

    ResourceHolder(ResourceHolder&& other) noexcept
        : data_(other.data_), size_(other.size_) {
        other.data_ = nullptr;
        other.size_ = 0;
    }

    ResourceHolder& operator=(ResourceHolder&& other) noexcept {
        if (this != &other) {
            delete[] data_;
            data_ = other.data_;
            size_ = other.size_;
            other.data_ = nullptr;
            other.size_ = 0;
        }
        return *this;
    }

    ResourceHolder(const ResourceHolder&) = delete;
    ResourceHolder& operator=(const ResourceHolder&) = delete;

    ~ResourceHolder() { delete[] data_; }

    size_t size() const { return size_; }
    int* data() { return data_; }
    const int* data() const { return data_; }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Move Semantics ===\n";

    ResourceHolder a(10);
    assert(a.size() == 10);

    ResourceHolder b = std::move(a);
    assert(b.size() == 10);
    assert(a.size() == 0);
    std::cout << "Move construction: OK\n";

    ResourceHolder c(5);
    c = std::move(b);
    assert(c.size() == 10);
    assert(b.size() == 0);
    std::cout << "Move assignment: OK\n";

    ResourceHolder d = std::move(ResourceHolder(3));
    assert(d.size() == 3);
    std::cout << "Chain move: OK\n";
}

// ============================================================================
// Exercise 4 Solution: std::variant
// ============================================================================

using Number = std::variant<int, double>;

Number add_numbers(const Number& a, const Number& b) {
    return std::visit([](const auto& x, const auto& y) -> Number {
        using TA = std::decay_t<decltype(x)>;
        using TB = std::decay_t<decltype(y)>;
        if constexpr (std::is_same_v<TA, double> || std::is_same_v<TB, double>) {
            return static_cast<double>(x) + static_cast<double>(y);
        } else {
            return x + y;
        }
    }, a, b);
}

Number multiply_numbers(const Number& a, const Number& b) {
    return std::visit([](const auto& x, const auto& y) -> Number {
        using TA = std::decay_t<decltype(x)>;
        using TB = std::decay_t<decltype(y)>;
        if constexpr (std::is_same_v<TA, double> || std::is_same_v<TB, double>) {
            return static_cast<double>(x) * static_cast<double>(y);
        } else {
            return x * y;
        }
    }, a, b);
}

std::string number_to_string(const Number& n) {
    return std::visit([](const auto& val) -> std::string {
        if constexpr (std::is_same_v<std::decay_t<decltype(val)>, int>) {
            return std::to_string(val);
        } else {
            return std::to_string(val);
        }
    }, n);
}

void exercise4() {
    std::cout << "\n=== Exercise 4: std::variant ===\n";

    Number a = 3;
    Number b = 2.5;

    auto sum = add_numbers(a, b);
    assert(std::holds_alternative<double>(sum));
    assert(std::get<double>(sum) == 5.5);
    std::cout << "3 + 2.5 = " << number_to_string(sum) << "\n";

    auto prod = multiply_numbers(a, Number(4));
    assert(std::holds_alternative<int>(prod));
    assert(std::get<int>(prod) == 12);
    std::cout << "3 * 4 = " << number_to_string(prod) << "\n";

    auto dsum = add_numbers(Number(2.5), Number(3.5));
    assert(std::holds_alternative<double>(dsum));
    std::cout << "2.5 + 3.5 = " << number_to_string(dsum) << "\n";
}

// ============================================================================
// Exercise 5 Solution: Modern C++ Pipeline
// ============================================================================

struct Product {
    std::string name;
    double price;
    int quantity;
};

std::vector<Product> filter_products(const std::vector<Product>& products,
                                     double max_price) {
    std::vector<Product> result;
    std::copy_if(products.begin(), products.end(), std::back_inserter(result),
                 [max_price](const Product& p) {
                     return p.quantity > 0 && p.price < max_price;
                 });
    return result;
}

double total_value(const std::vector<Product>& products) {
    return std::accumulate(products.begin(), products.end(), 0.0,
                           [](double sum, const Product& p) {
                               return sum + (p.price * p.quantity);
                           });
}

std::optional<Product> best_value_product(const std::vector<Product>& products) {
    if (products.empty()) return std::nullopt;

    auto best = std::max_element(products.begin(), products.end(),
                                 [](const Product& a, const Product& b) {
                                     return (a.price * a.quantity) < (b.price * b.quantity);
                                 });
    return *best;
}

std::string summarize(const std::vector<Product>& products) {
    std::string result;
    for (const auto& [name, price, qty] : products) {
        if (!result.empty()) result += " | ";
        result += name + "(" + std::to_string(qty) + ")";
    }
    return result;
}

void exercise5() {
    std::cout << "\n=== Exercise 5: Modern C++ Pipeline ===\n";

    std::vector<Product> inventory = {
        {"Widget", 9.99, 10},
        {"Gadget", 24.99, 5},
        {"Doohickey", 4.99, 0},
        {"Thingamajig", 99.99, 2},
        {"Whatchamacallit", 14.99, 8}
    };

    auto affordable = filter_products(inventory, 20.0);
    assert(affordable.size() == 3);
    std::cout << "Affordable in-stock products: " << affordable.size() << "\n";

    double total = total_value(affordable);
    std::cout << "Total value: $" << total << "\n";
    assert(total > 0);

    auto best = best_value_product(affordable);
    assert(best.has_value());
    std::cout << "Best value: " << best->name << "\n";

    std::string summary = summarize(inventory);
    std::cout << "Summary: " << summary << "\n";
    assert(!summary.empty());
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 08: Modern C++ Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
