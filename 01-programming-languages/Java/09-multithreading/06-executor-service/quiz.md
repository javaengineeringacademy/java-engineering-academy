# ExecutorService Quiz

## Q1: What happens when a fixed thread pool's queue is full?

<details><summary>Answer</summary>The RejectedExecutionHandler is invoked. Default is AbortPolicy which throws RejectedExecutionException.</details>

## Q2: What is the difference between shutdown() and shutdownNow()?

<details><summary>Answer</summary>shutdown() stops accepting new tasks and waits for existing tasks to complete. shutdownNow() interrupts running tasks and returns a list of pending tasks that were not started.</details>

## Q3: Why should you avoid Executors.newFixedThreadPool() in production?

<details><summary>Answer</summary>It uses an unbounded LinkedBlockingQueue. Under load, tasks pile up until you hit OutOfMemoryError. Use ThreadPoolExecutor directly with a bounded queue instead.</details>

## Q4: What is the recommended pool size for CPU-bound work?

<details><summary>Answer</summary>numCPUcores + 1. The extra thread compensates for page faults or other occasional stalls.</details>

## Q5: What is the recommended pool size for I/O-bound work?

<details><summary>Answer</summary>numCPUcores * (1 + waitTime / computeTime). For 4 cores with 80% wait time: 4 * (1 + 0.8/0.2) = 20 threads.</details>

## Q6: What is the difference between execute() and submit()?

<details><summary>Answer</summary>execute() returns void and swallows exceptions silently. submit() returns a Future and captures exceptions via ExecutionException.</details>

## Q7: When should you use CallerRunsPolicy?

<details><summary>Answer</summary>When you want backpressure — the caller thread runs the rejected task, slowing down submission rate to match processing rate.</details>

## Q8: How do you prevent thread leaks from ExecutorService?

<details><summary>Answer</summary>Always call shutdown() after use, then awaitTermination() with a reasonable timeout. Use shutdownNow() as a fallback if tasks don't complete in time.</details>

## Q9: What is the difference between ScheduledExecutorService.scheduleAtFixedRate and scheduleWithFixedDelay?

<details><summary>Answer</summary>scheduleAtFixedRate starts each task at fixed intervals regardless of previous task duration. scheduleWithFixedDelay waits for the previous task to finish before starting the delay countdown.</details>

## Q10: Why is ForkJoinPool different from other ExecutorService implementations?

<details><summary>Answer</summary>ForkJoinPool uses work-stealing — idle threads steal tasks from busy threads' queues. It's optimized for recursive divide-and-conquer tasks, not general-purpose thread pooling.</details>
