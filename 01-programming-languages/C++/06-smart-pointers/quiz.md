# Smart Pointers Quiz

## Questions

### 1. What is the main difference between `std::unique_ptr` and `std::shared_ptr`?
A) `unique_ptr` is faster
B) `unique_ptr` allows exclusive ownership, `shared_ptr` allows shared ownership
C) `shared_ptr` is older
D) No difference

### 2. What is `std::weak_ptr` used for?
A) Strong ownership
B) Breaking circular references
C) Faster access
D) Thread safety

### 3. Why use `std::make_shared` instead of `std::shared_ptr<T>(new T)`?
A) It's faster
B) It's exception-safe and more efficient
C) It's required by the standard
D) No difference

### 4. Can `std::unique_ptr` be copied?
A) Yes
B) No, but can be moved
C) Only with custom deleter
D) Only in C++14

### 5. What happens when the last `std::shared_ptr` to an object is destroyed?
A) Object is leaked
B) Object is destroyed
C) Program crashes
D) Nothing

### 6. Can you convert a `std::unique_ptr` to a `std::shared_ptr`?
A) Yes, using `std::shared_ptr` constructor
B) No, never
C) Only with a custom deleter
D) Only in C++17

### 7. What is the purpose of `std::enable_shared_from_this`?
A) To create shared pointers from raw pointers
B) To safely create shared_ptr from `this` inside a member function
C) To destroy shared pointers
D) To convert unique_ptr to shared_ptr

### 8. What is a custom deleter for smart pointers?
A) A function called when the pointer goes out of scope to clean up resources
B) A different way to allocate memory
C) A type of smart pointer
D) A debugging feature

### 9. What is the overhead of `std::shared_ptr` compared to `std::unique_ptr`?
A) No overhead
B) Extra memory for reference count and control block
C) Slower raw pointer access
D) Both B and C

### 10. When should you use `std::weak_ptr`?
A) When you need strong ownership
B) When you need to observe an object without owning it (e.g., cache, observer pattern)
C) When you need thread safety
D) When you need fast allocation

## Answers
1. B) `unique_ptr` allows exclusive ownership, `shared_ptr` allows shared ownership
2. B) Breaking circular references
3. B) It's exception-safe and more efficient
4. B) No, but can be moved
5. B) Object is destroyed
6. A) Yes, using `std::shared_ptr` constructor
7. B) To safely create shared_ptr from `this` inside a member function
8. A) A function called when the pointer goes out of scope to clean up resources
9. D) Both B and C
10. B) When you need to observe an object without owning it (e.g., cache, observer pattern)
