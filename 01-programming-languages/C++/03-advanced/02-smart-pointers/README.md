# C++ Smart Pointers

## Overview
Smart pointers manage memory automatically.

## unique_ptr
Exclusive ownership:
```cpp
auto ptr = std::make_unique<int>(42);
```

## Shared_ptr
Shared ownership:
```cpp
auto ptr = std::make_shared<int>(42);
```

## Weak_ptr
Non-owning reference:
```cpp
std::weak_ptr<int> weak = ptr;
```

## Resources
- [C++ Reference - Smart Pointers](https://en.cppreference.com/w/cpp/memory/unique_ptr)
