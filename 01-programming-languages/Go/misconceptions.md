# Go Common Misconceptions

## 1. Go Has No Generics

**Myth**: Go lacks generic programming capabilities.

**Reality**: Go 1.18+ (March 2022) introduced generics:
```go
func Map[T any, U any](s []T, f func(T) U) []U {
    result := make([]U, len(s))
    for i, v := range s {
        result[i] = f(v)
    }
    return result
}
```

**Why People Believe It**: Go famously rejected generics for years. Many resources predate 1.18.

**Evidence**: 
- Go 1.18 release notes detail generic implementation
- Type parameters work for functions, types, and methods
- Go team actively develops generic ecosystem

**Interview Relevance**: Mention Go's generic history. Discuss type constraints, when to use generics vs. interfaces, and current limitations.

---

## 2. Go Can't Handle Complex Systems

**Myth**: Go's simplicity limits it to simple applications.

**Reality**: Go powers complex distributed systems:
- Kubernetes (container orchestration)
- Docker (container runtime)
- Terraform (infrastructure as code)
- CockroachDB (distributed SQL database)
- Netflix (microservices)

**Why People Believe It**: Go lacks many features (inheritance, exceptions, generators). Simplicity implies limitation.

**Evidence**: 
- Go is the primary language at Google, Uber, Dropbox
- Cloud-native ecosystem is dominated by Go
- Go's concurrency model handles massive scale

**Interview Relevance**: Explain how Go's simplicity enables complexity. Discuss composition over inheritance, error handling philosophy, and concurrency primitives.

---

## 3. Goroutines are Threads

**Myth**: Goroutines are lightweight threads.

**Reality**: Goroutines are multiplexed onto OS threads:
- Goroutines start with 2KB stack (vs. 1MB for threads)
- The Go runtime schedules goroutines across available cores
- Context switching happens in user space, not kernel space
- M:N threading model (M goroutines on N threads)

**Why People Believe It**: Goroutines execute concurrently like threads. The terminology is similar.

**Evidence**: 
- Go can run millions of goroutines simultaneously
- `GOMAXPROCS` controls OS thread count
- Goroutines communicate via channels, not shared memory

**Interview Relevance**: Explain the M:N model. Discuss goroutine lifecycle, scheduling, and comparison to OS threads. Mention memory overhead.

---

## 4. Channels are Always Better Than Mutexes

**Myth**: Channels should replace mutexes in all concurrent scenarios.

**Reality**: Choose based on the problem:
- **Channels**: Communicating data between goroutines, pipeline patterns
- **Mutexes**: Protecting shared state, simple concurrent access
- **Atomic operations**: Simple counters, flags

```go
// Mutex appropriate
var mu sync.Mutex
var counter int

// Channel appropriate
ch := make(chan int)
go func() { ch <- compute() }()
```

**Why People Believe It**: "Don't communicate by sharing memory; share memory by communicating" is often misinterpreted as "always use channels."

**Evidence**: 
- Go's sync package provides Mutex, RWMutex, WaitGroup
- Channels add overhead for simple state protection
- Performance benchmarks show mutexes faster for simple operations

**Interview Relevance**: Discuss tradeoffs. Explain when channels add unnecessary complexity. Give concrete examples of each approach.

---

## 5. Go Has No OOP

**Myth**: Go doesn't support object-oriented programming.

**Reality**: Go supports OOP concepts differently:
- **Encapsulation**: Unexported fields/methods (lowercase)
- **Polymorphism**: Interfaces satisfied implicitly
- **Composition**: Struct embedding replaces inheritance
- **Methods**: Attached to types, not classes

```go
type Animal interface {
    Speak() string
}

type Dog struct{}
func (d Dog) Speak() string { return "Woof" }
```

**Why People Believe It**: Go lacks classes, inheritance, and traditional class hierarchies.

**Evidence**: 
- Go's interfaces enable polymorphism without explicit implementation
- Struct embedding provides composition
- Methods on types enable behavior attachment

**Interview Relevance**: Explain Go's OOP philosophy. Discuss composition vs. inheritance. Show how interfaces enable flexibility without traditional class hierarchies.

---

## 6. Go Error Handling is Verbose

**Myth**: Go's error handling (`if err != nil`) is unnecessarily verbose.

**Reality**: This is intentional design:
- Explicit error handling forces developers to acknowledge failures
- No hidden control flow (no exceptions unwinding stack)
- Error values can be inspected, logged, and wrapped
- Go 1.13+ adds error wrapping (`%w` verb)

**Why People Believe It**: Error handling lines accumulate. Other languages use try-catch with less boilerplate.

**Evidence**: 
- `errors.Is` and `errors.As` enable error inspection
- `fmt.Errorf` with `%w` creates error chains
- `defer` and `recover` handle panics (rare cases)

**Interview Relevance**: Discuss Go's error philosophy (explicit over implicit). Explain error wrapping and inspection. Mention when panics are appropriate.
