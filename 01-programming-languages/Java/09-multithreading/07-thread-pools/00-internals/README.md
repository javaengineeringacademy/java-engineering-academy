# Thread Pool Internals

## ThreadPoolExecutor Architecture

### Core Parameters

```
corePoolSize    → Minimum threads (always alive unless allowCoreThreadTimeOut)
maximumPoolSize → Upper thread limit
keepAliveTime   → Idle timeout for excess threads
workQueue       → BlockingQueue holding pending tasks
threadFactory   → Creates new threads
handler         → RejectedExecutionHandler
```

### Internal State Machine

```java
private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
```

ctl encodes two values in one atomic int:
- Worker count (lower 29 bits)
- Run state (upper 3 bits): RUNNING, SHUTDOWN, STOP, TIDYING, TERMINATED

### State Transitions

```
RUNNING → SHUTDOWN    (shutdown() called)
RUNNING → STOP        (shutdownNow() called)
SHUTDOWN → STOP       (shutdownNow() called)
STOP → TIDYING        (all workers terminated, queue empty)
SHUTDOWN → TIDYING    (queue empty, all workers terminated)
TIDYING → TERMINATED  (terminated() hook completes)
```

### Task Execution Algorithm

```java
public void execute(Runnable command) {
    int c = ctl.get();
    if (workerCountOf(c) < corePoolSize) {
        // Add worker with core task
        if (addWorker(command, true))
            return;
        c = ctl.get();
    }
    if (isRunning(c) && workQueue.offer(command)) {
        // Queued successfully, check if we need to add a worker
        int recheck = ctl.get();
        if (!isRunning(recheck) && remove(command))
            reject(command);
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
    else if (!addWorker(command, false))
        // Pool full, reject
        reject(command);
}
```

### Worker Thread Loop

```java
// AbstractQueuedSynchronizer-based loop
final void runWorker(Worker w) {
    Thread wt = Thread.currentThread();
    Runnable task = w.firstTask;
    w.firstTask = null;
    while (task != null || (task = getTask()) != null) {
        task.run();
        task = null;
    }
    // Worker exits when getTask() returns null
}

// getTask() — retrieves from queue or returns null to terminate
private Runnable getTask() {
    boolean timed = allowCoreThreadTimeOut || workerCount > corePoolSize;
    Runnable r = timed ? workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS)
                       : workQueue.take();
    return r;
}
```

### Thread Creation

```java
private boolean addWorker(Runnable firstTask, boolean core) {
    // 1. Atomically increment worker count
    // 2. Create new Worker (wraps thread + task)
    // 3. Add Worker to workers HashSet
    // 4. Start the thread
    Worker(Runnable firstTask) {
        setState(-1);
        this.firstTask = firstTask;
        this.thread = getThreadFactory().newThread(this);
    }
}
```
