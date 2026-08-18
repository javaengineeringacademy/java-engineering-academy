# Synchronization Quiz

## Question 1
What does `volatile` guarantee?

- A) Atomicity of compound operations
- B) Mutual exclusion
- C) Visibility of writes across threads
- D) Thread scheduling

**Answer: C**
`volatile` ensures that reads and writes go directly to main memory, making changes visible to all threads. It does NOT provide atomicity for compound operations like `count++`.

## Question 2
What causes a deadlock?

- A) Too many threads
- B) Circular lock dependency
- C) Using synchronized
- D) Using volatile

**Answer: B**
Deadlock occurs when two or more threads each hold a lock the other needs, creating a circular wait.
