# Decision Guide: Production Exception Patterns

## When to Use Each Pattern

### Global Exception Handling
**Use when:** Building REST APIs or any multi-endpoint application.
**Skip when:** Writing a simple CLI tool or single-endpoint script.

### Circuit Breaker
**Use when:** Calling external services (payment gateways, third-party APIs,
microservice dependencies).
**Skip when:** All operations are local/in-process with no external calls.

### Retry Pattern
**Use when:** Failures are transient (network timeouts, temporary overload,
database connection issues).
**Skip when:** Failures are deterministic (invalid input, permission denied,
resource not found).

### Graceful Degradation
**Use when:** The operation is non-critical and can tolerate reduced quality
(recommendations, analytics, non-essential features).
**Skip when:** The operation is critical (payment processing, authentication)
and cannot proceed without full functionality.

### Structured Error Responses
**Use when:** Building APIs consumed by other teams or external clients.
**Skip when:** Building internal tools where simple string messages suffice.

## Decision Matrix

| Scenario                               | Handler | Circuit Breaker | Retry | Fallback |
|----------------------------------------|---------|-----------------|-------|----------|
| REST API with external DB              | Yes     | Yes             | Yes   | Yes      |
| Microservice calling payment gateway   | Yes     | Yes             | Yes   | Yes      |
| CLI batch processor                    | No      | No              | Yes   | No       |
| Internal dashboard                     | Yes     | No              | No    | No       |
| Notification service (email/SMS)       | Yes     | Yes             | Yes   | Yes      |
| File upload service                    | Yes     | No              | No    | No       |

## Quick Rules

- **Every API** needs a global exception handler
- **Every external call** needs a circuit breaker
- **Every transient failure** needs a retry policy
- **Every critical path** needs a fallback
- **Every error response** needs a correlation ID
