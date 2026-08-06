# Functions in Go

Functions are first-class citizens in Go with support for multiple returns, named returns, and variadic arguments.

## Basic Function

```go
func add(a int, b int) int {
    return a + b
}
```

## Multiple Return Values

```go
func divide(a, b float64) (float64, error) {
    if b == 0 {
        return 0, fmt.Errorf("division by zero")
    }
    return a / b, nil
}
```

## Named Return Values

```go
func swap(a, b int) (x, y int) {
    x = b
    y = a
    return // Naked return
}
```

## Variadic Functions

```go
func sum(nums ...int) int {
    total := 0
    for _, n := range nums {
        total += n
    }
    return total
}

// Usage
sum(1, 2, 3)
sum(1, 2, 3, 4, 5)
```

## Anonymous Functions

```go
add := func(a, b int) int {
    return a + b
}
```

## Key Points
- Functions can be assigned to variables
- Closures capture outer variables
- Defer statement for cleanup
- Method receivers for OOP-style code
