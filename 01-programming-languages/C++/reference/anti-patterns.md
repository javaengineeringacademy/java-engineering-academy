# C++ Anti-Patterns

## Raw Pointers for Ownership
```cpp
// Bad
int* p = new int(42);
delete p;

// Good
auto p = std::make_unique<int>(42);
```

## Memory Leaks
```cpp
// Bad
void leak() { int* p = new int[100]; }

// Good
void safe() { std::vector<int> v(100); }
```

## Buffer Overflow
```cpp
// Bad
char buf[10];
strcpy(buf, "this string is too long");

// Good
std::string buf = "safe string";
```

## Dangling References
```cpp
// Bad
int& ref = getLocalRef();

// Good
int value = getLocalRef();
```

## Slicing
```cpp
// Bad
Base b = Derived(); // slices Derived

// Good
std::unique_ptr<Base> b = std::make_unique<Derived>();
```

## Excessive Copying
```cpp
// Bad
for (const auto& item : getLargeVector()) { /* ... */ }

// Good
const auto& vec = getLargeVector();
for (const auto& item : vec) { /* ... */ }
```

## Thread Safety
```cpp
// Bad
shared_ptr<T> global; // data race

// Good
std::mutex mtx;
std::lock_guard<std::mutex> lock(mtx);
```

## Exception Safety
```cpp
// Bad
void process() { resource->doWork(); delete resource; }

// Good
void process() {
    auto r = std::make_unique<Resource>();
    r->doWork();
}
```
