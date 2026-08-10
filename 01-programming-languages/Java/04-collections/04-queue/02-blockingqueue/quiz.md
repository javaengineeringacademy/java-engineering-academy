# BlockingQueue Quiz

## Questions

### Q1: What is the difference between ArrayBlockingQueue and LinkedBlockingQueue?
**Answer:** ArrayBlockingQueue is bounded (fixed capacity), uses arrays, and has a single lock. LinkedBlockingQueue can be optionally bounded, uses linked nodes, and has separate locks for put and take operations.

### Q2: What happens when you call put() on a full blocking queue?
**Answer:** put() blocks until space becomes available. offer() returns false immediately, and offer(timeout) blocks for the specified time.

### Q3: Is BlockingQueue thread-safe?
**Answer:** Yes. All BlockingQueue implementations are thread-safe. They use locks or CAS operations for concurrent access.

### Q4: What is the difference between take() and poll()?
**Answer:** take() blocks until an element is available. poll() returns null immediately if empty, poll(timeout) blocks for the specified time.

### Q5: When should you use SynchronousQueue?
**Answer:** When you need direct handoff between producer and consumer threads. SynchronousQueue has no capacity — each put must wait for a take.

### Q6: What is the default capacity of ArrayBlockingQueue?
**Answer:** You must specify capacity in the constructor. There's no default capacity.

### Q7: Can you have null elements in a BlockingQueue?
**Answer:** No. Most BlockingQueue implementations throw NullPointerException if you try to add null elements.

### Q8: What is the difference between offer() and add()?
**Answer:** offer() returns false if the queue is full. add() throws IllegalStateException if the queue is full.

### Q9: When should you use LinkedBlockingQueue over ArrayBlockingQueue?
**Answer:** When you need higher throughput with separate put/take locks, or when you want an optionally bounded queue.

### Q10: Can you use BlockingQueue for producer-consumer patterns?
**Answer:** Yes. BlockingQueue is ideal for producer-consumer patterns. Producers call put(), consumers call take(). The queue handles synchronization automatically.

## Bonus Questions

### Q11: What is drains() method used for?
**Answer:** drainTo() moves all or some elements from the queue to another collection in a single operation, which is more efficient than individual poll() calls.

### Q12: How do you implement a bounded buffer using BlockingQueue?
**Answer:** Use ArrayBlockingQueue with a fixed capacity. Producers call put() (blocks when full), consumers call take() (blocks when empty).
