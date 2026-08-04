# Pinot Queries

## SQL Query
```sql
SELECT
    page,
    event_type,
    count(*) as event_count,
    sum(revenue) as total_revenue,
    avg(revenue) as avg_revenue
FROM events
WHERE timestamp >= 1704067200000
  AND country = 'US'
GROUP BY page, event_type
ORDER BY event_count DESC
LIMIT 10;
```

## Aggregation Functions
```sql
-- Basic aggregations
SELECT count(*), sum(revenue), avg(revenue), min(revenue), max(revenue)
FROM events;

-- Distinct count
SELECT user_id, count(distinct event_id) as unique_events
FROM events GROUP BY user_id;

-- HAVING clause
SELECT page, count(*) as cnt
FROM events GROUP BY page HAVING cnt > 1000;
```

## Filtering
```sql
-- Range filter
SELECT * FROM events WHERE revenue > 100 AND revenue < 1000;

-- IN filter
SELECT * FROM events WHERE event_type IN ('click', 'purchase');

-- LIKE filter
SELECT * FROM events WHERE page LIKE '/product/%';

-- IS NOT NULL
SELECT * FROM events WHERE user_id IS NOT NULL;
```

## Window Functions
```sql
SELECT
    user_id,
    page,
    revenue,
    ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY revenue DESC) as rank,
    SUM(revenue) OVER (PARTITION BY user_id) as total_by_user
FROM events;
```

## Best Practices
1. Always filter on time column first
2. Use appropriate granularity
3. Limit result sets
4. Use approximate functions for large cardinality
5. Monitor query performance
