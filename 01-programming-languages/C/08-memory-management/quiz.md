# Memory Management Quiz

## Questions

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
11. What is a buffer overflow and how does it relate to memory management?
12. What is the difference between `free` and `realloc` with NULL?
13. What is memory fragmentation and how does it affect programs?
14. What is the purpose of `memcpy` vs `memmove`?
15. How do you detect memory leaks in production systems?

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
11. Writing beyond allocated memory bounds; can corrupt adjacent data or code, leading to crashes or security vulnerabilities
12. `free(NULL)` is a safe no-op; `realloc(NULL, size)` behaves like `malloc(size)`
13. External fragmentation: free memory is scattered in small blocks; internal fragmentation: allocated memory has unused space; both waste memory and slow allocation
14. `memcpy` copies bytes (undefined if regions overlap); `memmove` handles overlapping regions safely by using a temporary buffer
15. Use memory tracking tools (Valgrind, AddressSanitizer), custom allocators with logging, or heap profiling libraries like mimalloc
