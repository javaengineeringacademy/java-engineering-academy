# Queue Interface Quiz

## Questions

### Q1: What is the difference between Queue and Deque?
**Answer:** Queue is FIFO (First-In-First-Out), Deque is double-ended (can add/remove from both ends).

### Q2: What are the two main operations in a Queue?
**Answer:** offer() (add to tail) and poll() (remove from head). Also peek() to view head without removing.

### Q3: What happens when you call poll() on an empty queue?
**Answer:** poll() returns null. remove() throws NoSuchElementException.

### Q4: What is the difference between offer() and add()?
**Answer:** offer() returns false if queue is full (for bounded queues). add() throws IllegalStateException if full.

### Q5: Is Queue thread-safe?
**Answer:** Depends on implementation. PriorityQueue is not. ArrayBlockingQueue and LinkedBlockingQueue are thread-safe.

### Q6: What is a bounded queue?
**Answer:** A queue with a fixed capacity. When full, add operations block or throw exceptions.

### Q7: What is the difference between PriorityQueue and ArrayDeque?
**Answer:** PriorityQueue orders by priority (not insertion order). ArrayDeque maintains insertion order (FIFO/LIFO).

### Q8: When should you use LinkedBlockingQueue over ArrayBlockingQueue?
**Answer:** When you need higher throughput with separate put/take locks, or optionally bounded queue.

### Q9: What is the purpose of the peek() method?
**Answer:** Returns the head element without removing it. Returns null if empty (peek()) or throws exception (element()).

### Q10: Can a Queue contain null elements?
**Answer:** Depends on implementation. PriorityQueue and ArrayDeque don't allow nulls. LinkedList allows one null.

## Bonus Questions

### Q11: What is the difference between poll() and remove()?
**Answer:** poll() returns null if empty. remove() throws NoSuchElementException if empty.

### Q12: How do you implement a stack using a Queue?
**Answer:** Use ArrayDeque as a stack (push/pop operations) — it's faster than Stack class.
