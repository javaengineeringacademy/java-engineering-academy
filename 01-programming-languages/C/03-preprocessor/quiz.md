# Preprocessor Quiz

1. What is the difference between #include <> and #include ""?
2. What is the problem with this macro: #define SQUARE(x) x * x?
3. How do you create an include guard?
4. What does __FILE__ expand to?
5. What is the difference between #define and const?
6. How does #pragma once work?
7. What is stringification?
8. How do you concatenate tokens in a macro?
9. What is the preprocessor's role?
10. How do you conditionally compile code?

## Answers

1. <> searches system paths, "" searches local directory first
2. Missing parentheses: should be #define SQUARE(x) ((x) * (x))
3. Using #ifndef, #define, #endif
4. The current source file name
5. #define is text substitution, const is a typed variable
6. It's a non-standard directive to include a file once
7. Converting a macro argument to a string using #
8. Using the ## operator
9. Text substitution before compilation
10. Using #ifdef, #ifndef, #if, #elif, #else, #endif
