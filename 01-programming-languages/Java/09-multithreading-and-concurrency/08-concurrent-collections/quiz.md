# Concurrent Collections Quiz

## Question 1
Why is ConcurrentHashMap faster than Collections.synchronizedMap()?

- A) It uses no locks
- B) It uses lock striping (multiple locks for different segments)
- C) It uses volatile
- D) It uses atomic operations

**Answer: B**
ConcurrentHashMap divides the map into segments, each with its own lock. This allows concurrent reads and writes to different segments.

## Question 2
What happens when you iterate over CopyOnWriteArrayList while another thread modifies it?

- A) ConcurrentModificationException
- B) The iterator sees a snapshot of the list at creation time
- C) The iterator blocks
- D) Undefined behavior

**Answer: B**
CopyOnWriteArrayList creates a new copy of the array on each write. Iterators operate on the snapshot, so they never see concurrent modifications.
