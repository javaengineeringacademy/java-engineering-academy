# Templates Exercises

## Exercise 1: Generic Swap Function
Create a template function that swaps two values.

**Requirements:**
- Use template parameter for type
- Test with int, double, and string
- Ensure it works with const references

## Exercise 2: Template Container
Implement a simple template container class.

**Requirements:**
- Create a `Container<T>` class
- Implement add, remove, and get methods
- Use template specialization for string type
- Add bounds checking

## Exercise 3: Variadic Template Sum
Create a variadic template function that sums multiple arguments.

**Requirements:**
- Use recursive template instantiation
- Handle different numeric types
- Provide base case for single argument

## Exercise 4: SFINAE Type Checker
Implement a type trait using SFINAE.

**Requirements:**
- Create `is_integral` type trait
- Use `std::enable_if` for conditional compilation
- Test with different data types