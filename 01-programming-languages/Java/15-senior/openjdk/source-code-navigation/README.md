# Navigating the OpenJDK Source Code

This guide helps you find your way around the OpenJDK source tree, locate specific classes, and understand how the code is organized.

## Getting the Source Code

### Clone from GitHub

```bash
git clone https://github.com/openjdk/jdk.git
cd jdk
```

### Browse Online

- **GitHub**: [github.com/openjdk/jdk](https://github.com/openjdk/jdk)
- **GitHub Search**: Use the search bar to find classes, methods, or patterns
- **Cross-reference**: Click on class/method names to navigate definitions

### Specific Version

```bash
# Clone a specific JDK version
git clone --branch jdk-21+35 https://github.com/openjdk/jdk.git

# List available tags
git tag -l "jdk-*" | head -20
```

## Source Code Layout

```
jdk/
├── src/                        # All source code
│   ├── hotspot/                # HotSpot JVM (C++)
│   ├── java.base/              # Core Java APIs
│   ├── java.net/               # Networking
│   ├── java.sql/               # JDBC
│   ├── java.desktop/           # AWT, Swing
│   ├── jdk.*                   # JDK-specific modules
│   └── ...
├── make/                       # Build system
├── test/                       # Test suites
├── doc/                        # Documentation
├── .github/                    # CI configuration
└── README.md
```

## Key Directories

### src/java.base/ — Core Classes

The most frequently referenced directory:

```
src/java.base/share/classes/
├── java/lang/
│   ├── Object.java
│   ├── String.java
│   ├── System.java
│   ├── Thread.java
│   ├── Class.java
│   ├── Math.java
│   └── ...
├── java/util/
│   ├── ArrayList.java
│   ├── HashMap.java
│   ├── Stream.java
│   ├── Optional.java
│   └── ...
├── java/io/
│   ├── InputStream.java
│   ├── OutputStream.java
│   ├── Reader.java
│   └── ...
├── java/nio/
│   ├── ByteBuffer.java
│   ├── channels/
│   └── file/
└── java/math/
    ├── BigInteger.java
    └── BigDecimal.java
```

### src/java.desktop/ — GUI APIs

```
src/java.desktop/share/classes/
├── java/awt/
│   ├── Component.java
│   ├── Container.java
│   ├── Graphics.java
│   └── ...
├── javax/swing/
│   ├── JFrame.java
│   ├── JPanel.java
│   ├── JButton.java
│   └── ...
└── sun/awt/         # Internal implementation
```

### src/java.sql/ — JDBC

```
src/java.sql/share/classes/
├── java/sql/
│   ├── Connection.java
│   ├── Statement.java
│   ├── ResultSet.java
│   └── ...
└── javax/sql/
    ├── DataSource.java
    └── ...
```

### src/jdk.internal/ — Internal APIs

```
src/java.base/share/classes/jdk/internal/
├── misc/
│   └── Unsafe.java
├── reflect/
│   └── ReflectionFactory.java
├── loader/
│   └── ClassLoaders.java
└── ...
```

### src/hotspot/ — HotSpot JVM (C++)

```
src/hotspot/
├── share/                    # Platform-independent code
│   ├── runtime/              # Thread, safepoint, etc.
│   ├── gc/                   # Garbage collectors
│   ├── opto/                 # C2 compiler
│   ├── classfile/            # Class loading
│   ├── code/                 # Code cache, nmethods
│   ├── compiler/             # Compiler infrastructure
│   ├── oops/                 # Object representation
│   ├── memory/               # Memory management
│   └── ...
├── cpu/x86/                  # x86-specific code
├── cpu/aarch64/              # ARM64-specific code
├── os/linux/                 # Linux-specific code
├── os/posix/                 # POSIX-specific code
└── os/windows/               # Windows-specific code
```

### make/ — Build System

```
make/
├── autoconf/                 # Configure scripts
├── common/                   # Shared Makefile rules
├── conf/                     # Build configuration
└── data/                     # Build data files
```

### test/ — Test Suites

```
test/
├── jdk/                      # JDK tests (jtreg)
│   ├── java/lang/
│   ├── java/util/
│   └── ...
├──/hotspot/                  # HotSpot tests
├── langtools/                # Compiler tests
└── libraries/                # Test libraries
```

## How to Find a Class

### By Name

```bash
# Find java.lang.String
find src -name "String.java" -path "*/java/lang/*"

# Find all ArrayList files
find src -name "ArrayList*"

# Find HotSpot thread implementation
find src/hotspot -name "thread*"
```

### Using grep

```bash
# Find where a method is defined
rg "public static void main" src/java.base/

# Find all implementations of an interface
rg "implements Comparable" src/java.base/

# Find all GC-related files
rg -l "GarbageCollector" src/hotspot/
```

### Using GitHub Search

1. Go to [github.com/openjdk/jdk](https://github.com/openjdk/jdk)
2. Press `/` to focus the search bar
3. Type the class name or method name
4. Filter by path if needed (e.g., `path:src/java.base`)

### Using an IDE

IDEs like IntelliJ IDEA and VS Code can index the OpenJDK source:

1. Clone the repository
2. Open it in your IDE
3. Use "Go to Class" (Ctrl+N / Cmd+O) to find any class
4. Use "Find Usages" (Alt+F7 / Cmd+Option+F7) to see where something is used

## How to Read HotSpot Source (C++)

### Key Concepts

- **HotSpot is written in C++** with platform-specific assembly
- Platform-independent code is in `src/hotspot/share/`
- Platform-specific code is in `src/hotspot/cpu/<arch>/` and `src/hotspot/os/<os>/`
- The `oops` directory contains object representation (Ordinary Object Pointer)

### Reading Flow

When you want to understand how a feature works:

1. **Start with the Java API** — Find the public class (e.g., `java.lang.Thread`)
2. **Look for native methods** — `Thread.start()` calls `start0()` (JNI native)
3. **Find the JNI registration** — Search for `Java_java_lang_Thread_start0`
4. **Follow into the VM** — The native code calls into HotSpot C++ classes
5. **Trace the implementation** — Follow the call chain

### Example: Thread.start()

```
Java: Thread.start()
  → JNI: Thread.start0()
    → C++: Java_Java_lang_Thread_start0()
      → C++: JavaThread::start()
        → C++: os::start_thread()
          → OS: pthread_create()
```

### Naming Conventions

| Pattern | Meaning |
|---------|---------|
| `*Entry` | Entry point for a subsystem |
| `*Factory` | Creates instances of a type |
| `*Manager` | Manages resources or state |
| `*Cache` | Caches computed values |
| `*Table` | Hash table or lookup structure |
| `_name` | Instance field (underscore prefix) |
| `_NAME` | Static constant |

### Header Files

HotSpot uses `.hpp` (C++ header) and `.cpp` (implementation):

```
thread.hpp    → Class definition (what methods exist)
thread.cpp    → Implementation (how methods work)
```

Read the `.hpp` file first to understand the interface, then dive into `.cpp` for implementation details.

## Finding Native Methods

Many Java classes have native (C/C++) implementations:

```java
// In java/lang/Thread.java
private native void start0();
```

To find the native implementation:

```bash
# Search for JNI registration
rg "start0" src/java.base/ --include="*.cpp"

# Or search for the function name
rg "Java_java_lang_Thread_start0" src/ --include="*.cpp"
```

## Hot-Spot Source Navigation Tips

### Common Directories

| Directory | What's There |
|-----------|-------------|
| `src/hotspot/share/runtime/` | Thread, safepoint, stubs, etc. |
| `src/hotspot/share/gc/` | All garbage collector implementations |
| `src/hotspot/share/opto/` | C2 compiler |
| `src/hotspot/share/classfile/` | Class loading, verification |
| `src/hotspot/share/oops/` | Object representation (oops) |
| `src/hotspot/share/code/` | Code cache, compiled code |
| `src/hotspot/share/compiler/` | Compiler infrastructure |
| `src/hotspot/share/memory/` | Memory management, metaspace |

### Common Class Names

| Class | Purpose |
|-------|---------|
| `JavaThread` | Per-thread state |
| `Thread` | Base thread class |
| `CollectedHeap` | Heap abstraction |
| `Klass` | Class representation |
| `InstanceKlass` | Instance class |
| `oop` | Object pointer |
| `nmethod` | Compiled native method |
| `CodeCache` | JIT code storage |
| `SystemDictionary` | Class loading registry |
| `VMThread` | VM internal thread |
