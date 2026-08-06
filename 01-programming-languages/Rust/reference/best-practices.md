# Rust Best Practices

## Ownership
- Prefer borrowing over cloning
- Use `&str` over `&String` for function parameters
- Return values to transfer ownership

## Error Handling
- Use `Result` over `unwrap` in production code
- Create custom error types with `thiserror` or `anyhow`
- Use the `?` operator for error propagation

## Performance
- Use iterators over loops
- Prefer `&[T]` over `&Vec<T>`
- Use `Cow<'_, str>` for conditional cloning

## Code Organization
- Keep modules small and focused
- Use `pub` sparingly
- Prefer `impl` blocks over free functions

## Testing
- Write unit tests in each module
- Use `#[cfg(test)]` for test modules
- Test edge cases and error conditions

## Common Libraries
- `serde` for serialization
- `tokio` for async runtime
- `clap` for CLI arguments
- `thiserror` for error types
