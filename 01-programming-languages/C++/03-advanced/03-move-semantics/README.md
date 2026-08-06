# C++ Move Semantics

## Overview
Move semantics transfer resources instead of copying.

## std::move
```cpp
std::string a = "hello";
std::string b = std::move(a); // a is now empty
```

## Rvalue References
```cpp
void process(std::string&& s) { /* ... */ }
```

## Resources
- [C++ Reference - Move semantics](https://en.cppreference.com/w/cpp/language/move_constructor)
