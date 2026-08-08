# C++ Language Course

## Overview
C++ — the language of systems, games, and performance-critical applications. This comprehensive course takes you from absolute zero to production-ready C++ developer, covering everything from fundamental concepts to senior-level architecture and system design.

## Why C++?

### The Problem C++ Solves
Every system that demands direct hardware control, deterministic performance, or zero-overhead abstractions needs C++. Operating systems, game engines, databases, trading systems, embedded firmware, and browsers all depend on C++ because it provides raw power without sacrificing abstraction.

### The Core Value Proposition
- **Zero-Cost Abstractions**: Pay only for what you use — templates, lambdas, and move semantics compile to the same code as hand-written C
- **Deterministic Performance**: No garbage collector pauses, no runtime overhead — you control exactly when and how resources are allocated and freed
- **Direct Hardware Access**: Memory-mapped I/O, inline assembly, SIMD intrinsics, and cache-aware data structures
- **Dual Paradigm Support**: Procedural, object-oriented, generic, functional, and metaprogramming — choose the right paradigm for each problem
- **Ecosystem Maturity**: 40+ years of libraries, toolchains, and battle-tested production code across every industry

### Real-World Usage
| Domain | Examples | Why C++ |
|--------|----------|---------|
| Game Engines | Unreal, Unity (parts), Godot | Frame-rate determinism, hardware access |
| Databases | MySQL, MongoDB, PostgreSQL | Low-level I/O, memory control |
| Browsers | Chrome (V8, Blink), Firefox (SpiderMonkey) | JavaScript engines, rendering pipelines |
| Trading Systems | Bloomberg, Citadel, Jump | Microsecond latency requirements |
| Operating Systems | Windows (kernel), macOS (XNU), Linux (parts) | Direct hardware control |
| Embedded | Medical devices, automotive, IoT | Deterministic memory usage |
| Compilers | Clang, GCC, MSVC | Self-hosting, template metaprogramming |
| ML/AI Frameworks | TensorFlow, PyTorch (backend) | GPU acceleration, performance |

## Course Structure

| Module | Topic | Duration | Prerequisites |
|--------|-------|----------|---------------|
| 00 | Knowledge Atoms | 2 hours | None |
| 01 | Fundamentals | 12 hours | Module 00 |
| 02 | OOP | 10 hours | Module 01 |
| 03 | Templates | 10 hours | Module 02 |
| 04 | STL | 10 hours | Module 03 |
| 05 | Memory Management | 10 hours | Module 01 |
| 06 | Smart Pointers | 8 hours | Module 05 |
| 07 | Concurrency | 10 hours | Module 06 |
| 08 | Modern C++ | 10 hours | Modules 01-04 |
| 09 | Design Patterns | 12 hours | Modules 02, 08 |
| 10 | Testing | 6 hours | Module 09 |
| 11 | Performance | 10 hours | Modules 05, 07 |
| 12 | Networking | 8 hours | Modules 07, 08 |
| 13 | Build Systems | 8 hours | Module 10 |
| 14 | Best Practices | 8 hours | All previous |
| 15 | Senior | 15 hours | All previous |

## Learning Path

### Foundation (Student → Junior Developer)
```
00-knowledge-atoms → 01-fundamentals → 02-oop → 03-templates → 04-stl
```
**Outcome**: Can write basic C++ programs, understand compilation model, use STL containers and algorithms, and apply object-oriented design.

### Intermediate (Junior → Mid-Level Developer)
```
05-memory-management → 06-smart-pointers → 07-concurrency → 08-modern-cpp
```
**Outcome**: Can manage memory safely, write concurrent code, and leverage modern C++ features (C++17/20) for cleaner, safer code.

### Advanced (Mid-Level → Senior Developer)
```
09-design-patterns → 10-testing → 11-performance → 12-networking → 13-build-systems
```
**Outcome**: Can design scalable architectures, write production-quality tested code, optimize for performance, and build distributed systems.

### Expert (Senior → Staff/CTO)
```
14-best-practices → 15-senior
```
**Outcome**: Can lead technical teams, make architecture decisions, design for scalability, and align technical strategy with business goals.

## Module Details

### Module 00: Knowledge Atoms
Foundational concepts shared across all C++ programs: compilation model, type system, memory model, object model, and template metaprogramming.

### Module 01: Fundamentals
Building blocks: variables, data types, control structures, functions, pointers, references, and basic I/O. The foundation for all advanced topics.

### Module 02: Object-Oriented Programming
Classes, objects, inheritance, polymorphism, encapsulation, and abstraction. Real-world modeling with production incident analysis.

### Module 03: Templates
Generic programming: function templates, class templates, template specialization, SFINAE, variadic templates, and C++20 concepts.

### Module 04: Standard Template Library (STL)
Containers, algorithms, iterators, function objects, and adapters. Production-grade data structures with performance characteristics.

### Module 05: Memory Management
Stack vs heap, RAII principle, memory leaks, dangling pointers, and manual memory management best practices.

### Module 06: Smart Pointers
std::unique_ptr, std::shared_ptr, std::weak_ptr, custom deleters, and thread safety considerations.

### Module 07: Concurrency
Threads, mutexes, condition variables, atomics, async/futures, thread pools, and lock-free programming.

### Module 08: Modern C++
C++11/14/17/20 features: auto, lambdas, move semantics, structured bindings, concepts, coroutines, and ranges.

### Module 09: Design Patterns
Creational, structural, and behavioral patterns implemented in modern C++ with production examples.

### Module 10: Testing
Unit testing (Google Test), integration testing, mocking, test fixtures, property-based testing, and CI/CD integration.

### Module 11: Performance
Profiling, cache optimization, SIMD, memory pools, branch prediction, and performance measurement methodologies.

### Module 12: Networking
TCP/UDP sockets, HTTP clients, async networking with Boost.Asio, protocol design, and security considerations.

### Module 13: Build Systems
CMake, Makefiles, package management (vcpkg, Conan), cross-compilation, and CI/CD pipeline configuration.

### Module 14: Best Practices
Code style, const correctness, error handling, memory management guidelines, and team collaboration practices.

### Module 15: Senior Level
Architecture patterns (DDD, Hexagonal), system design, performance optimization, code generation, and technical leadership.

## Quick Start

```bash
# Clone or download the course
cd 01-programming-languages/C++

# Start with Module 00
cd 00-knowledge-atoms
cat README.md

# Build and run examples
g++ -std=c++17 -o example examples/example.cpp
./example
```

## Prerequisites
- Basic programming knowledge (any language)
- A C++ compiler (GCC 10+, Clang 12+, or MSVC 2019+)
- Text editor or IDE (VS Code, CLion, or Visual Studio)
- Basic command-line familiarity

## Recommended Tools
- **Compiler**: GCC 13+ or Clang 17+ (for C++20 support)
- **IDE**: VS Code + C/C++ extension, or CLion
- **Build System**: CMake 3.20+
- **Package Manager**: vcpkg or Conan 2
- **Static Analysis**: clang-tidy, cppcheck
- **Sanitizers**: ASan, TSan, UBSan (built into GCC/Clang)
- **Profiling**: perf, Valgrind, Intel VTune

## Course Quality Standard
Every module follows the **WHY-first narrative**:
1. **Problem**: What real-world problem does this solve?
2. **Concept**: What is the core idea?
3. **Architecture**: How does it fit into larger systems?
4. **Technology**: What tools and libraries are involved?
5. **Implementation**: How do you write the code?
6. **Production**: What goes wrong in real deployments?
7. **Operations**: How do you monitor and maintain it?

Each module includes:
- Engineering Decision Framework (When to use / When NOT to use)
- Expanded code examples with real-world scenarios
- Production incidents with root cause analysis
- Production checklist
- Maturity levels
- Common myths debunked
- One-Minute Revision table
- Exercises with solutions
- 10-question quiz with detailed answers
