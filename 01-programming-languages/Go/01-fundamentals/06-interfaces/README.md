# Interfaces in Go

Interfaces define behavior through method sets. Go uses implicit implementation (duck typing).

## Interface Definition

```go
type Writer interface {
    Write([]byte) (int, error)
}

type Reader interface {
    Read([]byte) (int, error)
}
```

## Implicit Implementation

```go
type MyWriter struct {
    data []byte
}

func (w *MyWriter) Write(p []byte) (int, error) {
    w.data = append(w.data, p...)
    return len(p), nil
}

// MyWriter implements Writer without explicit declaration
```

## Interface Composition

```go
type ReadWriter interface {
    Reader
    Writer
}
```

## Empty Interface

```go
// Accepts any type
func printAny(v interface{}) {
    fmt.Println(v)
}
```

## Type Assertions

```go
var i interface{} = "hello"
s, ok := i.(string) // Type assertion with comma-ok
```

## Key Points
- Interfaces are satisfied implicitly
- Small interfaces preferred (1-3 methods)
- Interface values have type and value
- nil interface vs interface with nil value
- Zero value of interface is nil
