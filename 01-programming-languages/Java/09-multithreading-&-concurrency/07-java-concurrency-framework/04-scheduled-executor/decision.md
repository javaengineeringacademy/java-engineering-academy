# Scheduled Executor - Decision Guide

## ScheduledExecutorService vs Timer

| Criteria | ScheduledExecutorService | Timer |
|----------|------------------------|-------|
| Single thread | No (configurable pool) | Yes |
| Thread recovery | Yes (continues after exception) | No (dies on unhandled exception) |
| Callable support | Yes | No (TimerTask only) |
| Clock accuracy | System.nanoTime() | System.currentTimeMillis() |
| Production ready | Yes | No |

## scheduleAtFixedRate vs scheduleWithFixedDelay

| Method | Timing | Use When |
|--------|--------|----------|
| scheduleAtFixedRate | Period measured from start of each execution | Rate matters (e.g., heartbeat) |
| scheduleWithFixedDelay | Delay measured from end of each execution | Cooldown between runs matters |

## Common Patterns

| Pattern | Implementation |
|---------|---------------|
| Periodic health check | `scheduleAtFixedRate(healthCheck, 0, 30, SECONDS)` |
| Retry with delay | `schedule(retryTask, delay, SECONDS)` |
| Timeout enforcement | `schedule(timeoutTask, timeout, MILLISECONDS)` |
| Scheduled cleanup | `scheduleWithFixedDelay(cleanup, 1, 5, MINUTES)` |

## Risk Mitigation

| Risk | Mitigation |
|------|------------|
| Unhandled exception kills task | Wrap task in try-catch, log and continue |
| Task takes longer than period | Use scheduleWithFixedDelay or increase period |
| Clock drift | Use scheduleAtFixedRate for absolute timing |
| Resource leak | Shutdown executor in finally or shutdown hook |
