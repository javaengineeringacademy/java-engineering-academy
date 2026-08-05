# PHP Best Practices

## Code Quality

1. Always use strict types at the top of files: `declare(strict_types=1);`
2. Use type declarations for parameters and return values
3. Follow PSR-12 coding standards for consistent formatting
4. Use Composer autoloading instead of manual `require` statements
5. Write unit tests with PHPUnit and aim for high coverage

## Security

6. Use prepared statements for all database queries
7. Escape all output with `htmlspecialchars()` to prevent XSS
8. Hash passwords with `password_hash()` and verify with `password_verify()`
9. Validate and sanitize all user input using filters
10. Implement CSRF tokens for all state-changing operations

## Architecture

11. Follow PSR-4 autoloading standards for namespace organization
12. Use dependency injection instead of creating objects directly
13. Keep controllers thin and move business logic to service classes
14. Separate concerns: controllers, models, services, repositories
15. Use interfaces for loose coupling between components

## Performance

16. Enable OPcache in production with proper tuning
17. Use APCu for in-memory caching of frequently accessed data
18. Implement database query caching for read-heavy operations
19. Use generators for processing large datasets to reduce memory usage
20. Profile with Xdebug or Blackfire before optimizing code

## Error Handling

21. Use exceptions instead of returning false or null for errors
22. Log errors with context information for debugging
23. Set custom exception handlers for production applications
24. Never expose stack traces or sensitive data in error messages
25. Use `try-catch-finally` blocks for proper resource cleanup

## Development

26. Use Composer scripts for common tasks (test, lint, format)
27. Set up pre-commit hooks with PHP_CodeSniffer or PHP-CS-Fixer
28. Use static analysis tools like PHPStan or Psalm
29. Document public APIs with PHPDoc blocks
30. Keep dependencies updated with `composer update`
