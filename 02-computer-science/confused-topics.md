# Thread vs Process

## What They Are

### Thread
A lightweight unit of execution within a process. Multiple threads share the same memory space and resources of their parent process. Each thread has its own stack, registers, and program counter.

### Process
An independent execution unit with its own memory space, resources, and environment. Processes are isolated from each other and communicate through inter-process communication (IPC) mechanisms.

## Key Difference Table

| Feature | Thread | Process |
|---------|--------|---------|
| Memory Space | Shared with parent process | Independent memory |
| Creation Overhead | Low (kilobytes) | High (megabytes) |
| Context Switching | Fast (microseconds) | Slow (milliseconds) |
| Communication | Direct (shared memory) | IPC (pipes, sockets) |
| Isolation | None (shares resources) | Complete isolation |
| Fault Impact | Can crash entire process | Limited to process |
| Resource Usage | Minimal | Significant |
| Number per System | Thousands | Hundreds |
| Scheduling | OS thread scheduler | OS process scheduler |
| Data Sharing | Easy (shared variables) | Complex (IPC) |

## When to Use Which

### Use Threads When
- Tasks are related and need to share data
- Performance is critical (frequent context switches)
- Building concurrent data structures
- Implementing worker pools
- I/O-bound tasks requiring many concurrent operations

### Use Processes When
- Tasks need complete isolation
- Stability is critical (fault tolerance)
- Running untrusted code
- Leveraging multiple CPU cores effectively
- Different programming languages or environments

## Interview Trap

**Trap**: "Threads are always faster than processes."

**Reality**: Threads are faster to create and switch between, but can cause contention issues. In some cases, processes provide better performance due to isolation (no lock contention, better cache behavior).

**Follow-up Trap**: "More threads always mean better performance."

**Reality**: Excessive threads cause overhead from context switching and resource contention. There's an optimal thread count based on workload type (CPU-bound vs I/O-bound).

## Visual Diagram

```
Process with Multiple Threads:
┌─────────────────────────────────────────────┐
│              Process                        │
│  ┌─────────────────────────────────────┐   │
│  │         Memory Space                │   │
│  │  ┌─────────┐ ┌─────────┐           │   │
│  │  │ Stack 1 │ │ Stack 2 │           │   │
│  │  └─────────┘ └─────────┘           │   │
│  │  ┌─────────┐ ┌─────────┐           │   │
│  │  │ Stack 3 │ │ Stack 4 │           │   │
│  │  └─────────┘ └─────────┘           │   │
│  │                                     │   │
│  │  ┌─────────────────────────────┐   │   │
│  │  │      Shared Memory          │   │   │
│  │  │  (Heap, Global Variables)   │   │   │
│  │  └─────────────────────────────┘   │   │
│  └─────────────────────────────────────┘   │
│                                             │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐      │
│  │ Thread 1│ │ Thread 2│ │ Thread 3│      │
│  └─────────┘ └─────────┘ └─────────┘      │
└─────────────────────────────────────────────┘

Multiple Processes:
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│  Process 1  │  │  Process 2  │  │  Process 3  │
│ ┌─────────┐ │  │ ┌─────────┐ │  │ ┌─────────┐ │
│ │ Memory  │ │  │ │ Memory  │ │  │ │ Memory  │ │
│ │ Space 1 │ │  │ │ Space 2 │ │  │ │ Space 3 │ │
│ └─────────┘ │  │ └─────────┘ │  │ └─────────┘ │
└─────────────┘  └─────────────┘  └─────────────┘
       │                │                │
       └────────────────┼────────────────┘
                        │
              IPC (Pipes, Sockets)
```

## Performance Metrics

| Metric | Thread | Process |
|--------|--------|---------|
| Creation time | ~10 microseconds | ~100 milliseconds |
| Context switch | ~1 microsecond | ~100 microseconds |
| Memory overhead | ~8KB stack | ~1MB+ |
| Communication | Direct memory | System calls |

## The Thread Pool Pattern

Instead of creating threads dynamically, use thread pools:
- Pre-create a fixed number of threads
- Reuse threads for multiple tasks
- Avoid thread creation overhead
- Control resource usage

## Key Insight

The choice between threads and processes is about trade-offs:

**Threads**: Performance + Shared State = Complexity
**Processes**: Isolation + Safety = Overhead

Modern applications often use both:
- Multiple processes for fault isolation
- Multiple threads within each process for concurrency
- Example: Browser (multiple processes) with threads per tab

## Language Considerations

- **Java**: Threads are first-class citizens
- **Python**: GIL limits thread parallelism (use processes for CPU-bound)
- **Go**: Goroutines are lightweight threads (user-space scheduling)
- **Node.js**: Single-threaded event loop (use worker threads for CPU work)
