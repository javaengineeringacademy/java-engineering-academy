# Decision: Performance Testing

## When to Use Performance Testing

**Use JMH when:**
- Comparing algorithm performance
- Micro-optimization decisions
- API performance validation
- Identifying bottlenecks

**Use Gatling when:**
- Load testing applications
- Stress testing systems
- Capacity planning
- End-to-end performance

## Benchmark Configuration

| Parameter | Guideline |
|-----------|-----------|
| Warmup iterations | 5-10 |
| Measurement iterations | 5-10 |
| Fork count | 1-3 |
| Time per iteration | 1-2 seconds |

## Common Pitfalls

1. Insufficient warmup
2. Dead code elimination
3. Benchmark mode selection
4. Too few iterations
5. Not forking JVM

## Performance Comparison

| Tool | Use Case | Level |
|------|----------|-------|
| JMH | Microbenchmarks | Method |
| Gatling | Load testing | Application |
| JMeter | Performance testing | System |
|wrk | HTTP benchmarks | API |
