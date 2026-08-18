# Virtual Threads Quiz

## Question 1
How many virtual threads can you create compared to platform threads?

- A) Same number
- B) Virtual threads are limited to 1000
- C) Millions of virtual threads vs thousands of platform threads
- D) Virtual threads are slower

**Answer: C**
Virtual threads use ~1KB stack vs 1MB for platform threads, enabling millions of concurrent threads.

## Question 2
What happens when a virtual thread blocks on I/O?

- A) The carrier thread is blocked
- B) The virtual thread is unmounted from the carrier, freeing it
- C) The JVM throws an exception
- D) The thread pool is exhausted

**Answer: B**
When a virtual thread blocks, the JVM unmounts it from the carrier thread and mounts another, so the carrier is never wasted on blocking.
