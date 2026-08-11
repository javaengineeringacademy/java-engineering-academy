# Decision Guide

## When to Use Suppressed Exceptions

### Automatic (TWR) — Use By Default

- Always use try-with-resources for `AutoCloseable` resources
- TWR handles suppressed exceptions automatically
- This is the standard, idiomatic approach

### Manual `addSuppressed()` — Use Only When Needed

- Custom resource management without TWR
- Parallel operation aggregation
- Wrapper code that must preserve all exceptions

## When NOT to Use Suppressed Exceptions

- Do not suppress exceptions to hide errors
- Do not use suppressed exceptions for flow control
- Do not add suppressed exceptions when a cause is more appropriate

## Debugging Decision

- Always check `getSuppressed()` when debugging failures
- Log suppressed exceptions, not just the primary
- In production, include suppressed exceptions in error reports

## Key Questions

| Question | Answer |
|----------|--------|
| Resource cleanup fails? | TWR adds it as suppressed automatically |
| Need to preserve cleanup failure? | Use `addSuppressed()` in finally block |
| Running parallel tasks? | Aggregate failures with `addSuppressed()` |
| Exception wrapping? | Use cause chaining, not suppressed |
| Debugging a failure? | Always check `getSuppressed()` |
