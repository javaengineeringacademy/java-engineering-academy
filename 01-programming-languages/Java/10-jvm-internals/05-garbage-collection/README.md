# 05. Garbage Collection

## Introduction

Garbage Collection (GC) is one of the most important features of the Java Virtual Machine. It automatically manages memory by identifying and reclaiming objects that are no longer in use, eliminating the need for manual memory management. Understanding GC is crucial for writing high-performance, memory-efficient Java applications.

The JVM's garbage collector runs in the background, periodically scanning the heap for unreachable objects and freeing their memory. This topic covers the fundamentals of garbage collection, different GC algorithms, tuning strategies, and common pitfalls.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Explain how garbage collection works in Java
- [ ] Identify different generations in the JVM heap
- [ ] Understand root objects and reachability analysis
- [ ] Tune GC parameters for different workloads
- [ ] Monitor GC activity using logs and tools
- [ ] Diagnose and fix memory-related issues
- [ ] Choose appropriate GC algorithms for different scenarios

## Prerequisites

- Completion of Topic 04: Memory Model
- Understanding of Java object lifecycle
- Basic knowledge of JVM memory areas
- Familiarity with command-line tools

## Why This Concept Exists

### The Memory Management Problem

Before garbage collection, developers had to manually manage memory:
- Allocate memory explicitly
- Track references to objects
- Free memory when objects are no longer needed
- Handle memory leaks and dangling pointers

This led to several problems:
- **Memory Leaks**: Forgetting to free memory
- **Dangling Pointers**: Accessing freed memory
- **Double Free**: Freeing the same memory twice
- **Complexity**: Manual memory management is error-prone

### The GC Solution

Garbage collection solves these problems by:
- **Automatic Memory Management**: No manual allocation/freeing
- **Safety**: No dangling pointers or double free
- **Simplicity**: Developers focus on business logic
- **Optimization**: GC can optimize memory layout

### Real-World Impact

GC performance affects:
- **Latency**: GC pauses can cause latency spikes
- **Throughput**: GC consumes CPU time
- **Memory Usage**: GC determines how much memory is available
- **Application Stability**: Memory leaks can crash applications

## Problem Statement

### The GC Challenge

Without understanding GC, developers face:
- **Long GC Pauses**: Applications become unresponsive
- **Memory Leaks**: Applications consume increasing memory
- **OutOfMemoryError**: Applications crash due to insufficient memory
- **Poor Performance**: GC overhead reduces throughput

### Real-World Example

A major e-commerce platform experienced:
- 500ms latency spikes every few minutes
- Memory usage growing from 2GB to 12GB over 24 hours
- CPU utilization at 80% despite low traffic

The root cause? Poor GC tuning and memory leaks.

## Theory

### How Garbage Collection Works

The JVM's garbage collector works by:
1. **Identifying Reachable Objects**: Starting from root objects and following references
2. **Marking Reachable Objects**: Marking all objects that can be reached
3. **Sweeping Unreachable Objects**: Reclaiming memory from unreachable objects
4. **Compacting Memory**: Moving objects to reduce fragmentation (optional)

### Root Objects

Root objects are the starting points for reachability analysis:
- **Local Variables**: Variables in stack frames
- **Static Variables**: Variables in class static fields
- **JNI References**: References from native code
- **Thread References**: References from active threads
- **Class Metadata**: References from loaded classes

### Object Reachability States

```
┌─────────────────────────────────────────────────────────────┐
│                    Object States                            │
│                                                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Reachable │  │  Unreachable│  │  Finalizable│        │
│  │             │  │             │  │             │        │
│  │  In use     │  │  Candidates │  │  Has final  │        │
│  │  by program │  │  for GC     │  │  method     │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
```

### JVM Heap Generations

The JVM heap is divided into generations:

```
┌─────────────────────────────────────────────────────────────┐
│                        Heap                                 │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                  Young Generation                   │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌────────────┐  │   │
│  │  │    Eden     │  │  Survivor 0 │  │ Survivor 1 │  │   │
│  │  │   (80%)     │  │   (10%)     │  │   (10%)    │  │   │
│  │  └─────────────┘  └─────────────┘  └────────────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                  Old Generation                     │   │
│  │                                                     │   │
│  │  (Long-lived objects that survived multiple GC)     │   │
│  │                                                     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                  Metaspace                          │   │
│  │  (Class metadata)                                   │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Internal Working

### GC Algorithm Overview

```
1. MARK PHASE
   - Start from root objects
   - Follow references and mark reachable objects
   - Create a graph of all reachable objects

2. SWEEP PHASE
   - Scan the heap for unmarked objects
   - Free memory of unmarked objects
   - Clear marks for next cycle

3. COMPACT PHASE (Optional)
   - Move surviving objects together
   - Reduce memory fragmentation
   - Update references to new locations
```

### GC Root Processing

```java
// Example: GC roots
public class GCRootExample {
    // Static variable - GC root
    private static Object staticRoot = new Object();
    
    // JNI reference - GC root
    private native void nativeMethod();
    
    public static void main(String[] args) {
        // Local variable - GC root
        Object localRoot = new Object();
        
        // Object created by new - GC root (if reachable)
        Object reachable = new Object();
        Object unreachable = new Object();  // Will be GC'd
    }
}
```

## JVM Perspective

### What the JVM Sees

The JVM sees:
- **Object Graph**: Network of objects connected by references
- **Root Set**: Starting points for reachability analysis
- **Generations**: Different memory areas for different object lifetimes
- **GC Threads**: Threads that perform garbage collection

### Memory Layout with GC

```
┌─────────────────────────────────────────────────────────────┐
│                    Young Generation                        │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Eden                                               │   │
│  │  ┌─────────────────────────────────────────────┐   │   │
│  │  │  New Objects                                 │   │   │
│  │  │  [A] [B] [C] [D] [E] [F] [G] [H]           │   │   │
│  │  └─────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Survivor 0                                         │   │
│  │  ┌─────────────────────────────────────────────┐   │   │
│  │  │  Survived Objects                            │   │   │
│  │  │  [A'] [B'] [C']                              │   │   │
│  │  └─────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Survivor 1                                         │   │
│  │  ┌─────────────────────────────────────────────┐   │   │
│  │  │  (Empty)                                     │   │   │
│  │  └─────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Memory Representation

### Object Memory Layout with GC

```
┌─────────────────────────────────────────────────────────────┐
│                    Object Header                            │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Mark Word (64 bits)                                │   │
│  │  - Hash code (31 bits)                              │   │
│  │  - GC age (4 bits)                                  │   │
│  │  - Lock state (2 bits)                              │   │
│  └─────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Klass Pointer (32 or 64 bits)                      │   │
│  │  - Points to class metadata                         │   │
│  └─────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────┤
│                    Instance Data                            │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  - Instance variables                               │   │
│  │  - Ordered by size (largest first)                  │   │
│  └─────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────┤
│                    Padding                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  - Aligned to 8 bytes                               │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Syntax

### GC Configuration Options

```bash
# Enable GC logging
java -Xlog:gc* MyApp

# Set heap size
java -Xms2g -Xmx4g MyApp

# Choose GC algorithm
java -XX:+UseG1GC MyApp
java -XX:+UseZGC MyApp
java -XX:+UseShenandoahGC MyApp

# Tune GC parameters
java -XX:MaxGCPauseMillis=200 MyApp
java -XX:GCTimeRatio=19 MyApp
```

### GC Monitoring APIs

```java
import java.lang.management.*;

// Get GC MXBean
List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

for (GarbageCollectorMXBean gcBean : gcBeans) {
    System.out.println("GC Name: " + gcBean.getName());
    System.out.println("Collection Count: " + gcBean.getCollectionCount());
    System.out.println("Collection Time: " + gcBean.getCollectionTime());
}
```

## Easy Example

### Basic GC Demonstration

```java
package academy.javaengineering.jvm.garbagecollection;

/**
 * Demonstrates basic garbage collection concepts.
 */
public class BasicGCDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Basic GC Demo ===\n");
        
        // Get initial memory
        printMemory("Initial");
        
        // Create objects
        for (int i = 0; i < 1000; i++) {
            Object obj = new Object();
        }
        
        // Memory after creating objects
        printMemory("After creating 1000 objects");
        
        // Force garbage collection
        System.out.println("\nForcing GC...");
        System.gc();
        
        // Memory after GC
        printMemory("After GC");
        
        // Demonstrate object reachability
        demonstrateReachability();
    }
    
    private static void printMemory(String label) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();
        
        System.out.printf("%s:%n", label);
        System.out.printf("  Used: %d MB%n", usedMemory / (1024 * 1024));
        System.out.printf("  Free: %d MB%n", freeMemory / (1024 * 1024));
        System.out.printf("  Total: %d MB%n", totalMemory / (1024 * 1024));
        System.out.printf("  Max: %d MB%n%n", maxMemory / (1024 * 1024));
    }
    
    private static void demonstrateReachability() {
        System.out.println("=== Object Reachability ===\n");
        
        // Create reachable object
        Object reachable = new Object();
        System.out.println("Reachable object created");
        
        // Create unreachable object
        Object temp = new Object();
        System.out.println("Unreachable object created");
        temp = null;  // Make it unreachable
        System.out.println("Unreachable object set to null");
        
        // Force GC
        System.gc();
        System.out.println("GC forced");
        
        System.out.println("\nReachable object is still available: " + reachable);
    }
}
```

## Medium Example

### Memory Leak Detection

```java
package academy.javaengineering.jvm.garbagecollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates memory leak detection and prevention.
 */
public class MemoryLeakDemo {
    
    // Simulate a memory leak
    private static final List<byte[]> memoryLeak = new ArrayList<>();
    
    public static void main(String[] args) {
        System.out.println("=== Memory Leak Demo ===\n");
        
        // Initial memory
        printMemory("Initial");
        
        // Simulate memory leak
        System.out.println("Simulating memory leak...");
        for (int i = 0; i < 100; i++) {
            // Allocate 1MB chunks
            memoryLeak.add(new byte[1024 * 1024]);
            System.out.printf("Added %d MB%n", i + 1);
            
            // Check memory usage
            if ((i + 1) % 10 == 0) {
                printMemory("After " + (i + 1) + " MB");
            }
            
            // Try to force GC
            if ((i + 1) % 20 == 0) {
                System.out.println("Forcing GC...");
                System.gc();
            }
        }
        
        // Final memory
        printMemory("Final");
        
        System.out.println("\nMemory leak simulation complete.");
        System.out.println("Notice that memory keeps growing despite GC.");
    }
    
    private static void printMemory(String label) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        System.out.printf("%s: Used = %d MB, Free = %d MB%n", 
            label, usedMemory / (1024 * 1024), freeMemory / (1024 * 1024));
    }
}
```

## Hard Example

### GC Tuning Benchmark

```java
package academy.javaengineering.jvm.garbagecollection;

import java.util.*;
import java.util.concurrent.*;

/**
 * Demonstrates GC tuning and performance optimization.
 */
public class GCTuningBenchmark {
    
    private static final int OBJECT_COUNT = 1_000_000;
    private static final int THREAD_COUNT = 4;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== GC Tuning Benchmark ===\n");
        
        // Benchmark different object creation patterns
        benchmarkObjectCreation();
        
        // Benchmark string concatenation
        benchmarkStringConcatenation();
        
        // Benchmark collection operations
        benchmarkCollectionOperations();
        
        // Print final statistics
        printStatistics();
    }
    
    private static void benchmarkObjectCreation() throws InterruptedException {
        System.out.println("--- Object Creation Benchmark ---");
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<?>> futures = new ArrayList<>();
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            futures.add(executor.submit(() -> {
                for (int j = 0; j < OBJECT_COUNT / THREAD_COUNT; j++) {
                    Object obj = new Object();
                }
            }));
        }
        
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Object creation: %d ms%n", duration);
        
        executor.shutdown();
    }
    
    private static void benchmarkStringConcatenation() {
        System.out.println("\n--- String Concatenation Benchmark ---");
        
        // Benchmark StringBuilder
        long startTime = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < OBJECT_COUNT; i++) {
            sb.append("a");
        }
        String result = sb.toString();
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("StringBuilder: %d ms%n", duration);
        
        // Benchmark String concatenation
        startTime = System.nanoTime();
        String str = "";
        for (int i = 0; i < 100_000; i++) {  // Fewer iterations
            str = str + "a";
        }
        duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("String concatenation: %d ms%n", duration);
    }
    
    private static void benchmarkCollectionOperations() {
        System.out.println("\n--- Collection Operations Benchmark ---");
        
        // Benchmark ArrayList
        long startTime = System.nanoTime();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < OBJECT_COUNT; i++) {
            list.add(i);
        }
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("ArrayList add: %d ms%n", duration);
        
        // Benchmark HashMap
        startTime = System.nanoTime();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < OBJECT_COUNT; i++) {
            map.put(i, i);
        }
        duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("HashMap put: %d ms%n", duration);
    }
    
    private static void printStatistics() {
        System.out.println("\n--- Statistics ---");
        
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();
        
        System.out.printf("Used Memory: %d MB%n", usedMemory / (1024 * 1024));
        System.out.printf("Free Memory: %d MB%n", freeMemory / (1024 * 1024));
        System.out.printf("Total Memory: %d MB%n", totalMemory / (1024 * 1024));
        System.out.printf("Max Memory: %d MB%n", maxMemory / (1024 * 1024));
        
        // Get GC information
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.printf("GC: %s, Collections: %d, Time: %d ms%n",
                gcBean.getName(), gcBean.getCollectionCount(), gcBean.getCollectionTime());
        }
    }
}
```

## Enterprise Example

### Production GC Monitoring

```java
package academy.javaengineering.jvm.garbagecollection;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Enterprise-grade GC monitoring and reporting.
 */
public class EnterpriseGCMonitor {
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
    private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private final List<GCEvent> gcEvents = new ArrayList<>();
    
    public void startMonitoring() {
        System.out.println("=== Enterprise GC Monitoring ===\n");
        
        // Schedule metric collection
        scheduler.scheduleAtFixedRate(this::collectMetrics, 0, 5, TimeUnit.SECONDS);
        
        // Schedule report generation
        scheduler.scheduleAtFixedRate(this::generateReport, 0, 1, TimeUnit.MINUTES);
        
        System.out.println("GC monitoring started. Press Ctrl+C to stop.\n");
    }
    
    private void collectMetrics() {
        try {
            // Collect GC metrics
            for (GarbageCollectorMXBean gcBean : gcBeans) {
                GCEvent event = new GCEvent(
                    gcBean.getName(),
                    gcBean.getCollectionCount(),
                    gcBean.getCollectionTime(),
                    System.currentTimeMillis()
                );
                
                synchronized (gcEvents) {
                    gcEvents.add(event);
                    
                    // Keep only last 1000 events
                    if (gcEvents.size() > 1000) {
                        gcEvents.remove(0);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error collecting GC metrics: " + e.getMessage());
        }
    }
    
    private void generateReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("GC MONITORING REPORT");
        System.out.println("=".repeat(60));
        
        // Memory information
        System.out.println("\n--- Memory ---");
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();
        
        System.out.printf("Heap: %d MB used / %d MB committed / %d MB max%n",
            heapUsage.getUsed() / (1024 * 1024),
            heapUsage.getCommitted() / (1024 * 1024),
            heapUsage.getMax() / (1024 * 1024));
        
        System.out.printf("Non-Heap: %d MB used / %d MB committed%n",
            nonHeapUsage.getUsed() / (1024 * 1024),
            nonHeapUsage.getCommitted() / (1024 * 1024));
        
        // GC information
        System.out.println("\n--- Garbage Collection ---");
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.printf("GC: %s%n", gcBean.getName());
            System.out.printf("  Collections: %d%n", gcBean.getCollectionCount());
            System.out.printf("  Time: %d ms%n", gcBean.getCollectionTime());
            
            // Calculate average pause time
            long[] collectionTimes = gcBean.getCollectionTimes();
            if (collectionTimes != null && collectionTimes.length > 0) {
                long totalTime = 0;
                for (long time : collectionTimes) {
                    totalTime += time;
                }
                long avgTime = totalTime / collectionTimes.length;
                System.out.printf("  Average Pause: %d ms%n", avgTime);
            }
        }
        
        // GC events analysis
        synchronized (gcEvents) {
            if (!gcEvents.isEmpty()) {
                System.out.println("\n--- Recent GC Events ---");
                int startIndex = Math.max(0, gcEvents.size() - 10);
                for (int i = startIndex; i < gcEvents.size(); i++) {
                    GCEvent event = gcEvents.get(i);
                    System.out.printf("  %s: Collections=%d, Time=%d ms%n",
                        event.name, event.collectionCount, event.collectionTime);
                }
            }
        }
        
        System.out.println("=".repeat(60));
    }
    
    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public static void main(String[] args) {
        EnterpriseGCMonitor monitor = new EnterpriseGCMonitor();
        monitor.startMonitoring();
        
        // Run for 5 minutes
        try {
            Thread.sleep(300_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        monitor.stop();
    }
    
    // GC event class
    private static class GCEvent {
        final String name;
        final long collectionCount;
        final long collectionTime;
        final long timestamp;
        
        GCEvent(String name, long collectionCount, long collectionTime, long timestamp) {
            this.name = name;
            this.collectionCount = collectionCount;
            this.collectionTime = collectionTime;
            this.timestamp = timestamp;
        }
    }
}
```

## Performance

### GC Performance Metrics

| Metric | Description | Target |
|--------|-------------|--------|
| **GC Pause Time** | Time spent in GC | < 100ms |
| **GC Frequency** | How often GC runs | Depends on app |
| **Throughput** | Time spent in application | > 95% |
| **Memory Usage** | Memory consumed | < 70% of max |
| **Promotion Rate** | Objects promoted to old gen | Minimize |

### GC Tuning Strategies

1. **Heap Size Tuning**
   - Set appropriate initial and maximum heap size
   - Balance between startup time and memory usage
   - Consider container memory limits

2. **GC Algorithm Selection**
   - **Serial GC**: Small applications, single CPU
   - **Parallel GC**: Throughput-focused applications
   - **G1 GC**: Balanced latency and throughput
   - **ZGC**: Ultra-low latency applications
   - **Shenandoah GC**: Low-latency applications

3. **GC Parameter Tuning**
   - **MaxGCPauseMillis**: Target maximum pause time
   - **GCTimeRatio**: Target GC time ratio
   - **NewRatio**: Ratio of young to old generation
   - **SurvivorRatio**: Ratio of Eden to Survivor

## Best Practices

### GC Best Practices

1. **Right-Size the Heap**
   - Don't set heap too large (causes long GC pauses)
   - Don't set heap too small (causes frequent GC)
   - Monitor and adjust based on workload

2. **Choose Appropriate GC Algorithm**
   - **Latency-sensitive**: Use G1, ZGC, or Shenandoah
   - **Throughput-focused**: Use Parallel GC
   - **Small applications**: Use Serial GC

3. **Monitor GC Activity**
   - Enable GC logging
   - Use JMX for monitoring
   - Set up alerts for GC issues

4. **Optimize Object Creation**
   - Reuse objects when possible
   - Use object pools for expensive objects
   - Minimize temporary object creation

5. **Prevent Memory Leaks**
   - Close resources properly
   - Use weak references appropriately
   - Monitor memory usage

## Common Mistakes

### Mistake 1: Setting Heap Too Large

```bash
# BAD: Setting heap too large
java -Xmx16g MyApp  # May cause long GC pauses

# GOOD: Setting heap appropriately
java -Xmx4g MyApp   # Based on workload
```

### Mistake 2: Not Monitoring GC

```java
// BAD: Not monitoring GC
public class BadGCPractice {
    // No GC monitoring
}

// GOOD: Monitoring GC
public class GoodGCPractice {
    private final List<GarbageCollectorMXBean> gcBeans = 
        ManagementFactory.getGarbageCollectorMXBeans();
    
    public void monitorGC() {
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.println("GC: " + gcBean.getName() + 
                ", Collections: " + gcBean.getCollectionCount());
        }
    }
}
```

### Mistake 3: Creating Too Many Objects

```java
// BAD: Creating too many objects
public class BadObjectCreation {
    public void process() {
        for (int i = 0; i < 1000000; i++) {
            String s = new String("test");  // Creates new object each time
        }
    }
}

// GOOD: Reusing objects
public class GoodObjectCreation {
    public void process() {
        String s = "test";  // Reuses string constant
        for (int i = 0; i < 1000000; i++) {
            // Use s
        }
    }
}
```

## Pitfalls

### Pitfall 1: Memory Leak

```java
// BAD: Memory leak
public class MemoryLeak {
    private static final List<byte[]> LEAK = new ArrayList<>();
    
    public void addToLeak(byte[] data) {
        LEAK.add(data);  // Never GC'd
    }
}

// GOOD: No memory leak
public class NoMemoryLeak {
    public void processData(byte[] data) {
        // Process data
        // data will be GC'd when method returns
    }
}
```

### Pitfall 2: GC Overhead

```java
// BAD: Too much object creation
public class GCOverhead {
    public void process() {
        for (int i = 0; i < 1000000; i++) {
            Object obj = new Object();  // Creates many short-lived objects
        }
    }
}

// GOOD: Reusing objects
public class NoGCOverhead {
    private final Object reusableObject = new Object();
    
    public void process() {
        for (int i = 0; i < 1000000; i++) {
            // Reuse reusableObject
        }
    }
}
```

## Debugging Tips

### GC Debug Commands

```bash
# Enable GC logging
java -Xlog:gc* MyApp

# Print GC details
java -XX:+PrintGCDetails MyApp

# Print GC timestamps
java -XX:+PrintGCDateStamps MyApp

# Print heap before/after GC
java -XX:+PrintHeapAtGC MyApp

# Print GC pause times
java -XX:+PrintGCApplicationStoppedTime MyApp
```

### Common GC Issues

| Issue | Symptom | Solution |
|-------|---------|----------|
| Long GC pauses | Application unresponsive | Tune GC parameters |
| Memory leaks | Memory keeps growing | Find and fix leak |
| Frequent GC | High CPU usage | Increase heap size |
| Promotion failure | Full GC too often | Increase old gen size |

## Comparison Table

### GC Algorithms

| Algorithm | Latency | Throughput | Memory | Use Case |
|-----------|---------|------------|--------|----------|
| **Serial GC** | High | High | Low | Small apps |
| **Parallel GC** | Medium | Very High | Medium | Throughput-focused |
| **G1 GC** | Low | High | Medium | Balanced |
| **ZGC** | Very Low | High | High | Ultra-low latency |
| **Shenandoah** | Very Low | High | High | Low latency |

### GC Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| **-Xms** | Initial heap size | 256MB |
| **-Xmx** | Maximum heap size | 256MB |
| **-XX:NewRatio** | Old:Young ratio | 2 |
| **-XX:SurvivorRatio** | Eden:Survivor ratio | 8 |
| **-XX:MaxGCPauseMillis** | Max pause time | 200ms |

## Decision Tree

### Choosing GC Algorithm

```
What is your priority?
├── Latency (Response Time)
│   ├── Ultra-low latency (< 10ms)
│   │   ├── Use: ZGC or Shenandoah
│   │   └── Memory: High
│   └── Low latency (< 100ms)
│       ├── Use: G1 GC
│       └── Memory: Medium
├── Throughput (Work Done)
│   ├── Use: Parallel GC
│   └── Memory: Medium
└── Small Application
    ├── Use: Serial GC
    └── Memory: Low
```

## Interview Questions

### Basic Questions

1. **What is garbage collection?**
   - Automatic memory management that reclaims memory from unreachable objects

2. **What are the different generations in the JVM heap?**
   - Young Generation (Eden, Survivor), Old Generation, Metaspace

3. **What are GC roots?**
   - Starting points for reachability analysis (local variables, static variables, etc.)

4. **What is the difference between mark-and-sweep and copying collectors?**
   - Mark-and-sweep: Marks reachable objects, sweeps unreachable
   - Copying: Copies reachable objects to new space

### Intermediate Questions

5. **What is the G1 GC?**
   - Garbage-First GC that divides heap into regions and prioritizes collection

6. **What is ZGC?**
   - Ultra-low latency GC that uses load barriers and colored pointers

7. **What is the difference between minor and major GC?**
   - Minor GC: Young Generation collection
   - Major GC: Full heap collection

8. **What is promotion?**
   - Moving objects from Young Generation to Old Generation

### Advanced Questions

9. **How does ZGC achieve low latency?**
   - Uses load barriers, colored pointers, and concurrent processing

10. **What is the difference between G1 and ZGC?**
    - G1: Region-based, good balance
    - ZGC: Ultra-low latency, higher memory overhead

11. **How does GC handle large objects?**
    - Large objects may be allocated directly in Old Generation

12. **What is GC overhead?**
    - CPU time spent on garbage collection

## Exercises

### Exercise 1: GC Logging
Enable GC logging and analyze the output for a simple application.

### Exercise 2: Memory Leak
Write a program with a memory leak and use GC tools to find it.

### Exercise 3: GC Tuning
Tune GC parameters for a specific workload and measure the impact.

### Exercise 4: Object Lifecycle
Track object creation and garbage collection using finalize() or Cleaner.

## Assignments

### Assignment 1: GC Benchmark
Create a GC benchmark that compares different GC algorithms.

### Assignment 2: Memory Leak Detection
Build a memory leak detection tool using heap dumps.

### Assignment 3: Production GC Monitoring
Implement production-grade GC monitoring for a web application.

## Mini Project

### GC Tuning Dashboard

Create a dashboard that:
1. Monitors GC activity in real-time
2. Shows GC pause times and frequency
3. Provides tuning recommendations
4. Alerts on GC issues

**Requirements:**
- Use JMX for data collection
- Create a web-based dashboard
- Support multiple GC algorithms
- Provide historical data

## Summary

### Key Takeaways

1. **GC is Automatic**: No manual memory management needed
2. **Generations Optimize Performance**: Different areas for different object lifetimes
3. **GC Tuning is Important**: Appropriate settings improve performance
4. **Monitoring is Essential**: Track GC activity in production
5. **Memory Leaks Happen**: Even with GC, leaks can occur

### Next Steps

- Continue to Topic 06: GC Algorithms
- Study GC tuning guides
- Practice with GC monitoring tools
- Read "Java Performance" by Scott Oaks

## References

### Official Documentation
- [Garbage Collection Tuning](https://docs.oracle.com/en/java/javase/21/docs/technotes/guides/vm/gctuning/index.html)
- [JVM GC Options](https://docs.oracle.com/en/java/javase/17/docs/specs/man/java.html)
- [G1 GC Documentation](https://docs.oracle.com/en/java/javase/21/docs/technotes/guides/vm/gctuning/g1_gc_tuning.html)

### Books
- "Java Performance" by Scott Oaks
- "Optimizing Java" by Benjamin J. Evans
- "The Garbage Collection Handbook" by Richard Jones

### Online Resources
- [GC Tuning Guide](https://www.baeldung.com/jvm-garbage-collection)
- [GC Algorithms](https://www.jvmhosting.com/garbage-collection-algorithms/)
- [ZGC Documentation](https://wiki.openjdk.java.net/zgc)

### Tools
- [GCViewer](https://github.com/chewiebug/GCViewer)
- [GCEasy](https://gceasy.io/)
- [JVisualVM](https://visualvm.java.net/)

---

**Next Topic**: [06. GC Algorithms](../06-gc-algorithms/README.md)
