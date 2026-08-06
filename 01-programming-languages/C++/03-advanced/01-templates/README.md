# C++ Templates

## Overview
Templates enable generic programming in C++.

## Function Templates
```cpp
template <typename T>
T max(T a, T b) {
    return (a > b) ? a : b;
}
```

## Class Templates
```cpp
template <typename T>
class Stack {
    vector<T> data;
public:
    void push(T value) { data.push_back(value); }
    T pop() { T val = data.back(); data.pop_back(); return val; }
};
```

## Resources
- [C++ Reference - Templates](https://en.cppreference.com/w/cpp/language/templates)
