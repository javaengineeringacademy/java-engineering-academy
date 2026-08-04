# InfluxDB Queries

## Comprehensive Guide to InfluxQL and Flux

InfluxDB supports InfluxQL (SQL-like) and Flux (functional) query languages.

---

## Table of Contents

1. [InfluxQL Basics](#influxql-basics)
2. [Flux Basics](#flux-basics)
3. [Aggregation](#aggregation)
4. [Best Practices](#best-practices)

---

## InfluxQL Basics

### SELECT Statements

```sql
-- Select all
SELECT * FROM cpu_usage

-- Select specific fields
SELECT usage_percent, cores FROM cpu_usage

-- Select with time range
SELECT * FROM cpu_usage
WHERE time > now() - 1h

-- Select with tag filter
SELECT * FROM cpu_usage
WHERE host = 'server01'
```

### WHERE Clauses

```sql
-- Time conditions
WHERE time > now() - 1h
WHERE time > '2024-01-01T00:00:00Z'
WHERE time BETWEEN '2024-01-01' AND '2024-01-02'

-- Tag conditions
WHERE host = 'server01'
WHERE host != 'server01'
WHERE host =~ /server0/
WHERE host !~ /server0/
```

### GROUP BY

```sql
-- Group by time
SELECT mean(usage_percent) FROM cpu_usage
GROUP BY time(1h)

-- Group by tag
SELECT mean(usage_percent) FROM cpu_usage
GROUP BY host

-- Group by time and tag
SELECT mean(usage_percent) FROM cpu_usage
GROUP BY time(1h), host
```

### Aggregation Functions

```sql
-- Mean
SELECT mean(usage_percent) FROM cpu_usage

-- Sum
SELECT sum(usage_percent) FROM cpu_usage

-- Min/Max
SELECT min(usage_percent), max(usage_percent) FROM cpu_usage

-- Count
SELECT count(usage_percent) FROM cpu_usage

-- Median
SELECT median(usage_percent) FROM cpu_usage

-- Percentile
SELECT percentile(usage_percent, 95) FROM cpu_usage
```

### ORDER BY and LIMIT

```sql
-- Order by time
SELECT * FROM cpu_usage
ORDER BY time DESC

-- Limit
SELECT * FROM cpu_usage
LIMIT 100

-- Offset
SELECT * FROM cpu_usage
LIMIT 100 OFFSET 200
```

---

## Flux Basics

### Basic Query

```flux
// Select data
from(bucket: "mydb")
  |> range(start: -1h)
  |> filter(fn: (r) => r._measurement == "cpu_usage")
  |> filter(fn: (r) => r._field == "usage_percent")
```

### Filter

```flux
// Filter by tag
from(bucket: "mydb")
  |> range(start: -1h)
  |> filter(fn: (r) => r.host == "server01")

// Filter by field value
from(bucket: "mydb")
  |> range(start: -1h)
  |> filter(fn: (r) => r._value > 80)
```

### Aggregate

```flux
// Mean
from(bucket: "mydb")
  |> range(start: -1h)
  |> filter(fn: (r) => r._measurement == "cpu_usage")
  |> aggregateWindow(every: 1h, fn: mean)

// Sum
from(bucket: "mydb")
  |> range(start: -24h)
  |> aggregateWindow(every: 1h, fn: sum)
```

---

## Aggregation

### Time Buckets

```sql
-- 1 minute buckets
SELECT mean(usage_percent) FROM cpu_usage
GROUP BY time(1m)

-- 5 minute buckets
SELECT mean(usage_percent) FROM cpu_usage
GROUP BY time(5m)

-- 1 hour buckets
SELECT mean(usage_percent) FROM cpu_usage
GROUP BY time(1h)
```

### Moving Average

```sql
-- Moving average
SELECT moving_average(usage_percent, 5) FROM cpu_usage
```

### Derivative

```sql
-- Rate of change
SELECT derivative(mean(usage_percent), 1h) FROM cpu_usage
GROUP BY time(1h)
```

---

## Best Practices

### 1. Use Time Ranges

```sql
-- Good - Time range
SELECT * FROM cpu_usage WHERE time > now() - 1h

-- Bad - No time range
SELECT * FROM cpu_usage
```

### 2. Use GROUP BY Time

```sql
-- Good - Grouped
SELECT mean(usage_percent) FROM cpu_usage
GROUP BY time(5m)

-- Bad - All data
SELECT mean(usage_percent) FROM cpu_usage
```

### 3. Use LIMIT

```sql
-- Good - Limited
SELECT * FROM cpu_usage LIMIT 1000

-- Bad - All data
SELECT * FROM cpu_usage
```

### 4. Use Tags for Filtering

```sql
-- Good - Tag filter
SELECT * FROM cpu_usage WHERE host = 'server01'

-- Bad - Field filter
SELECT * FROM cpu_usage WHERE host = 'server01'
```

### 5. Use Continuous Queries

```sql
-- Good - Pre-aggregated
CREATE CONTINUOUS QUERY "cq_1h" ON "mydb"
BEGIN
  SELECT mean(usage_percent) INTO "1h"."cpu_usage"
  FROM "cpu_usage"
  GROUP BY time(1h), host
END
```

---

## Further Reading

- [InfluxQL Reference](https://docs.influxdata.com/influxdb/v1.8/query_language/)
- [Flux Documentation](https://docs.influxdata.com/flux/)
- [Query Best Practices](https://docs.influxdata.com/influxdb/v1.8/query_language/guides/)
