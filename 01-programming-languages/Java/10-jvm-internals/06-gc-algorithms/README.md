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


---

**Continue to Part 2**: [README-part2.md](README-part2.md) | [Part 3](README-part3.md)