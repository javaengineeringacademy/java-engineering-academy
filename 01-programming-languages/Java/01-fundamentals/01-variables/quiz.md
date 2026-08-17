# Variables - Quiz

## Questions

### Q1: Which of the following is NOT a valid Java variable name?
- A) `_count`
- B) `$value`
- C) `3rdPlace`
- D) `MAX_SIZE`

### Q2: What is the default value of an instance `int` variable?
- A) `null`
- B) `0`
- C) `undefined`
- D) Compilation error

### Q3: Which keyword declares a constant in Java?
- A) `static`
- B) `final`
- C) `const`
- D) `var`

### Q4: What is the range of `byte` in Java?
- A) -128 to 127
- B) -256 to 255
- C) 0 to 255
- D) -32768 to 32767

### Q5: What does `var` do in Java 10+?
- A) Declares a dynamic type variable
- B) Infers the type from the initializer
- C) Creates a variant type
- D) Allows null assignment

### Q6: Which is a primitive type?
- A) `String`
- B) `Integer`
- C) `boolean`
- D) `Character`

### Q7: What is the scope of a local variable?
- A) Entire class
- B) Entire method
- C) Block where it's declared
- D) Entire package

### Q8: Can a `static final` variable be reassigned?
- A) Yes, anytime
- B) Yes, but only in static blocks
- C) No, it's a constant
- D) Only if it's not initialized

### Q9: What happens if a local variable is not initialized before use?
- A) Defaults to null
- B) Defaults to 0
- C) Compilation error
- D) Runtime exception

### Q10: What is the difference between `==` and `.equals()` for variables?
- A) They are identical
- B) `==` compares references, `.equals()` compares values
- C) `==` compares values, `.equals()` compares references
- D) `.equals()` is not usable on primitives

## Answers

1. **C** - Variable names cannot start with a digit
2. **B** - Instance fields default to 0 for numeric primitives
3. **B** - `final` declares constants
4. **A** - byte is 8-bit signed: -128 to 127
5. **B** - `var` enables local variable type inference
6. **C** - `boolean` is primitive; String, Integer, Character are wrapper classes
7. **C** - Local variables are scoped to the block they're declared in
8. **C** - `static final` variables are constants and cannot be reassigned
9. **C** - Local variables must be explicitly initialized before use
10. **B** - `==` checks reference equality, `.equals()` checks value equality
