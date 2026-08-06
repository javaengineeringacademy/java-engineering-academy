# C++ Variables

## Overview
C++ is statically typed with type inference via `auto`.

## Basic Types
```cpp
int x = 42;
double pi = 3.14;
bool flag = true;
char c = 'A';
std::string name = "C++";
```

## Type Inference
```cpp
auto x = 42;        // int
auto y = 3.14;      // double
auto z = "hello";   // const char*
```

## References
```cpp
int x = 42;
int& ref = x;       // reference
```

## Constants
```cpp
const int MAX = 100;
constexpr int SIZE = 50;
```

## Resources
- [C++ Reference - Variables](https://en.cppreference.com/w/cpp/language/initialization)
