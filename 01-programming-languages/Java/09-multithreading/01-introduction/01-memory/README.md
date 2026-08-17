# Introduction to Multithreading - Memory Model

## Thread Memory Layout in JVM

### Shared vs Thread-Specific Memory

```
JVM Process Memory Space:
┌─────────────────────────────────────────────────────┐
│                                                     │
│  HEAP (shared by all threads)                       │
│  ┌─────────────────────────────────────────────┐    │
│  │ Objects created with 'new'                  │    │
│  │ Arrays                                     │    │
│  │ Class static variables                     │    │
│  │ String constant pool                       │    │
│  └─────────────────────────────────────────────┘    │
│                                                     │
│  METHOD AREA (shared)                               │
│  ┌─────────────────────────────────────────────┐    │
│  │ Class metadata                             │    │
│  │ Bytecode                                   │    │
│  │ Constant pool                              │    │
│  └─────────────────────────────────────────────┘    │
│                                                     │
│  THREAD 1 STACK    THREAD 2 STACK    THREAD 3 STACK │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────┐  │
│  │ Local vars   │  │ Local vars   │  │ Local vars│  │
│  │ Method frames│  │ Method frames│  │ Frames    │  │
│  │ PC Register  │  │ PC Register  │  │ PC Reg    │  │
│  └──────────────┘  └──────────────┘  └───────────┘  │
│                                                     │
│  NATIVE MEMORY                                      │
│  ┌─────────────────────────────────────────────┐    │
│  │ Thread Control Blocks (TCBs)                │    │
│  │ JIT compiled code                           │    │
│  │ Direct byte buffers                         │    │
│  └─────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────┘
```

### What Each Thread Has Privately
- **Stack**: Local variables, method parameters, return addresses
- **Program Counter (PC)**: Points to current bytecode instruction
- **Thread-local storage**: Via `ThreadLocal<T>` class
- **CPU registers**: During execution (saved during context switch)

### What Threads Share
- **Heap**: All objects and arrays
- **Method area**: Class metadata, bytecode
- **Static variables**: Class-level state
- **Synchronized locks**: Monitor objects

### Memory Visibility Problem

Without synchronization, threads may see stale values:

```java
// Thread 1                    // Thread 2
sharedVar = 42;               while (sharedVar != 42) {
// CPU may cache 42               // May never see 42!
// in register or L1            // Stuck in infinite loop
// cache
```

The Java Memory Model (JMM) defines when writes by one thread become visible to reads by other threads.
