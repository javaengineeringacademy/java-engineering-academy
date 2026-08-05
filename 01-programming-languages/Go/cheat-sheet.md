# Go Cheat Sheet

## Variables

```go
var x int
var y string = "hello"
var z = 42
w := "short declaration"

const Pi = 3.14
const (
    StatusOK = 200
    StatusNotFound = 404
)
```

## Types

```go
bool, string
int, int8, int16, int32, int64
uint, uint8, uint16, uint32, uint64
float32, float64
complex64, complex128
byte (uint8), rune (int32)
```

## Collections

```go
arr := [5]int{1, 2, 3, 4, 5}
slice := []int{1, 2, 3}
slice = append(slice, 4)
m := map[string]int{"one": 1, "two": 2}
delete(m, "one")
```

## Structs

```go
type Person struct {
    Name string
    Age  int
}

p := Person{Name: "Alice", Age: 30}
p.Name = "Bob"

func (p Person) Greet() string {
    return "Hello, " + p.Name
}
```

## Interfaces

```go
type Writer interface {
    Write(p []byte) (n int, err error)
}

var w Writer = os.Stdout
```

## Control Flow

```go
if x > 0 {
    // positive
} else if x == 0 {
    // zero
} else {
    // negative
}

for i := 0; i < 10; i++ {}
for _, v := range slice {}
for condition {}
for {}

switch x {
case 1:
case 2, 3:
default:
}
```

## Functions

```go
func add(a, b int) int {
    return a + b
}

func divide(a, b float64) (float64, error) {
    if b == 0 {
        return 0, errors.New("division by zero")
    }
    return a / b, nil
}
```

## Goroutines and Channels

```go
go func() { /* concurrent */ }()

ch := make(chan int)
ch <- 42
v := <-ch

select {
case v := <-ch1:
case ch2 <- 5:
default:
}
```

## Error Handling

```go
result, err := doSomething()
if err != nil {
    return fmt.Errorf("context: %w", err)
}

var target *MyError
if errors.As(err, &target) {
    // handle MyError
}

if errors.Is(err, sql.ErrNoRows) {
    // handle specific error
}
```

## Defer, Panic, Recover

```go
defer file.Close()
defer func() {
    if r := recover(); r != nil {
        log.Println("recovered:", r)
    }
}()
panic("something went wrong")
```

## Testing

```go
func TestAdd(t *testing.T) {
    got := add(1, 2)
    want := 3
    if got != want {
        t.Errorf("add(1, 2) = %d, want %d", got, want)
    }
}

func BenchmarkAdd(b *testing.B) {
    for i := 0; i < b.N; i++ {
        add(1, 2)
    }
}
```

## Common Packages

```go
import (
    "fmt"      // Print, Sprintf
    "log"      // Logger
    "os"       // OS operations
    "io"       // I/O interfaces
    "net/http" // HTTP client/server
    "strings"  // String manipulation
    "strconv"  // String conversion
    "time"     // Time operations
    "context"  // Request context
    "sync"     // Synchronization
)
```
