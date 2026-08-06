# Go Best Practices

## Code Organization
- Use meaningful package names
- Keep packages small and focused
- Avoid package-level state
- Use internal packages for private code
- Follow Go naming conventions

## Error Handling
- Check errors immediately
- Use `fmt.Errorf` with `%w` for wrapping
- Create custom error types for complex cases
- Use sentinel errors for known conditions
- Return early on errors

## Concurrency
- Prefer channels over mutexes
- Use WaitGroup for synchronization
- Use context for cancellation
- Avoid goroutine leaks
- Use sync.Pool for object reuse

## Performance
- Profile before optimizing
- Use benchmarks to measure
- Avoid unnecessary allocations
- Use strings.Builder for concatenation
- Use sync.Pool for frequently allocated objects

## Testing
- Write table-driven tests
- Use t.Helper() in helpers
- Use t.Parallel() for concurrent tests
- Test edge cases
- Use testify for assertions (optional)

## Code Style
- Use gofmt for formatting
- Use goimports for imports
- Keep functions short
- Use early returns
- Prefer composition over inheritance

## Documentation
- Write clear package comments
- Document exported functions
- Use examples in tests
- Keep README updated
- Use godoc format
