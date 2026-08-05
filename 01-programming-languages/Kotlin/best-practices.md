# Kotlin Best Practices

## Null Safety

1. Use nullable types only when a value can genuinely be absent
2. Prefer safe calls (`?.`) over force unwrap (`!!`)
3. Use Elvis operator (`?:`) to provide sensible defaults
4. Use `let` for scoped operations on nullable values
5. Validate input at API boundaries using `require` and `check`

## Immutability

6. Use `val` instead of `var` for immutable references
7. Prefer immutable collections (`listOf`, `mapOf`) over mutable ones
8. Use data classes for value types to get equals, hashCode, and copy
9. Use sealed classes to model restricted type hierarchies
10. Avoid side effects in functions when possible

## Coroutines

11. Never use `GlobalScope` in production code
12. Use structured concurrency with `coroutineScope` or `supervisorScope`
13. Choose appropriate dispatchers for different work types
14. Use `withContext` to switch dispatchers for blocking operations
15. Handle exceptions with `CoroutineExceptionHandler`

## Code Organization

16. Use extension functions instead of utility classes
17. Keep classes small and focused on a single responsibility
18. Use interfaces for contracts and abstractions
19. Group related functions in companion objects or files
20. Use `object` declarations for singletons

## Performance

21. Use `inline` functions to avoid lambda allocation overhead
22. Use `crossinline` when inline lambdas should not use non-local returns
23. Use sequences for lazy evaluation on large collections
24. Avoid creating unnecessary intermediate collections
25. Use `@JvmStatic` for Java interop performance

## Error Handling

26. Use `Result` type instead of exceptions for expected failures
27. Use custom exception types for domain-specific errors
28. Log exceptions with context for debugging
29. Use `runCatching` for safe exception handling
30. Never catch generic `Exception` without rethrowing
