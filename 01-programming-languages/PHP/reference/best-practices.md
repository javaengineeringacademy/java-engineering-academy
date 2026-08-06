# PHP Best Practices

## Security
- Use prepared statements for SQL queries
- Sanitize user input with `filter_var`
- Use `password_hash` for passwords
- Enable strict mode in PHP 8

## Code Organization
- Use namespaces for autoloading
- Follow PSR-4 directory structure
- Keep classes small and focused

## Type Safety
- Use strict types: `declare(strict_types=1)`
- Add type hints to all functions
- Use union/intersection types in PHP 8

## Performance
- Use `opcache` in production
- Prefer `isset` over `array_key_exists`
- Use generators for large datasets

## Testing
- Use PHPUnit for unit tests
- Mock dependencies in tests
- Test edge cases and error conditions
