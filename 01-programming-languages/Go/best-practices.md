# Go Best Practices

## Error Handling

1. Always check errors explicitly, never ignore them
2. Wrap errors with context using `fmt.Errorf("context: %w", err)`
3. Use sentinel errors for expected conditions
4. Create custom error types for complex scenarios
5. Use `errors.Is` and `errors.As` for error comparison

## Code Organization

6. Keep packages small and focused on a single responsibility
7. Avoid package-level variables that hold mutable state
8. Use dependency injection instead of global variables
9. Place tests next to source files (`foo.go` and `foo_test.go`)
10. Use `internal/` for private code that should not be imported

## Performance

11. Pre-allocate slices and maps when size is known
12. Use `sync.Pool` for frequently allocated objects
13. Avoid string concatenation in loops, use `strings.Builder`
14. Use buffered channels for batching operations
15. Profile before optimizing, use benchmarks to measure

## Concurrency

16. Never start goroutines in `init()` functions
17. Use `sync.WaitGroup` for goroutine coordination
18. Use channels over mutexes when possible
19. Implement graceful shutdown with context cancellation
20. Limit goroutine count with worker pools

## Design Patterns

21. Accept interfaces, return structs
22. Use composition over inheritance
23. Keep zero values useful
24. Use functional options for configuration
25. Prefer explicit over implicit behavior

## Testing

26. Write table-driven tests for clarity
27. Use `t.Helper()` in test helper functions
28. Use `t.Parallel()` for concurrent tests
29. Test edge cases and error conditions
30. Use benchmarks to prevent performance regressions

## Code Quality

31. Use `golangci-lint` for static analysis
32. Run `go vet` regularly
33. Keep functions short and focused
34. Document exported functions and types
35. Use meaningful variable and function names
