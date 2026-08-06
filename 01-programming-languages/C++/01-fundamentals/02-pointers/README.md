# C++ Pointers

## Overview
C++ pointers store memory addresses. References are aliases.

## Basic Pointer
```cpp
int x = 42;
int* ptr = &x;
cout << *ptr; // 42
```

## References
```cpp
int x = 42;
int& ref = x;
ref = 100; // x is now 100
```

## Smart Pointers
```cpp
auto ptr = std::make_unique<int>(42);
auto shared = std::make_shared<int>(42);
```

## Resources
- [C++ Reference - Pointers](https://en.cppreference.com/w/cpp/language/pointer)
