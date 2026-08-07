# C++ STL Reference

## STL Containers

### Sequence Containers

| Container | Header | Access | Insert/Delete (End) | Insert/Delete (Mid) | Memory |
|-----------|--------|--------|---------------------|---------------------|--------|
| `std::vector` | `<vector>` | O(1) | Amortized O(1) | O(n) | Contiguous |
| `std::deque` | `<deque>` | O(1) | O(1) | O(n) | Chunked |
| `std::list` | `<list>` | O(n) | O(1) | O(1) | Doubly-linked |
| `std::array` | `<array>` | O(1) | N/A (fixed) | N/A (fixed) | Contiguous |

#### `std::vector`

```cpp
#include <vector>
#include <algorithm>
#include <iostream>

int main() {
    // Construction
    std::vector<int> v1;                    // Empty
    std::vector<int> v2(5, 10);             // 5 elements, all 10
    std::vector<int> v3 = {1, 2, 3, 4, 5}; // Initializer list
    std::vector<int> v4(v3.begin(), v3.end()); // Range

    // Access
    v3[0];           // No bounds check
    v3.at(0);        // With bounds check (throws std::out_of_range)
    v3.front();      // First element
    v3.back();       // Last element
    v3.data();       // Raw pointer to array

    // Capacity
    v3.size();       // Number of elements
    v3.capacity();   // Allocated space
    v3.empty();      // bool: is empty
    v3.max_size();   // Maximum possible size
    v3.reserve(100); // Pre-allocate
    v3.shrink_to_fit(); // Release excess memory
    v3.resize(10);   // Resize (adds default-constructed elements)

    // Modifiers
    v3.push_back(6);           // Add to end
    v3.emplace_back(7);        // Construct in-place at end
    v3.pop_back();             // Remove last
    v3.insert(v3.begin() + 1, 99); // Insert at position
    v3.erase(v3.begin());     // Remove at position
    v3.clear();               // Remove all

    // Iterators
    for (auto it = v3.begin(); it != v3.end(); ++it) { }
    for (auto it = v3.rbegin(); it != v3.rend(); ++it) { }  // Reverse
    for (auto& x : v3) { }  // Range-based for (C++11)

    // Algorithms
    std::sort(v3.begin(), v3.end());
    std::reverse(v3.begin(), v3.end());
    auto it = std::find(v3.begin(), v3.end(), 99);
    std::unique(v3.begin(), v3.end());

    return 0;
}
```

#### `std::deque`

```cpp
#include <deque>

std::deque<int> dq = {1, 2, 3};
dq.push_front(0);   // O(1) - vector can't do this efficiently
dq.push_back(4);    // O(1)
dq[0];              // O(1) random access
```

#### `std::list`

```cpp
#include <list>

std::list<int> lst = {3, 1, 4, 1, 5};
lst.push_front(0);          // O(1)
lst.push_back(6);           // O(1)
lst.sort();                 // O(n log n) - stable sort
lst.unique();               // Remove consecutive duplicates
lst.reverse();              // O(n)
lst.merge(other_list);      // Merge sorted lists
lst.splice(pos, other);     // O(1) transfer elements
```

#### `std::array` (C++11)

```cpp
#include <array>

std::array<int, 5> arr = {5, 3, 1, 4, 2};
arr.size();     // 5 (compile-time)
arr[0];         // Bounds unchecked
arr.at(0);      // Bounds checked
arr.front();    // First element
arr.back();     // Last element
arr.fill(0);    // Fill all with 0
```

---

### Associative Containers

| Container | Header | Key | Sorted? | Unique? | Complexity |
|-----------|--------|-----|---------|---------|------------|
| `std::set` | `<set>` | Value | Yes | Yes | O(log n) |
| `std::multiset` | `<set>` | Value | Yes | No | O(log n) |
| `std::map` | `<map>` | Key-Value | Yes | Yes | O(log n) |
| `std::multimap` | `<map>` | Key-Value | Yes | No | O(log n) |
| `std::unordered_set` | `<unordered_set>` | Value | No | Yes | O(1) avg |
| `std::unordered_map` | `<unordered_map>` | Key-Value | No | Yes | O(1) avg |

#### `std::set` and `std::map`

```cpp
#include <set>
#include <map>
#include <string>

// set - ordered unique values
std::set<int> s = {5, 3, 1, 4, 2};
s.insert(6);               // Add element
s.erase(3);                // Remove element
s.find(4);                 // Iterator or end()
s.count(4);                // 0 or 1
s.lower_bound(3);          // Iterator to first >= 3
s.upper_bound(3);          // Iterator to first > 3

// map - ordered key-value pairs
std::map<std::string, int> ages;
ages["Alice"] = 30;        // Insert or access
ages.insert({"Bob", 25});
ages.at("Alice");          // Throws if not found
ages.find("Bob");          // Iterator
ages.count("Bob");         // 0 or 1

// Iterate
for (const auto& [key, value] : ages) {  // C++17 structured bindings
    std::cout << key << ": " << value << "\n";
}
```

#### `std::unordered_map` (Hash Table)

```cpp
#include <unordered_map>

std::unordered_map<std::string, int> scores;
scores["Alice"] = 95;
scores["Bob"] = 87;

// O(1) average lookup
if (scores.find("Alice") != scores.end()) {
    // Found
}

// Custom hash function
struct MyHash {
    size_t operator()(const std::string& s) const {
        size_t h = 0;
        for (char c : s) h = h * 31 + c;
        return h;
    }
};
```

---

### Adaptor Containers

| Container | Header | Description |
|-----------|--------|-------------|
| `std::stack` | `<stack>` | LIFO (adapted from deque) |
| `std::queue` | `<queue>` | FIFO (adapted from deque) |
| `std::priority_queue` | `<queue>` | Max-heap (adapted from vector) |

```cpp
#include <stack>
#include <queue>

// Stack - LIFO
std::stack<int> stk;
stk.push(1);     // Add to top
stk.push(2);
stk.top();       // 2 (no removal)
stk.pop();       // Remove top
stk.empty();     // false
stk.size();      // 1

// Queue - FIFO
std::queue<int> q;
q.push(1);       // Add to back
q.push(2);
q.front();       // 1 (no removal)
q.pop();         // Remove front

// Priority Queue - Max-heap
std::priority_queue<int> pq;
pq.push(3);
pq.push(1);
pq.push(2);
pq.top();        // 3 (largest)
pq.pop();        // Remove largest

// Min-heap
std::priority_queue<int, std::vector<int>, std::greater<int>> min_pq;
```

---

## Smart Pointers (`<memory>`)

| Type | Ownership | Thread-safe? | Use Case |
|------|-----------|--------------|----------|
| `std::unique_ptr` | Single | Reference count not thread-safe | Exclusive ownership |
| `std::shared_ptr` | Shared | Reference count is thread-safe | Shared ownership |
| `std::weak_ptr` | Non-owning | No | Breaking cycles, observers |

### `std::unique_ptr`

```cpp
#include <memory>
#include <iostream>

class Resource {
public:
    Resource() { std::cout << "Acquired\n"; }
    ~Resource() { std::cout << "Released\n"; }
    void use() { std::cout << "Used\n"; }
};

int main() {
    // Basic usage
    auto p1 = std::make_unique<Resource>();  // C++14
    p1->use();

    // Transfer ownership
    auto p2 = std::move(p1);  // p1 is now nullptr
    // p1->use();  // Crash!

    // Array
    auto arr = std::make_unique<int[]>(5);
    arr[0] = 42;

    // Custom deleter
    auto deleter = [](FILE* f) { fclose(f); };
    std::unique_ptr<FILE, decltype(deleter)> file(fopen("test.txt", "r"), deleter);

    return 0;
}
```

### `std::shared_ptr`

```cpp
#include <memory>

auto p1 = std::make_shared<int>(42);
auto p2 = p1;  // Reference count = 2
std::cout << p1.use_count() << "\n";  // 2
p1.reset();    // Reference count = 1
p2.reset();    // Reference count = 0, deleted

// Make shared is more efficient (single allocation)
auto sp = std::make_shared<MyClass>(args...);

// Custom deleter for shared_ptr
std::shared_ptr<int> sp(new int(42), [](int* p) { delete p; });
```

### `std::weak_ptr`

```cpp
#include <memory>

class Node {
public:
    std::shared_ptr<Node> next;
    std::weak_ptr<Node> prev;  // Weak reference to avoid cycle
};

// Breaking circular reference
auto parent = std::make_shared<Node>();
auto child = std::make_shared<Node>();
parent->next = child;
child->prev = parent;  // weak_ptr doesn't increase ref count

// Using weak_ptr
std::weak_ptr<int> wp;
{
    auto sp = std::make_shared<int>(42);
    wp = sp;
    if (auto locked = wp.lock()) {
        std::cout << *locked << "\n";  // 42
    }
}
// wp is now expired
if (wp.expired()) {
    std::cout << "Expired\n";
}
```

---

## String Utilities

### `std::string`

```cpp
#include <string>

std::string s = "Hello, World!";

// Access
s[0];              // 'H' (no bounds check)
s.at(0);           // 'H' (bounds checked)
s.front();         // 'H'
s.back();          // '!'

// Capacity
s.size();          // 12
s.length();        // 12 (same as size)
s.capacity();      // Allocated space
s.empty();         // false
s.reserve(100);    // Pre-allocate

// Modifiers
s += " Goodbye";   // Concatenate
s.append("!");     // Append
s.insert(5, " cruel");  // Insert
s.erase(5, 6);     // Erase 6 chars at position 5
s.replace(0, 5, "Hi");  // Replace 5 chars with "Hi"
s.clear();         // Make empty

// Search
s.find("World");   // Position or npos
s.rfind("l");      // Last occurrence
s.find_first_of("aeiou");  // First vowel

// Conversion
std::string num = std::to_string(42);    // "42"
int val = std::stoi("42");               // 42
double dbl = std::stod("3.14");          // 3.14
const char* cstr = s.c_str();            // C-style string
```

### `std::string_view` (C++17)

```cpp
#include <string_view>

// Non-owning view into a string
std::string_view sv = "Hello, World!";  // No allocation
sv.substr(0, 5);     // "Hello"
sv.find("World");    // 7

// Efficient for function parameters
void process(std::string_view data) {
    // No copy needed
    for (char c : data) {
        // ...
    }
}

// Must not outlive the original string
const char* raw = "Hello";
std::string_view sv2 = raw;  // OK
// raw = nullptr;  // sv2 is now dangling!
```

---

## Algorithm Quick Reference (`<algorithm>`)

| Algorithm | Prototype | Complexity |
|-----------|-----------|------------|
| `std::sort` | `void sort(RandomIt first, RandomIt last)` | O(n log n) |
| `std::stable_sort` | `void stable_sort(...)` | O(n log²n) |
| `std::partial_sort` | `void partial_sort(...)` | O(n log k) |
| `std::nth_element` | `void nth_element(...)` | O(n) avg |
| `std::find` | `It find(first, last, value)` | O(n) |
| `std::find_if` | `It find_if(first, last, pred)` | O(n) |
| `std::count` | `Difference count(first, last, value)` | O(n) |
| `std::accumulate` | `T accumulate(first, last, init)` | O(n) |
| `std::transform` | `It transform(first, last, result, op)` | O(n) |
| `std::copy` | `It copy(first, last, result)` | O(n) |
| `std::fill` | `void fill(first, last, value)` | O(n) |
| `std::remove_if` | `It remove_if(first, last, pred)` | O(n) |
| `std::unique` | `It unique(first, last)` | O(n) |
| `std::reverse` | `void reverse(first, last)` | O(n) |
| `std::rotate` | `void rotate(first, middle, last)` | O(n) |
| `std::min_element` | `It min_element(first, last)` | O(n) |
| `std::max_element` | `It max_element(first, last)` | O(n) |
| `std::binary_search` | `bool binary_search(...)` | O(log n) |
| `std::lower_bound` | `It lower_bound(...)` | O(log n) |
| `std::upper_bound` | `It upper_bound(...)` | O(log n) |
| `std::merge` | `void merge(...)` | O(n) |
| `std::inplace_merge` | `void inplace_merge(...)` | O(n) |

### Code Examples

```cpp
#include <algorithm>
#include <vector>
#include <numeric>
#include <iostream>

int main() {
    std::vector<int> v = {5, 3, 1, 4, 2};

    // Sort with custom comparator
    std::sort(v.begin(), v.end());
    std::sort(v.begin(), v.end(), std::greater<int>());

    // Stable sort
    std::stable_sort(v.begin(), v.end());

    // Find
    auto it = std::find(v.begin(), v.end(), 4);
    if (it != v.end()) {
        std::cout << "Found at: " << std::distance(v.begin(), it) << "\n";
    }

    // Find if
    auto even = std::find_if(v.begin(), v.end(), [](int x) {
        return x % 2 == 0;
    });

    // Count
    int count = std::count(v.begin(), v.end(), 3);

    // Accumulate (sum)
    int sum = std::accumulate(v.begin(), v.end(), 0);
    double avg = sum / static_cast<double>(v.size());

    // Transform
    std::vector<int> doubled(v.size());
    std::transform(v.begin(), v.end(), doubled.begin(),
                   [](int x) { return x * 2; });

    // Remove-erase idiom
    v.erase(std::remove_if(v.begin(), v.end(),
            [](int x) { return x > 3; }), v.end());

    // Unique (must be sorted first)
    std::sort(v.begin(), v.end());
    v.erase(std::unique(v.begin(), v.end()), v.end());

    // Binary search (must be sorted)
    bool found = std::binary_search(v.begin(), v.end(), 3);

    // Min/Max
    auto [min_it, max_it] = std::minmax_element(v.begin(), v.end());

    return 0;
}
```

---

## Lambda Syntax

```cpp
// Basic syntax
[capture](parameters) -> return_type { body }

// Examples
auto add = [](int a, int b) -> int { return a + b; };
auto greet = []() { std::cout << "Hello!\n"; };

// Capture modes
int x = 10;
int y = 20;

auto f1 = [x]() { return x; };        // Copy x
auto f2 = [&x]() { x += 1; };         // Reference to x
auto f3 = [=]() { return x + y; };     // Copy all by value
auto f4 = [&]() { x++; y++; };         // Reference all
auto f5 = [x, &y]() { return x + y; }; // Mix
auto f6 = [this]() { member++; };       // Capture this pointer

// Mutable lambda
auto counter = [count = 0]() mutable {
    return ++count;
};

// Generic lambda (C++14)
auto print = [](const auto& x) {
    std::cout << x << "\n";
};

// Init capture (C++14)
auto ptr = std::make_unique<int>(42);
auto moved = [p = std::move(ptr)]() {
    std::cout << *p << "\n";
};

// Constexpr lambda (C++17)
constexpr auto square = [](int x) { return x * x; };
static_assert(square(5) == 25);
```

### Common Patterns

```cpp
// Sort by custom field
std::sort(v.begin(), v.end(),
    [](const Person& a, const Person& b) {
        return a.age < b.age;
    });

// Filter
auto it = std::remove_if(v.begin(), v.end(),
    [](int x) { return x < 0; });

// Transform with lambda
std::transform(v.begin(), v.end(), result.begin(),
    [](int x) { return x * x; });

// Reduce with lambda
int sum = std::accumulate(v.begin(), v.end(), 0,
    [](int acc, int x) { return acc + x; });

// Bind to member
std::sort(v.begin(), v.end(),
    std::bind(&Person::age, std::placeholders::_1) <
    std::bind(&Person::age, std::placeholders::_2));
```

---

## Common Design Patterns

### RAII (Resource Acquisition Is Initialization)

```cpp
class FileHandler {
    std::FILE* file;
public:
    explicit FileHandler(const char* name)
        : file(std::fopen(name, "r")) {
        if (!file) throw std::runtime_error("Failed to open file");
    }
    ~FileHandler() {
        if (file) std::fclose(file);
    }
    // Non-copyable
    FileHandler(const FileHandler&) = delete;
    FileHandler& operator=(const FileHandler&) = delete;
};
```

### Builder Pattern

```cpp
class QueryBuilder {
    std::string table_;
    std::string where_;
    int limit_ = 0;
public:
    QueryBuilder& table(const std::string& t) { table_ = t; return *this; }
    QueryBuilder& where(const std::string& w) { where_ = w; return *this; }
    QueryBuilder& limit(int l) { limit_ = l; return *this; }
    std::string build() const {
        return "SELECT * FROM " + table_ +
               (where_.empty() ? "" : " WHERE " + where_) +
               (limit_ ? " LIMIT " + std::to_string(limit_) : "");
    }
};

// Usage
auto q = QueryBuilder().table("users").where("age > 18").limit(10).build();
```

### Observer Pattern

```cpp
#include <functional>
#include <vector>

class EventEmitter {
    std::vector<std::function<void(int)>> listeners_;
public:
    void on(std::function<void(int)> fn) { listeners_.push_back(fn); }
    void emit(int event) {
        for (auto& fn : listeners_) fn(event);
    }
};

// Usage
EventEmitter emitter;
emitter.on([](int e) { std::cout << "Event: " << e << "\n"; });
emitter.emit(42);
```

### Singleton

```cpp
class Database {
public:
    static Database& instance() {
        static Database db;
        return db;
    }
    Database(const Database&) = delete;
    Database& operator=(const Database&) = delete;
private:
    Database() = default;
};
```

### Strategy Pattern

```cpp
class SortStrategy {
public:
    virtual ~SortStrategy() = default;
    virtual void sort(std::vector<int>& data) = 0;
};

class QuickSort : public SortStrategy {
public:
    void sort(std::vector<int>& data) override {
        std::sort(data.begin(), data.end());
    }
};

class Context {
    std::unique_ptr<SortStrategy> strategy_;
public:
    void setStrategy(std::unique_ptr<SortStrategy> s) {
        strategy_ = std::move(s);
    }
    void doSort(std::vector<int>& data) {
        if (strategy_) strategy_->sort(data);
    }
};
```

---

## One-Minute Revision Table

| Category | Key Types/Functions | Header |
|----------|---------------------|--------|
| **Vector** | `push_back`, `emplace_back`, `pop_back`, `size`, `clear` | `<vector>` |
| **Deque** | `push_front`, `push_back`, `pop_front`, `pop_back` | `<deque>` |
| **List** | `push_front`, `push_back`, `sort`, `merge`, `splice` | `<list>` |
| **Array** | `size`, `at`, `fill`, `data` | `<array>` |
| **Set** | `insert`, `erase`, `find`, `count` | `<set>` |
| **Map** | `insert`, `erase`, `find`, `operator[]`, `at` | `<map>` |
| **Unordered** | `bucket_count`, `load_factor`, `rehash` | `<unordered_map>` |
| **Stack** | `push`, `pop`, `top`, `empty` | `<stack>` |
| **Queue** | `push`, `pop`, `front`, `back` | `<queue>` |
| **Priority** | `push`, `pop`, `top` | `<priority_queue>` |
| **Unique Ptr** | `make_unique`, `release`, `reset`, `get` | `<memory>` |
| **Shared Ptr** | `make_shared`, `use_count`, `reset`, `get` | `<memory>` |
| **Weak Ptr** | `lock`, `expired`, `use_count` | `<memory>` |
| **String** | `substr`, `find`, `replace`, `c_str`, `to_string` | `<string>` |
| **String View** | `substr`, `find`, `data`, `size` | `<string_view>` |
| **Sort** | `sort`, `stable_sort`, `partial_sort`, `nth_element` | `<algorithm>` |
| **Search** | `find`, `find_if`, `binary_search`, `lower_bound` | `<algorithm>` |
| **Transform** | `transform`, `copy`, `fill`, `generate` | `<algorithm>` |
| **Numeric** | `accumulate`, `inner_product`, `iota` | `<numeric>` |

### Essential Syntax

```cpp
// Auto type deduction
auto x = 42;
auto& ref = x;
const auto& cref = x;

// Range-based for
for (const auto& item : container) { }
for (auto&& item : container) { }  // Universal reference

// Structured bindings (C++17)
auto [key, value] = *map.begin();
auto [a, b, c] = std::tuple{1, 2.0, '3'};

// If with initializer (C++17)
if (auto it = map.find(key); it != map.end()) {
    // use it
}

// constexpr if (C++17)
if constexpr (std::is_same_v<T, int>) {
    // compile-time branch
}

// Null pointer
auto p = nullptr;  // Better than NULL or 0

// Lambda
auto f = [](int x) { return x * 2; };

// Smart pointer
auto up = std::make_unique<int>(42);
auto sp = std::make_shared<int>(42);

// Move
auto b = std::move(a);

// Forward
template<typename T>
void wrapper(T&& arg) {
    func(std::forward<T>(arg));
}
```

### C++ Standard Versions

| Version | Key Features |
|---------|--------------|
| C++11 | `auto`, lambdas, move semantics, smart pointers, `constexpr` |
| C++14 | Generic lambdas, `make_unique`, `[[deprecated]]` |
| C++17 | `std::optional`, `std::variant`, structured bindings, `if constexpr`, `string_view` |
| C++20 | Concepts, ranges, coroutines, `std::format`, modules |
| C++23 | `std::expected`, ranges improvements, deducing `this` |
