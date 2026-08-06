# ThreadPoolExecutor Source Code Walkthrough

ThreadPoolExecutor is the foundation of Java's concurrency framework. Understanding its implementation is crucial for building scalable applications.

## Core Architecture

### Key Fields

```java
private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
private static final int COUNT_BITS = Integer.SIZE - 3; // 29
private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

private static final int RUNNING    = -1 << COUNT_BITS;
private static final int SHUTDOWN   =  0 << COUNT_BITS;
private static final int STOP       =  1 << COUNT_BITS;
private static final int TIDYING    =  2 << COUNT_BITS;
private static final int TERMINATED =  3 << COUNT_BITS;
```

### State Encoding

```java
private static int runStateOf(int c)     { return c & ~CAPACITY; }
private static int workerCountOf(int c)  { return c & CAPACITY; }
private static int ctlOf(int rs, int wc) { return rs | wc; }
```

## Core Pool vs Max Pool

### Configuration

```java
private volatile int corePoolSize;
private volatile int maximumPoolSize;
private volatile long keepAliveTime;
```

### Behavior

```java
if (workerCount < corePoolSize) {
    addWorker(null, true);
} else if (isRunning(c) && workQueue.offer(command)) {
    if (!isRunning(recheck) && remove(command))
        reject(command);
    else if (workerCountOf(recheck) == 0)
        addWorker(null, false);
} else if (!addWorker(command, false))
    reject(command);
```

## Work Queue (BlockingQueue)

### Interface

```java
private final BlockingQueue<Runnable> workQueue;
```

### Common Implementations

1. **LinkedBlockingQueue**: Unbounded (default)
2. **ArrayBlockingQueue**: Bounded, FIFO
3. **SynchronousQueue**: Zero capacity
4. **PriorityBlockingQueue**: Priority ordering

## RejectedExecutionHandler

### Built-in Policies

```java
// 1. AbortPolicy (default)
public static class AbortPolicy implements RejectedExecutionHandler {
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        throw new RejectedExecutionException("Task rejected");
    }
}

// 2. CallerRunsPolicy
public static class CallerRunsPolicy implements RejectedExecutionHandler {
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            r.run();
        }
    }
}

// 3. DiscardPolicy
public static class DiscardPolicy implements RejectedExecutionHandler {
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        // Do nothing
    }
}

// 4. DiscardOldestPolicy
public static class DiscardOldestPolicy implements RejectedExecutionHandler {
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            e.getQueue().poll();
            e.execute(r);
        }
    }
}
```

## Worker Thread Lifecycle

### Worker Class

```java
private final class Worker
    extends AbstractQueuedSynchronizer
    implements Runnable
{
    final Thread thread;
    Runnable firstTask;
    volatile long completedTasks;

    Worker(Runnable firstTask) {
        setState(-1);
        this.firstTask = firstTask;
        this.thread = getThreadFactory().newThread(this);
    }

    public void run() {
        runWorker(this);
    }
}
```

### Worker State Machine

```
                    +----------------+
                    |   Created      |
                    +----------------+
                           |
                           v
                    +----------------+
                    |   Running      |
                    +----------------+
                           |
              +------------+------------+
              |                         |
              v                         v
    +----------------+        +----------------+
    |  Task Complete  |        |  Exception     |
    +----------------+        +----------------+
              |                         |
              v                         v
    +----------------+        +----------------+
    |   Waiting      |        |   Terminated   |
    +----------------+        +----------------+
```

## execute() Implementation

### Entry Point

```java
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();

    int c = ctl.get();

    if (workerCountOf(c) < corePoolSize) {
        if (addWorker(command, true))
            return;
        c = ctl.get();
    }

    if (isRunning(c) && workQueue.offer(command)) {
        int recheck = ctl.get();
        if (!isRunning(recheck) && remove(command))
            reject(command);
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
    else if (!addWorker(command, false))
        reject(command);
}
```

### addWorker() Method

```java
private boolean addWorker(Runnable firstTask, boolean core) {
    retry:
    for (;;) {
        int c = ctl.get();
        int rs = runStateOf(c);

        if (rs >= SHUTDOWN &&
            !(rs == SHUTDOWN && firstTask == null && !workQueue.isEmpty()))
            return false;

        for (;;) {
            int wc = workerCountOf(c);
            if (wc >= CAPACITY ||
                wc >= (core ? corePoolSize : maximumPoolSize))
                return false;
            if (compareAndIncrementWorkerCount(c))
                break retry;
            c = ctl.get();
            if (runStateOf(c) != rs)
                continue retry;
        }
    }

    boolean workerStarted = false;
    boolean workerAdded = false;
    Worker w = null;
    try {
        w = new Worker(firstTask);
        final Thread t = w.thread;
        if (t != null) {
            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                int rs = runStateOf(ctl.get());
                if (rs < SHUTDOWN ||
                    (rs == SHUTDOWN && firstTask == null)) {
                    if (t.isAlive())
                        throw new IllegalThreadStateException();
                    workers.add(w);
                    int s = workers.size();
                    if (s > largestPoolSize)
                        largestPoolSize = s;
                    workerAdded = true;
                }
            } finally {
                mainLock.unlock();
            }
            if (workerAdded) {
                t.start();
                workerStarted = true;
            }
        }
    } finally {
        if (!workerStarted)
            addWorkerFailed(w);
    }
    return workerStarted;
}
```

## runWorker() Implementation

### Main Loop

```java
final void runWorker(Worker w) {
    Thread wt = Thread.currentThread();
    Runnable task = w.firstTask;
    w.firstTask = null;
    w.unlock();
    boolean completedAbruptly = true;
    try {
        while (task != null || (task = getTask()) != null) {
            w.lock();
            if ((runStateAtLeast(ctl.get(), STOP) ||
                 (Thread.interrupted() &&
                  runStateAtLeast(ctl.get(), STOP))) &&
                !wt.isInterrupted())
                wt.interrupt();
            try {
                beforeExecute(wt, task);
                Throwable thrown = null;
                try {
                    task.run();
                } catch (RuntimeException | Error e) {
                    thrown = e;
                    throw e;
                } catch (Throwable t) {
                    thrown = new Error(t);
                    throw t;
                } finally {
                    afterExecute(task, thrown);
                }
            } finally {
                task = null;
                w.completedTasks++;
                w.unlock();
            }
        }
        completedAbruptly = false;
    } finally {
        processWorkerExit(w, completedAbruptly);
    }
}
```

### getTask() Method

```java
private Runnable getTask() {
    boolean timedOut = false;

    for (;;) {
        int c = ctl.get();
        int rs = runStateOf(c);

        if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
            decrementWorkerCount();
            return null;
        }

        int wc = workerCountOf(c);

        boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;

        if ((timed && timedOut) ||
            (wc > 1 && workQueue.isEmpty()) ||
            (wc >= maximumPoolSize && timed)) {
            if (compareAndDecrementWorkerCount(c))
                return null;
            continue;
        }

        try {
            Runnable r = timed ?
                workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                workQueue.take();
            if (r != null)
                return r;
            timedOut = true;
        } catch (InterruptedException retry) {
            timedOut = false;
        }
    }
}
```

## shutdown() Implementation

### shutdown() Method

```java
public void shutdown() {
    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        int c = ctl.get();
        if (runStateLessThan(c, SHUTDOWN))
            ctl.set ctlOf(SHUTDOWN, workerCountOf(c));
        interruptIdleWorkers();
    } finally {
        mainLock.unlock();
    }
    tryTerminate();
}
```

### shutdownNow() Method

```java
public List<Runnable> shutdownNow() {
    List<Runnable> tasks;
    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        int c = ctl.get();
        if (runStateLessThan(c, STOP))
            ctl.set ctlOf(STOP, workerCountOf(c));
        interruptWorkers();
        tasks = new ArrayList<>(workQueue);
        workQueue.clear();
    } finally {
        mainLock.unlock();
    }
    tryTerminate();
    return tasks;
}
```

## Key Design Decisions

### 1. AtomicInteger for State

```java
private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
```

- **Why?**: Pack state + worker count in one atomic variable
- **Benefit**: Single CAS for both values

### 2. Lock per Worker

```java
w.lock(); // Only lock during task execution
```

- **Why?**: Prevent interruption during task execution
- **Tradeoff**: Fine-grained vs coarse locking

### 3. Queue Before Reject

```java
// execute() logic:
// 1. Add core worker if possible
// 2. Queue the task
// 3. Add non-core worker if needed
// 4. Reject if all else fails
```

- **Why?**: Maximize throughput by queuing
- **Tradeoff**: Latency vs throughput

### 4. Double-Check Pattern

```java
if (isRunning(c) && workQueue.offer(command)) {
    int recheck = ctl.get();
    if (!isRunning(recheck) && remove(command))
        reject(command);
}
```

- **Why?**: Handle race condition where shutdown happens after queue check
- **Benefit**: Never lose a task unexpectedly

## Performance Characteristics

| Operation | Complexity |
|-----------|------------|
| execute() | O(1) amortized |
| submit() | O(1) amortized |
| shutdown() | O(n) |
| shutdownNow() | O(n) |

## Common Configurations

### CPU-Bound Tasks

```java
int cores = Runtime.getRuntime().availableProcessors();
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    cores,
    cores,
    0L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<>()
);
```

### I/O-Bound Tasks

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    10, // core
    100, // max
    60L, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(1000)
);
```

### Fixed-Size Pool

```java
ExecutorService executor = Executors.newFixedThreadPool(10);
```

### Cached Pool

```java
ExecutorService executor = Executors.newCachedThreadPool();
```

## Common Mistakes

### 1. Unbounded Queue

```java
// Bad: Unbounded queue can cause OOM
new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<>()); // Unbounded!

// Good: Bounded queue
new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<>(1000)); // Bounded
```

### 2. Ignoring Rejection

```java
// Bad: Using default AbortPolicy without handling
executor.execute(() -> {
    // What if rejected?
});

// Good: Custom handler
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    10, 10, 0L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<>(1000),
    new ThreadPoolExecutor.CallerRunsPolicy()
);
```

### 3. Not Shutting Down

```java
// Bad: Never shuts down
ExecutorService executor = Executors.newFixedThreadPool(10);
// Application exits without cleanup

// Good: Always shutdown
ExecutorService executor = Executors.newFixedThreadPool(10);
try {
    // Submit tasks
} finally {
    executor.shutdown();
    executor.awaitTermination(60, TimeUnit.SECONDS);
}
```

## Resources

- **Java ThreadPoolExecutor Official Docs**
- **OpenJDK Source**: `src/java.base/java/util/concurrent/ThreadPoolExecutor.java`
- **"Java Concurrency in Practice"** by Brian Goetz
- **"Concurrent Programming in Java"** by Doug Lea