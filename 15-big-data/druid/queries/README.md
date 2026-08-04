# Druid Queries

## Native Query
```json
{
  "queryType": "topN",
  "dataSource": "events",
  "intervals": ["2024-01-01/2024-02-01"],
  "granularity": "month",
  "dimension": "page",
  "metric": "count",
  "threshold": 10,
  "aggregations": [
    {"type": "count", "name": "count"},
    {"type": "doubleSum", "fieldName": "revenue", "name": "total_revenue"}
  ],
  "filter": {
    "type": "and",
    "fields": [
      {"type": "selector", "dimension": "country", "value": "US"},
      {"type": "bound", "dimension": "revenue", "lower": "100"}
    ]
  }
}
```

## SQL Query
```sql
SELECT
    DATE_TRUNC('hour', __time) as hour,
    page,
    COUNT(*) as events,
    SUM(revenue) as total_revenue
FROM events
WHERE __time >= CURRENT_TIMESTAMP - INTERVAL '1' DAY
  AND country = 'US'
GROUP BY 1, 2
ORDER BY events DESC
LIMIT 10;
```

## Aggregation Types
```json
{"type": "count", "name": "total_count"},
{"type": "longSum", "fieldName": "impressions", "name": "total_impressions"},
{"type": "doubleSum", "fieldName": "revenue", "name": "total_revenue"},
{"type": "hyperUnique", "fieldName": "user_id", "name": "unique_users"},
{"type": "thetaSketch", "fieldName": "user_id", "name": "unique_users_v2"}
```

## Best Practices
1. Use appropriate granularity for time bucketing
2. Filter on dimensions before aggregation
3. Use approximate aggregations for cardinality
4. Limit result sets with threshold
5. Use intervals to narrow time range
