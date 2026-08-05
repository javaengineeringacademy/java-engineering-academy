# Java Internals

## JVM Architecture

The Java Virtual Machine is a specification and runtime environment for executing Java bytecode. The JVM loads `.class` files, verifies bytecode integrity, and executes instructions on the host system. The HotSpot JVM (OpenJDK default) uses a mixed-mode execution: initially interprets bytecode, then JIT-compiles hot methods to native code. The JIT compiler profiles method execution and optimizes frequently executed paths.

The JVM runtime data area consists of the heap (object storage), method area (class metadata), stack (per-thread execution), program counter (current instruction), and native method stack. Each thread gets its own stack and PC register. The heap is shared across all threads. The JVM specification defines the instruction set and memory model.

The JVM initializes by loading the bootstrap class loader, which loads core Java classes. The class loading process involves loading, linking (verification, preparation, resolution), and initialization. The JVM uses a class loading delegation model where child class loaders delegate to parents before attempting to load classes themselves.

The JVM uses memory-mapped files for class loading. The `mmap()` system call maps class files into memory. The JVM uses copy-on-write for shared class metadata. The class data sharing (CDS) feature pre-processes class files for faster startup. The JVM supports compressed oops (ordinary object pointers) for heaps up to 32GB.

## ClassLoader Subsystem

The ClassLoader subsystem loads classes from the file system, network, or other sources. The bootstrap class loader loads core Java classes (rt.jar). The extension class loader loads classes from the ext directory. The application class loader loads classes from the classpath. Child class loaders delegate to parent class loaders before attempting to load themselves, ensuring core classes cannot be replaced.

Custom class loaders override `findClass()` to load classes from non-standard sources (databases, encrypted files). The `defineClass()` method converts byte arrays to Class objects. Class loaders are key to application servers (hot deployment), OSGi (module systems), and instrumentation agents (bytecode modification). Each class loader has its own namespace, so the same class loaded by different loaders produces distinct Class objects.

The ClassLoader uses lazy loading: classes are loaded on first use. The `Class.forName()` method triggers class loading. The `Class.forName(String, boolean, ClassLoader)` method controls initialization. Class loading can be monitored using `-verbose:class` JVM flag. The `ClassNotFoundException` and `NoClassDefFoundError` indicate loading failures.

The class loading process has three phases: loading (finding the byte array), linking (verification, preparation, resolution), and initialization (executing static initializers). The verification phase checks bytecode safety. The preparation phase allocates memory for static fields. The resolution phase replaces symbolic references with direct references.

## Java Memory Model

The JMM defines rules for visibility and ordering of memory operations across threads. Variables are stored in main memory; threads maintain working copies. The happens-before relationship establishes ordering guarantees: a write by one thread happens-before a read by another thread if certain conditions are met (volatile write, synchronized block, Thread.start()).

The volatile keyword ensures visibility: writes to volatile variables are immediately flushed to main memory, and reads fetch from main memory. It does not provide atomicity for compound operations (read-modify-write). The synchronized keyword provides both visibility and atomicity by establishing a happens-before edge at monitor exit/entry. Java 5+ introduced java.util.concurrent locks (ReentrantLock) with equivalent semantics.

The `java.util.concurrent.atomic` package provides lock-free atomic operations using CAS (Compare-And-Swap). AtomicReference, AtomicInteger, and AtomicLong provide atomic read-modify-write operations. The `Unsafe` class provides low-level memory operations. The `VarHandle` API (Java 9+) provides volatile access semantics for fields.

The JMM defines memory barriers for hardware-level ordering. Load barriers ensure reads are visible to other processors. Store barriers ensure writes are visible to other processors. The `StoreLoad` barrier is the most expensive, preventing reordering of stores and loads. The JVM uses memory barriers to implement volatile and synchronized semantics.

## Garbage Collection Algorithms

Serial GC uses a single thread for collection. It is stop-the-world: all application threads pause during collection. The young generation uses copying; the old generation uses mark-sweep-compact. Serial GC is suitable for single-CPU machines and small heaps. It is enabled with `-XX:+UseSerialGC`.

Parallel GC (Throughput GC) uses multiple threads for young generation collection. It achieves high throughput by minimizing GC pause time relative to application runtime. G1 GC (Garbage-First) divides the heap into regions. It prioritizes collecting regions with the most garbage first. ZGC is a concurrent, low-latency collector that pauses application threads for sub-millisecond intervals. Shenandoah is similar to ZGC with concurrent compaction.

The G1 GC uses a predictable pause-time model. The `-XX:MaxGCPauseMillis` flag targets maximum pause times. ZGC uses colored pointers and load barriers for concurrent operations. Shenandoah uses Brooks pointers for concurrent compaction. Both ZGC and Shenandoah are designed for heaps up to 16TB.

The young generation is divided into Eden and Survivor spaces. New objects are allocated in Eden. Surviving objects are copied to Survivor spaces after minor GC. Objects surviving multiple minor GC cycles are promoted to the old generation. The `-XX:MaxTenuringThreshold` flag controls promotion age. The `-XX:PretenureSizeThreshold` flag sets the threshold for direct old generation allocation.

## HotSpot JIT Compilation

HotSpot uses two JIT compilers: C1 (client) and C2 (server). C1 compiles quickly with minimal optimization; C2 performs aggressive optimizations (inlining, loop unrolling, escape analysis). Tiered compilation uses C1 first, then upgrades to C2 after sufficient profiling. The `-XX:+TieredCompilation` flag enables tiered compilation.

The JIT compiler performs speculative optimizations based on profiling data. If assumptions are violated (e.g., a virtual method call site monomorphically), the compiled code is deoptimized and execution falls back to interpretation. The JIT maintains a code cache for compiled methods. Method inlining eliminates virtual call overhead by replacing virtual calls with direct calls when the receiver type is monomorphic.

The code cache is divided into segments: non-profiled (optimized code), profiled (speculatively optimized), and archived (saved for future use). The `-XX:ReservedCodeCacheSize` flag configures code cache size. Code cache flushing occurs when the cache is full. The `-XX:+PrintCompilation` flag logs JIT compilation events.

The JIT compiler uses escape analysis to determine if objects can be allocated on the stack instead of the heap. Scalar replacement replaces object allocations with individual field allocations. Loop unrolling reduces loop overhead by replicating loop body. Intrinsics replace method calls with optimized native code. The `-XX:+PrintInlining` flag logs inlining decisions.

## Bytecode Execution

Java bytecode is a stack-based instruction set. Each instruction pushes/pops values on an operand stack. The JVM executes instructions sequentially. Branch instructions modify the program counter. Method invocation uses `invokevirtual`, `invokeinterface`, `invokestatic`, and `invokespecial`. The `invokedynamic` instruction (Java 7+) supports dynamic languages and lambda expressions.

Bytecode verification checks type safety, stack consistency, and access control before execution. The JVM's verifier prevents common security vulnerabilities: stack overflow, invalid casts, and unauthorized field access. Bytecode instrumentation agents can modify class files at load time, enabling APM tools, logging frameworks, and code coverage analysis.

The JVM supports multiple bytecode formats: class files, JAR files, and modules (Java 9+). The `javap` command disassembles class files to readable bytecode. The ASM library provides programmatic bytecode manipulation. Bytecode engineering enables dynamic proxy generation, AOP frameworks, and runtime code generation.

The JVM uses just-in-time compilation to convert hot bytecode to native code. The interpreter handles cold code. The JIT compiler uses profiling data to identify hot methods. Method entry and exit hooks collect profiling data. The JIT compiler compiles methods based on invocation count and loop back-edge count. The `-XX:CompileThreshold` flag controls compilation thresholds.

## JVM Memory Management

Heap memory is divided into generations. The young generation contains recently created objects and is collected frequently. The old generation contains long-lived objects. The permanent generation (removed in Java 8) stored class metadata; metaspace stores it in native memory. The `-XX:MaxMetaspaceSize` flag limits metaspace size.

Thread-local allocation buffers (TLABs) provide each thread with a private heap region for fast allocation. When a TLAB is full, the thread allocates from a shared Eden space. Object allocation typically involves only a pointer bump in TLAB, making it extremely fast. The JVM periodically triggers minor GC to reclaim young generation space and promote surviving objects to the old generation.

The JVM uses a generational hypothesis: most objects die young. Objects surviving multiple minor GC cycles are promoted to the old generation. The `-XX:MaxTenuringThreshold` flag controls promotion age. The `-XX:NewRatio` flag sets the old-to-young generation ratio. The `-XX:SurvivorRatio` flag sets the Eden-to-Survivor ratio.

The JVM supports large pages for heap allocation. Large pages reduce TLB misses and improve performance for large heaps. The `-XX:+UseLargePages` flag enables large pages. The `-XX:+UseTransparentHugePages` flag enables transparent huge pages. The `jcmd GC.heap_info` command shows heap configuration.

## JVM Flags and Tuning

JVM flags control runtime behavior. `-Xms` and `-Xmx` set initial and maximum heap size. `-XX:NewSize` and `-XX:MaxNewSize` configure young generation size. `-XX:+UseG1GC` enables G1 garbage collector. `-XX:+UseZGC` enables ZGC garbage collector. The `-XX:+PrintGCDetails` flag logs garbage collection details.

Diagnostic flags include `-XX:+HeapDumpOnOutOfMemoryError` for automatic heap dumps. The `-XX:HeapDumpPath` flag specifies dump location. The `-XX:+PrintFlagsFinal` flag lists all JVM flags. The `-XX:+UnlockDiagnosticVMOptions` flag enables experimental flags. The JHSDB tool provides runtime JVM inspection.

Performance monitoring uses JMX (Java Management Extensions). The `jstat` command monitors garbage collection. The `jmap` command generates heap dumps. The `jstack` command generates thread dumps. The `jcmd` command provides unified diagnostic commands. The VisualVM and JConsole tools provide graphical monitoring.

JVM flags are categorized into product flags, diagnostic flags, and experimental flags. Product flags are stable and supported. Diagnostic flags are for debugging. Experimental flags require `-XX:+UnlockExperimentalVMOptions`. The `java -XX:+PrintFlagsFinal -version` command lists all flags with their values. The `jinfo` command modifies flags at runtime.
