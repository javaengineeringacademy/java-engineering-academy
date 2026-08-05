# JDK vs JRE vs JVM

## What They Are

### JDK (Java Development Kit)
The complete development environment for building Java applications. Contains tools for developing, testing, and debugging Java code. Includes compiler (javac), debugger, and other development utilities.

### JRE (Java Runtime Environment)
The runtime environment needed to execute Java applications. Contains the JVM, core libraries, and other components required to run compiled Java bytecode. Does not include development tools.

### JVM (Java Virtual Machine)
The virtual machine that executes Java bytecode. Provides platform independence by translating bytecode to native machine code at runtime. Manages memory, garbage collection, and thread execution.

## Key Difference Table

| Feature | JDK | JRE | JVM |
|---------|-----|-----|-----|
| Purpose | Development | Execution | Interpretation |
| Contains Compiler | Yes | No | No |
| Contains JVM | Yes | Yes | Itself |
| Platform | Development | Runtime | Execution Engine |
| Memory Management | Developer tools | Runtime libraries | Garbage collector |
| Typical User | Developer | End User | System |
| Size | Largest | Medium | Core component |
| Contains Debugging Tools | Yes | No | Debugging interfaces |

## When to Use Which

### Use JDK When
- Writing Java applications
- Compiling source code to bytecode
- Debugging applications
- Using development tools (javadoc, javap)
- Building and packaging applications

### Use JRE When
- Running pre-compiled Java applications
- Deploying applications to end users
- Minimal runtime environment needed
- No development activities required

### Use JVM When
- Understanding execution behavior
- Optimizing performance
- Analyzing memory usage
- Debugging runtime issues
- Platform-specific implementations

## Interview Trap

**Trap**: "You need the JDK to run Java applications."

**Reality**: You only need the JRE to run compiled Java applications. The JDK is for development. Many production servers install only the JRE to reduce attack surface and resource usage.

**Follow-up Trap**: "The JVM is the same as the JRE."

**Reality**: The JVM is a component of the JRE. The JRE includes the JVM plus core libraries needed to execute applications.

## Visual Diagram

```
┌─────────────────────────────────────────────────────────┐
│                        JDK                              │
│  ┌─────────────────────────────────────────────────┐   │
│  │                    JRE                          │   │
│  │  ┌─────────────────────────────────────────┐   │   │
│  │  │                  JVM                     │   │   │
│  │  │  ┌─────────────┐  ┌─────────────────┐   │   │   │
│  │  │  │  Class       │  │   Execution     │   │   │   │
│  │  │  │  Loader      │  │   Engine        │   │   │   │
│  │  │  └─────────────┘  └─────────────────┘   │   │   │
│  │  │  ┌─────────────┐  ┌─────────────────┐   │   │   │
│  │  │  │  Garbage     │  │   Runtime       │   │   │   │
│  │  │  │  Collector   │  │   Libraries     │   │   │   │
│  │  │  └─────────────┘  └─────────────────┘   │   │   │
│  │  └─────────────────────────────────────────┘   │   │
│  │  Core Libraries (java.lang, java.util, etc.)   │   │
│  └─────────────────────────────────────────────────┘   │
│  Development Tools: javac, javadoc, jdb, jar             │
└─────────────────────────────────────────────────────────┘

Source Code (.java) → javac → Bytecode (.class) → JVM → Machine Code
```

## Key Insight

The JDK, JRE, and JVM form a layered architecture:

1. **JVM** = The execution engine (bytecode interpreter)
2. **JRE** = JVM + core libraries (runtime environment)
3. **JDK** = JRE + development tools (complete kit)

This is why you can install just the JRE on production servers but need the full JDK for development.

## Historical Note

Before Java 11, Oracle distributed separate JDK and JRE installers. With the shift to modular Java (Project Jlink) and the adoption of OpenJDK as the reference implementation, many distributions now offer consolidated installers. However, the conceptual distinction remains important for understanding Java's architecture.
