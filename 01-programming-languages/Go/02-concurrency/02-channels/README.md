# Channels in Go

Channels are typed conduits for communication between goroutines. They ensure safe data sharing.

## Channel Types

```go
ch := make(chan int)        // Unbuffered
ch := make(chan int, 10)    // Buffered (capacity 10)
```

## Operations

```go
ch <- value     // Send
value := <-ch   // Receive
close(ch)       // Close channel
```

## Direction

```go
func producer(ch chan<- int) { }  // Send-only
func consumer(ch <-chan int) { }  // Receive-only
```

## Buffered vs Unbuffered

| Feature | Unbuffered | Buffered |
|---------|-----------|----------|
| Blocking | Both send/receive block | Only blocks when full/empty |
| Synchronization | Built-in | Optional |
| Use case | Handoff | Producer/consumer |

## Key Points
- Channels are reference types (use make)
- Close channels from sender side
- Range over channels until closed
- Nil channels block forever
- Closed channels return zero values
- `select` for multiple channel operations
