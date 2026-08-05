# JavaScript Best Practices

## Code Style

1. Use `const` by default, `let` when reassignment needed, avoid `var`
2. Use arrow functions for callbacks and anonymous functions
3. Use template literals over string concatenation
4. Use destructuring for object and array access
5. Use spread operator for copying arrays and objects

## Functions

6. Keep functions small and focused on a single task
7. Use default parameters instead of checking for undefined
8. Use rest parameters instead of arguments object
9. Return early to reduce nesting
10. Use pure functions when possible

## Objects and Arrays

11. Use object shorthand notation `{ name, age }` instead of `{ name: name, age: age }`
12. Use array methods (map, filter, reduce) over loops
13. Use optional chaining (`?.`) for safe property access
14. Use nullish coalescing (`??`) for default values
15. Use Object.freeze for immutable data

## Async Code

16. Prefer async/await over raw Promises
17. Always handle Promise rejections
18. Use Promise.all for concurrent operations
19. Avoid async functions in callbacks
20. Use try/catch with async/await for error handling

## Modules

21. Use ES modules (import/export) over CommonJS
22. Import modules at the top of the file
23. Use named exports for better tree shaking
24. Avoid default exports for large modules
25. Use barrel files (index.js) for re-exports

## Testing

26. Write tests alongside code
27. Use descriptive test names
28. Mock external dependencies
29. Test edge cases and error conditions
30. Use beforeEach/afterEach for setup/cleanup

## Security

31. Never store secrets in client-side code
32. Validate all user inputs
33. Sanitize HTML to prevent XSS
34. Use Content Security Policy headers
35. Implement proper authentication and authorization

## Performance

36. Debounce expensive event handlers
37. Lazy load images and components
38. Use requestAnimationFrame for animations
39. Avoid layout thrashing in DOM operations
40. Profile before optimizing
