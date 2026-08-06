# Unit Testing in Go

Go has built-in testing support via the `testing` package and `go test` command.

## Test Functions

```go
func TestAdd(t *testing.T) {
    result := Add(2, 3)
    if result != 5 {
        t.Errorf("Add(2, 3) = %d; want 5", result)
    }
}
```

## Table-Driven Tests

```go
func TestAdd(t *testing.T) {
    tests := []struct {
        a, b, want int
    }{
        {1, 2, 3},
        {-1, 1, 0},
        {0, 0, 0},
    }

    for _, tt := range tests {
        t.Run(fmt.Sprintf("%d+%d", tt.a, tt.b), func(t *testing.T) {
            if got := Add(tt.a, tt.b); got != tt.want {
                t.Errorf("Add(%d, %d) = %d; want %d", tt.a, tt.b, got, tt.want)
            }
        })
    }
}
```

## Run Tests

```bash
go test ./...
go test -v ./...
go test -run TestAdd
```

## Key Points
- Test files end with `_test.go`
- Test functions start with `Test`
- Use `t.Error`/`t.Fatal` for failures
- Table-driven tests are idiomatic
- Use `t.Helper()` in helper functions
- Use `t.Parallel()` for concurrent tests
