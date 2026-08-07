# OOP Quiz

## Questions

### 1. What is the purpose of a virtual destructor?
A) To make destruction faster
B) To ensure proper cleanup of derived class objects
C) To prevent memory leaks
D) To make the class abstract

### 2. What is the difference between `struct` and `class` in C++?
A) `struct` cannot have methods
B) `class` members are private by default
C) `struct` cannot inherit
D) No difference

### 3. What is the diamond problem?
A) A problem with multiple inheritance where a class inherits from two classes that share a common base
B) A problem with templates
C) A problem with memory management
D) A problem with function overloading

### 4. Which keyword is used to prevent a class from being inherited?
A) `final`
B) `sealed`
C) `static`
D) `const`

### 5. What is the Rule of Three?
A) A class should have three constructors
B) If you define any of destructor, copy constructor, or copy assignment operator, you should define all three
C) A class should have three methods
D) A class should have three data members

### 6. What is the output of this code?
```cpp
class Base {
public:
    virtual void foo() { std::cout << "Base"; }
};
class Derived : public Base {
public:
    void foo() override { std::cout << "Derived"; }
};
Base* p = new Derived();
p->foo();
```
A) Base
B) Derived
C) Compilation error
D) Undefined behavior

### 7. What is the difference between `virtual` and `override`?
A) No difference
B) `virtual` enables dynamic dispatch; `override` documents intent to override a base class function
C) `override` is required for virtual functions
D) `virtual` is deprecated

### 8. What is object slicing?
A) Cutting objects in memory
B) When a derived object is assigned to a base object, derived-specific members are lost
C) Optimizing object size
D) Removing unused members

### 9. What is the purpose of a pure virtual function?
A) To make the function faster
B) To make the class abstract (cannot be instantiated)
C) To prevent inheritance
D) To make the function inline

### 10. What is the difference between composition and inheritance?
A) No difference
B) Composition: "has-a" relationship; Inheritance: "is-a" relationship
C) Inheritance is always better
D) Composition is faster

## Answers
1. B) To ensure proper cleanup of derived class objects
2. B) `class` members are private by default
3. A) A problem with multiple inheritance where a class inherits from two classes that share a common base
4. A) `final`
5. B) If you define any of destructor, copy constructor, or copy assignment operator, you should define all three
6. B) Derived
7. B) `virtual` enables dynamic dispatch; `override` documents intent to override a base class function
8. B) When a derived object is assigned to a base object, derived-specific members are lost
9. B) To make the class abstract (cannot be instantiated)
10. B) Composition: "has-a" relationship; Inheritance: "is-a" relationship
