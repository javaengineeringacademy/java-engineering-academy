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

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| TWR automatic suppression | No manual addSuppressed(); compiler-enforced | Suppressed exceptions may be overlooked by callers |
| Manual addSuppressed() in finally | Full control over suppression | More code; risk of forgetting to add |
| Aggregating parallel failures | All failures visible; no early termination | Complex handling; callers must inspect multiple exceptions |
| Swallowing suppressed exceptions | Cleaner primary stack trace | Hides close() failures that may be the real problem |

## Common Code Review Comments

- "Check `getSuppressed()` — the `close()` failure may be the real issue."
- "Don't swallow suppressed exceptions — they often contain the root cause."
- "TWR already handles suppression — you don't need manual `addSuppressed()` here."
- "If you're aggregating parallel tasks, use `addSuppressed()` to preserve all failures."
- "Log suppressed exceptions at the same level as the primary — not at a lower level."

## Common Production Mistakes

- **Only logging the primary exception**: TWR adds `close()` failures as suppressed — if you only log `e.getMessage()`, you miss the `IOException` from `close()` that corrupted the resource.
- **Forgetting addSuppressed() in manual cleanup**: `finally { conn.close(); }` — if `close()` throws, the original exception from try is lost. Use `addSuppressed()` or TWR.
- **Not checking getSuppressed() during debugging**: A test fails with an exception that has suppressed causes — the developer fixes the primary and misses the suppressed close failure.
- **Double suppression**: Calling `addSuppressed()` on an exception that TWR already suppressed — produces duplicate suppressed entries.

## When to Escalate

- You are designing a custom resource with complex close semantics — the suppression strategy needs review.
- A production issue involves a suppressed exception that was the actual root cause — the team needs to establish logging and debugging conventions.
- You are building a framework that manages resources and exceptions — the suppression contract should be reviewed.
