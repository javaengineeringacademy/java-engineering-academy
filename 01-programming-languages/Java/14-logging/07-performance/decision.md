# Decision Framework: Performance Optimization

## When to Optimize Logging

### Optimize When:

- **Logging is > 5% of CPU** - Profiling shows logging overhead
- **Latency spikes correlate with log writes** - I/O blocking
- **Log volume exceeds 10K events/sec** - Throughput bottleneck
- **GC pauses involve log objects** - Memory pressure
- **Disk I/O is saturated** - Write amplification

### Don't Optimize When:

- **Logging is < 1% of CPU** - Negligible impact
- **Low log volume** - < 1K events/sec
- **Development environment** - Optimization adds complexity
- **Readability is critical** - Simpler code may be better

## Sync vs Async Decision

| Scenario | Recommendation | Reason |
|----------|---------------|--------|
| High throughput (>1K/sec) | Async | Non-blocking I/O |
| Low latency requirements | Async | Avoids I/O jitter |
| Debugging/development | Sync | Easier to trace |
| Console output only | Sync | Minimal I/O |
| File/network output | Async | Batches I/O |
| Mixed workloads | Async with fallback | Best of both |

## Level Check Strategy

| Approach | When to Use |
|----------|-------------|
| `if (logger.isDebugEnabled())` | Before expensive operations only |
| Parameterized logging | Default for all logging |
| No guard | Simple, low-cost messages |
| `@Enabled` annotation | Declarative, framework-managed |

## Buffer Configuration

| Buffer Size | Tradeoff |
|-------------|----------|
| Small (4KB) | Less memory, more syscalls |
| Medium (8KB) | Balanced |
| Large (16KB+) | More memory, fewer syscalls |
| No buffer (immediateFlush=true) | Safest, slowest |

## Async Queue Sizing

| Queue Size | Behavior |
|------------|----------|
| Small (64) | Low memory, may block |
| Medium (256) | Balanced default |
| Large (1024+) | More memory, fewer drops |
| discardingThreshold=0 | Never discard (may block) |

## Performance Testing Strategy

1. **Baseline measurement** - Log current performance
2. **Identify bottlenecks** - Profile logging code
3. **Apply optimizations** - One change at a time
4. **Re-measure** - Compare before/after
5. **Monitor in production** - Ensure improvements persist
