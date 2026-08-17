# Virtual Threads Quiz

## Q1: What is the main difference between virtual threads and platform threads?

<details><summary>Answer</summary>Virtual threads are managed by the JVM, not the OS. They use ~1KB stack (vs 1MB for platform threads) and can be created in millions. The JVM multiplexes them onto a small number of carrier (platform) threads.</details>

## Q2: What is pinning and how do you avoid it?

<details><summary>Answer</summary>Pinning occurs when a virtual thread is stuck on a carrier thread during synchronized blocks or native methods. Avoid by replacing synchronized with ReentrantLock.</details>

## Q3: When should you NOT use virtual threads?

<details><summary>Answer</summary>For CPU-bound work (no advantage over platform threads), when ThreadLocal spans blocking operations (carrier thread changes), and when synchronized blocks are held during I/O (causes pinning).</details>

## Q4: How do you create a virtual thread executor?

<details><summary>Answer</summary>Executors.newVirtualThreadPerTaskExecutor() creates one virtual thread per submitted task. Use try-with-resources for automatic shutdown.</details>

## Q5: What is StructuredTaskScope?

<details><summary>Answer</summary>A Java 21 API for composing multiple virtual threads. ShutdownOnFailure cancels all if any fails. ShutdownOnSuccess cancels losers when one succeeds. Ensures all subtasks complete before scope closes.</details>

## Q6: What is the performance difference between virtual and platform threads for I/O?

<details><summary>Answer</summary>Virtual threads can be 10-100x faster for I/O-bound work because they don't block carrier threads during sleep/IO. 1000 tasks sleeping 10ms each: platform threads (pool=200) takes ~50ms, virtual threads takes ~10ms.</details>

## Q7: What is the carrier thread pool?

<details><summary>Answer</summary>A ForkJoinPool that backs virtual threads. Default size = available processors. Virtual threads mount/unmount from carrier threads during blocking. Set via -Djdk.virtualThreadScheduler.parallelism.</details>

## Q8: How does ScopedValue replace ThreadLocal?

<details><summary>Answer</summary>ScopedValue is immutable and automatically scoped to task execution. No memory leak risk, no cleanup needed. Use ScopedValue.where(key, value).run(() -> { ... }).</details>

## Q9: Can virtual threads use synchronized blocks?

<details><summary>Answer</summary>Yes, but synchronized causes pinning — the virtual thread is stuck on the carrier thread during the block. If the block contains I/O or sleep, use ReentrantLock instead.</details>

## Q10: What is the memory impact of 1 million virtual threads vs platform threads?

<details><summary>Answer</summary>Platform threads: ~1TB (1MB stack each). Virtual threads: ~1GB (1KB stack each). Virtual threads reduce stack memory by 1000x.</details>
