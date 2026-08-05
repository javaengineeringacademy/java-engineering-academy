# Java Debugging

> JDB, IntelliJ debugger, thread dumps, heap dumps, and async-profiler.

## Debugging Tools Overview

| Tool | Type | Use Case |
|------|------|----------|
| JDB | CLI | Low-level debugging |
| IntelliJ Debugger | GUI | Step-by-step debugging |
| async-profiler | Agent | CPU/memory profiling |
| JFR | Built-in | Continuous monitoring |
| jstack | CLI | Thread dumps |
| jmap | CLI | Heap dumps |
| VisualVM | GUI | General monitoring |
| jcmd | CLI | Diagnostic commands |

## IDE Debugging (IntelliJ)

### Setting Breakpoints

1. Click gutter area next to line number
2. Right-click breakpoint for conditions
3. Conditional: `i == 5` (only stops when i equals 5)
4. Exception breakpoint: runs when exception thrown

### Debug Actions

| Action | Shortcut | Description |
|--------|----------|-------------|
| Step Over | F8 | Execute current line |
| Step Into | F7 | Enter method call |
| Step Out | Shift+F8 | Return from method |
| Run to Cursor | Alt+F9 | Run to current line |
| Evaluate | Alt+F8 | Evaluate expression |
| Resume | F9 | Continue execution |
| Stop | Ctrl+F2 | Stop debugging |

### Evaluate Expression

```java
// In debug console
user.getName()
list.stream().filter(u -> u.getAge() > 25).collect(Collectors.toList())
new String(data, StandardCharsets.UTF_8)
```

## Thread Dumps

### jstack

```bash
# Thread dump
jstack <pid>

# Thread dump with locks
jstack -l <pid>

# Thread dump to file
jstack <pid> > thread_dump.txt

# Multiple dumps for analysis
for i in {1..5}; do
    jstack <pid> > thread_$i.txt
    sleep 5
done
```

### jcmd Thread Dump

```bash
# Thread dump via jcmd
jcmd <pid> Thread.print

# With locked monitors
jcmd <pid> Thread.print -l
```

### Analyzing Thread Dump

```
"main" #1 prio=5 os_prio=0 tid=0x00007f8b1c00a800 nid=0x1 runnable [0x00007f8b1cfdf000]
   java.lang.Thread.State: RUNNABLE
    at com.example.App.process(App.java:25)
    - waiting to lock <0x00000007aab0a398> (a java.lang.Object)
    - locked <0x00000007aab0a440> (a java.lang.Object)
    at com.example.App.main(App.java:10)

"worker-1" #12 prio=5 os_prio=0 tid=0x00007f8b1c123800 nid=0x12 waiting [0x00007f8b1c122000]
   java.lang.Thread.State: WAITING (parking)
    at jdk.internal.misc.Unsafe.park(Native Method)
    at java.util.concurrent.locks.LockSupport.park(LockSupport.java:194)
```

### Thread State Analysis

| State | Meaning | Action |
|-------|---------|--------|
| RUNNABLE | Executing | Check CPU usage |
| BLOCKED | Waiting for lock | Check lock contention |
| WAITING | Waiting indefinitely | Check wait/notify |
| TIMED_WAITING | Waiting with timeout | Normal if expected |
| TERMINATED | Completed | No action needed |

## Heap Dumps

### jmap

```bash
# Heap dump
jmap -dump:live,format=b,file=heap.hprof <pid>

# Heap dump without live objects
jmap -dump:format=b,file=heap.hprof <pid>

# Histogram of objects
jmap -histo <pid> | head -30

# Histogram with size
jmap -histo:live <pid> | head -30
```

### Heap Dump on OOM

```bash
# JVM flags
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/tmp/heapdumps/

# Trigger manually
jcmd <pid> GC.heap_dump /tmp/manual_dump.hprof
```

### Analyzing Heap Dumps

```bash
# Eclipse MAT
# Open heap.hprof in Eclipse Memory Analyzer

# Key views:
# - Dominator Tree: objects retaining most memory
# - Leak Suspects: automated analysis
# - Histogram: object counts and sizes

# VisualVM
# Open heap.hprof in VisualVM
# Analyze: Classes view, OQL queries
```

## async-profiler

### CPU Profiling

```bash
# Profile CPU for 30 seconds
./profiler.sh -d 30 -f cpu_profile.html <pid>

# Profile with flame graph
./profiler.sh -d 30 -o flamegraph -f cpu_flame.svg <pid>

# Wall-clock profiling
./profiler.sh -d 30 -e wall -f wall_profile.html <pid>
```

### Memory Profiling

```bash
# Allocation profiling
./profiler.sh -d 30 -e alloc -f alloc_profile.html <pid>

# Live objects
./profiler.sh -d 30 -e live -f live_profile.html <pid>
```

### Lock Profiling

```bash
# Contention profiling
./profiler.sh -d 30 -e lock -f lock_profile.html <pid>
```

## Java Flight Recorder

```bash
# Quick recording
jcmd <pid> JFR.start name=quick duration=60s filename=quick.jfr

# Profile recording
jcmd <pid> JFR.start settings=profile filename=profile.jfr

# Continuous with limits
jcmd <pid> JFR.start settings=default maxage=24h maxsize=100m filename=continuous.jfr

# Dump and stop
jcmd <pid> JFR.dump filename=dump.jfr
jcmd <pid> JFR.stop
```

## Common Debug Scenarios

### Debugging Deadlock

```bash
# Find deadlock in thread dump
jstack <pid> | grep -A 20 "deadlock"

# jcmd
jcmd <pid> Thread.print | grep -A 20 "BLOCKED"
```

### Debugging Memory Leak

```bash
# 1. Take heap dump
jmap -dump:live,format=b,file=heap.hprof <pid>

# 2. Analyze with MAT
# Look for:
# - Large objects in Dominator Tree
# - Retained size
# - Leak Suspects report

# 3. Compare dumps
# Take dump before and after suspected leak
# Compare object counts
```

### Debugging High CPU

```bash
# 1. Find process
top -p <pid>

# 2. Take thread dump
jstack <pid> > thread_dump.txt

# 3. Check RUNNABLE threads
grep -A 5 "RUNNABLE" thread_dump.txt

# 4. Profile with async-profiler
./profiler.sh -d 30 -f cpu.html <pid>
```

## JFR Analysis

```bash
# Quick summary
jfr summary recording.jfr

# Print events
jfr print --events GC recording.jfr
jfr print --events CPULoad recording.jfr

# View in JDK Mission Control
# Open recording.jfr in JMC
```

## References

- [JDB Documentation](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/jdb.html)
- [async-profiler](https://github.com/async-profiler/async-profiler)
- [Java Flight Recorder](https://docs.oracle.com/javase/8/docs/technotes/guides/jfr/)

---
**Prerequisites:** [Java pitfalls](pitfalls.md)
**Related:** [Java troubleshooting](troubleshooting.md) | [Java monitoring](monitoring.md)
**Next:** [Java troubleshooting](troubleshooting.md)
