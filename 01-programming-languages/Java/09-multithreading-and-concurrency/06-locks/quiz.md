# Locks Quiz

## Question 1
What happens if you forget to call `unlock()` in a finally block?

- A) Nothing
- B) The lock is never released, causing deadlock
- C) The lock auto-releases after timeout
- D) An exception is thrown

**Answer: B**
Unlike `synchronized`, explicit locks require manual unlock. Forgetting causes permanent deadlock.

## Question 2
When should you use ReadWriteLock over ReentrantLock?

- A) Always
- B) When reads significantly outnumber writes
- C) When writes outnumber reads
- D) Never

**Answer: B**
ReadWriteLock allows multiple concurrent readers but exclusive writes. It's beneficial when reads >> writes.
