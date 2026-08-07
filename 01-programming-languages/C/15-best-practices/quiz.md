# Best Practices Quiz

## Questions

1. What is the single responsibility principle?
2. Why should you avoid global variables?
3. What is the difference between * and const * parameters?
4. Why use static functions?
5. What is defensive programming?
6. Why check malloc return values?
7. What is the difference between assert and error handling?
8. Why use include guards?
9. What is code review?
10. Why document code?
11. What is the difference between header files and source files?
12. What is the purpose of `const` in function parameters?
13. What is the DRY principle?
14. What is the difference between a macro and an inline function?
15. What is API design and why does it matter?

## Answers

1. Each function should do one thing well
2. They create hidden dependencies and make testing harder
3. const * indicates read-only access
4. Limit scope to file, avoid naming conflicts
5. Assuming things can go wrong and handling them
6. malloc can fail and return NULL
7. assert is for programming errors; error handling for runtime errors
8. Prevent multiple inclusion of headers
9. Reviewing code by peers for quality and bugs
10. Helps others understand and maintain code
11. Header files declare interfaces (types, function prototypes); source files contain implementations
12. Signals the function will not modify the pointed-to data; allows passing const pointers and improves code clarity
13. Don't Repeat Yourself — avoid duplicating code by abstracting common logic into functions or modules
14. Macros do text substitution (no type checking, no scope); inline functions are type-safe, have scope, and are debuggable
15. Designing a clean, consistent public interface for a module; good API design improves usability, maintainability, and adoption
