# Thread Dump Analysis

## Thread Dump Format

```
"main" #1 prio=5 tid=0x00007f8b1c008800 java.lang.Thread.State: RUNNABLE
    at com.example.MyClass.myMethod(MyClass.java:42)
    - locked <0x000000076ab0a1b8> (a java.lang.Object)
    at com.example.MyClass.run(MyClass.java:20)
    at java.lang.Thread.run(Thread.java:748)
```

Key fields:
- **Thread name** + **thread ID** + **priority**
- **Thread state**: RUNNABLE, BLOCKED, WAITING, TIMED_WAITING
- **Stack trace**: current call stack
- **Lock info**: `locked <address>` or `waiting to lock <address>`

## Thread States

| State | Meaning | Thread Dump Indicator |
|-------|---------|----------------------|
| NEW | Created, not started | Never started |
| RUNNABLE | Executing or ready | `State: RUNNABLE` |
| BLOCKED | Waiting for monitor lock | `State: BLOCKED` + `waiting to lock` |
| WAITING | Indefinite wait | `State: WAITING` + `Object.wait()` |
| TIMED_WAITING | Timed wait | `State: TIMED_WAITING` + `sleep()/wait(ms)` |
| TERMINATED | Finished | Thread count decreasing |

## Analysis Methodology

### Step 1: Take Multiple Dumps
```bash
# Take 3 dumps, 5 seconds apart
jstack <pid> > dump1.txt
sleep 5
jstack <pid> > dump2.txt
sleep 5
jstack <pid> > dump3.txt
```
Threads stuck in same state across all 3 dumps = problem.

### Step 2: Count Thread States
```bash
grep "java.lang.Thread.State" dump1.txt | sort | uniq -c
```
- High BLOCKED count → lock contention
- High WAITING count → thread starvation
- Growing thread count → thread leak

### Step 3: Find Deadlock
```bash
grep -A 20 "Found one Java-level deadlock" dump1.txt
```
Or use `jcmd <pid> Thread.print` which highlights deadlocks.

### Step 4: Identify Contention Points
```bash
grep "waiting to lock" dump1.txt | sort | uniq -c | sort -rn
```
The most-waited-for lock is your bottleneck.

### Step 5: Check Thread Pools
```bash
grep -c "pool-" dump1.txt        # count pool threads
grep "pool-.*BLOCKED" dump1.txt  # blocked pool threads
```

## Common Patterns

### Pattern 1: Deadlock
```
"Thread-1" BLOCKED, holds <0xA>, waits <0xB>
"Thread-2" BLOCKED, holds <0xB>, waits <0xA>
→ Circular dependency, jstack detects automatically
```

### Pattern 2: Thread Starvation
```
"pool-1-thread-1" WAITING
"pool-1-thread-2" WAITING
... (all pool threads WAITING)
→ Pool exhausted, all threads waiting on same resource
```

### Pattern 3: Lock Contention
```
"Worker-1" BLOCKED, waiting to lock <0xA>
"Worker-2" BLOCKED, waiting to lock <0xA>
"Worker-3" BLOCKED, waiting to lock <0xA>
→ Hot lock, consider lock striping or concurrent collections
```

### Pattern 4: Thread Leak
```
"CustomThread-1" TERMINATED
"CustomThread-2" TERMINATED
"CustomThread-3" TIMED_WAITING  ← still alive
→ Thread started but never stopped/joined
```

### Pattern 5: Excessive Waiting
```
"Timer-1" TIMED_WAITING at Thread.sleep(1000)
→ Polling with sleep, consider blocking queue or scheduled executor
```

## Tools

| Tool | Command | Use Case |
|------|---------|----------|
| `jstack` | `jstack <pid>` | Quick thread dump |
| `jcmd` | `jcmd <pid> Thread.print` | Better formatting |
| VisualVM | Connect → Threads tab | Live monitoring |
| Thread Dump Analyzer | tda.jar | Compare multiple dumps |
| Arthas | `thread` command | Runtime analysis |

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
