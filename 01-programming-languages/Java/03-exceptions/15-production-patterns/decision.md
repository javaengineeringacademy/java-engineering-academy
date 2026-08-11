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

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Global exception handler | Consistent error responses; single logging point | May hide endpoint-specific issues |
| Circuit breaker | Prevents cascade failures; fast-fail | Extra infrastructure; requires tuning thresholds |
| Retry with backoff | Handles transient failures automatically | Adds latency; may overwhelm recovering service |
| Graceful degradation | Maintains partial availability | Reduced functionality; may hide underlying issues |
| Structured error responses | Client-friendly; machine-readable | More code; must not leak internal details |

## Common Code Review Comments

- "This endpoint has no global exception handler — errors return raw 500s."
- "You're calling an external service without a circuit breaker — what happens when it's down?"
- "Add exponential backoff to this retry — fixed intervals cause thundering herd."
- "This fallback returns null — that will cause NPEs downstream; return a meaningful default."
- "Don't include stack traces in error responses — log them server-side only."

## Common Production Mistakes

- **No global exception handler**: Each endpoint handles exceptions differently — inconsistent error formats; some return 500, some return 200 with error body.
- **Retrying deterministic failures**: Retrying `IllegalArgumentException` or `400 Bad Request` — wastes resources; only retry transient failures (5xx, timeouts).
- **Circuit breaker with no fallback**: Circuit opens but there's no fallback — callers get an error with no alternative path.
- **Logging exceptions at every layer**: Same exception logged at controller, service, and DAO — triple log entries; log once at the boundary.
- **Exposing internal details in error responses**: Stack traces, SQL state, or class names in API responses — security risk; leaks implementation details.

## When to Escalate

- You are designing the error handling strategy for a microservice architecture — the circuit breaker, retry, and fallback patterns need architectural review.
- A production incident reveals cascading failures — the architect needs to review the resilience patterns.
- You are building a shared error handling library for multiple teams — the contract and conventions need agreement.
