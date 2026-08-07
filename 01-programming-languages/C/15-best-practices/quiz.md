# Best Practices Quiz

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
