# OpenJDK Architecture

OpenJDK is a collection of components that together form the Java SE platform. Understanding the architecture helps you navigate the source code and contribute effectively.

## Major Components

### 1. javac — The Java Compiler

javac compiles human-readable `.java` source files into platform-independent `.class` bytecode files. The compilation pipeline consists of:

1. **Lexical Analysis** — Source code is tokenized into keywords, identifiers, literals, and operators
2. **Parsing** — Tokens are organized into an Abstract Syntax Tree (AST)
3. **Annotation Processing** — APT (Annotation Processing Tool) runs processors before type-checking
4. **Type Checking & Attribution** — The compiler resolves types, performs overload resolution, and checks semantics
5. **Bytecode Generation** — The AST is lowered to bytecode instructions stored in `.class` files

javac is itself written in Java, which means it can be bootstrapped — an older version compiles the newer version.

### 2. HotSpot — The Java Virtual Machine

HotSpot is the runtime that executes Java bytecode. It is responsible for:

- **Class Loading** — Loading `.class` files, verifying bytecode, linking, and initialization
- **Bytecode Interpretation** — Interpreting bytecode for initial execution
- **JIT Compilation** — Compiling hot methods to native code via C1 and C2 compilers
- **Garbage Collection** — Managing heap memory through G1, ZGC, Shenandoah, Serial, or Parallel collectors
- **Thread Management** — Mapping Java threads to OS threads, scheduling, and synchronization
- **Runtime Data Areas** — Heap, method area, stack, and program counter for each thread

HotSpot is written primarily in C++ with platform-specific assembly for performance-critical paths.

### 3. Class Libraries — Standard Java APIs

The class libraries provide the Java API surface that application code depends on. They are organized into modules:

| Module | Contents |
|--------|----------|
| `java.base` | `java.lang`, `java.util`, `java.io`, `java.math`, `java.time` |
| `java.net` | HTTP client, URL handling, sockets |
| `java.nio` | Non-blocking I/O, channels, buffers, file systems |
| `java.sql` | JDBC API |
| `java.desktop` | AWT, Swing, JavaFX |
| `jdk.internal` | Internal APIs not part of the public specification |

The class libraries are compiled against a minimized set of internal JDK APIs and then stored as pre-compiled modules in the `lib/modules` file.

### 4. Build System

OpenJDK uses a GNU Make-based build system with a `configure` script:

```
bash configure    # Detect system, generate Makefiles
make images       # Build the full JDK image
make test         # Run the test suite
```

The build system handles:
- Cross-compilation for different architectures
- Bootstrapping with an existing JDK
- Selecting which GC implementations to include
- Producing JDK and JRE images

### 5. Test Infrastructure

OpenJDK uses multiple testing frameworks:

| Framework | Purpose |
|-----------|---------|
| jtreg | Primary test harness for JDK tests |
| JCStress | Concurrency stress testing |
| JMH | Microbenchmarks for performance |
| TCK | Technology Compatibility Kit (commercial certification) |

## How the Components Interact

```
                    ┌──────────────┐
                    │  Application │
                    │    Code      │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │    javac     │
                    │  Compiler    │
                    └──────┬───────┘
                           │ .class files
                           ▼
                    ┌──────────────┐
                    │   HotSpot    │
                    │     JVM      │
                    ├──────────────┤
                    │ Class Loader │
                    │ Interpreter  │
                    │ C1 / C2 JIT  │
                    │ GC (G1/ZGC)  │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │  Class       │
                    │  Libraries   │
                    │ (java.base)  │
                    └──────────────┘
```

When you run `javac Hello.java`, the compiler produces `Hello.class`. When you run `java Hello`, HotSpot loads the class file, interprets the bytecode, identifies hot methods, compiles them to native code via JIT, and manages memory through garbage collection — all while your code calls into the class libraries for I/O, collections, networking, and more.

## Source Code Organization

The OpenJDK repository is organized as follows:

```
jdk/
├── src/
│   ├── hotspot/          # HotSpot JVM (C++)
│   ├── java.base/        # Core classes (Java)
│   ├── java.net/         # Networking APIs (Java)
│   ├── java.nio/         # NIO APIs (Java)
│   ├── java.sql/         # JDBC (Java)
│   ├── java.desktop/     # AWT/Swing (Java)
│   ├── jdk.*             # JDK-specific modules
│   └── ...               # Other modules
├── make/                 # Build system
├── test/                 # Test suites
├── make/conf/            # Build configuration
└── doc/                  # Documentation
```

Each module under `src/` contains its own `module-info.java`, source code, and any native (C/C++) code. The HotSpot JVM source lives entirely under `src/hotspot/` and is written in C++ with platform-specific assembly.
