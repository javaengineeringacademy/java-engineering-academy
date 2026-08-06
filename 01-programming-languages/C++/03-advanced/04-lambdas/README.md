# C++ Lambdas

## Overview
Lambdas provide anonymous functions in C++.

## Basic Lambda
```cpp
auto add = [](int a, int b) { return a + b; };
```

## Capture Clauses
```cpp
int x = 10;
auto f = [x]() { cout << x; };
```

## Mutable Lambdas
```cpp
auto f = [x]() mutable { x++; };
```

## Resources
- [C++ Reference - Lambda](https://en.cppreference.com/w/cpp/language/lambda)
