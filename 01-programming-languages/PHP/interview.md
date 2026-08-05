# PHP Interview Questions

## Basics

1. **What are the main differences between PHP 7 and PHP 8?**
PHP 8 introduces JIT compilation, union types, attributes, named arguments, match expression, constructor property promotion, and null safe operator.

2. **Explain the difference between `==` and `===` operators.**
`==` performs type coercion (loose comparison), `===` checks type and value without conversion (strict comparison).

3. **What is the difference between `include`, `require`, `include_once`, and `require_once`?**
`require` and `require_once` throw fatal errors on failure, `include` and `include_once` emit warnings. `*_once` versions ensure files are included only once.

4. **How does PHP handle memory management?**
PHP uses reference counting for variable lifecycle and a garbage collector to clean up circular references. Memory is freed at the end of each request.

## OOP

5. **What is the difference between an abstract class and an interface?**
Abstract classes can have implementation details and concrete methods. Interfaces define contracts only. PHP 8 allows interfaces to have default methods.

6. **Explain the purpose of traits in PHP.**
Traits allow code reuse in single inheritance languages. They provide methods that can be used in multiple classes without requiring inheritance hierarchies.

7. **What are namespaces and why are they important?**
Namespaces organize code into logical groups and prevent naming conflicts. They enable PSR-4 autoloading and better code organization.

8. **What is dependency injection and how does PHP support it?**
Dependency injection passes dependencies as parameters rather than creating them internally. PHP supports constructor injection, method injection, and property injection.

## Security

9. **How do you prevent SQL injection in PHP?**
Use PDO or MySQLi prepared statements with parameterized queries. Never concatenate user input directly into SQL strings.

10. **What is XSS and how do you prevent it?**
Cross-Site Scripting injects malicious scripts. Prevent by escaping all output with `htmlspecialchars()` and implementing Content Security Policy headers.

11. **How should passwords be stored in PHP?**
Use `password_hash()` with `PASSWORD_BCRYPT` or `PASSWORD_ARGON2I`. Never store plain text passwords or use MD5/SHA1.

12. **What is CSRF and how do you protect against it?**
Cross-Site Request Forgery tricks users into performing unwanted actions. Use unique, unpredictable tokens in all forms and validate them server-side.

## Performance

13. **What is OPcache and how does it improve performance?**
OPcache caches compiled PHP bytecode in shared memory, eliminating the need to parse and compile scripts on every request.

14. **Explain the difference between `apc_store` and `apcu_store`.**
`apc_store` was deprecated. `apcu_store` is the current APCu function for user-land caching in PHP 7+.

15. **How do you profile a PHP application?**
Use Xdebug for step debugging, Blackfire for profiling, or New Relic for production monitoring. Check slow query logs and enable OPcache statistics.
