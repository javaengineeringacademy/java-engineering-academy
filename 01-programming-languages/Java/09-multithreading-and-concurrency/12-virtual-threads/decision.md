# Virtual Threads Decision Guide

## Platform vs Virtual Threads

| Use Case | Platform | Virtual |
|----------|----------|---------|
| CPU-bound, few tasks | Yes | No |
| I/O-bound, many tasks | No | Yes |
| Long-running native code | Yes | No |
| Simple migration from blocking | No | Yes |
| Thread-local resources | Yes | Yes (with care) |

## Virtual Thread Best Practices

1. **One task per thread** — no pooling needed
2. **Don't pool virtual threads** — they're cheap to create
3. **Avoid synchronized** — use Lock instead (synchronized blocks carrier)
4. **Use thread-local carefully** — virtual threads may be created in millions
5. **Monitor carrier threads** — pinning detection

## When NOT to Use Virtual Threads

- CPU-bound work (use platform thread pools)
- Tasks requiring thread affinity
- Native/JNI calls that block
- Very long-lived threads (use platform threads)
