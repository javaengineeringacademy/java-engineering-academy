# InfluxDB Fundamentals

## Comprehensive Guide to Time-Series Databases

InfluxDB is a time-series database designed for metrics, events, and analytics. This guide covers measurements, tags, retention policies, and best practices.

---

## Table of Contents

1. [InfluxDB Overview](#influxdb-overview)
2. [Data Model](#data-model)
3. [RetentionPolicy](#retention-policy)
4. [Continuous Queries](#continuous-queries)
5. [Best Practices](#best-practices)

---

## InfluxDB Overview

### Architecture

```
+------------------+
|   Application    |
+------------------+
        |
        v
+------------------+
|   InfluxDB       |
+------------------+
        |
        v
+------------------+
|   Storage        |
|   (TSM Engine)   |
+------------------+
```

### Features

```
- Purpose-built for time-series data
- SQL-like query language (InfluxQL)
- Built-in time functions
- Retention policies
- Continuous queries
- Kapacitor for alerting
```

---

## Data Model

### Components

```
Measurement  - Table name (e.g., "cpu_usage")
Tags         - Indexed metadata (e.g., "host=server01")
Fields       - Data values (e.g., "usage_percent=85.3")
Timestamp    - Time index
```

### Data Structure

```lineprotocol
measurement,tag1=value1,tag2=value2 field1=value1,field2=value2 timestamp

Example:
cpu_usage,host=server01,region=us-east usage_percent=85.3,cores=4 1706140800000000000
```

### Write Data

```bash
# Write single point
influx -execute 'INSERT cpu_usage,host=server01 usage_percent=85.3,cores=4'

# Write from line protocol
curl -XPOST 'http://localhost:8086/write?db=mydb' \
  --data-binary 'cpu_usage,host=server01 usage_percent=85.3 1706140800000000000'

# Write from file
influx -import -path=data.txt -precision=s
```

### Query Data

```sql
-- Basic query
SELECT * FROM cpu_usage

-- Time range
SELECT * FROM cpu_usage
WHERE time > now() - 1h

-- Aggregation
SELECT mean(usage_percent) FROM cpu_usage
WHERE time > now() - 24h
GROUP BY time(1h)

-- Tag filter
SELECT * FROM cpu_usage
WHERE host = 'server01'
```

---

## RetentionPolicy

### Create Retention Policy

```sql
-- Create retention policy
CREATE RETENTION POLICY "one_week" ON "mydb"
DURATION 1w REPLICATION 1

-- Create with default
CREATE RETENTION POLICY "default" ON "mydb"
DURATION INF REPLICATION 1 DEFAULT

-- Alter retention policy
ALTER RETENTION POLICY "one_week" ON "mydb"
DURATION 30d REPLICATION 1
```

### Drop Retention Policy

```sql
-- Drop retention policy
DROP RETENTION POLICY "one_week" ON "mydb"
```

---

## Continuous Queries

### Create Continuous Query

```sql
-- Downsample data
CREATE CONTINUOUS QUERY "cq_cpu_usage" ON "mydb"
BEGIN
  SELECT mean(usage_percent) AS avg_usage
  INTO "one_month"."cpu_usage_averages"
  FROM "cpu_usage"
  GROUP BY time(1h), host
END

-- Continuous query with fill
CREATE CONTINUOUS QUERY "cq_fill" ON "mydb"
BEGIN
  SELECT mean(usage_percent) AS avg_usage
  INTO "cpu_usage_filled"
  FROM "cpu_usage"
  GROUP BY time(5m), host
  FILL(linear)
END
```

### Drop Continuous Query

```sql
DROP CONTINUOUS QUERY "cq_cpu_usage" ON "mydb"
```

---

## Best Practices

### 1. Use Tags for Metadata

```lineprotocol
# Good - Indexed tags for filtering
cpu_usage,host=server01,region=us-east usage_percent=85.3

# Bad - Everything as fields
cpu_usage host="server01",region="us-east",usage_percent=85.3
```

### 2. Use Appropriate Precision

```bash
# Nanosecond precision (default)
influx -precision=ns

# Microsecond precision
influx -precision=us

# Millisecond precision
influx -precision=ms

# Second precision
influx -precision=s
```

### 3. Use Retention Policies

```sql
-- Raw data: 7 days
CREATE RETENTION POLICY "raw" ON "mydb" DURATION 7d REPLICATION 1

-- 1-hour averages: 30 days
CREATE RETENTION POLICY "hourly" ON "mydb" DURATION 30d REPLICATION 1

-- Daily averages: forever
CREATE RETENTION POLICY "daily" ON "mydb" DURATION INF REPLICATION 1
```

### 4. Use Continuous Queries

```sql
-- Downsample every 5 minutes
CREATE CONTINUOUS QUERY "cq_5min" ON "mydb"
BEGIN
  SELECT mean(usage_percent) INTO "5min"."cpu_usage"
  FROM "cpu_usage"
  GROUP BY time(5m), host
END
```

### 5. Use Batch Writes

```bash
# Good - Batch write
curl -XPOST 'http://localhost:8086/write?db=mydb' \
  --data-binary '
cpu_usage,host=server01 usage_percent=85.3 1706140800000000000
cpu_usage,host=server02 usage_percent=72.1 1706140800000000000
cpu_usage,host=server03 usage_percent=91.2 1706140800000000000
'
```

---

## Further Reading

- [InfluxDB Documentation](https://docs.influxdata.com/)
- [InfluxQL Reference](https://docs.influxdata.com/influxdb/v1.8/query_language/)
- [Line Protocol](https://docs.influxdata.com/influxdb/v1.8/write_protocols/line_protocol_tutorial/)
