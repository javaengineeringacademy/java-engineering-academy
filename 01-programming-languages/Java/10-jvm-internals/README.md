# Module 10: JVM Internals

> **Difficulty:** ⭐⭐⭐⭐ Advanced  
> **Reading:** 50 min | **Practice:** 60 min | **Total:** 110 min

## Overview

The Java Virtual Machine (JVM) is the runtime engine that executes Java bytecode. Understanding JVM internals — architecture, class loading, memory management, garbage collection, JIT compilation, and profiling — is essential for performance tuning, debugging, and production reliability. This module covers the JVM from hardware-level execution to high-level monitoring tools.

## Learning Objectives

- [ ] Explain the JVM architecture (class loader, runtime data areas, execution engine)
- [ ] Describe the class loading lifecycle (loading, linking, initialization)
- [ ] Identify JVM memory areas (heap, stack, metaspace, native memory)
- [ ] Compare GC algorithms (G1, ZGC, Shenandoah, CMS) and choose the right one
- [ ] Understand JIT compilation and common optimizations
- [ ] Use profiling tools (VisualVM, JFR, async-profiler) to diagnose performance issues
- [ ] Apply JVM tuning flags for production workloads
- [ ] Diagnose memory leaks, thread contention, and GC pauses

## Prerequisites

- Java fundamentals and OOP
- Basic understanding of threads and concurrency (Module 09)
- Familiarity with command-line tools

## History

- **1996** — Java 1.0 introduced the JVM with interpreted bytecode execution
- **1997** — Java 1.1 added JNI (Java Native Interface) for native code integration
- **2000** — Java 1.3 introduced HotSpot JVM with JIT compilation
- **2004** — Java 5 added generational GC, concurrent Mark-Sweep
- **2011** — Java 7 introduced G1 garbage collector
- **2014** — Java 8 removed PermGen, added Metaspace
- **2017** — Java 9 added module system (JPMS) affecting class loading
- **2018** — Java 11 added ZGC (experimental) and application CDS
- **2021** — Java 17 made ZGC production-ready
- **2023** — Java 21 added generational ZGC, virtual threads

## Production Notes

- **Where is it used?** In every Java application that runs on the JVM
- **Why is it useful?** Understanding JVM internals enables performance tuning, debugging, and production reliability
- **When should it be avoided?** Not applicable; JVM knowledge is essential for production Java
- **Alternative?** GraalVM native image (ahead-of-time compilation), but JVM is the standard

## Why This Concept Exists

Without JVM knowledge:
- Cannot diagnose performance issues
- Cannot tune garbage collection
- Cannot debug memory leaks
- Cannot optimize JIT compilation
- Cannot size containers correctly

## Core Concepts

### JVM Architecture

```
┌─────────────────────────────────────┐
│           JVM Architecture          │
├─────────────────────────────────────┤
│  Class Loader Subsystem             │
│  ┌─────────────────────────────┐    │
│  │ Bootstrap → Platform → App  │    │
│  └─────────────────────────────┘    │
├─────────────────────────────────────┤
│  Runtime Data Areas                 │
│  ┌─────────────────────────────┐    │
│  │ Method Area → Heap          │    │
│  │ Stack → PC Registers        │    │
│  │ Native Method Stack         │    │
│  └─────────────────────────────┘    │
├─────────────────────────────────────┤
│  Execution Engine                   │
│  ┌─────────────────────────────┐    │
│  │ Interpreter → JIT Compiler  │    │
│  │ Garbage Collector            │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

### Memory Layout

```
┌─────────────────────────────────────┐
│           JVM Heap Memory           │
├─────────────────────────────────────┤
│  Young Generation                   │
│  ┌─────────────────────────────┐    │
│  │ Eden │ Survivor 0 │ Surv 1  │    │
│  └─────────────────────────────┘    │
├─────────────────────────────────────┤
│  Old Generation (Tenured)           │
│  ┌─────────────────────────────┐    │
│  │   Long-lived objects        │    │
│  └─────────────────────────────┘    │
├─────────────────────────────────────┤
│  Metaspace (Non-heap)               │
│  ┌─────────────────────────────┐    │
│  │ Class metadata, code cache  │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

### Class Loading Lifecycle

1. **Loading** — Read `.class` bytes, create `Class` object
2. **Linking**
   - **Verification** — Ensure bytecode is valid
   - **Preparation** — Allocate static fields
   - **Resolution** — Resolve symbolic references
3. **Initialization** — Execute static initializers

### Garbage Collection

| Algorithm | Pause Time | Throughput | Best For |
|-----------|-----------|------------|----------|
| Serial | Long | High | Single CPU, small heaps |
| Parallel | Medium | Highest | Throughput-sensitive apps |
| CMS | Short | Medium | Latency-sensitive apps |
| G1 | Short | High | Balanced workloads |
| ZGC | Very short | High | Large heaps, low latency |
| Shenandoah | Very short | High | Large heaps, low latency |

## Internal Working

### JIT Compilation Tiers

```
Bytecode → Interpreter → C1 Compiler → C2 Compiler → Native Code
          (startup)     (client)      (server)      (optimized)
```

### Object Header Layout

```
┌─────────────────────────────────────┐
│         Object Header               │
├─────────────────────────────────────┤
│  Mark Word (64-bit on 64-bit JVM)   │
│  ┌─────────────────────────────┐    │
│  │ Hash code, GC age, lock     │    │
│  └─────────────────────────────┘    │
│  Klass Pointer                     │
│  ┌─────────────────────────────┐    │
│  │ Pointer to class metadata   │    │
│  └─────────────────────────────┘    │
│  [Padding for alignment]           │
└─────────────────────────────────────┘
```

## Syntax

```java
// JVM diagnostic commands
// jps - list Java processes
// jstat -gc <pid> - GC statistics
// jmap -heap <pid> - heap dump
// jstack <pid> - thread dump
// jcmd <pid> VM.flags - JVM flags

// Programmatic access
long heapSize = Runtime.getRuntime().totalMemory();
long heapFree = Runtime.getRuntime().freeMemory();
long maxHeap = Runtime.getRuntime().maxMemory();

int cores = Runtime.getRuntime().availableProcessors();
```

## Examples

### Easy: Memory Monitoring
```java
public class MemoryMonitor {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        
        System.out.println("Max Memory: " + runtime.maxMemory() / 1024 / 1024 + " MB");
        System.out.println("Total Memory: " + runtime.totalMemory() / 1024 / 1024 + " MB");
        System.out.println("Free Memory: " + runtime.freeMemory() / 1024 / 1024 + " MB");
        
        // Force GC and measure
        System.gc();
        System.out.println("After GC:");
        System.out.println("Free Memory: " + runtime.freeMemory() / 1024 / 1024 + " MB");
    }
}
```

### Medium: Object Size Analysis
```java
import java.lang.instrument.Instrumentation;

public class ObjectSize {
    private static Instrumentation instrumentation;
    
    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }
    
    public static long getObjectSize(Object obj) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialized");
        }
        return instrumentation.getObjectSize(obj);
    }
}
```

### Hard: GC Analysis
```java
public class GCAnalysis {
    public static void main(String[] args) {
        // Run with: -verbose:gc -Xlog:gc*
        // Observe GC pause times and frequency
        
        for (int i = 0; i < 100000; i++) {
            // Allocate objects
            byte[] data = new byte[1024];
        }
        
        // Monitor with: jstat -gc <pid> 1000
        // Look for: YGC, YGCT, FGC, FGCT
    }
}
```

### Enterprise: JVM Tuning
```java
// Production JVM flags
// java -Xms4g -Xmx4g \
//      -XX:+UseG1GC \
//      -XX:MaxGCPauseMillis=200 \
//      -XX:G1HeapRegionSize=16m \
//      -XX:+HeapDumpOnOutOfMemoryError \
//      -XX:HeapDumpPath=/tmp/heapdump.hprof \
//      -verbose:gc -Xlog:gc* \
//      -jar app.jar

public class JVMTuning {
    public static void main(String[] args) {
        // Monitor GC behavior
        // Use JFR for detailed profiling
        // Use async-profiler for flame graphs
    }
}
```

### Production: JFR Monitoring Example
```java
import jdk.jfr.*;

@Name("com.example.RequestEvent")
@Label("HTTP Request")
@StackTrace(true)
public class RequestEvent extends Event {
    @Label("URL")
    String url;
    
    @Label("Duration")
    @Timespan
    long duration;
    
    @Label("Status Code")
    int statusCode;
}

public class JFRMonitoring {
    public static void main(String[] args) throws Exception {
        Recording recording = new Recording();
        recording.enable(RequestEvent.class);
        recording.start();
        
        // Simulate requests
        for (int i = 0; i < 100; i++) {
            RequestEvent event = new RequestEvent();
            event.url = "/api/user/" + i;
            event.begin();
            Thread.sleep(10);
            event.end();
            event.commit();
        }
        
        recording.stop();
        recording.dump(Paths.get("recording.jfr"));
    }
}
```

### Advanced: Memory Leak Detection
```java
import java.util.*;
import java.lang.ref.*;

public class MemoryLeakDetector {
    private static Map<String, WeakReference<Object>> references = new HashMap<>();
    
    public static void track(String name, Object obj) {
        references.put(name, new WeakReference<>(obj));
    }
    
    public static void reportLeaks() {
        System.out.println("=== Memory Leak Report ===");
        for (Map.Entry<String, WeakReference<Object>> entry : references.entrySet()) {
            WeakReference<Object> ref = entry.getValue();
            if (ref.get() != null) {
                System.out.println("LEAK: " + entry.getKey() + " is still alive");
            } else {
                System.out.println("OK: " + entry.getKey() + " was collected");
            }
        }
    }
    
    public static void main(String[] args) {
        // Track objects
        byte[] data = new byte[1024 * 1024]; // 1MB
        track("largeArray", data);
        
        // Force GC
        System.gc();
        Thread.sleep(100);
        
        // Check for leaks
        reportLeaks();
    }
}
```

### Production: GC Tuning Simulator
```java
public class GCTuningSimulator {
    private static final int OBJECT_SIZE = 1024;
    private static final int ALLOCATIONS_PER_SECOND = 10000;
    
    public static void simulateG1GC() {
        System.out.println("=== G1GC Simulation ===");
        System.out.println("Region size: 16MB");
        System.out.println("Heap: 4GB");
        System.out.println("Max pause: 200ms");
        
        // Simulate allocation
        List<byte[]> objects = new ArrayList<>();
        for (int i = 0; i < ALLOCATIONS_PER_SECOND; i++) {
            objects.add(new byte[OBJECT_SIZE]);
            
            // Simulate GC every 1000 objects
            if (i % 1000 == 0) {
                System.gc();
                System.out.println("GC triggered at allocation " + i);
            }
        }
    }
    
    public static void simulateZGC() {
        System.out.println("=== ZGC Simulation ===");
        System.out.println("Heap: 16GB");
        System.out.println("Max pause: 10ms");
        
        // ZGC uses colored pointers and load barriers
        // Simulates concurrent marking and relocation
        List<byte[]> objects = new ArrayList<>();
        for (int i = 0; i < ALLOCATIONS_PER_SECOND * 10; i++) {
            objects.add(new byte[OBJECT_SIZE]);
        }
    }
}
```

## Performance Considerations

| Operation | Cost | Notes |
|-----------|------|-------|
| Object allocation | ~10ns | TLAB allocation, very fast |
| GC pause (G1) | ~100ms | Depends on heap size |
| GC pause (ZGC) | ~10ms | Sub-millisecond target |
| JIT compilation | ~100ms | Background compilation |
| Class loading | ~1ms | First-time loading |

## Best Practices

**Do's:**
- Set `-Xms` and `-Xmx` to same value (avoid heap resizing)
- Use G1GC for most workloads
- Enable GC logging in production
- Use JFR for continuous profiling
- Monitor heap usage and GC frequency
- Set `-XX:+HeapDumpOnOutOfMemoryError`

**Don'ts:**
- Don't set heap larger than container memory
- Don't use CMS (deprecated in Java 9, removed in Java 14)
- Don't ignore GC pauses in latency-sensitive apps
- Don't use `System.gc()` to force garbage collection
- Don't disable GC verification in production

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Setting heap > container memory | OOM killer terminates JVM | Set `-Xmx` ≤ container memory - 20% |
| Using CMS in production | Deprecated, removed in Java 14 | Use G1GC or ZGC |
| Ignoring GC logs | Cannot diagnose performance | Enable GC logging |
| Not monitoring heap | Memory leaks undetected | Use JFR, VisualVM |
| Forgetting Metaspace | Class metadata leak | Monitor Metaspace usage |

## Interview Questions

### Q1: What is the difference between JVM, JRE, and JDK?
**Answer:** JVM (Java Virtual Machine) executes bytecode. JRE (Java Runtime Environment) = JVM + class libraries. JDK (Java Development Kit) = JRE + development tools (compiler, debugger).

### Q2: What is JIT compilation and why is it important?
**Answer:** JIT (Just-In-Time) compilation converts bytecode to native machine code at runtime. It identifies hot methods and optimizes them, providing near-native performance while maintaining platform independence.

### Q3: What is the difference between G1 and ZGC?
**Answer:** G1 divides heap into regions, targets ~200ms pauses. ZGC targets sub-millisecond pauses regardless of heap size. ZGC is better for large heaps (10GB+) and low-latency requirements.

### Q4: What is class loading delegation?
**Answer:** When a class is requested, the class loader delegates to its parent first. Only if the parent cannot find the class does the child attempt loading. This prevents duplicate loading and ensures core classes are loaded by the bootstrap loader.

### Q5: What is Metaspace and how does it differ from PermGen?
**Answer:** Metaspace (Java 8+) stores class metadata in native memory (not heap). PermGen (Java 7 and below) stored metadata in a fixed-size heap area. Metaspace grows dynamically, reducing OOM errors.

### Q6: What is the object header and why does it matter?
**Answer:** The object header contains the mark word (hash code, GC age, lock state) and klass pointer (metadata). It adds 8-16 bytes per object, which impacts memory usage for small objects.

### Q7: How do you diagnose a memory leak?
**Answer:** Use `jmap -dump` to capture heap dump, analyze with Eclipse MAT or VisualVM. Look for objects with unexpected retention. Use JFR for continuous monitoring. Compare heap dumps over time.

### Q8: What is the difference between `System.gc()` and actual GC?
**Answer:** `System.gc()` is a suggestion to the JVM, not a command. The JVM may ignore it or schedule a full GC. In production, never rely on `System.gc()` — use proper tuning instead.

### Q9: What is TLAB (Thread Local Allocation Buffer)?
**Answer:** Each thread gets a private memory region for object allocation. This eliminates contention for small objects. TLABs are allocated from Eden and are very fast (~10ns per allocation).

### Q10: How does the JVM handle string interning?
**Answer:** String literals are interned in the string pool (PermGen/Metaspace). `String.intern()` adds strings to the pool. Excessive interning can cause Metaspace leaks. Use carefully in production.

### Q11: What is the difference between `-Xmx` and `-Xms`?
**Answer:** `-Xms`: initial heap size (minimum memory allocated). `-Xmx`: maximum heap size (upper limit). Set both to the same value for production to avoid resize pauses.

### Q12: What is JIT compilation and what are its tiers?
**Answer:** JIT converts bytecode to native code at runtime. Tiers: 0 (interpreter), 1-3 (C1 compiler with varying optimization), 4 (C2 compiler with full optimization). Hot methods progress through tiers.

### Q13: What is the difference between `Minor GC` and `Major GC`?
**Answer:** `Minor GC`: collects young generation (Eden + Survivor spaces), fast (~10ms). `Major GC`: collects old generation, slower (~100ms+). `Full GC`: collects entire heap + Metaspace, slowest.

### Q14: What is the `jcmd` tool?
**Answer:** A diagnostic tool for JVM management. Use for: GC runs, heap dumps, thread dumps, VM flags, native memory tracking. More powerful than individual tools (`jmap`, `jstack`).

### Q15: What is the difference between `Concurrent Mark Sweep (CMS)` and `G1GC`?
**Answer:** CMS: concurrent marking, no compaction (fragmentation risk). G1GC: regional, incremental compaction. G1GC is the default since Java 9; CMS was deprecated in Java 14.

## Cross-References

- **Previous Module:** [09 - Multithreading](../09-multithreading-&-concurrency/)
- **Next Module:** [11 - Design Patterns](../11-design-patterns/)
- **Related:** [00 - Knowledge Atoms](../00-knowledge-atoms/) — memory model, object layout
- **Related:** [15 - Senior](../15-senior/) — performance engineering

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| OutOfMemoryError | Heap dump (`jmap -dump`) | Analyze with MAT: find dominator tree |
| High CPU | Thread dump (`jstack`) | Find busy threads, analyze stack traces |
| GC pauses | JFR + GC logs | Monitor pause times, frequency, cause |
| Class loading issue | `-verbose:class` | Track class loading and unloading |
| Native memory leak | `jcmd VM.native_memory` | Track native memory allocations |

## Code Review Checklist

- [ ] JVM flags set appropriately for workload
- [ ] GC logging enabled
- [ ] Heap dump on OOM enabled
- [ ] Metaspace limits configured
- [ ] Container memory limits respected
- [ ] No `System.gc()` calls
- [ ] String interning used carefully

## Architecture Considerations

JVM internals are the foundation of Java application performance. At scale, GC algorithm choice, heap sizing, and JIT compilation directly impact throughput and latency. For microservices, JVM tuning per container prevents resource contention. For cloud-native apps, understanding JVM memory model enables proper container sizing.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| G1GC | General purpose | Pros: Balanced, predictable; Cons: Not lowest latency |
| ZGC | Low latency, large heap | Pros: Sub-ms pauses; Cons: Higher CPU usage |
| Shenandoah | Low latency | Pros: Concurrent compaction; Cons: CPU overhead |
| ZGC + Generational | Most workloads (Java 21+) | Pros: Best of both; Cons: Requires Java 21+ |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Bytecode verification bypass | Arbitrary code execution | Enable verification, use SecurityManager |
| Native method access | System-level attacks | Restrict JNI usage |
| Heap dump contains sensitive data | Information exposure | Secure heap dump files |
| JMX remote access | Unauthorized monitoring | Authenticate JMX connections |
| Class loading vulnerability | Remote code execution | Restrict custom class loaders |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0 | Basic JVM | N/A — foundational |
| Java 1.3 | HotSpot JVM | Use HotSpot for JIT |
| Java 8 | Metaspace replaces PermGen | Monitor Metaspace instead of PermGen |
| Java 9 | G1GC default | Review GC configuration |
| Java 11 | ZGC (experimental) | Test for latency-sensitive apps |
| Java 17 | ZGC production-ready | Use ZGC for large heaps |
| Java 21 | Generational ZGC | Use for most workloads |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| G1GC | Java 7 | Stable |
| ZGC | Java 17 | Stable |
| Shenandoah | Java 12 | Stable |
| Generational ZGC | Java 21 | Stable |
| Application CDS | Java 12 | Stable |
| JFR | Java 11 | Stable |

## Production Incidents

### Incident 1: GC Pause Causing Request Timeout

**Problem:** A high-throughput API showed intermittent 5-second response times under load.
**Cause:** G1GC was collecting the entire heap (4GB) in a single pause; old generation was too full.
**Impact:** 5% of requests timed out; SLA violations; customer complaints.
**Detection:** Monitoring showed 5-second GC pauses every 30 seconds.
**Solution:** Increased heap to 8GB; tuned G1GC region size; added `-XX:MaxGCPauseMillis=200`.
**Prevention:** Monitor GC pauses; tune heap size for workload; use ZGC for low latency.

### Incident 2: Metaspace Leak Causing OOM

**Problem:** Application crashed with `OutOfMemoryError: Metaspace` after running for 72 hours.
**Cause:** Dynamic class loading (reflection, Groovy scripts) created classes that were never unloaded.
**Impact:** Application crash every 72 hours; required restart.
**Detection:** OOM error in logs; `jstat -gc` showed Metaspace growing.
**Solution:** Increased `-XX:MaxMetaspaceSize`; fixed class loading leak; added Metaspace monitoring.
**Prevention:** Monitor Metaspace; limit dynamic class loading; add class loading quotas.

### Incident 3: Container OOM Kill Due to Heap Sizing

**Problem:** Kubernetes pod was OOM-killed every few hours in production.
**Cause:** JVM heap was set to 8GB but container had 4GB memory limit.
**Impact:** Pod restarts every few hours; service degradation; data loss.
**Detection:** Kubernetes events showed OOM kill; JVM logs showed heap at 8GB.
**Solution:** Set `-Xmx3g` (container limit - 1GB for overhead); enabled `-XX:+UseContainerSupport`.
**Prevention:** Always set heap ≤ container memory; use container-aware JVM flags.

### Incident 4: JIT Compilation Causing Performance Regression

**Problem:** Application performance degraded after upgrading from Java 11 to Java 17.
**Cause:** JIT compiler in Java 17 had different optimization heuristics; some hot methods were not compiled.
**Impact:** 20% throughput drop; response times increased.
**Detection:** JFR showed fewer compiled methods; profiling revealed interpreter overhead.
**Solution:** Added `-XX:CompileCommand` to force compilation of critical methods; tuned `-XX:ReservedCodeCacheSize`.
**Prevention:** Benchmark before/after upgrades; monitor JIT compilation; use `-XX:+PrintCompilation` for debugging.

### Incident 5: String Deduplication Causing Memory Issues

**Problem:** Application using G1GC with string deduplication showed increased CPU usage.
**Cause:** String deduplication added overhead for short-lived strings; frequent GC cycles.
**Impact:** 15% CPU increase; GC cycles took longer.
**Detection:** CPU profiling showed G1GC deduplication overhead; GC logs showed longer pauses.
**Solution:** Disabled `-XX:+UseStringDeduplication` for short-lived strings; tuned `-XX:G1PeriodicGCInterval`.
**Prevention:** Benchmark string deduplication impact; monitor GC overhead; use JFR to analyze string usage.

## Production Checklist

- [ ] JVM flags set appropriately for workload
- [ ] GC logging enabled
- [ ] Heap dump on OOM enabled
- [ ] Metaspace limits configured
- [ ] Container memory limits respected
- [ ] No `System.gc()` calls
- [ ] String interning used carefully
- [ ] GC pauses monitored
- [ ] Memory leaks detected early
- [ ] JIT compilation verified

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses default JVM settings; doesn't understand GC; ignores GC logs |
| Intermediate | Tunes heap size; uses G1GC; reads GC logs |
| Advanced | Profiles with JFR; tunes GC for workload; diagnoses memory leaks |
| Expert | Designs JVM tuning strategy; contributes to JVM; mentors on internals |

## Common Myths

1. **Myth**: Bigger heap is always better
   **Truth**: Larger heap increases GC pause times. For low-latency apps, smaller heap + ZGC may be better.

2. **Myth**: `System.gc()` forces garbage collection
   **Truth**: It's a suggestion to the JVM, not a command. The JVM may ignore it.

3. **Myth**: GC is the enemy
   **Truth**: GC is essential for memory safety. Well-tuned GC has minimal impact on throughput.

4. **Myth**: JIT compilation always improves performance
   **Truth**: JIT compilation itself has a cost. For short-lived apps, interpretation may be faster.

5. **Myth**: Metaspace is unlimited
   **Thought**: Metaspace grows dynamically but can still leak if classes are loaded excessively.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Execute Java bytecode |
| Architecture | Class Loader + Runtime Data Areas + Execution Engine |
| Memory | Heap (Young/Old) + Metaspace + Stack |
| GC | G1GC (general), ZGC (low latency), Shenandoah (low latency) |
| JIT | Compiles hot bytecode to native code |
| Tools | VisualVM, JFR, async-profiler, jcmd, jstack |
| Best practice | Set heap = container size; enable GC logging |
| Common mistake | Heap > container memory |
| When to use | All Java applications |
| When to avoid | Never — JVM is essential |
