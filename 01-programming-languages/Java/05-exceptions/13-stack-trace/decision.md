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
