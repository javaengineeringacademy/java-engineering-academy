# C Language Course

## Overview

The C programming language is the foundation of modern computing. Created by Dennis Ritchie at Bell Labs in 1972, C provides direct hardware access, deterministic performance, and unparalleled portability. This course takes you from absolute zero to production-ready C developer capable of building operating systems, embedded firmware, high-performance servers, and safety-critical systems.

## Why C Matters

### The Problem C Solves

Every computing system — from a 8-bit microcontroller to a 1024-core supercomputer — ultimately executes machine code on hardware. Most languages add abstraction layers between you and the hardware, introducing garbage collectors, virtual machines, and runtime overhead. C gives you near-direct hardware access while providing just enough abstraction to write portable, readable code.

### Why Learn C in 2026?

| Reason | Explanation |
|--------|-------------|
| **Foundation** | C syntax and concepts underpin C++, Java, Go, Rust, and most system languages |
| **Performance** | No garbage collector, no runtime — deterministic, predictable execution |
| **Portability** | Compiles to native code on virtually every architecture ever built |
| **Ubiquity** | Linux kernel, SQLite, Redis, Nginx, OpenSSL — critical infrastructure is C |
| **Embedded** | Microcontrollers, RTOS, firmware, device drivers — C dominates embedded |
| **Security** | Understanding C is essential for vulnerability research and secure coding |
| **Career** | Systems programming, firmware, game engines, compilers, databases |

### Who Uses C Today?

- **Operating Systems**: Linux (28M+ lines), Windows kernel, macOS XNU
- **Databases**: SQLite, PostgreSQL, MySQL, Redis
- **Web Servers**: Nginx, Apache, Lighttpd
- **Cryptography**: OpenSSL, libsodium, BoringSSL
- **Embedded**: Arduino core, FreeRTOS, Zephyr RTOS, automotive ECU firmware
- **Game Engines**: Unreal Engine (partially), id Tech engines
- **Compilers**: GCC, Clang, Lua, PHP interpreter

## Course Structure

| Module | Topic | Duration | Level | Prerequisites |
|--------|-------|----------|-------|---------------|
| 00 | [Knowledge Atoms](00-knowledge-atoms/README.md) | 2 hours | Beginner | None |
| 01 | [Fundamentals](01-fundamentals/README.md) | 15 hours | Beginner | 00 |
| 02 | [Structures & Unions](02-structures/README.md) | 8 hours | Beginner-Intermediate | 01 |
| 03 | [Preprocessor](03-preprocessor/README.md) | 6 hours | Intermediate | 01 |
| 04 | [File I/O](04-file-io/README.md) | 6 hours | Intermediate | 01, 02 |
| 05 | [Advanced Pointers](05-pointers-advanced/README.md) | 10 hours | Intermediate | 01, 02 |
| 06 | [Data Structures](06-data-structures/README.md) | 12 hours | Intermediate | 02, 05 |
| 07 | [Algorithms](07-algorithms/README.md) | 12 hours | Intermediate | 06 |
| 08 | [Memory Management](08-memory-management/README.md) | 10 hours | Intermediate | 01, 05 |
| 09 | [Concurrency](09-concurrency/README.md) | 8 hours | Advanced | 01, 08 |
| 10 | [Networking](10-networking/README.md) | 8 hours | Advanced | 04, 09 |
| 11 | [Security](11-security/README.md) | 6 hours | Advanced | 01, 08, 10 |
| 12 | [Performance](12-performance/README.md) | 8 hours | Advanced | 07, 08 |
| 13 | [Testing](13-testing/README.md) | 6 hours | Advanced | 01, 04 |
| 14 | [Build Systems](14-build-systems/README.md) | 6 hours | Advanced | 03 |
| 15 | [Best Practices](15-best-practices/README.md) | 8 hours | Senior | All above |
| 16 | [Senior](16-senior/README.md) | 15 hours | Senior | All above |

**Total Duration: ~144 hours**

## Learning Path

### Foundation (Student → Junior) — 37 hours

```
00-knowledge-atoms → 01-fundamentals → 02-structures → 03-preprocessor → 04-file-io
```

**Outcome**: Can write standalone C programs, handle files, use structures, and understand the compilation pipeline.

### Intermediate (Junior → Mid-Level) — 50 hours

```
05-pointers-advanced → 06-data-structures → 07-algorithms → 08-memory-management
```

**Outcome**: Can implement data structures, optimize algorithms, manage memory safely, and use advanced pointer patterns.

### Advanced (Mid-Level → Senior) — 42 hours

```
09-concurrency → 10-networking → 11-security → 12-performance → 13-testing → 14-build-systems
```

**Outcome**: Can build concurrent networked applications, write secure code, optimize performance, and manage complex build systems.

### Expert (Senior → Staff/CTO) — 23 hours

```
15-best-practices → 16-senior
```

**Outcome**: Can design system architectures, lead C development teams, make technology decisions, and define coding standards.

## Real-World Projects by Module

| Module | Project | Industry Example |
|--------|---------|-----------------|
| 01 | Calculator interpreter | Embedded HMI systems |
| 02 | Student database system | HR/payroll software |
| 03 | Config file parser | Nginx configuration |
| 04 | CSV analyzer | ETL data pipelines |
| 05 | Dynamic array library | Redis internal containers |
| 06 | Hash table implementation | Database indexing engines |
| 07 | Pathfinding algorithm | Game AI navigation |
| 08 | Custom memory allocator | JVM/glibc memory pools |
| 09 | Thread pool | Web server request handling |
| 10 | HTTP client | curl/libcurl |
| 11 | Input sanitizer | OpenSSH packet handling |
| 12 | Profiling tool | Google PerfTools |
| 13 | Test framework | Unity/Check test suites |
| 14 | Build system | Linux kernel Kbuild |
| 15 | Style guide | Linux kernel coding style |
| 16 | Module architecture | SQLite virtual machine |

## Toolchain

### Essential Tools

| Tool | Purpose | Install |
|------|---------|---------|
| GCC/Clang | Compiler | `apt install gcc` / `brew install gcc` |
| GDB | Debugger | `apt install gdb` / `brew install gdb` |
| Valgrind | Memory analysis | `apt install valgrind` / `brew install valgrind` |
| Make | Build automation | Usually pre-installed |
| CMake | Cross-platform builds | `apt install cmake` / `brew install cmake` |
| AddressSanitizer | Runtime error detection | Built into GCC/Clang (`-fsanitize=address`) |
| ThreadSanitizer | Data race detection | Built into GCC/Clang (`-fsanitize=thread`) |

### Recommended Compiler Flags

```bash
# Development (catch bugs early)
gcc -Wall -Wextra -Werror -Wpedantic -g -O0 -fsanitize=address,undefined

# Production (optimize for speed)
gcc -O2 -march=native -DNDEBUG -fstack-protector-strong -D_FORTIFY_SOURCE=2

# Maximum optimization (profile-guided)
gcc -O3 -march=native -flto -fprofile-generate
```

## Course Resources

- **Exercises**: Each module includes hands-on exercises (13 total exercise sets)
- **Quizzes**: 17 quizzes to test understanding
- **C Files**: 13 example C programs
- **Reference**: [C Standard Library Quick Reference](reference/README.md)

## How to Use This Course

1. **Sequential Learning**: Follow modules 00-16 in order for complete coverage
2. **Prerequisites First**: Each module lists prerequisites — complete them first
3. **Hands-On Practice**: Write code for every concept before moving on
4. **Build Projects**: Complete the module projects for real-world experience
5. **Review Regularly**: Use One-Minute Revision tables for spaced repetition
6. **Read Incident Studies**: Production incidents provide context for why best practices matter

## Quality Standards

Every module README follows this structure:

- **Why-first Narrative**: Problem → Concept → Architecture → Technology → Implementation → Production → Operations
- **Engineering Decision Framework**: When to use / When NOT to use / Alternatives / Production Examples
- **Expanded Code Examples**: Real-world scenarios, not toy examples
- **Production Incidents**: Real-world failure scenarios with detection and prevention
- **Production Checklist**: Actionable items for shipping code
- **Maturity Levels**: Beginner → Intermediate → Advanced progression
- **Common Myths Debunked**: Factual corrections to common misconceptions
- **One-Minute Revision Table**: Quick reference for spaced repetition
- **Related Topics**: Cross-linked navigation to other modules
