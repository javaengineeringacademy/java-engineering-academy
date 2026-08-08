# Modern C++ Quiz

## Questions

### 1. What does `auto` do in C++11 and later?
A) Declares a variable with static storage duration
B) Deduces the variable's type from its initializer
C) Creates a variable with runtime type information
D) Declares a variable as anonymous

**Answer**: **B** — `auto` uses the initializer to deduce the type at compile time. It does NOT affect storage duration or create runtime type info. Example: `auto x = 42;` deduces `int`. It's especially useful with complex types like iterators: `auto it = vec.begin();`.

---

### 2. What is the key difference between `std::move` and an actual move operation?
A) `std::move` performs the move immediately
B) `std::move` casts its argument to an rvalue reference, enabling move overloads to be selected
C) `std::move` copies the data efficiently
D) `std::move` deletes the source object

**Answer**: **B** — `std::move` is just a cast to `T&&` (rvalue reference). It does NOT move anything by itself. It merely enables the compiler to choose move constructors/assignment operators. The actual move happens when the rvalue reference is used.

---

### 3. What happens if you use a lambda that captures by reference, and the referenced variable goes out of scope before the lambda executes?
A) The lambda uses a copy of the variable
B) The lambda compiles but behaves unpredictably — likely a dangling reference
C) The compiler prevents the lambda from being created
D) The lambda captures a default value

**Answer**: **B** — Capturing by reference (`[&]`) creates a reference to the original variable. If the variable is destroyed before the lambda runs, you get a dangling reference — undefined behavior. This is a common source of bugs in async code.

---

### 4. What is the purpose of `std::optional`?
A) To create a container that holds any type
B) To represent a value that may or may not exist, without using sentinel values or pointer indirection
C) To make variables optional in function signatures
D) To provide a nullable reference type

**Answer**: **B** — `std::optional<T>` explicitly communicates "this might not have a value." It avoids sentinel values (like -1 for "not found") and raw pointers. Use `.has_value()` to check, `.value()` or `*opt` to access.

---

### 5. In C++17, what is `std::variant`?
A) A variable that can change its type at runtime
B) A type-safe union that can hold one of several types, with automatic type management
C) A template that creates multiple versions of a class
D) A union that allows multiple active members

**Answer**: **B** — `std::variant` is a discriminated union. It can hold one of its listed types at a time. Use `std::holds_alternative<T>()` to check which type is active, `std::get<T>()` to access, or `std::visit()` to handle all cases.

---

### 6. What is structured binding in C++17?
A) A way to create aliases for existing variables
B) A way to decompose tuples, pairs, or aggregate types into named variables
C) A way to bind template parameters
D) A way to create compile-time constants

**Answer**: **B** — Structured bindings let you write `auto [x, y] = pair;` or `auto [name, age] = get_person();`. They work with pairs, tuples, arrays, and structs with public members. They make code cleaner than `.first`/`.second`.

---

### 7. What is a mutable lambda?
A) A lambda that cannot be copied
B) A lambda whose captured variables can be modified, even in a `const` lambda call operator
C) A lambda that modifies external state without capturing
D) A lambda that can only be called once

**Answer**: **B** — By default, the `operator()` of a lambda is `const`. Adding `mutable` allows the lambda to modify its captured-by-value copies. This is useful for stateful lambdas like counters. Example: `[count = 0]() mutable { return ++count; }`.

---

### 8. What is the advantage of `std::string_view` over `const std::string&`?
A) `string_view` is always faster
B) `string_view` can reference substrings without allocation, and works with string literals without creating a temporary `std::string`
C) `string_view` can modify the underlying string
D) `string_view` owns its memory

**Answer**: **B** — `string_view` is a non-owning pointer+length pair. It avoids constructing a `std::string` from a `const char*` or `const char[]`. However, the underlying data must remain valid while the `string_view` is in use. It's read-only.

---

### 9. What does `constexpr` enable that `const` does not?
A) Compile-time evaluation of functions and variables
B) Runtime constness
C) Thread-safe constants
D) Automatic memory management

**Answer**: **A** — `const` means "read-only at runtime." `constexpr` means "can be evaluated at compile time." A `constexpr` function can be used where a compile-time constant is required (e.g., array sizes, template arguments). At runtime, `constexpr` functions still work normally.

---

### 10. What is the benefit of concepts (C++20) over `enable_if`?
A) Concepts are faster at runtime
B) Concepts provide clearer, more readable constraint syntax and produce better error messages than SFINAE-based `enable_if`
C) Concepts can only be used with classes
D) Concepts replace the need for type checking

**Answer**: **B** — Concepts express constraints directly in the template declaration: `template<Numeric T>`. With `enable_if`, errors are cryptic. Concepts produce clear messages like "T does not satisfy Numeric." They also work with function overloading and are composable.

---

## Detailed Answer Explanations

| # | Correct | Key Takeaway |
|---|---------|-------------|
| 1 | B | `auto` deduces type from initializer at compile time |
| 2 | B | `std::move` is just a cast — the actual move happens at usage |
| 3 | B | Dangling references from `[&]` capture are undefined behavior |
| 4 | B | `std::optional` explicitly represents nullable values |
| 5 | B | `std::variant` is a type-safe discriminated union |
| 6 | B | Structured bindings decompose composite types into named variables |
| 7 | B | `mutable` lets lambdas modify captured values |
| 8 | B | `string_view` avoids allocation for read-only string references |
| 9 | A | `constexpr` enables compile-time evaluation; `const` only means read-only |
| 10 | B | Concepts are more readable and produce better errors than SFINAE |
