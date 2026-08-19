# 08. Profiling Tools - Quiz

## Questions

### Q1: Profiling Types
What is the difference between sampling and instrumentation profiling?
- A) Sampling is more accurate, instrumentation is faster
- B) Sampling periodically captures call stacks (low overhead), instrumentation modifies bytecode (higher overhead, exact measurements)
- C) They are identical
- D) Sampling is for CPU, instrumentation is for memory

**Answer: B**
Explanation: Sampling periodically snapshots call stacks with low overhead. Instrumentation adds timing code to every method entry/exit for exact measurements but higher overhead.

### Q2: JFR
What is Java Flight Recorder (JFR)?
- A) A commercial profiling tool
- B) A low-overhead, built-in profiling framework included in the JDK
- C) A garbage collector
- D) A JIT compiler

**Answer: B**
Explanation: JFR is a production-time profiling framework built into the JVM. It has minimal overhead (<1%) and can run continuously without significant performance impact.

### Q3: VisualVM
What is VisualVM primarily used for?
- A) Compiling Java code
- B) JVM monitoring, profiling, and troubleshooting
- C) Managing Maven dependencies
- D) Writing unit tests

**Answer: B**
Explanation: VisualVM provides CPU and memory profiling, thread monitoring, heap dumps, and MBean browsing. It connects to local and remote JVMs.

### Q4: async-profiler
What makes async-profiler different from other profilers?
- A) It uses JVM TI agents
- B) It uses async-profiler's own stack walking (no safepoint bias)
- C) It only works on Linux
- D) It requires application restart

**Answer: B**
Explanation: async-profiler uses perf_events (Linux) or unwind info (macOS) to walk stacks without requiring JVM safepoints, eliminating safepoint bias in profiling results.

### Q5: Flame Graphs
What does a flame graph show?
- A) Memory allocation patterns
- B) Call stack visualization where width represents time spent
- C) Thread states over time
- D) GC pause timeline

**Answer: B**
Explanation: Flame graphs visualize call stacks as rectangles. The x-axis represents the proportion of samples, and the y-axis shows stack depth. Wider rectangles mean more time spent.

### Q6: Allocation Profiling
What does allocation profiling track?
- A) CPU time per method
- B) Where and how much memory is being allocated on the heap
- C) Thread contention
- D) I/O operations

**Answer: B**
Explanation: Allocation profiling tracks object allocations by call site, showing where memory is being consumed. This helps identify high-allocation code paths.

### Q7: JFR Events
Which JFR event type has the lowest overhead?
- A) Instant events (every occurrence recorded)
- B) Duration events (start and end time)
- C) Sampled events (periodically sampled)
- D) All have the same overhead

**Answer: C**
Explanation: Sampled events (like CPU sampling) have the lowest overhead because they are collected periodically rather than on every occurrence. Instant events have the highest overhead.

### Q8: Thread Profiling
What does thread profiling help identify?
- A) Memory leaks
- B) Thread contention, deadlocks, and threads spending time in WAITING/BLOCKED states
- C) CPU instruction reordering
- D) Class loading order

**Answer: B**
Explanation: Thread profiling shows thread states, lock contention, deadlock detection, and which threads are blocked/waiting. Essential for diagnosing concurrency issues.

### Q9: Production Profiling
Which tool is best suited for continuous production profiling?
- A) VisualVM (high overhead, not for production)
- B) Java Flight Recorder (low overhead, built-in)
- C) YourKit (requires license, may have overhead)
- D) javap (disassembly tool, not a profiler)

**Answer: B**
Explanation: JFR is designed for production use with minimal overhead. It can run continuously and record events with configurable detail levels.

### Q10: Profiling Overhead
What is the typical overhead of a well-configured production profiler?
- A) 50-100%
- B) 10-30%
- C) 1-5%
- D) 0%

**Answer: C**
Explanation: Well-configured production profilers like JFR and async-profiler typically add 1-5% overhead. Higher detail levels or sampling rates increase overhead.

## Score Guide
- **9-10 correct**: Profiling expert
- **7-8 correct**: Solid understanding, review tool-specific features
- **5-6 correct**: Good start, study profiling methodology
- **Below 5**: Review basics before proceeding
