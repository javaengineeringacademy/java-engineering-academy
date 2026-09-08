# C Capstone Project — Architecture Decision Records

## ADR-001: Use Custom Memory Pool Allocator

**Status:** Accepted

**Context:** The capstone project requires frequent allocation and deallocation of fixed-size objects (connections, requests, responses). Standard `malloc/free` introduces fragmentation and non-deterministic latency in long-running servers.

**Decision:** Implement a custom pool allocator that pre-allocates a large memory block and subdivides it into fixed-size chunks.

**Consequences:**
- ✅ O(1) allocation and deallocation
- ✅ No external fragmentation
- ✅ Predictable memory usage
- ❌ Requires tuning pool size for expected load
- ❌ Cannot allocate variable-size objects
- ❌ More code to maintain and debug

---

## ADR-002: Event-Driven I/O with epoll/kqueue

**Status:** Accepted

**Context:** The capstone network server must handle thousands of concurrent connections efficiently. Thread-per-connection models consume too many resources and cause context switch overhead.

**Decision:** Use event-driven I/O multiplexing (`epoll` on Linux, `kqueue` on macOS, `select` as fallback).

**Consequences:**
- ✅ Handles thousands of connections with few threads
- ✅ Lower memory usage per connection
- ✅ Better CPU utilization
- ❌ More complex code (event loop, state machines)
- ❌ Harder to debug than blocking I/O
- ❌ Requires platform-specific abstractions

---

## ADR-003: Opaque Pointers for ABI Stability

**Status:** Accepted

**Context:** The capstone project exposes a shared library API. Internal struct layouts must be able to change without breaking compiled applications that depend on the library.

**Decision:** Use opaque pointers (forward-declared structs in public headers, defined only in implementation files).

**Consequences:**
- ✅ Internal layout can change freely
- ✅ Binary compatibility maintained across versions
- ✅ Forces clean API design
- ❌ Slight runtime overhead (pointer indirection)
- ❌ Cannot allocate on stack from caller
- ❌ Requires accessor functions for all struct members

---

## ADR-004: Platform Abstraction Layer

**Status:** Accepted

**Context:** The capstone project must run on Linux, macOS, and potentially Windows. Platform-specific APIs differ for threading, networking, and file I/O.

**Decision:** Create a platform abstraction layer with function pointers selected at compile time based on target OS.

**Consequences:**
- ✅ Single application codebase across platforms
- ✅ Platform-specific optimizations where needed
- ✅ Clean separation of concerns
- ❌ Additional indirection layer
- ❌ Must test on all target platforms
- ❌ Some platform differences cannot be fully abstracted

---

## ADR-005: Thread Pool for Concurrency

**Status:** Accepted

**Context:** The capstone project needs to handle concurrent tasks (request processing, background jobs). Creating a new thread for each task is expensive and does not scale.

**Decision:** Use a fixed-size thread pool with a task queue, mutex, and condition variable.

**Consequences:**
- ✅ Reuses threads, avoids creation overhead
- ✅ Limits concurrent threads to CPU core count
- ✅ Simple work-stealing pattern
- ❌ Fixed pool size may not adapt to load changes
- ❌ Task starvation if pool is too small
- ❌ Requires careful shutdown handling

---

## ADR-006: Reference Counting for Connection Management

**Status:** Accepted

**Context:** Network connections may be referenced by multiple threads simultaneously (handler thread, timeout thread, cleanup thread). Premature deallocation causes use-after-free bugs.

**Decision:** Use atomic reference counting on connection objects. Connections are freed only when the reference count reaches zero.

**Consequences:**
- ✅ Prevents use-after-free bugs
- ✅ Works across threads without locks for count updates
- ✅ Clear ownership semantics
- ❌ Overhead of atomic operations
- ❌ Potential leaks if reference count is never decremented
- ❌ More complex than simple mutex-based protection

---

## ADR-007: Configuration via File + CLI Overrides

**Status:** Accepted

**Context:** The capstone project needs runtime configuration (port, thread count, database path, log level). Hardcoded values are inflexible; environment variables are not sufficient for all settings.

**Decision:** Load configuration from a key-value file at startup, with command-line arguments overriding file values.

**Consequences:**
- ✅ Flexible configuration without recompilation
- ✅ CLI overrides for testing and deployment
- ✅ Sensible defaults for quick startup
- ❌ Two configuration sources to document
- ❌ Must validate all configuration values
- ❌ Requires config file parsing code

---

## ADR-008: Custom Binary Protocol over TCP

**Status:** Accepted

**Context:** The capstone project needs a network protocol for client-server communication. HTTP adds overhead; raw TCP requires protocol design.

**Decision:** Implement a simple binary protocol with length-prefixed messages and a small set of opcodes.

**Consequences:**
- ✅ Efficient parsing and serialization
- ✅ Minimal overhead
- ✅ Flexible for custom operations
- ❌ Requires protocol documentation
- ❌ No existing tooling (unlike HTTP)
- ❌ Must handle versioning and backward compatibility

---

## ADR-009: Static Analysis and Fuzzing in CI

**Status:** Accepted

**Context:** C is prone to memory safety bugs, buffer overflows, and undefined behavior. These must be caught before production deployment.

**Decision:** Integrate Clang Static Analyzer, AddressSanitizer, and AFL fuzzing into the CI pipeline.

**Consequences:**
- ✅ Catches bugs before they reach production
- ✅ Automated, runs on every commit
- ✅ Finds edge cases that manual testing misses
- ❌ CI build times increase
- ❌ False positives require triage
- ❌ Fuzzing requires corpus management

---

## ADR-010: goto Cleanup Pattern for Error Handling

**Status:** Accepted

**Context:** C does not have exceptions. Complex functions with multiple resources need to clean up on error, leading to deeply nested if-else chains.

**Decision:** Use the `goto cleanup` pattern: label cleanup code at the end of the function; jump to cleanup on any error.

**Consequences:**
- ✅ Flat code structure (no deep nesting)
- ✅ All cleanup in one place (DRY)
- ✅ Clear error propagation
- ❌ goto is considered controversial
- ❌ Requires careful label naming
- ❌ Must ensure all resources are initialized before potential goto targets
