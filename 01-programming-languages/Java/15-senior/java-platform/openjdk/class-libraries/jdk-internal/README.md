# JDK Internal APIs

JDK internal APIs (`jdk.internal.*`, `sun.misc.*`) are implementation details not part of the Java SE specification. They may change or be removed between versions without notice.

## sun.misc.Unsafe

The most well-known internal API. Provides low-level memory operations:

```java
import sun.misc.Unsafe;

Unsafe unsafe = Unsafe.getUnsafe(); // or via reflection

// Allocate memory
long addr = unsafe.allocateMemory(100);

// Read/write memory
unsafe.putByte(addr, (byte) 42);
byte b = unsafe.getByte(addr);

// Object field offsets
long offset = unsafe.objectFieldOffset(
    MyClass.class.getDeclaredField("value"));
unsafe.getObject(obj, offset);
unsafe.putObject(obj, offset, newValue);

// Compare and swap (atomic)
unsafe.compareAndSwapInt(obj, offset, expected, update);

// Free memory
unsafe.freeMemory(addr);
```

### What Unsafe Enables

- Lock-free and wait-free data structures
- Off-heap memory allocation
- Direct memory access
- Object layout manipulation
- Atomic operations without reflection

### Migration Path

Java is migrating from `Unsafe` to safer alternatives:

| Unsafe Method | Replacement |
|---------------|-------------|
| `allocateMemory` | `java.lang.foreign.MemorySegment` (Java 22+) |
| `objectFieldOffset` | `MethodHandles.Lookup` |
| `compareAndSwapInt` | `VarHandle.compareAndSet()` |
| `park/unpark` | `LockSupport.park()/unpark()` |
| `defineClass` | `MethodHandles.Lookup.defineClass()` |

## jdk.internal.misc.Unsafe

The internal replacement for `sun.misc.Unsafe`:

```java
import jdk.internal.misc.Unsafe;

// Same functionality, but internal
Unsafe.getUnsafe().compareAndSetInt(obj, offset, expected, update);
```

## jdk.internal.reflect

Internal reflection implementation:

```java
jdk.internal.reflect.ReflectionFactory rf =
    jdk.internal.reflect.ReflectionFactory.getReflectionFactory();
```

Used internally by `java.lang.reflect` to create instances without constructors.

## jdk.internal.vm.compiler

Graal compiler internals (when using experimental JVMCI in HotSpot):

```java
// Internal Graal APIs
org.graalvm.compiler.core.CompilerConfiguration
org.graalvm.compiler.hotspot.HotSpotGraalRuntime
```

## jdk.internal.loader

Class loader internals:

```java
// Module class loader
jdk.internal.loader.ClassLoaders.bootClassLoader();
jdk.internal.loader.ClassLoaders.platformClassLoader();
jdk.internal.loader.ClassLoaders.appClassLoader();
```

## jdk.internal.util

Internal utility classes:

```java
// Bit manipulation
jdk.internal.util.Bits

// Native encoding detection
jdk.internal.util.UTF128
```

## Module Access

Internal APIs are accessible to JDK modules but not to application code by default. To access them from application code:

```bash
# Add --add-opens (not recommended for production)
java --add-opens java.base/sun.misc=ALL-UNNAMED MyApp

# Add --add-exports
java --add-exports java.base/jdk.internal.misc=ALL-UNNAMED MyApp
```

## Warnings

```java
@Deprecated(since="17", forRemoval=true)
// Accessing internal APIs triggers compiler warnings
```

Since Java 9, importing `sun.misc.*` or `jdk.internal.*` generates deprecation warnings and may fail in future versions.

## Key Source Files

| Path | Contents |
|------|----------|
| `src/jdk.unsupported/share/classes/sun/misc/` | Unsafe |
| `src/java.base/share/classes/jdk/internal/misc/` | Internal Unsafe |
| `src/java.base/share/classes/jdk/internal/reflect/` | Reflection internals |
| `src/java.base/share/classes/jdk/internal/loader/` | Class loading internals |
| `src/java.base/share/classes/jdk/internal/util/` | Internal utilities |

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
