# Module 12: Testing

> **Difficulty:** ⭐⭐ Easy  
> **Reading:** 25 min | **Practice:** 45 min | **Total:** 70 min

## Overview
Bugs inevitably slip into production — logic errors, runtime exceptions, concurrency issues, and memory leaks. Debugging is the systematic process of finding and fixing those defects using breakpoints, logging, profiling, and structured analysis. This module covers IDE debugging, remote debugging, stack trace analysis, and production troubleshooting techniques.

## Learning Objectives
- Set breakpoints and step through code to isolate bugs in the IDE
- Analyze stack traces to trace exceptions back to their root cause
- Configure and use logging effectively to trace production behavior
- Connect to remote JVMs for live debugging without redeploying
- Apply systematic debugging workflows to reproduce and fix issues efficiently

## Prerequisites
- Java fundamentals
- IDE experience
- Basic JVM knowledge

## History
- **1995** — Java 1.0 had only manual debugging with `System.out.println`
- **1998** — Java 1.2 added `assert` keyword for pre/post conditions
- **2001** — Java 1.4 added `java.util.logging` for standard logging
- **2004** — Java 5 improved stack traces and debugging support
- **2006** — Java 6 added `jstack`, `jmap`, `jhat` tools
- **2011** — Java 7 added `jdeps` for dependency analysis
- **2014** — Java 8 improved `Optional` for null-safe debugging
- **2017** — Java 9 added `jshell` for interactive debugging and REPL
- **2018** — Java 11 added Flight Recorder as a production profiling tool
- **2021** — Java 17 enhanced `NullPointerException` messages with variable names
- **2023** — Java 21 added structured concurrency and scoped values (preview)

## Why This Concept Exists
Bugs lead to:
- System failures
- Data corruption
- Security vulnerabilities
- User dissatisfaction

Effective debugging:
- Reduces fix time
- Prevents regressions
- Improves code quality
- Enhances understanding

## Problem Statement
How do you efficiently find and fix software defects?

## Core Concepts

### Debugging Techniques

| Technique | Use Case |
|-----------|----------|
| Breakpoints | Code execution pause |
| Step Through | Line-by-line analysis |
| Watch Variables | Value inspection |
| Logging | Runtime tracking |
| Stack Trace | Error analysis |
| Profiling | Performance issues |

### Common Bug Types

| Type | Description |
|------|-------------|
| Logic Error | Incorrect algorithm |
| Runtime Error | Exception thrown |
| Syntax Error | Code won't compile |
| Concurrency Bug | Threading issue |
| Memory Leak | Unreleased resources |

## Internal Working

### Debugger Process
1. Set breakpoint
2. Run in debug mode
3. Inspect state
4. Step through code
5. Identify issue
6. Fix and verify

### Stack Trace Analysis
1. Find exception origin
2. Follow call chain
3. Identify root cause
4. Check similar cases
5. Implement fix

## JVM Perspective

### JVM Debugging
- JDWP (Java Debug Wire Protocol)
- JDI (Java Debug Interface)
- Agentlib:jdwp
- Remote debugging

### Memory Analysis
- Heap dumps
- Thread dumps
- GC logs
- Memory profilers

## Architecture Diagram

```mermaid
graph TD
    A[Debugging] --> B[Breakpoints]
    A --> C[Logging]
    A --> D[Profiling]
    A --> E[Analysis]
    
    B --> F[Step Through]
    B --> G[Watch Variables]
    
    C --> H[Log Levels]
    C --> I[Log Patterns]
    
    D --> J[CPU Profile]
    D --> K[Memory Profile]
    
    E --> L[Stack Trace]
    E --> M[Heap Dump]
```

## Flow Diagram

```mermaid
graph TD
    A[Issue Reported] --> B[Reproduce Issue]
    B --> C{Reproducible?}
    C -->|No| D[Add Logging]
    C -->|Yes| E[Debug with IDE]
    D --> B
    E --> F[Set Breakpoints]
    F --> G[Step Through Code]
    G --> H[Identify Root Cause]
    H --> I[Implement Fix]
    I --> J[Verify Fix]
    J --> K[Add Tests]
```

## Syntax

### IDE Debugging
```java
public class DebugExample {
    public static int calculate(int a, int b) {
        int result = a + b;  // Set breakpoint here
        if (result > 100) {
            result = result / 2;  // Watch this line
        }
        return result;
    }
    
    public static void main(String[] args) {
        int x = 50;
        int y = 60;
        int result = calculate(x, y);  // Step into this
        System.out.println("Result: " + result);
    }
}
```

### Logging with SLF4J
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExample {
    private static final Logger logger = LoggerFactory.getLogger(LoggingExample.class);
    
    public void processData(String data) {
        logger.debug("Processing data: {}", data);
        
        try {
            // Process
            logger.info("Successfully processed data");
        } catch (Exception e) {
            logger.error("Failed to process data: {}", data, e);
        }
    }
}
```

### Remote Debugging
```bash
# Start application with debug agent
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
     -jar app.jar

# Connect debugger to port 5005
```

## Easy Example
```java
public class DebuggingEasyExample {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5};
        int sum = 0;
        
        // Bug: incorrect loop boundary
        for (int i = 0; i <= numbers.length; i++) {  // Should be <
            sum += numbers[i];
        }
        
        System.out.println("Sum: " + sum);
    }
    
    // Fixed version
    public static void mainFixed(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5};
        int sum = 0;
        
        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
        }
        
        System.out.println("Sum: " + sum);
    }
}
```

## Medium Example
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebuggingMediumExample {
    private static final Logger logger = LoggerFactory.getLogger(DebuggingMediumExample.class);
    
    public static void main(String[] args) {
        DebuggingMediumExample example = new DebuggingMediumExample();
        
        try {
            example.processOrder("ORDER-123");
        } catch (Exception e) {
            logger.error("Failed to process order", e);
        }
    }
    
    public void processOrder(String orderId) {
        logger.debug("Starting processing order: {}", orderId);
        
        // Step 1: Validate
        if (!validateOrder(orderId)) {
            logger.warn("Order validation failed: {}", orderId);
            throw new IllegalArgumentException("Invalid order: " + orderId);
        }
        
        // Step 2: Process
        logger.info("Processing order: {}", orderId);
        processPayment(orderId);
        
        // Step 3: Complete
        logger.info("Order completed: {}", orderId);
    }
    
    private boolean validateOrder(String orderId) {
        logger.debug("Validating order: {}", orderId);
        return orderId != null && !orderId.isEmpty();
    }
    
    private void processPayment(String orderId) {
        logger.debug("Processing payment for order: {}", orderId);
        // Simulate payment processing
    }
}
```

## Hard Example
```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class DebuggingHardExample {
    // Concurrent bug example
    private static int counter = 0;
    private static final AtomicReference<Integer> atomicCounter = 
        new AtomicReference<>(0);
    
    public static void main(String[] args) throws Exception {
        // Bug: race condition
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                counter++;  // Not thread-safe
                atomicCounter.updateAndGet(v -> v + 1);
                latch.countDown();
            });
        }
        
        latch.await();
        System.out.println("Counter: " + counter);  // Expected: 100
        System.out.println("Atomic: " + atomicCounter.get());  // Always 100
        
        executor.shutdown();
    }
}
```

## Enterprise Example
```java
import java.lang.management.*;
import java.util.*;

public class DebuggingEnterpriseExample {
    // Thread dump analysis
    public static void analyzeThreads() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        
        System.out.println("Thread Count: " + threadBean.getThreadCount());
        System.out.println("Peak Thread Count: " + threadBean.getPeakThreadCount());
        System.out.println("Daemon Thread Count: " + threadBean.getDaemonThreadCount());
        
        // Find deadlocked threads
        long[] deadlockedThreads = threadBean.findDeadlockedThreads();
        if (deadlockedThreads != null) {
            System.out.println("DEADLOCK DETECTED!");
            ThreadInfo[] threadInfos = threadBean.getThreadInfo(deadlockedThreads);
            for (ThreadInfo info : threadInfos) {
                System.out.println("  " + info.getThreadName() + 
                    " waiting for " + info.getLockName());
            }
        }
    }
    
    // Memory leak detection
    public static void detectMemoryLeak() {
        Runtime runtime = Runtime.getRuntime();
        
        long before = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory before: " + before / 1024 / 1024 + " MB");
        
        // Simulate memory leak
        List<byte[]> leak = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            leak.add(new byte[1024 * 1024]);  // 1MB each
        }
        
        long after = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory after: " + after / 1024 / 1024 + " MB");
        System.out.println("Memory leak: " + (after - before) / 1024 / 1024 + " MB");
    }
    
    public static void main(String[] args) {
        analyzeThreads();
        detectMemoryLeak();
    }
}
```

## Performance Considerations
- Use appropriate log levels
- Avoid debug prints in production
- Use conditional logging
- Profile before optimizing

## Time & Space Complexity

| Operation | Time | Space |
|-----------|------|-------|
| Breakpoint | O(1) | O(1) |
| Step through | O(1) | O(1) |
| Log statement | O(1) | O(1) |
| Heap dump | O(n) | O(n) |

## Thread Safety
- Debuggers may pause threads
- Thread dumps are point-in-time
- Use non-intrusive debugging
- Avoid debug artifacts in production

## Best Practices
1. Reproduce before fixing
2. Use version control
3. Write tests for bugs
4. Log strategically
5. Use IDE debugger

## Common Mistakes
1. Fixing symptoms not causes
2. Not reproducing first
3. Excessive logging
4. Debug prints in production

## Comparison Table

| Tool | Type | Use Case |
|------|------|----------|
| IntelliJ Debugger | IDE | Development |
| JDB | CLI | Remote |
| VisualVM | Profiler | Production |
| JFR | Recorder | Production |

## Interview Questions

### Q1: What is debugging?
**Answer:** Finding and fixing software defects.

### Q2: What is a stack trace?
**Answer:** List of method calls leading to an exception.

### Q3: What is remote debugging?
**Answer:** Debugging application running on different machine.

### Q4: What is a memory leak?
**Answer:** Unreleased memory that should be garbage collected.

### Q5: What is a thread dump?
**Answer:** Snapshot of all thread states at a point in time.

### Q6: What is the difference between error and exception?
**Answer:** Errors are serious (OutOfMemory), exceptions are recoverable.

### Q7: What is logging?
**Answer:** Recording runtime events for debugging and monitoring.

### Q8: What is a breakpoint?
**Answer:** Marker to pause execution at specific line.

### Q9: What is step-through debugging?
**Answer:** Executing code line by line to observe behavior.

### Q10: What is a watchdog?
**Answer:** Timer that detects and recovers from hangs.

### Q11: What is heap dump analysis?
**Answer:** Examining memory snapshot for leaks.

### Q12: What is thread contention?
**Answer:** Threads waiting for locks held by others.

### Q13: What is a race condition?
**Answer:** Bug where outcome depends on timing of operations.

### Q14: What is deadlock?
**Answer:** Two or more threads waiting for each other's locks.

### Q15: What is the difference between debugging and profiling?
**Answer:** Debugging finds bugs, profiling measures performance.

## Exercises

### Easy
1. Debug a simple program
2. Use breakpoints effectively
3. Analyze a stack trace

### Medium
1. Fix a concurrency bug
2. Analyze a thread dump
3. Use logging for debugging

### Hard
1. Debug a memory leak
2. Fix a deadlock
3. Debug production issues

## Summary
Effective debugging is essential for software quality. Master IDE tools, logging, and systematic analysis.

## Examples

[Code examples demonstrating the concept]

## Performance

[Performance considerations and benchmarks]

## Pitfalls

[Common mistakes and anti-patterns]

## References
- Debugging by David Agans
- Java Debugging Guide
- Effective Debugging

## Cross-References

- **Previous Module:** [11 - Design Patterns](../11-design-patterns/)
- **Related:** [01 - Fundamentals](../01-fundamentals/) — basic syntax for reading stack traces
- **Related:** [02 - OOP](../02-oop/) — object lifecycle and memory
- **Related:** [03 - Exception Handling](../03-exception-handling/) — exception analysis and debugging
- **Related:** [09 - Multithreading](../09-multithreading/) — thread dumps and deadlock detection
- **Related:** [10 - JVM Internals](../10-jvm-internals/) — heap dumps, GC logs, JFR
- **External:** [Oracle Java Debugging Guide](https://docs.oracle.com/javase/tutorial/essential/io/)
- **External:** [Effective Debugging by Diomidis Spinellis](https://www.oreilly.com/library/view/effective-debugging/9780134685809/)

## Prerequisites

- [OOP](../02-oop/README.md)

## Related Topics

- [Reflection & Annotations](../13-reflection-annotations/README.md)

## Next

- [Reflection & Annotations](../13-reflection-annotations/README.md)

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Quality assurance |
| Complexity | Varies |
| Thread Safe | Yes (tests should be independent) |
| Ordered | No (tests should be independent) |
| Allows Null | No (assertions) |
| Best Alternative | Manual testing (for exploratory) |
| When to Use | Code verification |
| When to Avoid | Skipping tests |
