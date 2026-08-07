# Smart Pointers Exercises

## Exercise 1: Unique Pointer Usage
Implement a simple object ownership system using `std::unique_ptr`.

**Requirements:**
- Create a class hierarchy (Animal → Dog, Cat)
- Use `std::unique_ptr` for ownership
- Demonstrate moving ownership
- Show automatic cleanup

## Exercise 2: Shared Pointer Reference Counting
Demonstrate reference counting with `std::shared_ptr`.

**Requirements:**
- Create shared pointers to objects
- Show reference count changes
- Demonstrate circular reference problem
- Use `std::weak_ptr` to break cycle

## Exercise 3: Custom Deleter
Implement a custom deleter for file handling.

**Requirements:**
- Create `std::unique_ptr` with custom deleter
- Use `std::shared_ptr` with custom deleter
- Demonstrate resource cleanup

## Exercise 4: Smart Pointer Performance
Compare performance of smart pointers vs raw pointers.

**Requirements:**
- Measure creation time
- Measure access time
- Measure destruction time
- Document findings