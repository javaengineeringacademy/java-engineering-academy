# Goroutines in Go

Goroutines are lightweight threads managed by the Go runtime. They enable concurrent execution.

## Starting a Goroutine

```go
go function()
go func() {
    // anonymous function
}()
```

## WaitGroup

```go
var wg sync.WaitGroup

for i := 0; i < 5; i++ {
    wg.Add(1)
    go func(id int) {
        defer wg.Done()
        fmt.Printf("Goroutine %d\n", id)
    }(i)
}

wg.Wait() // Block until all goroutines complete
```

## Key Points
- Goroutines start with `go` keyword
- Extremely lightweight (~2KB stack)
- Communicate via channels
- Use WaitGroup for synchronization
- Main goroutine exits when done
- No built-in way to kill a goroutine

## Common Patterns
- Fan-out/fan-in
- Worker pools
- Pipeline processing
