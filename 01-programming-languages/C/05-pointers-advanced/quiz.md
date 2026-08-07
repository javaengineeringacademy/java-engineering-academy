# Advanced Pointers Quiz

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
