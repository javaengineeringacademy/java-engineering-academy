# Java Architecture

> JVM architecture, class loading, memory model, garbage collection, and JIT compilation.

## JVM Architecture Overview

```mermaid
graph TB
    A[Source Code .java] --> B[Java Compiler javac]
    B --> C[Bytecode .class]
    C --> D[ClassLoader Subsystem]
    D --> E[Runtime Data Areas]
    E --> F[Execution Engine]
    F --> G[Native Method Interface JNI]
    
    subgraph "Runtime Data Areas"
        E1[Method Area]
        E2[Heap]
        E3[Stack - per thread]
        E4[PC Registers - per thread]
        E5[Native Method Stack - per thread]
    end
    
    subgraph "Execution Engine"
        F1[Interpreter]
        F2[JIT Compiler]
        F3[Garbage Collector]
    end
```

## Class Loading Subsystem

```mermaid
graph LR
    A[Bootstrap ClassLoader] --> B[Extension/Platform ClassLoader]
    B --> C[Application ClassLoader]
    C --> D[Custom ClassLoaders]
    
    A --> E[java.lang.Object]
    A --> F[java.lang.String]
    B --> G[jdk.internal.*]
    C --> H[App classes on classpath]
    D --> I[Plugin classes]
```

### Loading Phases

| Phase | Description | Example |
|-------|-------------|---------|
| Loading | Reads .class file bytes into memory | `Class.forName()` |
| Linking | Verification, preparation, resolution | Static memory allocation |
| Initialization | Executes static blocks, assigns values | `<clinit>` method |

### Three ClassLoaders

| Loader | Loads From | Visibility |
|--------|-----------|------------|
| Bootstrap | `JAVA_HOME/lib` (rt.jar, jrt) | Core classes only |
| Extension | `JAVA_HOME/lib/ext` | Platform modules |
| Application | Application classpath | User classes |

```java
// Custom ClassLoader
public class CustomClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = loadClassBytes(name);
        if (bytes == null) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }
}
```

## JVM Memory Model

```mermaid
graph TB
    subgraph "Heap Memory"
        A[Young Generation]
        A1[Eden Space - 80%]
        A2[Survivor S0 - 10%]
        A3[Survivor S1 - 10%]
        B[Old Generation / Tenured]
        C[Optional: Metaspace - off-heap]
    end
    
    subgraph "Non-Heap Memory"
        D[Method Area / Metaspace]
        E[Code Cache]
        F[Direct ByteBuffers]
    end
    
    subgraph "Per-Thread Memory"
        G[Thread Stack]
        G1[Local Variables]
        G2[Operand Stack]
        G3[Frame Data]
        H[PC Register]
        I[Native Method Stack]
    end
```

### Memory Size Defaults

| Area | Default | Configuration |
|------|---------|--------------|
| Heap (initial) | 256 MB | `-Xms` |
| Heap (max) | 25% of RAM or 1 GB | `-Xmx` |
| Young Gen | 1/3 of heap | `-Xmn` |
| Metaspace | 256 MB | `-XX:MaxMetaspaceSize` |
| Thread Stack | 512 KB - 1 MB | `-Xss` |
| Code Cache | 240 MB | `-XX:ReservedCodeCacheSize` |

## Garbage Collection Zones

```mermaid
graph TB
    A[Object Creation in Eden] --> B{Minor GC Triggered?}
    B -->|Yes| C[Mark Live Objects]
    C --> D[Copy to Survivor]
    D --> E{Age > Threshold?}
    E -->|No| F[Remain in Survivor]
    E -->|Yes| G[Promote to Old Gen]
    B -->|No| H[Keep in Eden]
    G --> I{Old Gen Full?}
    I -->|Yes| J[Major/Full GC]
    J --> K[Mark-Compact or Sweep]
```

### GC Algorithms

| Algorithm | Type | Use Case |
|-----------|------|----------|
| Serial GC | Single-threaded | Small apps, single CPU |
| Parallel GC | Multi-threaded throughput | Batch processing |
| G1 GC | Region-based (default since Java 9) | Balanced latency/throughput |
| ZGC | Ultra-low latency (<10ms) | Large heaps, latency-critical |
| Shenandoah | Concurrent low-pause | Low-latency apps |
| Epsilon | No-op GC | Testing, short-lived apps |

```bash
# GC Selection
java -XX:+UseG1GC -jar app.jar          # G1 (default)
java -XX:+UseZGC -jar app.jar            # ZGC
java -XX:+UseShenandoahGC -jar app.jar   # Shenandoah
java -XX:+UseSerialGC -jar app.jar       # Serial
java -XX:+UseParallelGC -jar app.jar     # Parallel
```

### G1 GC Regions

```mermaid
graph TB
    subgraph "G1 Heap - Region Based"
        A[Eden Region]
        B[Survivor Region]
        C[Old Region]
        D[Humongous Region - large objects]
        E[Free Region]
    end
```

## JIT Compilation

```mermaid
graph LR
    A[Bytecode] --> B[Interpreter]
    B --> C{Hot Spot Detected?}
    C -->|No| D[Continue Interpreting]
    C -->|Yes| E[C1 Compiler - Client]
    E --> F[C2 Compiler - Server]
    F --> G[Optimized Native Code]
    G --> H[Code Cache]
```

### JIT Optimization Techniques

| Technique | Description |
|-----------|-------------|
| Method Inlining | Replace call with method body |
| Escape Analysis | Determine if object escapes method |
| Loop Unrolling | Reduce loop overhead |
| Dead Code Elimination | Remove unreachable code |
| Intrinsics | Replace method with optimized assembly |
| On-Stack Replacement | Optimize running code in loop |

### JIT Thresholds

| Flag | Default | Description |
|------|---------|-------------|
| `-XX:CompileThreshold` | 10000 | Methods compiled after N calls |
| `-XX:+TieredCompilation` | true | Multi-tier compilation |
| `-XX:ReservedCodeCacheSize` | 240 MB | JIT code cache size |

## Thread Model

```mermaid
graph TB
    A[Java Thread] --> B[OS Thread Mapping]
    B --> C{Platform Model}
    C -->|Java <= 20| D[1:1 Mapping - Green Threads]
    C -->|Java 21+| E[Virtual Threads - Loom]
    E --> F[Many-to-Few Carrier Threads]
```

```java
// Virtual Threads (Java 21+)
Thread.startVirtualThread(() -> {
    // Runs on carrier thread pool
    handleRequest();
});

// Structured Concurrency (Preview)
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> user = scope.fork(() -> fetchUser());
    Subtask<Order> order = scope.fork(() -> fetchOrder());
    scope.join();
    return new Response(user.get(), order.get());
}
```

## Key JVM Flags Reference

| Category | Flag | Description |
|----------|------|-------------|
| Heap | `-Xms4g` | Initial heap size |
| Heap | `-Xmx4g` | Maximum heap size |
| GC | `-XX:+UseG1GC` | Select G1 garbage collector |
| GC | `-XX:MaxGCPauseMillis=200` | Target max GC pause |
| JIT | `-XX:+TieredCompilation` | Enable tiered JIT |
| Diagnostics | `-XX:+HeapDumpOnOutOfMemoryError` | Dump heap on OOM |
| Diagnostics | `-XX:HeapDumpPath=/tmp` | Heap dump location |
| Diagnostics | `-XX:+PrintGCDetails` | GC logging (Java 8) |
| Diagnostics | `-Xlog:gc*` | GC logging (Java 9+) |

## References

- [Oracle JVM Documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/)
- [OpenJDK Documentation](https://openjdk.org/projects/jdk/)
- [Inside the JVM - Bill Venners](https://www.artima.com/insidejvm2e/)

---
**Prerequisites:** [Java core-concepts](core-concepts.md)
**Related:** [Java performance](performance.md) | [Java internals](internals.md)
**Next:** [Java configuration](configuration.md)
