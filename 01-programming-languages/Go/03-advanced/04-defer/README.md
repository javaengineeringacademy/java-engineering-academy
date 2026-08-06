# Defer, Panic, and Recover in Go

Go provides `defer`, `panic`, and `recover` for cleanup and error handling.

## Defer

```go
func readFile(name string) error {
    f, _ := os.Open(name)
    defer f.Close() // Executes when function returns

    // process file...
    return nil
}
```

## Panic

```go
func divide(a, b int) int {
    if b == 0 {
        panic("division by zero")
    }
    return a / b
}
```

## Recover

```go
func safeDivide(a, b int) (result int, err error) {
    defer func() {
        if r := recover(); r != nil {
            err = fmt.Errorf("recovered: %v", r)
        }
    }()
    return a / b, nil
}
```

## Key Points
- Defers execute in LIFO order
- Defers execute before function returns
- Panic stops normal execution
- Recover catches panics
- Use recover in deferred functions
- Don't use panic for normal errors
