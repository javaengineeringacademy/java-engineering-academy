# Error Handling in Go

Go uses explicit error handling with the `error` interface instead of exceptions.

## Error Interface

```go
type error interface {
    Error() string
}
```

## Creating Errors

```go
// Simple error
err := errors.New("something went wrong")

// Formatted error
err := fmt.Errorf("invalid value: %d", val)

// Custom error type
type ValidationError struct {
    Field   string
    Message string
}

func (e *ValidationError) Error() string {
    return fmt.Sprintf("%s: %s", e.Field, e.Message)
}
```

## Error Checking

```go
result, err := doSomething()
if err != nil {
    return fmt.Errorf("context: %w", err)
}
```

## errors.Is and errors.As

```go
if errors.Is(err, os.ErrNotExist) {
    fmt.Println("File not found")
}

var pathError *os.PathError
if errors.As(err, &pathError) {
    fmt.Println("Path error:", pathError.Path)
}
```

## Key Points
- Check errors immediately after call
- Use `%w` to wrap errors
- Use `errors.Is` for comparison
- Use `errors.As` for type assertion
- Return nil for no error
- Errors are values (can be stored/passed)
