# Go Core Concepts

## Basic Types

- `bool`, `string`
- `int`, `int8`, `int16`, `int32`, `int64`
- `uint`, `uint8`, `uint16`, `uint32`, `uint64`
- `float32`, `float64`
- `complex64`, `complex128`
- `byte` (alias for uint8)
- `rune` (alias for int32, represents Unicode code point)

## Composite Types

- **Arrays**: Fixed-size, value type `[N]T`
- **Slices**: Dynamic views into arrays `[]T`
- **Maps**: Key-value pairs `map[K]V`
- **Structs**: Named fields `type Person struct { Name string }`
- **Pointers**: `*T` for address-of, `&` for dereference
- **Functions**: `func(a int, b int) (int, error)`
- **Channels**: `chan T`, `chan<- T`, `<-chan T`

## Structs and Methods

```go
type Server struct {
    host string
    port int
}

func (s *Server) Address() string {
    return fmt.Sprintf("%s:%d", s.host, s.port)
}
```

- Value receivers create copies
- Pointer receivers modify the original
- Embedding enables composition over inheritance

## Interfaces

Interfaces are implicitly implemented:

```go
type Writer interface {
    Write(p []byte) (n int, err error)
}
```

- Empty interface `interface{}` accepts any type
- Type assertions check and convert: `v, ok := i.(Type)`
- Type switches handle multiple types
- Interface values store type and value pairs
- Satisfy interfaces implicitly without explicit declaration

## Goroutines

Goroutines are lightweight concurrent functions:

```go
go myFunction()
go func() {
    // anonymous goroutine
}()
```

- Start with 2KB stack, grows dynamically
- Managed by the Go runtime scheduler
- Thousands to millions can run concurrently
- Use channels or sync primitives for communication
- Never start goroutines in `init()`

## Channels

Channels enable goroutine communication:

```go
ch := make(chan int)       // unbuffered
ch := make(chan int, 100)  // buffered
ch <- value               // send
value := <-ch             // receive
close(ch)                 // close channel
```

- `range` iterates over channel values
- `select` handles multiple channel operations
- `nil` channels block forever
- Closed channels return zero values
- Send to closed channel panics

## Error Handling

Go uses explicit error returns:

```go
result, err := doSomething()
if err != nil {
    return fmt.Errorf("context: %w", err)
}
```

- Errors are values implementing the `error` interface
- Use `%w` verb for error wrapping
- `errors.Is` and `errors.As` check wrapped errors
- `errors.New` creates simple errors
- Custom error types provide structured errors

## Packages and Modules

```go
package main

import (
    "fmt"
    "myproject/pkg/handler"
)

func main() {
    handler.Serve()
}
```

- `go.mod` defines the module and dependencies
- `go.sum` provides checksums for dependencies
- Exported names start with uppercase letters
- `init()` runs before `main()`
- `go mod tidy` cleans up dependencies

## Built-in Functions

- `make` creates slices, maps, channels
- `new` allocates zero-value memory
- `len` returns length
- `cap` returns capacity
- `append` adds elements to slices
- `copy` copies slice elements
- `delete` removes map entries
- `close` closes channels
- `panic` and `recover` handle runtime errors
