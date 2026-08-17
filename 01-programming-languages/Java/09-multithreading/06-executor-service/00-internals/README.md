# ExecutorService Internals

## Core Architecture

ExecutorService is built on the ThreadPoolExecutor class, which manages a pool of worker threads and a task queue.

### Thread States in ThreadPoolExecutor

```
New → Running → Shutdown → Terminated
         ↓
    ShuttingDown
```

### Internal Components

1. **Core Pool** — Minimum threads that stay alive (even idle)
2. **Maximum Pool** — Upper limit on thread count
3. **Work Queue** — BlockingQueue holding pending tasks
4. **Thread Factory** — Creates new threads with custom names/daemon settings
5. **Rejection Handler** — Handles task rejection when pool/queue full

### Task Execution Flow

```
submit(task) → queue.offer(task) → core thread available?
                                      ├── Yes → thread.execute(task)
                                      └── No → pool size < max?
                                                  ├── Yes → create new thread
                                                  └── No → rejection handler
```

### Thread Lifecycle

1. Core threads created on pool creation (or lazily with prestartAllCoreThreads)
2. When a task arrives and all core threads busy → task goes to queue
3. When queue is full and pool < max → new thread created
4. When pool = max and queue full → rejection handler invoked
5. Idle threads beyond core count are terminated after keepAliveTime

### Key Fields

```java
private final AtomicInteger ctl;        // Pool state + worker count
private final BlockingQueue<Runnable> workQueue;
private final HashSet<Worker> workers;
private int corePoolSize;
private int maximumPoolSize;
private long keepAliveTime;
private volatile RejectedExecutionHandler handler;
```

### Worker Thread Implementation

Each worker is a Thread that:
1. Takes task from queue (blocking if empty)
2. Executes the task
3. Returns to pool for next task
4. Terminates if queue empty and not core thread

### Shutdown Sequence

1. `shutdown()` — Sets state to SHUTDOWN, stops accepting new tasks
2. Workers process remaining tasks in queue
3. `shutdownNow()` — Sets state to STOP, interrupts running workers
4. `tryTerminate()` — Checks if pool can transition to TERMINATED
