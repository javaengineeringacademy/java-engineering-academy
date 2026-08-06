# Go Anti-Patterns

Common mistakes and anti-patterns to avoid in Go.

## 1. Ignoring Errors

```go
// BAD
result, _ := doSomething()

// GOOD
result, err := doSomething()
if err != nil {
    return fmt.Errorf("doSomething: %w", err)
}
```

## 2. Using panic for errors

```go
// BAD
if err != nil {
    panic(err)
}

// GOOD
if err != nil {
    return fmt.Errorf("context: %w", err)
}
```

## 3. goroutine leaks

```go
// BAD - goroutine runs forever
func leak() {
    ch := make(chan int)
    go func() {
        val := <-ch // Blocks forever
        fmt.Println(val)
    }()
}

// GOOD - use context or done channel
func noLeak(ctx context.Context) {
    ch := make(chan int)
    go func() {
        select {
        case val := <-ch:
            fmt.Println(val)
        case <-ctx.Done():
            return
        }
    }()
}
```

## 4. Not using defer for cleanup

```go
// BAD
f, _ := os.Open("file.txt")
// ... use file
f.Close()

// GOOD
f, _ := os.Open("file.txt")
defer f.Close()
```

## 5. Empty interface abuse

```go
// BAD - loses type safety
func process(data interface{}) { }

// GOOD - use generics or specific types
func process(data string) { }
```

## 6. String concatenation in loops

```go
// BAD
result := ""
for _, s := range strings {
    result += s
}

// GOOD
var builder strings.Builder
for _, s := range strings {
    builder.WriteString(s)
}
result := builder.String()
```

## 7. Not checking len before indexing

```go
// BAD - may panic
val := slice[0]

// GOOD
if len(slice) > 0 {
    val := slice[0]
}
```

## 8. Using sync.Mutex instead of channels

```go
// BAD - sharing memory
var mu sync.Mutex
var counter int

// GOOD - communicate by sharing memory
ch := make(chan int)
go func() {
    ch <- counter + 1
}()
```

## Best Practices Summary
- Always check errors
- Use defer for cleanup
- Prefer channels over mutexes when possible
- Keep interfaces small
- Use context for cancellation
- Don't ignore goroutine lifecycle
