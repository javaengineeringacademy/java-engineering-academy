# Locks Decision Guide

## ReentrantLock vs ReadWriteLock vs StampedLock

| Feature | ReentrantLock | ReadWriteLock | StampedLock |
|---------|--------------|---------------|-------------|
| Read concurrency | Exclusive | Concurrent reads | Optimistic read |
| Write concurrency | Exclusive | Exclusive | Exclusive |
| Multiple conditions | Yes | Yes | No |
| Reentrant | Yes | Yes | No |
| Fairness | Configurable | Configurable | No |
| Performance | Good | Better for reads | Best for reads |

## When to Use Each

| Situation | Lock |
|-----------|------|
| General synchronization | ReentrantLock |
| Read-heavy, rare writes | ReadWriteLock |
| Maximum read performance | StampedLock (optimistic) |
| Multiple wait conditions | ReentrantLock with Conditions |
| Try-lock with timeout | ReentrantLock |

## ReentrantLock vs synchronized

| Feature | synchronized | ReentrantLock |
|---------|-------------|---------------|
| Syntax | Simple | try-finally |
| Auto-release | Yes | No |
| Try-lock | No | Yes |
| Timed lock | No | Yes |
| Interruptible | No | Yes |
| Fairness | No | Configurable |
| Conditions | 1 (wait/notify) | Multiple |
