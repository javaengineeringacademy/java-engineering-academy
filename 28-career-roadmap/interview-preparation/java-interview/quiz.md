# Java Interview Quiz

## Question 1
What is the difference between `==` and `.equals()` for object comparison?
- A) They are identical
- B) `==` compares object references (memory addresses), `.equals()` compares object content
- C) `.equals()` is always faster
- D) `==` compares content, `.equals()` compares references

**Answer: B**
**Explanation:** `==` checks if two references point to the same object in memory. `.equals()` can be overridden to compare logical equality (content). For String and wrapper classes, `.equals()` compares the actual values.

## Question 2
What makes a Java class immutable?
- A) Using the `final` keyword on the class
- B) Making all fields private and final, providing no setters, and ensuring no mutable objects are exposed
- C) Using only primitive types
- D) Having no constructors

**Answer: B**
**Explanation:** An immutable class requires: private final fields, no setter methods, deep copy of mutable objects in constructor and getters, and optionally a final class to prevent subclassing from breaking immutability.

## Question 3
What is the difference between `ArrayList` and `LinkedList`?
- A) They are identical
- B) `ArrayList` uses a dynamic array (O(1) random access), `LinkedList` uses doubly-linked nodes (O(1) insert/delete)
- C) `LinkedList` is always faster
-D) `ArrayList` doesn't support generics

**Answer: B**
**Explanation:** `ArrayList` provides O(1) random access via array indexing but O(n) insert/delete. `LinkedList` provides O(1) insert/delete at known positions but O(n) random access. Use ArrayList for most cases due to cache efficiency.

## Question 4
What is the diamond problem in Java and how does Java solve it?
- A) A problem with diamond-shaped UI layouts
- B) Ambiguity when a class inherits from two interfaces with the same default method
- C) A memory leak pattern
- D) A sorting algorithm issue

**Answer: B**
**Explanation:** The diamond problem occurs when a class implements two interfaces with the same default method. Java resolves it by requiring the implementing class to explicitly override the method, eliminating the ambiguity.

## Question 5
What is the purpose of the `volatile` keyword in Java?
- A) To make a variable immutable
- B) To ensure visibility of changes to a variable across threads by preventing compiler and CPU optimizations
- C) To make a variable thread-safe for all operations
- D) To increase variable performance

**Answer: B**
**Explanation:** `volatile` ensures that reads and writes to a variable go directly to main memory, not CPU caches. It guarantees visibility (all threads see the latest value) but does not guarantee atomicity for compound operations like i++.