# C Capstone Project — Quiz

## Module Integration Quiz

### Q1: Which module provides the opaque pointer pattern used for ABI stability?
- A) Module 02 — Structures
- B) Module 05 — Pointers Advanced
- C) Module 16 — Senior Topics
- D) Both B and C

**Answer: D** — Opaque pointers are introduced in Module 05 and applied for ABI stability in Module 16.

### Q2: What is the primary purpose of a thread pool in the capstone project?
- A) To avoid repeated malloc/free calls
- B) To handle concurrent requests without creating a thread per connection
- C) To implement virtual dispatch
- D) To manage file I/O

**Answer: B** — Thread pools reuse a fixed number of threads to handle concurrent work, avoiding the overhead of thread creation.

### Q3: Which data structure is best suited for the key-value store in the capstone?
- A) Linked list
- B) Binary search tree
- C) Hash table with open addressing
- D) Array

**Answer: C** — Hash tables provide O(1) average-case lookup, ideal for key-value stores.

### Q4: Why use a custom memory pool allocator instead of malloc/free?
- A) malloc is not available in C
- B) Pool allocators reduce fragmentation and provide predictable performance
- C) Pool allocators are faster than malloc on all platforms
- D) Pool allocators are required by the C standard

**Answer: B** — Pool allocators eliminate fragmentation for fixed-size objects and provide O(1) allocation/deallocation.

### Q5: What is the correct order for resource cleanup in the capstone project?
- A) Free in the same order as allocation
- B) Free in reverse order of allocation
- C) Free in any order
- D) Only free at program exit

**Answer: B** — Reverse order ensures dependent resources are released before the resources they depend on.

### Q6: How does the capstone project handle platform differences?
- A) Separate codebases for each platform
- B) Platform abstraction layer with function pointers
- C) #ifdef everywhere in the code
- D) Only supports Linux

**Answer: B** — A platform abstraction layer isolates platform-specific code into separate files behind a uniform interface.

### Q7: What protocol does the capstone network server use?
- A) HTTP only
- B) Custom binary protocol over TCP
- C) UDP broadcast
- D) Unix domain sockets only

**Answer: B** — A custom binary protocol over TCP provides efficiency and flexibility for the capstone project.

### Q8: Why is `const` important in the capstone API?
- A) It makes variables immutable at compile time
- B) It documents intent and enables compiler optimizations
- C) It prevents all bugs
- D) It is required by the C standard

**Answer: B** — `const` communicates that a parameter is read-only, enabling compiler optimizations and preventing accidental modification.

### Q9: What tool is used to detect memory leaks in the capstone project?
- A) gcc
- B) gdb
- C) valgrind
- D) make

**Answer: C** — Valgrind's Memcheck tool detects memory leaks, use-after-free, and other memory errors.

### Q10: How does the capstone project ensure thread safety?
- A) No shared state is used
- B) All shared state is protected by mutexes
- C) Only atomic operations are used
- D) Threads communicate via global variables

**Answer: B** — Mutexes protect all shared mutable state to prevent race conditions.

### Q11: What is the purpose of the reference counting in the capstone connection handler?
- A) To count the number of bytes sent
- B) To prevent use-after-free by tracking how many threads reference an object
- C) To limit the number of connections
- D) To implement a cache

**Answer: B** — Reference counting ensures objects are only freed when no thread is using them.

### Q12: Which module's concept is applied when the capstone uses `snprintf` instead of `sprintf`?
- A) Module 01 — Fundamentals
- B) Module 11 — Security
- C) Module 12 — Performance
- D) Both A and B

**Answer: D** — String formatting is a fundamental skill (Module 01) and using `snprintf` prevents buffer overflows (Module 11).

### Q13: What is the benefit of using `epoll`/`kqueue` in the capstone network server?
- A) Simpler code
- B) Event-driven I/O that scales to thousands of connections without thread-per-connection
- C) Faster computation
- D) Better memory usage

**Answer: B** — Event-driven I/O multiplexing handles many connections efficiently without the overhead of one thread per connection.

### Q14: How does the capstone project handle configuration?
- A) Hardcoded values only
- B) Config file parsed at startup + CLI argument overrides
- C) Environment variables only
- D) Compile-time constants only

**Answer: B** — Configuration is loaded from a file with CLI argument overrides for flexibility.

### Q15: What is the purpose of the `__attribute__((visibility("hidden")))` annotation?
- A) To make functions faster
- B) To hide internal symbols from the shared library's public ABI
- C) To enable debugging
- D) To improve code readability

**Answer: B** — Hidden visibility reduces the ABI surface and improves load time by excluding internal symbols from the dynamic symbol table.
