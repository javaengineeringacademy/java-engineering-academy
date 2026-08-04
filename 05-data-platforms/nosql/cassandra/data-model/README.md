# Cassandra Data Modeling

## Comprehensive Guide to Denormalization and Query-Driven Design

Cassandra uses denormalized data models optimized for read performance. This guide covers query-driven design, denormalization, and data patterns.

---

## Table of Contents

1. [Query-Driven Design](#query-driven-design)
2. [Denormalization](#denormalization)
3. [Data Patterns](#data-patterns)
4. [Anti-Patterns](#anti-patterns)
5. [Best Practices](#best-practices)

---

## Query-Driven Design

### Design Process

```
1. Identify query patterns
2. Design tables for each query
3. Denormalize data
4. Duplicate data across tables
5. Use materialized views for additional access patterns
```

### Query Examples

```sql
-- Query 1: Get user by ID
SELECT * FROM users WHERE user_id = ?;

-- Query 2: Get user's posts by date
SELECT * FROM user_posts
WHERE user_id = ?
AND created_at >= '2024-01-01'
ORDER BY created_at DESC;

-- Query 3: Get posts by tag
SELECT * FROM posts_by_tag
WHERE tag = 'cassandra'
AND created_at >= '2024-01-01';
```

### Table Design

```sql
-- Table for Query 1
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    username TEXT,
    email TEXT,
    created_at TIMESTAMP
);

-- Table for Query 2
CREATE TABLE user_posts (
    user_id UUID,
    created_at TIMESTAMP,
    post_id UUID,
    title TEXT,
    content TEXT,
    PRIMARY KEY (user_id, created_at)
) WITH CLUSTERING ORDER BY (created_at DESC);

-- Table for Query 3
CREATE TABLE posts_by_tag (
    tag TEXT,
    created_at TIMESTAMP,
    post_id UUID,
    user_id UUID,
    title TEXT,
    PRIMARY KEY (tag, created_at)
) WITH CLUSTERING ORDER BY (created_at DESC);
```

---

## Denormalization

### Why Denormalize?

```
- Cassandra is optimized for reads
- JOINs are not supported
- Data is duplicated for query performance
- Writes are more expensive but infrequent
```

### Denormalization Example

```sql
-- Original normalized tables (RDBMS style)
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    username TEXT,
    email TEXT
);

CREATE TABLE posts (
    post_id UUID PRIMARY KEY,
    user_id UUID,
    title TEXT,
    content TEXT
);

-- Denormalized for Cassandra
CREATE TABLE user_posts (
    user_id UUID,
    post_id UUID,
    username TEXT,  -- Duplicated from users
    title TEXT,
    content TEXT,
    PRIMARY KEY (user_id, post_id)
);
```

### Data Duplication

```sql
-- Same data in multiple tables
CREATE TABLE user_by_id (
    user_id UUID PRIMARY KEY,
    username TEXT,
    email TEXT
);

CREATE TABLE user_by_email (
    email TEXT PRIMARY KEY,
    user_id UUID,
    username TEXT
);

-- Update both tables when data changes
INSERT INTO user_by_id (user_id, username, email)
VALUES (uuid(), 'john', 'john@example.com');

INSERT INTO user_by_email (email, user_id, username)
VALUES ('john@example.com', uuid(), 'john');
```

---

## Data Patterns

### Time-Series Pattern

```sql
-- Sensor data (bucket by day)
CREATE TABLE sensor_data (
    sensor_id UUID,
    event_date DATE,
    event_time TIMESTAMP,
    value DOUBLE,
    PRIMARY KEY ((sensor_id, event_date), event_time)
) WITH CLUSTERING ORDER BY (event_time DESC);
```

### Wide-Row Pattern

```sql
-- User activity log
CREATE TABLE user_activity (
    user_id UUID,
    activity_date DATE,
    activity_id TIMEUUID,
    activity_type TEXT,
    activity_data MAP<TEXT, TEXT>,
    PRIMARY KEY ((user_id, activity_date), activity_id)
) WITH CLUSTERING ORDER BY (activity_id DESC);
```

### Materialized Views

```sql
-- Create materialized view
CREATE MATERIALIZED VIEW user_posts_view AS
SELECT user_id, post_id, title, created_at
FROM user_posts
WHERE user_id IS NOT NULL
AND post_id IS NOT NULL
AND created_at IS NOT NULL
PRIMARY KEY (user_id, created_at, post_id);
```

### Secondary Indexes

```sql
-- Index on non-primary key
CREATE INDEX ON users (email);

-- Index on map values
CREATE INDEX ON user_activity (activity_type);
```

---

## Anti-Patterns

### 1. Large Partitions

```sql
-- Bad - Can create very large partitions
CREATE TABLE user_events (
    user_id UUID,
    event_time TIMESTAMP,
    event_data TEXT,
    PRIMARY KEY (user_id, event_time)
);

-- Better - Bucket by date
CREATE TABLE user_events (
    user_id UUID,
    event_date DATE,
    event_time TIMESTAMP,
    event_data TEXT,
    PRIMARY KEY ((user_id, event_date), event_time)
);
```

### 2. Allow Filtering

```sql
-- Bad - Inefficient
SELECT * FROM users WHERE status = 'active' ALLOW FILTERING;

-- Better - Create table for this query
CREATE TABLE active_users (
    status TEXT,
    user_id UUID,
    username TEXT,
    PRIMARY KEY (status, user_id)
);
```

### 3. High Cardinality Partition Keys

```sql
-- Bad - Too many unique partition keys
CREATE TABLE logs (
    log_id UUID PRIMARY KEY,
    message TEXT
);

-- Better - Use time-based partitioning
CREATE TABLE logs (
    log_date DATE,
    log_id TIMEUUID,
    message TEXT,
    PRIMARY KEY (log_date, log_id)
);
```

---

## Best Practices

### 1. Design for One Query Per Table

```sql
-- Each table serves one query pattern
CREATE TABLE user_by_id (...);
CREATE TABLE user_by_email (...);
CREATE TABLE posts_by_user (...);
CREATE TABLE posts_by_tag (...);
```

### 2. Use TimeUUID for Time-Series

```sql
CREATE TABLE events (
    partition_key UUID,
    event_time TIMEUUID,
    event_data TEXT,
    PRIMARY KEY (partition_key, event_time)
) WITH CLUSTERING ORDER BY (event_time DESC);
```

### 3. Bucket Large Datasets

```sql
-- Bucket by month
CREATE TABLE metrics (
    metric_name TEXT,
    metric_month DATE,
    metric_time TIMESTAMP,
    value DOUBLE,
    PRIMARY KEY ((metric_name, metric_month), metric_time)
);
```

### 4. Use Lightweight Transactions

```sql
-- Prevent duplicates
INSERT INTO users (user_id, username)
VALUES (uuid(), 'john')
IF NOT EXISTS;
```

### 5. Monitor Partition Size

```bash
# Check partition size
nodetool tablestats mykeyspace.users
```

---

## Further Reading

- [Cassandra Data Modeling](https://cassandra.apache.org/doc/latest/datamodeling/)
- [Query-Driven Development](https://www.datastax.com/blog/cassandra-data-modeling-best-practices)
- [Denormalization](https://cassandra.apache.org/doc/latest/datamodeling/design_schema.html)
