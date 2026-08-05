# Performance vs Correctness

## Problem Statement

Faster systems often achieve speed by cutting corners on accuracy. The question is not whether you can make something faster, but whether the loss of correctness is acceptable for your use case.

## The Core Tension

Performance and correctness exist on a spectrum. Every optimization involves a decision:

- Cache stale data or query fresh data?
- Approximate results or exact results?
- Process now and reconcile later or wait for complete data?
- Optimistic execution or pessimistic validation?

## When to Choose Performance

Choose performance when approximate results are good enough:

- **Search engines**: Showing relevant results in 50ms beats showing perfect results in 2 seconds
- **Analytics dashboards**: Users need rapid feedback; slight inaccuracies in aggregate metrics are acceptable
- **Recommendations**: Slightly wrong suggestions are better than no suggestions
- **Ad targeting**: Perfect targeting is impossible; fast approximation works
- **Real-time monitoring**: Dashboards that lag by seconds lose their value

### Performance Techniques That Sacrifice Correctness

**Caching**: Serving stale data from cache while updates propagate. Acceptable when data freshness is not critical.

**Approximate algorithms**: HyperLogLog for cardinality estimation, Count-Min Sketch for frequency estimation. Use O(1) or O(log n) space instead of O(n).

**Sampling**: Process a subset of data and extrapolate. Netflix does not need to analyze every viewing second to understand engagement.

**Eventual consistency**: Accept writes immediately, reconcile later. Works for shopping carts, user preferences, activity feeds.

**Batch processing**: Aggregate results periodically instead of computing in real-time. Trade accuracy for throughput.

## When to Choose Correctness

Choose correctness when errors cause real harm:

- **Financial calculations**: Rounding errors compound. IEEE 754 floating point is dangerous for money.
- **Medical systems**: Approximate drug interaction checks are unacceptable
- **Legal compliance**: Audit trails must be exact and complete
- **Scientific computing**: Simulation results must be reproducible
- **Security**: Hash collisions cannot be tolerated in password storage

### Correctness Techniques That Sacrifice Performance

**Strong consistency**: Synchronous replication, distributed transactions, consensus protocols.

**Validation on every operation**: Schema validation, type checking, boundary checking. Adds latency but prevents corruption.

**Full scans vs indexes**: Sometimes the only correct answer requires reading all data.

**ACID transactions**: Rollback capability, isolation levels, durability guarantees.

## Approximate Queries

Databases offer approximate query capabilities:

```sql
-- PostgreSQL: approximate count (fast, not exact)
EXPLAIN ANALYZE SELECT COUNT(*) FROM events;
-- vs exact count, full scan

-- BigQuery: approximate aggregation
SELECT APPROX_COUNT_DISTINCT(user_id) FROM events;

-- ClickHouse: sampling
SELECT count() FROM events SAMPLE 0.1;
```

HyperLogLog can estimate cardinality with 0.8% standard error using 12KB of memory. Counting exactly might require gigabytes.

## Eventual Consistency Patterns

**Last-writer-wins**: Simple, but loses concurrent updates. Acceptable for user profile updates.

**Version vectors**: Track causality, detect conflicts. More correct, more complex.

**CRDTs**: Conflict-free replicated data types. Mathematically guaranteed to converge. Higher memory cost.

**Saga pattern**: Distributed transactions broken into local transactions with compensation. Correctness through eventual reconciliation.

## Decision Matrix

| Factor | Choose Performance | Choose Correctness |
|--------|-------------------|-------------------|
| Error cost | Low | High |
| User tolerance for inaccuracy | High | Low |
| Data freshness requirement | Seconds OK | Must be real-time |
| Regulatory compliance | Lenient | Strict |
| Volume | Very high (billions) | Moderate |
| Reversibility | Easy to fix | Hard to fix |

## Real-World Examples

**Google Search**: Uses approximate matching and ranking. Not every document is indexed perfectly, but results are useful in milliseconds.

**Banking**: Uses exact arithmetic with fixed-point decimals. Floating point is never used for money. Performance is sacrificed for correctness.

**Social media feeds**: Uses eventual consistency. Your friend may see your post seconds before others. This is acceptable.

**Stock trading**: Requires both speed and correctness. This is why it is so expensive -- they solve an extremely hard version of this trade-off.

## Interview Relevance

**Common questions**:
- "How would you design a real-time analytics system?"
- "Design a distributed counter"
- "How do you handle conflicting writes?"

**What interviewers want**:
- You can identify when approximation is acceptable
- You know specific approximate data structures
- You can reason about error bounds
- You understand that correctness is not always binary

**Red flags**:
- Always choosing correctness without considering performance
- Never mentioning approximate algorithms
- Not understanding that some domains require exact answers
- Confusing eventual consistency with incorrectness

## Key Takeaway

Not all data requires the same level of correctness. A user profile photo that loads 200ms late is fine. A bank balance that is off by one cent is not. Classify your data by correctness requirements and optimize accordingly.
