# JVM Internals - Examples Index

## Architecture & Components
| # | File | Topic | Key Concepts |
|---|------|-------|--------------|
| 1 | `JvmArchitectureDemo.java` | JVM Architecture | Runtime data areas, execution engine, native method interface |
| 2 | `ClassLoaderDemo.java` | Custom ClassLoaders | Bootstrap, platform, application, custom classloaders, delegation model |
| 3 | `ClassLoadingDemo.java` | Class Loading Lifecycle | Loading, linking, initialization, resolving, verification |

## Memory Model & Garbage Collection
| # | File | Topic | Key Concepts |
|---|------|-------|--------------|
| 4 | `MemoryModelDemo.java` | Memory Model | Happens-before, volatile, synchronized, JMM rules |
| 5 | `GarbageCollectionDemo.java` | GC Concepts | Generational model, Eden/Survivor/Old, GC roots, reachability |
| 6 | `GcAlgorithmsDemo.java` | GC Algorithms | G1, ZGC, Shenandoah, comparison, tuning flags |

## Performance & Diagnostics
| # | File | Topic | Key Concepts |
|---|------|-------|--------------|
| 7 | `JitCompilationDemo.java` | JIT Compilation | C1/C2 compilers, tiered compilation, OSR, inline caching |
| 8 | `ProfilingDemo.java` | Profiling Tools | JFR, async-profiler, JMH, flame graphs |
| 9 | `JvmDiagnosticsDemo.java` | Diagnostics Tools | jcmd, jstack, jmap, jstat, jinfo, VisualVM |
| 10 | `JvmTuningDemo.java` | JVM Tuning | Heap sizing, GC selection, tuning flags, performance metrics |

## Security & Modules
| # | File | Topic | Key Concepts |
|---|------|-------|--------------|
| 11 | `JvmSecurityDemo.java` | Security | SecurityManager, permissions, policies, classloader isolation |
| 12 | `ModuleSystemDemo.java` | JPMS | Module declarations, requires/exports, services, migration |

## Running Examples

```bash
# Compile and run any example
javac academy/javaengineering/jvm/examples/ClassName.java
java academy.javaengineering.jvm.examples.ClassName

# Run with specific JVM flags
java -XX:+PrintGCDetails academy.javaengineering.jvm.examples.GarbageCollectionDemo
java -XX:+FlightRecorder academy.javaengineering.jvm.examples.ProfilingDemo
```

## Prerequisites
- JDK 17+ (for JPMS and modern GC features)
- Understanding of Java basics and OOP concepts
- Familiarity with command-line tools
