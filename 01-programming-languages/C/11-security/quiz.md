# Security Quiz

1. What is a buffer overflow?
2. Why is gets() dangerous?
3. What is a format string attack?
4. What is integer overflow?
5. How do you prevent null pointer dereference?
6. What compiler flags help with security?
7. What is stack protection?
8. What is ASLR?
9. What is the principle of least privilege?
10. What is input validation?

## Answers

1. Writing beyond array bounds, corrupting memory
2. It doesn't check buffer size
3. Using user input as format string (e.g., printf(user_input))
4. Arithmetic operation exceeding type's range
5. Always check pointers before dereferencing
6. -fstack-protector, -D_FORTIFY_SOURCE=2
7. Compiler feature to detect stack buffer overruns
8. Address Space Layout Randomization - randomizes memory layout
9. Granting minimum necessary permissions
10. Verifying input meets expected constraints
