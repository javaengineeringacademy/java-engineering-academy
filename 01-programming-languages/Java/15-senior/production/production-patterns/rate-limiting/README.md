# Rate Limiting

## What Is It?

Rate limiting controls the number of requests a client can make to an API within a given time window. It protects services from overload and abuse.

## Algorithms

### Token Bucket

- Tokens added at a fixed rate up to a max capacity
- Each request consumes one token
- Allows bursts up to bucket capacity
- **Best for**: APIs needing burst tolerance

### Fixed Window

- Counts requests in a fixed time interval
- Simple to implement
- Boundary problem: up to 2x burst at window edges
- **Best for**: Simple use cases

### Sliding Window Log

- Tracks exact timestamps of each request
- Most accurate but memory-intensive
- No boundary issues
- **Best for**: Strict rate limiting needs

### Sliding Window Counter

- Weighted average of current and previous window
- Good balance of accuracy and efficiency
- **Best for**: Production systems

## Comparison

| Algorithm | Accuracy | Memory | Burst Handling |
|-----------|----------|--------|----------------|
| Token Bucket | High | Low | Allows bursts |
| Fixed Window | Medium | Low | Boundary issues |
| Sliding Log | Highest | High | No bursts |
| Sliding Counter | High | Low | Smooth |

## When to Use

- **API Gateway**: Protect backend services
- **User Limits**: Prevent abuse per user
- **Downstream Protection**: Avoid overwhelming third-party APIs
- **Resource Management**: Control access to shared resources
- **Cost Control**: Limit expensive operations

## Implementation Options

- **Resilience4j RateLimiter**: Production-ready
- **Bucket4j**: Distributed rate limiting
- **Guava RateLimiter**: Single JVM token bucket
- **Redis + Lua**: Distributed sliding window

## Best Practices

1. Return meaningful HTTP 429 responses
2. Include rate limit headers (X-RateLimit-*)
3. Allow configuration per endpoint
4. Consider distributed rate limiting for multi-instance deployments
5. Monitor and alert on limit breaches

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Overview

[Brief description of the topic]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
