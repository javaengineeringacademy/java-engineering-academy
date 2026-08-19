# JVM Internals

> Understanding the Java Virtual Machine at a deep level — architecture, class loading, memory management, garbage collection, JIT compilation, profiling, and tuning.

## Why JVM Internals?

Understanding the JVM is essential for:
- **Performance tuning** — knowing how GC and JIT work lets you write faster code
- **Debugging** — understanding memory layout helps diagnose leaks and OOM errors
- **Production reliability** — tuning JVM flags prevents crashes and slowdowns
- **Architecture decisions** — choosing the right GC algorithm for your workload

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | [Introduction](01-introduction/) | JVM architecture overview |
| 02 | [ClassLoader](02-classloader/) | Class loading mechanism |
| 03 | [Class Loading](03-class-loading/) | Class loading lifecycle |
| 04 | [Memory Model](04-memory-model/) | Java Memory Model and happens-before |
| 05 | [Garbage Collection](05-garbage-collection/) | GC concepts and collectors |
| 06 | [GC Algorithms](06-gc-algorithms/) | G1, ZGC, Shenandoah, CMS |
| 07 | [JIT Compilation](07-jit-compilation/) | Just-in-time compilation and optimizations |
| 08 | [Profiling Tools](08-profiling-tools/) | VisualVM, JProfiler, async-profiler |
| 09 | [JVM Diagnostics](09-jvm-diagnostics/) | jcmd, jmap, jstack, jstat |
| 10 | [JVM Tuning](10-jvm-tuning/) | GC tuning, heap sizing, flags |
| 11 | [JVM Security](11-jvm-security/) | Security manager, bytecode verification |
| 12 | [Module System](12-module-system/) | Java 9+ module system (JPMS) |

## Learning Path

```
Introduction → ClassLoader → Class Loading → Memory Model
     ↓
Garbage Collection → GC Algorithms → JIT Compilation
     ↓
Profiling Tools → JVM Diagnostics → JVM Tuning
     ↓
JVM Security → Module System
```

## Key JVM Concepts

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
│  │                             │    │
│  │   Long-lived objects        │    │
│  │                             │    │
│  └─────────────────────────────┘    │
├─────────────────────────────────────┤
│  Metaspace (Non-heap)               │
│  ┌─────────────────────────────┐    │
│  │ Class metadata, code cache  │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

## Resources

- [Oracle JVM Documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/)
- [OpenJDK Source](https://github.com/openjdk/jdk)
- [Inside the JVM](https://blog.jooq.org/inside-the-jvm/)
- [JVM Internals](https://www.jvmhost.com/articles/jvm-internals/)
- [JVM Performance Engineering](https://www.oreilly.com/library/view/java-performance/9781492056034/)

## Contributing

See [CONTRIBUTING.md](../../CONTRIBUTING.md) for guidelines.
