# C++ Inheritance

## Overview
Inheritance allows creating new classes from existing ones.

## Basic Inheritance
```cpp
class Animal {
public:
    void speak() { cout << "..."; }
};

class Dog : public Animal {
public:
    void bark() { cout << "woof"; }
};
```

## Virtual Functions
```cpp
class Base {
public:
    virtual void show() { cout << "Base"; }
};

class Derived : public Base {
public:
    void show() override { cout << "Derived"; }
};
```

## Resources
- [C++ Reference - Inheritance](https://en.cppreference.com/w/cpp/language/derived_class)
