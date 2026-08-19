# 01. JVM Internals Deep Dive

## JVM Architecture

The JVM consists of three main subsystems:

### 1. Class Loader Subsystem

Responsible for loading, linking, and initializing classes:

```
Bootstrap ClassLoader (C/C++, loads java.base)
    ↑ delegates to
Platform ClassLoader (Java, loads java.xml, java.sql)
    ↑ delegates to
Application ClassLoader (Java, loads classpath)
    ↑ delegates to
Custom ClassLoaders (user-defined)
```

**Loading**: Reads .class bytes from disk/network/byte array, creates `java.lang.Class` instance

**Linking**: Verification (bytecode integrity) → Preparation (allocate static fields) → Resolution (replace symbolic references)

**Initialization**: Execute static initializers (`<clinit>`) in order

### 2. Runtime Data Areas

| Area | Scope | Purpose |
|------|-------|---------|
| **Heap** | All threads | Object instances, arrays |
| **Method Area (Metaspace)** | All threads | Class metadata, method bytecode, constant pool |
| **Stack** | Per thread | Stack frames, local variables, partial results |
| **Program Counter** | Per thread | Address of current JVM instruction |
| **Native Method Stack** | Per thread | Native method calls (JNI) |

### 3. Execution Engine

- **Interpreter**: Executes bytecode line-by-line (fast startup, slow execution)
- **JIT Compiler**: Compiles hot bytecode to native code (slow startup, fast execution)
- **Garbage Collector**: Automatic memory management

## Stack Frame Structure

```
┌─────────────────────────────────┐
│         Stack Frame             │
├─────────────────────────────────┤
│  Local Variable Array           │
│  - Method parameters            │
│  - Local variables              │
├─────────────────────────────────┤
│  Operand Stack                  │
│  - Intermediate results         │
│  - Method call arguments        │
├─────────────────────────────────┤
│  Frame Data                     │
│  - Constant pool reference      │
│  - Return address               │
│  - Exception table              │
└─────────────────────────────────┘
```

## Key Takeaways

1. The JVM abstracts away platform differences through bytecode execution
2. Memory is divided into per-thread areas (Stack, PC) and shared areas (Heap, Metaspace)
3. The class loader hierarchy ensures core classes cannot be replaced by application code
4. JIT compilation provides near-native performance for hot code paths
5. Garbage collection eliminates manual memory management
