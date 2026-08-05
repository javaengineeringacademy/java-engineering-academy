# Go Architecture

## Compiler Pipeline

Go uses a single-pass compiler with three stages:

1. **Lexing/Scanning**: Source code is tokenized into tokens
2. **Parsing**: Tokens are parsed into an Abstract Syntax Tree (AST)
3. **Compilation**: AST is compiled to machine code via SSA (Static Single Assignment)

The compiler produces statically-linked binaries with no runtime dependencies.

## Go Runtime

The runtime manages memory, goroutines, garbage collection, and I/O operations.

Key components:

- **Memory Allocator**: TCMalloc-inspired allocator with mcache, mcentral, mheap
- **Garbage Collector**: Concurrent, tri-color, mark-sweep collector
- **Scheduler**: M:N threading model with GMP architecture
- **Stack Manager**: Segmented stacks that grow/shrink dynamically

## GMP Scheduler

Go uses the GMP model for goroutine scheduling:

- **G (Goroutine)**: Lightweight thread of execution with its own stack (2KB initial)
- **M (Machine)**: OS thread that executes goroutines
- **P (Processor)**: Logical processor with a local run queue

Scheduling flow:

1. Global run queue holds ready goroutines
2. Each P has a local run queue
3. Work stealing balances load across Ps
4. System calls block M but release P for other work
5. GOMAXPROCS controls the number of Ps

## Garbage Collector

Go's GC is concurrent and optimized for low latency:

- **Trigger**: GC runs when heap grows by GOGC percentage (default 100%)
- **Mark phase**: Concurrent with application using write barriers
- **Sweep phase**: Concurrent and incremental
- **Pacer**: Adjusts GC frequency based on allocation rate and heap size
- **Stack scanning**: Precise, not conservative
- **STW pauses**: Sub-millisecond for mark setup and mark termination

## Channel Model

Channels provide CSP-based communication between goroutines:

- **Unbuffered**: Synchronous communication, sender blocks until receiver
- **Buffered**: Asynchronous up to capacity, then blocks
- **Select**: Multiplexes multiple channel operations
- **Direction**: Channels can be send-only or receive-only
- **Implementation**: Hchan struct with ring buffer and wait queues

Runtime channel operations use semaphores for blocking and atomic operations for synchronization.

## Memory Model

Go's memory model defines happens-before relationships:

- Goroutine creation happens-before goroutine execution
- Channel send happens-before channel receive
- Mutex unlock happens-before subsequent lock
- `sync.Once` guarantees single execution
- `sync/atomic` provides atomic operations

## Package System

- Packages are directories containing Go source files
- Import paths map to directory structure
- `init()` functions run before `main()`
- Blank imports (`_`) execute side effects only
- Internal packages restrict access to parent directories

## Build System

- `go build` compiles and links
- `go test` runs tests with coverage
- `go generate` runs code generation tools
- `go vet` performs static analysis
- Build tags enable conditional compilation
- Cross-compilation via GOOS and GOARCH environment variables
