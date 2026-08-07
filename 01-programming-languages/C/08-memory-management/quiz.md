# Memory Management Quiz

1. What is the difference between malloc and calloc?
2. What happens if malloc fails?
3. What is a memory leak?
4. What is a dangling pointer?
5. What is a double free?
6. How do you prevent memory leaks?
7. What does valgrind do?
8. What is a memory pool?
9. What is the difference between stack and heap?
10. When should you use realloc?

## Answers

1. calloc zeros memory; malloc does not
2. Returns NULL
3. Memory allocated but not freed
4. Pointer to memory that has been freed
5. Freeing the same memory twice
6. Track allocations and free all memory
7. Detects memory leaks and errors
8. Pre-allocated memory for fast allocation/deallocation
9. Stack: automatic, fast; Heap: manual, large
10. When you need to resize an allocation

## Answers

1. calloc zeros memory; malloc does not
2. Returns NULL
3. Memory allocated but not freed
4. Pointer to memory that has been freed
5. Freeing the same memory twice
6. Track allocations and free all memory
7. Detects memory leaks and errors
8. Pre-allocated memory for fast allocation/deallocation
9. Stack: automatic, fast; Heap: manual, large
10. When you need to resize an allocation
