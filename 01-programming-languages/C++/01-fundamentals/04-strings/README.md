# C++ Strings

## Overview
C++ offers `std::string` and `std::string_view` for string handling.

## std::string
```cpp
std::string s = "hello";
s += " world";
```

## std::string_view (C++17)
```cpp
std::string_view sv = "hello";
```

## String Functions
```cpp
s.length();
s.substr(0, 5);
s.find("world");
```

## Resources
- [C++ Reference - string](https://en.cppreference.com/w/cpp/string/basic_string)
