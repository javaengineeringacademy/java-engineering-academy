# Structures Quiz

1. What is the difference between struct and typedef struct?
2. How do you access a member of a structure pointer?
3. What is structure padding?
4. What is a union?
5. When would you use a bit field?
6. Can structures contain other structures?
7. How do you initialize a structure at declaration?
8. What is the difference between . and -> operators?
9. How do you pass a large structure to a function efficiently?
10. What is the size of a union?

## Answers

1. struct requires using `struct name`, typedef creates an alias
2. Using the arrow operator (->)
3. Padding bytes inserted for memory alignment
4. A data type where all members share the same memory
5. When you need precise bit-level control (flags, registers)
6. Yes, through nesting
7. Using brace initialization: struct S s = {val1, val2};
8. . for direct access, -> for pointer access
9. Pass by pointer (const if read-only)
10. Size of the largest member
