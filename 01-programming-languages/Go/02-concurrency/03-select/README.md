# Select Statement in Go

`select` multiplexes channel operations, waiting for multiple communications.

## Basic Syntax

```go
select {
case v := <-ch1:
    fmt.Println("Received from ch1:", v)
case ch2 <- value:
    fmt.Println("Sent to ch2")
case <-time.After(time.Second):
    fmt.Println("Timeout")
default:
    fmt.Println("No communication")
}
```

## Timeout Pattern

```go
select {
case result := <-ch:
    fmt.Println(result)
case <-time.After(5 * time.Second):
    fmt.Println("Timed out")
}
```

## Done Channel Pattern

```go
done := make(chan bool)
go func() {
    // work
    done <- true
}()

select {
case <-done:
    fmt.Println("Completed")
case <-ctx.Done():
    fmt.Println("Cancelled")
}
```

## Key Points
- Blocks until one case is ready
- Random selection if multiple cases ready
- `default` makes select non-blocking
- `time.After` creates timeout channels
- Used with context for cancellation
