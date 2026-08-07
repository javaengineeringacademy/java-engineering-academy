# Concurrency Quiz

## Questions

### 1. What is a data race?
A) When threads run at different speeds
B) When two threads access shared data without synchronization
C) When one thread is faster than another
D) When threads access different data

### 2. What is the purpose of `std::lock_guard`?
A) To manually lock a mutex
B) To automatically manage mutex locking/unlocking
C) To create a new thread
D) To wait for a thread to finish

### 3. What is `std::atomic` used for?
A) Atomic operations on shared data
B) Creating atomic threads
C) Memory allocation
D) File I/O

### 4. What is a deadlock?
A) When a thread crashes
B) When two or more threads are blocked forever
C) When a thread runs too fast
D) When memory is exhausted

### 5. What is the difference between `join()` and `detach()`?
A) `join()` waits, `detach()` runs in background
B) `join()` runs in background, `detach()` waits
C) No difference
D) `join()` is faster

### 6. What is a condition variable used for?
A) Creating conditions
B) Waiting for a condition to be met before proceeding
C) Checking thread conditions
D) Setting thread priorities

### 7. What is `std::unique_lock` and how does it differ from `std::lock_guard`?
A) No difference
B) `unique_lock` supports deferred locking, try-lock, and manual unlock; `lock_guard` is simpler and always holds the lock
C) `lock_guard` is more flexible
D) `unique_lock` is deprecated

### 8. What is a thread-safe queue?
A) A queue that works on one thread
B) A queue that can be safely accessed from multiple threads simultaneously
C) A queue that uses threads internally
D) A queue that never blocks

### 9. What is `std::future` and `std::promise`?
A) Error handling mechanisms
B) `promise` sets a value; `future` retrieves it asynchronously
C) Memory management tools
D) Synchronization primitives

### 10. What is `std::async`?
A) Running code synchronously
B) Launching a function asynchronously and returning a future
C) Creating a new thread manually
D) Waiting for all threads to finish

## Answers
1. B) When two threads access shared data without synchronization
2. B) To automatically manage mutex locking/unlocking
3. A) Atomic operations on shared data
4. B) When two or more threads are blocked forever
5. A) `join()` waits, `detach()` runs in background
6. B) Waiting for a condition to be met before proceeding
7. B) `unique_lock` supports deferred locking, try-lock, and manual unlock; `lock_guard` is simpler and always holds the lock
8. B) A queue that can be safely accessed from multiple threads simultaneously
9. B) `promise` sets a value; `future` retrieves it asynchronously
10. B) Launching a function asynchronously and returning a future
