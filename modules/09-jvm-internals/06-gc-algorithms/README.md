# 06. GC Algorithms

## Introduction

Garbage Collection algorithms are the heart of Java's memory management system. Each algorithm has its own approach to identifying and reclaiming unused memory, with different trade-offs between latency, throughput, and memory usage. Understanding these algorithms is essential for choosing the right GC for your application and tuning it for optimal performance.

This topic provides a deep dive into the major GC algorithms used in modern JVMs, including Serial GC, Parallel GC, G1 GC, ZGC, and Shenandoah GC. We'll explore how each algorithm works, when to use it, and how to tune it for specific workloads.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Explain the differences between major GC algorithms
- [ ] Choose the appropriate GC algorithm for different scenarios
- [ ] Tune GC parameters for optimal performance
- [ ] Understand the internals of G1, ZGC, and Shenandoah
- [ ] Monitor and analyze GC behavior
- [ ] Diagnose and fix GC-related performance issues
- [ ] Apply GC tuning best practices

## Prerequisites

- Completion of Topic 05: Garbage Collection
- Understanding of JVM heap structure
- Basic knowledge of concurrency concepts
- Familiarity with performance monitoring

## Why This Concept Exists

### The Performance Trade-off

Different applications have different performance requirements:
- **Web Servers**: Need low latency for user requests
- **Batch Processing**: Need high throughput for large datasets
- **Real-time Systems**: Need predictable, ultra-low latency
- **Embedded Systems**: Need minimal memory footprint

No single GC algorithm can optimize for all these requirements simultaneously.

### The Algorithm Evolution

GC algorithms have evolved over time:
- **Serial GC**: Simple, single-threaded (Java 1.0)
- **Parallel GC**: Multi-threaded, throughput-focused (Java 1.2)
- **CMS GC**: Concurrent, low-latency (Java 1.4, deprecated)
- **G1 GC**: Region-based, balanced (Java 7)
- **ZGC**: Ultra-low latency (Java 11)
- **Shenandoah GC**: Low-latency, concurrent (Java 12)

### Real-World Requirements

Modern applications require:
- **Consistent Latency**: No long GC pauses
- **High Throughput**: Maximize work done
- **Memory Efficiency**: Use memory effectively
- **Scalability**: Handle growing workloads

## Problem Statement

### The Algorithm Choice Challenge

Without understanding GC algorithms, developers face:
- **Poor Performance**: Wrong algorithm for the workload
- **Long Pauses**: GC pauses affecting user experience
- **Memory Waste**: Inefficient memory usage
- **Unpredictable Behavior**: Inconsistent performance

### Real-World Example

A financial trading platform experienced:
- 500ms GC pauses causing missed trades
- High throughput but unpredictable latency
- Memory usage growing over time

The solution? Switching from Parallel GC to ZGC with proper tuning.

## Theory

### GC Algorithm Categories

```
┌─────────────────────────────────────────────────────────────┐
│                    GC Algorithms                            │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Serial GC                                           │   │
│  │  - Single-threaded                                   │   │
│  │  - Stop-the-world                                    │   │
│  │  - Simple, low overhead                              │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Parallel GC                                         │   │
│  │  - Multi-threaded                                    │   │
│  │  - Stop-the-world                                    │   │
│  │  - High throughput                                   │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  G1 GC                                               │   │
│  │  - Region-based                                      │   │
│  │  - Concurrent + Stop-the-world                       │   │
│  │  - Balanced latency/throughput                       │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  ZGC                                                 │   │
│  │  - Load barriers                                     │   │
│  │  - Concurrent                                        │   │
│  │  - Ultra-low latency                                 │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Shenandoah GC                                       │   │
│  │  - Brooks pointers                                   │   │
│  │  - Concurrent                                        │   │
│  │  - Low latency                                       │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### G1 GC Internals

G1 GC divides the heap into regions:

```
┌─────────────────────────────────────────────────────────────┐
│                    G1 GC Heap                               │
│                                                             │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐        │
│  │  E  │ │  E  │ │  S  │ │  O  │  │  O  │ │  H  │        │
│  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘        │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐        │
│  │  E  │ │  E  │ │  S  │ │  O  │  │  O  │ │  H  │        │
│  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘        │
│                                                             │
│  E = Eden (Young)                                          │
│  S = Survivor (Young)                                      │
│  O = Old                                                   │
│  H = Humongous (Large objects)                             │
│  Free = Unassigned                                         │
└─────────────────────────────────────────────────────────────┘
```

### ZGC Internals

ZGC uses colored pointers and load barriers:

```
┌─────────────────────────────────────────────────────────────┐
│                    ZGC Pointer Colors                       │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Marked0 (M0) - Blue                                │   │
│  │  - Object is marked by concurrent mark              │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Marked1 (M1) - Red                                 │   │
│  │  - Object is marked by concurrent mark              │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Remapped - Green                                   │   │
│  │  - Object has been remapped                         │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Finalizable - Yellow                               │   │
│  │  - Object has finalizer                             │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Internal Working

### G1 GC Collection Process

```
1. Young Generation Collection
   ├── Select all Eden and Survivor regions
   ├── Copy live objects to new Survivor regions
   └── Promote old objects to Old regions

2. Mixed Collection
   ├── Select Young regions + some Old regions
   ├── Collect selected regions
   └── Update heap statistics

3. Full GC (Rare)
   ├── Stop all application threads
   ├── Collect entire heap
   └── Compact heap
```

### ZGC Collection Process

```
1. Pause Mark Start
   ├── Scan roots
   ├── Mark reachable objects
   └── Very short pause (< 1ms)

2. Concurrent Mark
   ├── Continue marking from roots
   ├── Use load barriers
   └── No pause

3. Pause Mark End
   ├── Process remaining work
   ├── Handle reference processing
   └── Very short pause (< 1ms)

4. Concurrent Prepare for Relocate
   ├── Identify free regions
   └── Plan relocation

5. Pause Relocate Start
   ├── Initialize relocation
   └── Very short pause (< 1ms)

6. Concurrent Relocate
   ├── Move objects to new locations
   ├── Update references
   └── No pause
```

## JVM Perspective

### What the JVM Sees

The JVM sees:
- **Heap Regions**: Different areas for different object types
- **Object Graphs**: Network of objects connected by references
- **GC Roots**: Starting points for reachability analysis
- **GC Threads**: Threads that perform garbage collection

### Memory Layout with Different Algorithms

```
Serial GC:
┌─────────────────────────────────────────────────────────────┐
│  Young Gen  │  Old Gen  │                                   │
└─────────────────────────────────────────────────────────────┘

Parallel GC:
┌─────────────────────────────────────────────────────────────┐
│  Young Gen (Parallel)  │  Old Gen (Parallel)  │            │
└─────────────────────────────────────────────────────────────┘

G1 GC:
┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐
│  E  │ │  E  │ │  S  │ │  O  │ │  O  │ │ Free│
└─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘

ZGC:
┌─────────────────────────────────────────────────────────────┐
│  Multi-Page Heap with Colored Pointers                      │
└─────────────────────────────────────────────────────────────┘
```

## Memory Representation

### Object Header with GC Information

```
┌─────────────────────────────────────────────────────────────┐
│                    Object Header                            │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Mark Word (64 bits)                                │   │
│  │  - Hash code (31 bits)                              │   │
│  │  - GC age (4 bits)                                  │   │
│  │  - Lock state (2 bits)                              │   │
│  │  - GC color (2 bits) - ZGC                          │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Klass Pointer (32 or 64 bits)                      │   │
│  │  - Points to class metadata                         │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Syntax

### GC Algorithm Selection

```bash
# Serial GC
java -XX:+UseSerialGC MyApp

# Parallel GC
java -XX:+UseParallelGC MyApp

# G1 GC (default in Java 9+)
java -XX:+UseG1GC MyApp

# ZGC (Java 11+)
java -XX:+UseZGC MyApp

# Shenandoah GC
java -XX:+UseShenandoahGC MyApp
```

### GC Tuning Parameters

```bash
# G1 GC tuning
java -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -XX:GCTimeRatio=19 \
     -XX:InitiatingHeapOccupancyPercent=45 \
     MyApp

# ZGC tuning
java -XX:+UseZGC \
     -XX:SoftMaxHeapSize=4g \
     -XX:ConcGCThreads=4 \
     MyApp

# Shenandoah tuning
java -XX:+UseShenandoahGC \
     -XX:ShenandoahGCHeuristics=compact \
     MyApp
```

## Easy Example

### Basic GC Algorithm Comparison

```java
package academy.javaengineering.jvm.gc_algorithms;

/**
 * Demonstrates basic GC algorithm comparison.
 */
public class BasicGCAlgorithmDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Basic GC Algorithm Demo ===\n");
        
        // Get current GC information
        printGCInfo();
        
        // Create some objects
        createObjects();
        
        // Force GC
        System.gc();
        
        // Print memory usage
        printMemoryUsage();
    }
    
    private static void printGCInfo() {
        System.out.println("--- GC Information ---");
        
        java.lang.management.MemoryMXBean memoryBean = 
            java.lang.management.ManagementFactory.getMemoryMXBean();
        
        java.lang.management.MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        System.out.printf("Heap Used: %d MB%n", heapUsage.getUsed() / (1024 * 1024));
        System.out.printf("Heap Committed: %d MB%n", heapUsage.getCommitted() / (1024 * 1024));
        System.out.printf("Heap Max: %d MB%n", heapUsage.getMax() / (1024 * 1024));
        
        System.out.println();
    }
    
    private static void createObjects() {
        System.out.println("--- Creating Objects ---");
        
        for (int i = 0; i < 1000; i++) {
            Object obj = new Object();
        }
        
        System.out.println("Created 1000 objects");
        System.out.println();
    }
    
    private static void printMemoryUsage() {
        System.out.println("--- Memory Usage ---");
        
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        System.out.printf("Used: %d MB%n", usedMemory / (1024 * 1024));
        System.out.printf("Free: %d MB%n", freeMemory / (1024 * 1024));
        System.out.printf("Total: %d MB%n", totalMemory / (1024 * 1024));
    }
}
```

## Medium Example

### G1 GC Configuration

```java
package academy.javaengineering.jvm.gc_algorithms;

import java.lang.management.*;
import java.util.*;

/**
 * Demonstrates G1 GC configuration and monitoring.
 */
public class G1GCConfigDemo {
    
    public static void main(String[] args) {
        System.out.println("=== G1 GC Configuration Demo ===\n");
        
        // Print current GC configuration
        printGCConfiguration();
        
        // Simulate workload
        simulateWorkload();
        
        // Print GC statistics
        printGCStatistics();
    }
    
    private static void printGCConfiguration() {
        System.out.println("--- GC Configuration ---");
        
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        List<String> vmArgs = runtimeBean.getInputArguments();
        
        System.out.println("JVM Arguments:");
        for (String arg : vmArgs) {
            if (arg.contains("GC") || arg.contains("gc") || arg.contains("Heap")) {
                System.out.println("  " + arg);
            }
        }
        
        System.out.println();
    }
    
    private static void simulateWorkload() {
        System.out.println("--- Simulating Workload ---");
        
        // Create many objects to trigger GC
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            objects.add(new byte[1024]);  // 1KB objects
            
            // Remove some objects to create garbage
            if (i % 10 == 0 && !objects.isEmpty()) {
                objects.remove(0);
            }
        }
        
        System.out.println("Workload simulation complete");
        System.out.println();
    }
    
    private static void printGCStatistics() {
        System.out.println("--- GC Statistics ---");
        
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.printf("GC: %s%n", gcBean.getName());
            System.out.printf("  Collections: %d%n", gcBean.getCollectionCount());
            System.out.printf("  Time: %d ms%n", gcBean.getCollectionTime());
        }
        
        System.out.println();
        
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        
        System.out.printf("Heap Usage: %d MB / %d MB%n",
            heapUsage.getUsed() / (1024 * 1024),
            heapUsage.getCommitted() / (1024 * 1024));
    }
}
```

## Hard Example

### GC Algorithm Benchmark

```java
package academy.javaengineering.jvm.gc_algorithms;

import java.util.*;
import java.util.concurrent.*;

/**
 * Comprehensive GC algorithm benchmark.
 */
public class GCAlgorithmBenchmark {
    
    private static final int ITERATIONS = 10;
    private static final int OBJECTS_PER_ITERATION = 1_000_000;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== GC Algorithm Benchmark ===\n");
        
        // Benchmark different allocation patterns
        benchmarkAllocationPatterns();
        
        // Benchmark different object sizes
        benchmarkObjectSizes();
        
        // Benchmark different lifetimes
        benchmarkObjectLifetimes();
        
        // Print final statistics
        printFinalStatistics();
    }
    
    private static void benchmarkAllocationPatterns() throws InterruptedException {
        System.out.println("--- Allocation Pattern Benchmark ---");
        
        // Sequential allocation
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            allocateSequentially();
        }
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Sequential allocation: %d ms%n", duration);
        
        // Random allocation
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            allocateRandomly();
        }
        duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Random allocation: %d ms%n", duration);
        
        System.out.println();
    }
    
    private static void allocateSequentially() {
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < OBJECTS_PER_ITERATION; i++) {
            objects.add(new Object());
        }
    }
    
    private static void allocateRandomly() {
        List<Object> objects = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < OBJECTS_PER_ITERATION; i++) {
            objects.add(new Object());
            if (random.nextBoolean() && !objects.isEmpty()) {
                objects.remove(random.nextInt(objects.size()));
            }
        }
    }
    
    private static void benchmarkObjectSizes() throws InterruptedException {
        System.out.println("--- Object Size Benchmark ---");
        
        // Small objects
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            allocateSmallObjects();
        }
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Small objects (16 bytes): %d ms%n", duration);
        
        // Medium objects
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            allocateMediumObjects();
        }
        duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Medium objects (1KB): %d ms%n", duration);
        
        // Large objects
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            allocateLargeObjects();
        }
        duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Large objects (1MB): %d ms%n", duration);
        
        System.out.println();
    }
    
    private static void allocateSmallObjects() {
        for (int i = 0; i < OBJECTS_PER_ITERATION; i++) {
            new Object();
        }
    }
    
    private static void allocateMediumObjects() {
        for (int i = 0; i < OBJECTS_PER_ITERATION; i++) {
            new byte[1024];
        }
    }
    
    private static void allocateLargeObjects() {
        for (int i = 0; i < 1000; i++) {
            new byte[1024 * 1024];
        }
    }
    
    private static void benchmarkObjectLifetimes() throws InterruptedException {
        System.out.println("--- Object Lifetime Benchmark ---");
        
        // Short-lived objects
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            allocateShortLivedObjects();
        }
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Short-lived objects: %d ms%n", duration);
        
        // Long-lived objects
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            allocateLongLivedObjects();
        }
        duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Long-lived objects: %d ms%n", duration);
        
        System.out.println();
    }
    
    private static void allocateShortLivedObjects() {
        for (int i = 0; i < OBJECTS_PER_ITERATION; i++) {
            new Object();  // Immediately eligible for GC
        }
    }
    
    private static void allocateLongLivedObjects() {
        List<Object> longLived = new ArrayList<>();
        for (int i = 0; i < OBJECTS_PER_ITERATION; i++) {
            longLived.add(new Object());  // Kept alive
        }
    }
    
    private static void printFinalStatistics() {
        System.out.println("--- Final Statistics ---");
        
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();
        
        System.out.printf("Used Memory: %d MB%n", usedMemory / (1024 * 1024));
        System.out.printf("Free Memory: %d MB%n", freeMemory / (1024 * 1024));
        System.out.printf("Total Memory: %d MB%n", totalMemory / (1024 * 1024));
        System.out.printf("Max Memory: %d MB%n", maxMemory / (1024 * 1024));
        
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.printf("GC: %s, Collections: %d, Time: %d ms%n",
                gcBean.getName(), gcBean.getCollectionCount(), gcBean.getCollectionTime());
        }
    }
}
```

## Enterprise Example

### Production GC Monitoring System

```java
package academy.javaengineering.jvm.gc_algorithms;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Production-grade GC monitoring system.
 */
public class EnterpriseGCMonitoringSystem {
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    private final List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
    private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    
    private final List<GCEvent> gcEvents = new CopyOnWriteArrayList<>();
    private final Map<String, Long> gcCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> gcTimes = new ConcurrentHashMap<>();
    
    public void startMonitoring() {
        System.out.println("=== Enterprise GC Monitoring System ===\n");
        
        // Schedule metric collection
        scheduler.scheduleAtFixedRate(this::collectMetrics, 0, 5, TimeUnit.SECONDS);
        
        // Schedule report generation
        scheduler.scheduleAtFixedRate(this::generateReport, 0, 1, TimeUnit.MINUTES);
        
        // Schedule anomaly detection
        scheduler.scheduleAtFixedRate(this::detectAnomalies, 0, 30, TimeUnit.SECONDS);
        
        System.out.println("Monitoring system started. Press Ctrl+C to stop.\n");
    }
    
    private void collectMetrics() {
        try {
            // Collect GC metrics
            for (GarbageCollectorMXBean gcBean : gcBeans) {
                String gcName = gcBean.getName();
                long collectionCount = gcBean.getCollectionCount();
                long collectionTime = gcBean.getCollectionTime();
                
                // Store metrics
                gcCounts.put(gcName, collectionCount);
                gcTimes.put(gcName, collectionTime);
                
                // Create GC event
                GCEvent event = new GCEvent(gcName, collectionCount, collectionTime, 
                    System.currentTimeMillis());
                gcEvents.add(event);
                
                // Keep only last 1000 events
                if (gcEvents.size() > 1000) {
                    gcEvents.remove(0);
                }
            }
        } catch (Exception e) {
            System.err.println("Error collecting metrics: " + e.getMessage());
        }
    }
    
    private void generateReport() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("GC MONITORING REPORT");
        System.out.println("=".repeat(70));
        
        // Memory information
        System.out.println("\n--- Memory Usage ---");
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
        }
        
        // Thread information
        System.out.println("\n--- Threads ---");
        System.out.printf("Active: %d, Peak: %d, Daemon: %d%n",
            threadBean.getThreadCount(),
            threadBean.getPeakThreadCount(),
            threadBean.getDaemonThreadCount());
        
        System.out.println("=".repeat(70));
    }
    
    private void detectAnomalies() {
        // Simple anomaly detection
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            long collectionTime = gcBean.getCollectionTime();
            if (collectionTime > 1000) {  // More than 1 second
                System.out.println("WARNING: Long GC pause detected: " + 
                    gcBean.getName() + " - " + collectionTime + " ms");
            }
        }
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
        EnterpriseGCMonitoringSystem monitor = new EnterpriseGCMonitoringSystem();
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

### GC Algorithm Performance Comparison

| Algorithm | Latency | Throughput | Memory | Use Case |
|-----------|---------|------------|--------|----------|
| **Serial GC** | High | High | Low | Small apps |
| **Parallel GC** | Medium | Very High | Medium | Throughput-focused |
| **G1 GC** | Low | High | Medium | Balanced |
| **ZGC** | Very Low | High | High | Ultra-low latency |
| **Shenandoah** | Very Low | High | High | Low latency |

### Performance Tuning Guidelines

1. **For Low Latency**
   - Use ZGC or Shenandoah
   - Set appropriate heap size
   - Tune concurrent threads

2. **For High Throughput**
   - Use Parallel GC
   - Maximize heap size
   - Tune parallel threads

3. **For Balanced Workloads**
   - Use G1 GC
   - Set MaxGCPauseMillis
   - Tune region size

## Best Practices

### GC Algorithm Selection Best Practices

1. **Understand Your Workload**
   - Latency-sensitive vs. throughput-focused
   - Object creation rate
   - Object lifetime distribution

2. **Test with Production Data**
   - Use realistic workloads
   - Measure under load
   - Compare different algorithms

3. **Monitor and Tune**
   - Enable GC logging
   - Use JMX for monitoring
   - Adjust parameters based on data

4. **Consider Hardware**
   - CPU cores affect parallelism
   - Memory size affects heap sizing
   - Storage speed affects GC performance

## Common Mistakes

### Mistake 1: Using Wrong Algorithm

```bash
# BAD: Using Serial GC for large application
java -XX:+UseSerialGC -Xmx16g LargeApplication

# GOOD: Using appropriate algorithm
java -XX:+UseG1GC -Xmx16g LargeApplication
```

### Mistake 2: Not Tuning GC

```bash
# BAD: Using default settings
java MyApp

# GOOD: Tuning for workload
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xmx4g MyApp
```

### Mistake 3: Ignoring GC Logs

```java
// BAD: Not analyzing GC logs
public class BadGCPractice {
    // No GC logging
}

// GOOD: Analyzing GC logs
public class GoodGCPractice {
    public void analyzeGCLogs() {
        // Parse and analyze GC logs
    }
}
```

## Pitfalls

### Pitfall 1: Long GC Pauses

```java
// BAD: Creating too many objects
public class LongGCPause {
    public void process() {
        for (int i = 0; i < 1000000; i++) {
            new Object();  // Creates many objects
        }
    }
}

// GOOD: Reusing objects
public class ShortGCPause {
    private final Object reusableObject = new Object();
    
    public void process() {
        for (int i = 0; i < 1000000; i++) {
            // Reuse reusableObject
        }
    }
}
```

### Pitfall 2: Memory Fragmentation

```java
// BAD: Creating many small objects
public class Fragmentation {
    public void process() {
        for (int i = 0; i < 1000000; i++) {
            new byte[16];  // Many small objects
        }
    }
}

// GOOD: Using larger objects
public class NoFragmentation {
    public void process() {
        for (int i = 0; i < 1000; i++) {
            new byte[1024];  // Fewer larger objects
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

### GC Algorithm Features

| Feature | Serial | Parallel | G1 | ZGC | Shenandoah |
|---------|--------|----------|-----|-----|------------|
| **Threading** | Single | Multi | Multi | Multi | Multi |
| **Concurrent** | No | No | Partial | Yes | Yes |
| **Pause Model** | STW | STW | Mixed | Ultra-short | Ultra-short |
| **Heap Layout** | Contiguous | Contiguous | Regions | Multi-page | Regions |
| **Compaction** | Full | Full | Partial | Concurrent | Concurrent |

### GC Algorithm Tuning Parameters

| Parameter | Serial | Parallel | G1 | ZGC | Shenandoah |
|-----------|--------|----------|-----|-----|------------|
| **-XX:+UseGC** | UseSerialGC | UseParallelGC | UseG1GC | UseZGC | UseShenandoahGC |
| **-XX:MaxGCPauseMillis** | N/A | N/A | 200 | N/A | N/A |
| **-XX:GCTimeRatio** | N/A | N/A | 12 | N/A | N/A |
| **-XX:ConcGCThreads** | N/A | N/A | N/A | 4 | 4 |

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

1. **What are the different GC algorithms in Java?**
   - Serial, Parallel, G1, ZGC, Shenandoah

2. **What is the difference between Serial and Parallel GC?**
   - Serial: Single-threaded
   - Parallel: Multi-threaded

3. **What is G1 GC?**
   - Garbage-First GC that divides heap into regions

4. **What is ZGC?**
   - Ultra-low latency GC using colored pointers

### Intermediate Questions

5. **When should you use G1 GC?**
   - For balanced latency and throughput workloads

6. **When should you use ZGC?**
   - For ultra-low latency applications

7. **What is the difference between G1 and ZGC?**
   - G1: Region-based, good balance
   - ZGC: Ultra-low latency, higher memory overhead

8. **What is Shenandoah GC?**
   - Low-latency GC using Brooks pointers

### Advanced Questions

9. **How does ZGC achieve low latency?**
   - Uses load barriers, colored pointers, and concurrent processing

10. **What is the difference between G1 and Shenandoah?**
    - G1: Region-based, good balance
    - Shenandoah: Brooks pointers, more concurrent

11. **How does GC handle large objects?**
    - Large objects may be allocated directly in Old Generation

12. **What is GC overhead?**
    - CPU time spent on garbage collection

## Exercises

### Exercise 1: Algorithm Comparison
Compare the performance of different GC algorithms for a specific workload.

### Exercise 2: GC Tuning
Tune GC parameters for a specific workload and measure the impact.

### Exercise 3: GC Logging
Enable GC logging and analyze the output for a simple application.

### Exercise 4: Memory Leak
Write a program with a memory leak and use GC tools to find it.

## Assignments

### Assignment 1: GC Benchmark
Create a GC benchmark that compares different GC algorithms.

### Assignment 2: Production Monitoring
Implement production-grade GC monitoring for a web application.

### Assignment 3: Algorithm Selection
Choose the appropriate GC algorithm for different deployment scenarios.

## Mini Project

### GC Algorithm Benchmark Tool

Create a tool that:
1. Benchmarks different GC algorithms
2. Measures latency and throughput
3. Generates performance reports
4. Provides tuning recommendations

**Requirements:**
- Support multiple GC algorithms
- Measure key performance metrics
- Generate HTML reports
- Provide tuning suggestions

## Summary

### Key Takeaways

1. **Different Algorithms for Different Needs**: No one-size-fits-all
2. **G1 is the Default**: Good balance for most applications
3. **ZGC for Ultra-Low Latency**: When every millisecond counts
4. **Monitoring is Essential**: Track GC behavior in production
5. **Tuning Matters**: Appropriate settings improve performance

### Next Steps

- Continue to Topic 07: JIT Compilation
- Study GC tuning guides
- Practice with GC monitoring tools
- Read "The Garbage Collection Handbook"

## References

### Official Documentation
- [G1 GC Documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/g1_gc_tuning.html)
- [ZGC Documentation](https://wiki.openjdk.java.net/zgc)
- [Shenandoah GC](https://wiki.openjdk.java.net/shenandoah)

### Books
- "The Garbage Collection Handbook" by Richard Jones
- "Java Performance" by Scott Oaks
- "Optimizing Java" by Benjamin J. Evans

### Online Resources
- [GC Algorithms](https://www.jvmhosting.com/garbage-collection-algorithms/)
- [ZGC Tuning](https://wiki.openjdk.java.net/zgc)
- [Shenandoah GC](https://wiki.openjdk.java.net/shenandoah)

### Tools
- [GCViewer](https://github.com/chewiebug/GCViewer)
- [GCEasy](https://gceasy.io/)
- [JVisualVM](https://visualvm.java.net/)

---

**Next Topic**: [07. JIT Compilation](../07-jit-compilation/README.md)
