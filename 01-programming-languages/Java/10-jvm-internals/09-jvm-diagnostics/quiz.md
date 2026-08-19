# 09. JVM Diagnostics - Quiz

## Questions

### Q1: Thread Dump
How do you capture a thread dump?
- A) jmap -dump
- B) jstack <pid> or kill -3 <pid>
- C) jstat -gc
- D) jcmd <pid> GC.heap_info

**Answer: B**
Explanation: jstack captures thread dumps. On Unix, kill -3 also works. Thread dumps show all thread states, stack traces, and lock information.

### Q2: Heap Dump
What is the difference between jmap -dump and jcmd GC.heap_dump?
- A) They are identical
- B) jcmd is preferred (more reliable, better options)
- C) jmap is faster
- D) jcmd cannot create heap dumps

**Answer: B**
Explanation: jcmd is the recommended tool for heap dumps. It provides more options (live dump, compression) and is more reliable than jmap in modern JDKs.

### Q3: Thread States
In a thread dump, what does BLOCKED state indicate?
- A) Thread is waiting for I/O
- B) Thread is waiting to acquire a monitor lock
- C) Thread is sleeping
- D) Thread is running

**Answer: B**
Explanation: BLOCKED means the thread is waiting to enter a synchronized block or method. The lock is held by another thread. This indicates potential deadlock or contention.

### Q4: jstat
What does jstat -gc <pid> 1000 show?
- A) Heap dump information
- B) GC statistics every 1000ms
- C) Thread dump
- D) Compilation events

**Answer: B**
Explanation: jstat -gc shows GC statistics including Eden, Survivor, Old, Metaspace usage and GC counts/times. The 1000 is the interval in milliseconds.

### Q5: jcmd
Which jcmd command shows VM flags?
- A) jcmd <pid> VM.flags
- B) jcmd <pid> GC.heap_info
- C) jcmd <pid> Thread.print
- D) jcmd <pid> VM.classloader_stats

**Answer: A**
Explanation: jcmd <pid> VM.flags shows all effective JVM flags including defaults and command-line overrides. Essential for verifying production configurations.

### Q6: Heap Dump Analysis
What is the first thing to look at when analyzing a heap dump for memory leaks?
- A) Thread stacks
- B) GC roots and dominator tree
- C) Class loading statistics
- D) JIT compilation events

**Answer: B**
Explanation: The dominator tree shows objects that hold the most retained memory. GC roots show why objects cannot be collected. Together they identify leak sources.

### Q7: Deadlock Detection
How does jstack detect deadlocks?
- A) It analyzes thread timing
- B) It finds cycles in lock ownership (thread A waits for B, B waits for A)
- C) It checks for long-running threads
- D) It monitors CPU usage

**Answer: B**
Explanation: jstack detects deadlocks by analyzing lock ownership chains. If a cycle exists (A waits for B, B waits for A), it reports the deadlock with involved threads and locks.

### Q8: Flight Recording
What is a JFR event?
- A) A GC pause
- B) A timestamped record of a JVM or application occurrence
- C) A thread dump
- D) A compilation event

**Answer: B**
Explanation: JFR events are timestamped records of JVM or application occurrences (GC, compilation, I/O, etc.). They are collected with minimal overhead and stored in .jfr files.

### Q9: jhat Alternative
What replaced jhat for heap dump analysis?
- A) jmap
- B) VisualVM, Eclipse MAT, or jhsdb
- C) jstat
- D) jstack

**Answer: B**
Explanation: jhat was removed in JDK 9. VisualVM, Eclipse MAT, and jhsdb are modern alternatives for heap dump analysis.

### Q10: OutOfMemoryError
When an OutOfMemoryError occurs, how do you automatically capture a heap dump?
- A) -XX:+HeapDumpOnOutOfMemoryError
- B) -XX:+PrintGCDetails
- C) -XX:+TraceClassLoading
- D) -XX:+PrintCompilation

**Answer: A**
Explanation: -XX:+HeapDumpOnOutOfMemoryError automatically captures a heap dump when OOM occurs. Pair with -XX:HeapDumpPath to specify the output location.

## Score Guide
- **9-10 correct**: Diagnostics expert
- **7-8 correct**: Solid understanding, review tool-specific options
- **5-6 correct**: Good start, study dump analysis techniques
- **Below 5**: Review basics before proceeding
