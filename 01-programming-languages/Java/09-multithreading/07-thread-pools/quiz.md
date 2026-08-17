# Thread Pool Configuration Quiz

## Q1: What is the formula for CPU-bound pool sizing?

<details><summary>Answer</summary>poolSize = numCPUcores + 1. The extra thread compensates for page faults or OS scheduling overhead.</details>

## Q2: What is the formula for I/O-bound pool sizing?

<details><summary>Answer</summary>poolSize = numCPUcores * (1 + waitTime / computeTime). For 4 cores with 80% wait time: 4 * (1 + 4) = 20 threads.</details>

## Q3: Why should you never use Executors.newFixedThreadPool() in production?

<details><summary>Answer</summary>It uses an unbounded LinkedBlockingQueue. Under sustained load, tasks accumulate until OutOfMemoryError.</details>

## Q4: What is the difference between scheduleAtFixedRate and scheduleWithFixedDelay?

<details><summary>Answer</summary>scheduleAtFixedRate starts each task at fixed intervals (period measured from start to start). scheduleWithFixedDelay waits for the task to finish before starting the delay countdown.</details>

## Q5: When would you use a SynchronousQueue?

<details><summary>Answer</summary>When you want direct handoff — no buffering. The task must be immediately accepted by a thread, or rejected. Used in CachedThreadPool.</details>

## Q6: What does ThreadPoolExecutor.allowCoreThreadTimeOut(true) do?

<details><summary>Answer</summary>Allows core threads to be terminated after keepAliveTime of idleness. Default behavior keeps core threads alive forever.</details>

## Q7: How do you detect thread pool saturation?

<details><summary>Answer</summary>Monitor queue depth growth, active count at max, rejection rate > 0, and increasing task latency.</details>

## Q8: What is work-stealing in ForkJoinPool?

<details><summary>Answer</summary>Idle threads steal pending tasks from busy threads' deque (double-ended queue), improving utilization across the pool.</details>

## Q9: When should you use separate thread pools for different workloads?

<details><summary>Answer</summary>When tasks have different characteristics (CPU-bound vs I/O-bound, fast vs slow). Mixing them causes thread starvation — slow tasks block threads needed for fast tasks.</details>

## Q10: What are the signs of an undersized thread pool?

<details><summary>Answer</summary>Growing queue depth, increasing latency, CPU idle while tasks queue, and high active thread count relative to pool size.</details>
