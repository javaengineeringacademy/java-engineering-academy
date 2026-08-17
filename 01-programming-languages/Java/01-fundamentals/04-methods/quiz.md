# Methods - Quiz

## Questions

### Q1: What is method overloading?
- A) Same method name, different parameter list
- B) Same method name, same parameters
- C) Overriding a parent method
- D) Calling a method recursively

### Q2: Can two methods differ only by return type?
- A) Yes
- B) No, that causes a compilation error
- C) Only with generics
- D) Only in interfaces

### Q3: What is `varargs`?
- A) A method that takes no arguments
- B) A method that accepts a variable number of arguments
- C) A loop construct
- D) An operator

### Q4: What is the syntax for varargs?
- A) `String... args`
- B) `String[] args`
- C) `String args...`
- D) `String* args`

### Q5: What is the scope of a method parameter?
- A) Entire class
- B) Only within the method
- C) Entire package
- D) The entire program

### Q6: Can a method return multiple values?
- A) Yes, using an array or record
- B) No, never
- C) Only with void
- D) Only with generics

### Q7: What does `static` mean for a method?
- A) Cannot be changed
- B) Belongs to the class, not an instance
- C) Is thread-safe
- D) Runs faster

### Q8: What is a recursive method?
- A) A method that calls another method
- B) A method that calls itself
- C) A method with no return
- D) A static method

### Q9: What happens if a recursive method has no base case?
- A) Compilation error
- B) StackOverflowError
- C) Returns null
- D) Runs forever

### Q10: What is pass-by-value in Java?
- A) References are passed directly
- B) A copy of the value is passed (primitives) or a copy of the reference (objects)
- C) Objects are cloned
- D) Nothing is passed

## Answers

1. **A** - Overloading: same name, different parameter types/count
2. **B** - Return type alone cannot distinguish overloaded methods
3. **B** - varargs allow passing zero or more arguments of the same type
4. **A** - `Type... name` is varargs syntax (compiled to array)
5. **B** - Parameters exist only within the method body
6. **A** - Return an array, record, or wrapper object
7. **B** - `static` means class-level; no instance needed to call
8. **B** - Recursion is a method calling itself with a base case
9. **B** - Infinite recursion causes StackOverflowError
10. **B** - Java always passes by value; for objects, a copy of the reference is passed
