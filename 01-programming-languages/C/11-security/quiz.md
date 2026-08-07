# Security Quiz

## Questions

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
11. What is a use-after-free vulnerability?
12. What is the difference between XSS and SQL injection?
13. What is a race condition exploit (TOCTOU)?
14. What are canary values in stack protection?
15. What is the purpose of `-fstack-protector-strong`?

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
11. Accessing memory after it has been freed; attacker can control reallocated memory to execute arbitrary code
12. XSS injects malicious scripts into web pages; SQL injection manipulates database queries through unsanitized input
13. TOCTOU (Time of Check to Time of Use): attacker exploits gap between checking a resource and using it; e.g., symlink swap between stat() and open()
14. Random values placed on the stack before function return; if a buffer overflow overwrites the return address, the canary mismatch is detected and the program aborts
15. Extends `-fstack-protector` to protect more functions — those with buffers, arrays, or address-taken variables — providing broader coverage against stack smashing
