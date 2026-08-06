# Caching Concepts

## Caching Strategies
- **Cache-Aside**: App manages cache manually
- **Read-Through**: Cache loads data automatically
- **Write-Through**: Writes to cache and DB together
- **Write-Behind**: Async write to DB

## When to Cache
- Frequently accessed data
- Expensive computations
- API responses
- Database query results
- Session data

## Cache Invalidation
- Time-based expiration (TTL)
- Event-based invalidation
- Version-based invalidation
- Manual invalidation

## Common Patterns
- **Cache-Aside**: Most common
- **Read-Through**: Transparent loading
- **Refresh-Ahead**: Pre-emptive refresh
- **Write-Through**: Consistent writes

## Eviction Policies
- **LRU**: Least Recently Used
- **LFU**: Least Frequently Used
- **FIFO**: First In First Out
- **TTL**: Time To Live
- **Size-based**: Maximum entries

## Caffeine Features
- High-performance cache
- Size-based eviction
- Time-based expiration
- Asynchronous loading
- Statistics collection
- Removal listeners

## Common Issues
- Cache stampede (thundering herd)
- Cache penetration
- Cache avalanche
- Data consistency
- Memory pressure

## Best Practices
- Set appropriate cache sizes
- Use TTL for all entries
- Monitor hit/miss rates
- Implement cache warming
- Handle cache failures gracefully
