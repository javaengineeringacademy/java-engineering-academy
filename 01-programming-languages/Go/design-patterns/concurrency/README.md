# Go Concurrency Patterns

## Overview

Go's goroutines and channels enable powerful concurrency patterns that are difficult or impossible in other languages. These patterns are fundamental to writing efficient, concurrent Go programs.

## Patterns

| Pattern | Description | Key Mechanism |
|---------|-------------|---------------|
| Fan-in/Fan-out | Distribute work, merge results | Multiple goroutines writing to one channel |
| Pipeline | Stage-by-stage processing | Channels connecting goroutine stages |
| Worker Pool | Fixed goroutine pool processing jobs | Buffered channel with N workers |
| Goroutine | Lightweight concurrent execution | `go` keyword with functions |
| Context | Cancellation and timeout | `context.Context` propagation |
| Rate Limiting | Control execution rate | `time.Ticker` or token bucket |
| Semaphore | Limit concurrent access | Buffered channel as semaphore |

## Go Concurrency Primitives

- **Goroutines**: Lightweight threads managed by Go runtime
- **Channels**: Typed communication channels between goroutines
- **Select**: Multiplexing channel operations
- **sync包**: Mutex, WaitGroup, Once, Pool
- **Context**: Cancellation, timeouts, and request-scoped values

## Best Practices

- Prefer channels over shared memory
- Always cancel contexts when done
- Use `sync.WaitGroup` for waiting on goroutines
- Avoid goroutine leaks by ensuring they terminate
- Use buffered channels when possible
- Profile with `go tool pprof` for concurrency issues

## References

- "Concurrency in Go" - Katherine Cox-Buday
- Go Blog: "Go Concurrency Patterns"
- Go Blog: "Advanced Go Concurrency Patterns"
- Rob Pike: "Go Proverbs"
