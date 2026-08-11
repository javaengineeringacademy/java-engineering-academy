# Stack Trace — Decision Guide

## When to Capture Full Stack Traces

| Situation                          | Capture Trace? | Why                                      |
|------------------------------------|----------------|-------------------------------------------|
| Unexpected exception in production | Yes            | Diagnostics for post-mortem               |
| Validated user input               | No             | Use message, not stack trace              |
| Framework/wrapper exception        | Yes            | Preserve original context                 |
| High-throughput hot path           | Maybe          | Sample or skip depending on volume        |
| Control flow via exceptions        | No             | Avoid entirely — use return values        |

## When to Suppress Stack Traces

- **LightweightException** pattern: predictable errors with no diagnostic value.
- **Retries**: transient network/DB errors where the caller already logged context.
- **Performance-critical loops**: exception per iteration → replace with null check.

## When to Filter Frames

- **Logs**: hide `java.lang.*`, `sun.*`, `jdk.internal.*` — application frames matter most.
- **Error services**: send full trace; let the UI filter.
- **User-facing messages**: show only your code; hide internals.

## When to Use setStackTrace()

- Wrapping checked exceptions into custom unchecked exceptions.
- Cleaning up framework-internal frames before re-throwing.
- Replacing trace with empty array for performance.
- Enriching exception with additional context (e.g., correlation ID).

## Key Questions

1. **Is this exception exceptional?** If yes, capture. If no, fix the control flow.
2. **Will this be logged?** If yes, optimize the trace for readability.
3. **Is stack depth predictable?** If deep, consider suppression or sampling.
4. **Is this user-facing?** If yes, filter internals before exposing.

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Full stack traces in production | Complete diagnostic context; root cause visible | Performance cost on hot paths; larger log volumes |
| Suppressing stack traces | Faster exception creation; smaller logs | Lost diagnostic context; harder to debug |
| Filtering framework frames | Cleaner logs; application code visible | May hide framework bugs; less information |
| Using setStackTrace() for cleanup | Cleaner traces; removes noise | Manual maintenance; risk of losing important frames |

## Common Code Review Comments

- "This exception has no stack trace — how will we debug it in production?"
- "Don't call `e.printStackTrace()` — use a logging framework."
- "This stack trace is 200 frames deep — filter the JDK internal frames."
- "You're creating exceptions in a tight loop — check if the stack trace cost is acceptable."
- "Log the stack trace once at the catch site, not at every re-throw."

## Common Production Mistakes

- **Using `e.printStackTrace()` instead of a logger**: Writes to stderr, not structured logging; lost in production; no correlation with request IDs.
- **Suppressing stack traces for "performance"**: Creating exceptions without traces in production — when the "fast" path fails, debugging is impossible.
- **Not logging the stack trace at the boundary**: Logging only `e.getMessage()` — the root cause is lost; you cannot determine where the exception originated.
- **Stack trace pollution from proxies/reflection**: Deep JDK/framework frames obscure the actual application code — filter for readability.

## When to Escalate

- You are designing a custom exception class with special stack trace behavior (e.g., lightweight exceptions for hot paths) — the trade-offs need review.
- A production debugging issue is caused by missing or filtered stack traces — the team needs to agree on trace policy.
- You are building an error reporting service that processes stack traces — the aggregation and filtering strategy needs architectural input.
