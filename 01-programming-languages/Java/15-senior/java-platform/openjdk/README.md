# OpenJDK — The Reference Implementation of Java SE

OpenJDK is the **reference implementation** of Java SE (Standard Edition). It is the open-source Java Development Kit, providing the canonical implementation of the Java programming language, JVM (HotSpot), and standard class libraries.

## What's Inside OpenJDK

| Component | Description |
|-----------|-------------|
| [HotSpot](hotspot/) | The default JVM — JIT compilation, garbage collection, runtime services |
| [javac](javac/) | The Java compiler — parsing, AST, annotation processing, bytecode generation |
| [Class Libraries](class-libraries/) | Core Java APIs — `java.base`, `java.net`, `java.nio`, and internal APIs |
| [Build Process](build-process/) | How OpenJDK is compiled from source |
| [JEPs](jeps/) | Java Enhancement Proposals — how features are proposed and delivered |
| [Release Process](release-process/) | Release cadence, LTS, testing, and certification |
| [Contribution Guide](contribution-guide/) | How to contribute to OpenJDK |
| [Source Code Navigation](source-code-navigation/) | How to find your way around the OpenJDK source tree |

## OpenJDK at a Glance

```
OpenJDK
├── HotSpot JVM
│   ├── Runtime (threads, memory, class loading)
│   ├── Interpreter (bytecode execution)
│   ├── Compiler (C1, C2, Graal JIT)
│   └── Garbage Collectors (G1, ZGC, Shenandoah, Serial, Parallel)
├── javac Compiler
│   ├── Parsing & Tokenization
│   ├── Abstract Syntax Tree
│   ├── Annotation Processing
│   └── Bytecode Generation
├── Class Libraries
│   ├── java.base (lang, util, io, nio)
│   ├── java.net (HTTP client, sockets)
│   ├── java.nio (channels, buffers)
│   └── Internal APIs (jdk.internal.*)
└── Build System
    ├── Configure scripts
    ├── Make-based build
    └── Test infrastructure (jtreg, JCStress, JMH)
```

## Key Facts

- **License**: GPLv2 with Classpath Exception
- **Repository**: [github.com/openjdk/jdk](https://github.com/openjdk/jdk)
- **Release cadence**: Every 6 months (non-LTS), every 2 years (LTS)
- **Governance**: Board includes Oracle, Red Hat, Google, Amazon, Microsoft

## How to Navigate This Guide

1. Start with [Architecture](architecture/) for a high-level overview
2. Read [HotSpot](hotspot/) to understand the JVM internals
3. Read [javac](javac/) to understand the compiler pipeline
4. Read [Class Libraries](class-libraries/) for the standard API modules
5. Use [Source Code Navigation](source-code-navigation/) to find your way around the codebase
6. Refer to [Build Process](build-process/) when you need to compile from source
