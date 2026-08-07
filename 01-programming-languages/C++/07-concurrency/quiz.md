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

## Answers
1. B) When two threads access shared data without synchronization
2. B) To automatically manage mutex locking/unlocking
3. A) Atomic operations on shared data
4. B) When two or more threads are blocked forever
5. A) `join()` waits, `detach()` runs in background