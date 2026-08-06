# C++ Polymorphism

## Overview
Polymorphism allows treating objects of different types through a common interface.

## Virtual Functions
```cpp
class Base {
public:
    virtual void show() { cout << "Base"; }
};
```

## Pure Virtual Functions
```cpp
class Interface {
public:
    virtual void execute() = 0;
};
```

## VTable
Compiler creates a virtual table for dynamic dispatch.

## Resources
- [C++ Reference - Polymorphism](https://en.cppreference.com/w/cpp/language/polymorphism)
