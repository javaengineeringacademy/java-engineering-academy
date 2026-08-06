# Pointers in Go

Pointers store memory addresses. Go has pointers but no pointer arithmetic.

## Pointer Basics

```go
x := 42
p := &x      // p is *int, points to x
fmt.Println(*p) // 42 (dereference)
*p = 100     // modify x through pointer
```

## new Function

```go
p := new(int)    // *int pointing to zero-valued int
p = new(string)  // *string pointing to ""
```

## Pointer Receivers

```go
type Counter struct {
    value int
}

func (c *Counter) Increment() {
    c.value++ // Modifies original
}

func (c Counter) Value() int {
    return c.value // Copy
}
```

## Key Points
- No pointer arithmetic (safe)
- Automatic dereferencing in many cases
- Pass by value (copies data)
- Use pointers for mutation
- Garbage collected (no manual free)
- `nil` is zero value for pointers
