# C++ Classes

## Overview
C++ classes encapsulate data and behavior.

## Basic Class
```cpp
class Person {
public:
    string name;
    int age;
    Person(string n, int a) : name(n), age(a) {}
};
```

## Constructor/Destructor
```cpp
class Resource {
public:
    Resource() { cout << "created"; }
    ~Resource() { cout << "destroyed"; }
};
```

## Access Specifiers
- `public`: Accessible from anywhere
- `protected`: Accessible within class and subclasses
- `private`: Accessible only within class

## Resources
- [C++ Reference - Classes](https://en.cppreference.com/w/cpp/language/class)
