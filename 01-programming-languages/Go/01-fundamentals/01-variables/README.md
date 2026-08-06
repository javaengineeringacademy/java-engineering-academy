# Variables in Go

Go provides multiple ways to declare and initialize variables with strong static typing.

## Declaration Methods

```go
// var declaration with explicit type
var name string = "Go"

// var declaration with type inference
var age = 25

// Multiple declarations
var (
    x int
    y float64
)

// Short declaration (inside functions only)
count := 10

// Constants
const Pi = 3.14159
const (
    StatusOK = 200
    StatusNotFound = 404
)
```

## Zero Values

When variables are declared without initialization, they get zero values:
- `0` for numeric types
- `""` for strings
- `false` for booleans
- `nil` for pointers, slices, maps, channels, interfaces, functions

## Type Conversions

Go requires explicit type conversions:

```go
var i int = 42
var f float64 = float64(i)
var u uint = uint(f)
```

## Key Points
- Variables must be used (compiler error otherwise)
- `:=` only works inside functions
- Constants are evaluated at compile time
- Package-level variables can be initialized with expressions
- Use `_` for blank identifier (discard values)
