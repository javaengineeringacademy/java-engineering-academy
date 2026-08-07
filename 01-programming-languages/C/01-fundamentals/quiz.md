# Fundamentals Quiz

## Variables

1. What is the difference between `int *p` and `int * const p`?
2. What is the size of `char` on most systems?
3. What happens if you use an uninitialized variable?

## Operators

4. What is the result of `5 / 2` in C?
5. What is the difference between `&&` and `&`?
6. What is operator precedence?

## Control Flow

7. What is the difference between `while` and `do-while`?
8. When would you use `goto`?
9. What happens if you forget `break` in a switch case?

## Functions

10. What is pass by value vs pass by reference?
11. What is a static function?
12. What is recursion?

## Arrays

13. How do you find the size of an array?
14. What happens when you access an array out of bounds?
15. How do arrays decay to pointers?

## Strings

16. What marks the end of a C string?
17. What is the difference between `strcpy` and `strncpy`?
18. Why is `==` not used to compare strings?

## Pointers

19. What is a null pointer?
20. What is pointer arithmetic?
21. What is a void pointer?

## Memory

22. What is the difference between stack and heap?
23. What does `malloc` return on failure?
24. What is a memory leak?

## Answers

1. `int *p` is a pointer to int; `int * const p` is a const pointer (cannot change what it points to)
2. 1 byte
3. Undefined behavior
4. 2 (integer division)
5. `&&` is logical AND, `&` is bitwise AND
6. Rules determining order of evaluation
7. `do-while` executes at least once
8. Breaking out of nested loops, error handling
9. Falls through to next case
10. Value: copy of data; Reference: pointer to data
11. File-scope only function
12. Function calling itself
13. `sizeof(arr) / sizeof(arr[0])`
14. Undefined behavior
15. Array name becomes pointer to first element
16. Null character '\0'
17. `strncpy` allows specifying max length
18. `==` compares pointers, not content
19. Points to nothing (NULL)
20. Adding/subtracting from pointers
21. Pointer to void (generic pointer)
22. Stack: automatic, fast; Heap: manual, large
23. NULL
24. Memory allocated but never freed

## Answers

1. `int *p` is a pointer to int; `int * const p` is a const pointer (cannot change what it points to)
2. 1 byte
3. Undefined behavior
4. 2 (integer division)
5. `&&` is logical AND, `&` is bitwise AND
6. Rules determining order of evaluation
7. `do-while` executes at least once
8. Breaking out of nested loops, error handling
9. Falls through to next case
10. Value: copy of data; Reference: pointer to data
11. File-scope only function
12. Function calling itself
13. `sizeof(arr) / sizeof(arr[0])`
14. Undefined behavior
15. Array name becomes pointer to first element
16. Null character '\0'
17. `strncpy` allows specifying max length
18. `==` compares pointers, not content
19. Points to nothing (NULL)
20. Adding/subtracting from pointers
21. Pointer to void (generic pointer)
22. Stack: automatic, fast; Heap: manual, large
23. NULL
24. Memory allocated but never freed
