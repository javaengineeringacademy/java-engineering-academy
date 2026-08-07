# Advanced Pointers Quiz

## Questions

1. What is a pointer to a pointer?
2. What is the difference between int *arr[10] and int (*arr)[10]?
3. How do you declare a function pointer?
4. What is a callback function?
5. What is an opaque pointer?
6. How do you interpret complex declarations?
7. What is the spiral rule?
8. When would you use arrays of function pointers?
9. What is the advantage of opaque pointers?
10. How do you pass a function as an argument?
11. What is the difference between `const int *p` and `int * const p`?
12. How do you safely return a pointer from a function?
13. What is a dangling pointer and how do you prevent it?
14. What is the relationship between arrays and function pointers?
15. What is the `restrict` keyword and when do you use it?

## Answers

1. A variable that stores the address of another pointer
2. Array of 10 int pointers vs pointer to array of 10 ints
3. int (*func)(int, int) - parentheses are required
4. A function passed as an argument to another function
5. A pointer to a struct whose definition is hidden
6. Using the clockwise/spiral rule
7. Start at the variable name, go right, then spiral inward
8. For implementing state machines or dispatch tables
9. ABI stability and information hiding
10. Using function pointer syntax
11. `const int *p` — pointer to const int (can't modify data); `int * const p` — const pointer to int (can't repoint)
12. Return a pointer to static local, heap-allocated memory, or pass output via pointer parameter; never return pointer to local stack variable
13. A pointer referencing freed/invalid memory; prevent by setting pointers to NULL after freeing and validating before use
14. Arrays can be treated as pointers to their first element; function pointers can be stored in arrays and passed like data
15. `restrict` tells the compiler a pointer is the sole access path to data, enabling optimization; misuse causes undefined behavior
