# Cassandra Consistency

## Comprehensive Guide to Tunable Consistency Levels

Cassandra offers tunable consistency levels. This guide covers consistency levels, trade-offs, and best practices.

---

## Table of Contents

1. [Consistency Levels](#consistency-levels)
2. [Trade-offs](#trade-offs)
3. [Configuration](#configuration)
4. [Best Practices](#best-practices)

---

## Consistency Levels

### Consistency Levels

```
ONE          - Single replica acknowledgment
TWO          - Two replica acknowledgments
THREE        - Three replica acknowledgments
QUORUM       - Majority of replicas (N/2 + 1)
ALL          - All replicas
ANY          - At least one replica (including hinted handoff)
EACH_QUORUM  - Quorum in each datacenter
LOCAL_QUORUM - Quorum in local datacenter
LOCAL_ONE    - Single replica in local datacenter
```

### Read Consistency

```sql
-- Read with consistency level
SELECT * FROM users WHERE user_id = ?
USING CONSISTENCY QUORUM;

-- Set default consistency
CONSISTENCY QUORUM;
SELECT * FROM users WHERE user_id = ?;
```

### Write Consistency

```sql
-- Write with consistency level
INSERT INTO users (user_id, name)
VALUES (uuid(), 'John')
USING CONSISTENCY QUORUM;

-- Update with consistency
UPDATE users SET name = 'Jane'
WHERE user_id = ?
USING CONSISTENCY LOCAL_QUORUM;
```

---

## Trade-offs

### Consistency vs Availability

```
Strong Consistency (QUORUM, ALL):
- Higher latency
- Lower availability
- Guaranteed read-your-writes

Weak Consistency (ONE, ANY):
- Lower latency
- Higher availability
- Possible stale reads
```

### Latency vs Consistency

```
Consistency Level    Latency    Availability    Consistency
ONE                  Low        High            Weak
QUORUM               Medium     Medium          Strong
ALL                  High       Low             Strongest
```

---

## Configuration

### Cluster Configuration

```yaml
# cassandra.yaml
consistency:
  default:
    read: LOCAL_QUORUM
    write: LOCAL_QUORUM

# Or per-keyspace
CREATE KEYSPACE myapp
WITH replication = {
    'class': 'NetworkTopologyStrategy',
    'us-east': 3,
    'eu-west': 2
};
```

### Driver Configuration

```java
// Java driver
CqlSession session = CqlSession.builder()
    .withConfigLoader(DriverConfigLoader.fromString(
        "basic.request.consistency = LOCAL_QUORUM"))
    .build();
```

---

## Best Practices

### 1. Use LOCAL_QUORUM for Multi-DC

```sql
-- Good - Local quorum
INSERT INTO users (user_id, name)
VALUES (uuid(), 'John')
USING CONSISTENCY LOCAL_QUORUM;

-- Bad - Global quorum (higher latency)
INSERT INTO users (user_id, name)
VALUES (uuid(), 'John')
USING CONSISTENCY QUORUM;
```

### 2. Use ONE for Reads When Stale Data is OK

```sql
-- Good - Read from nearest replica
SELECT * FROM users WHERE user_id = ?
USING CONSISTENCY ONE;
```

### 3. Use QUORUM for Critical Writes

```sql
-- Good - Ensure write consistency
INSERT INTO financial_transactions (transaction_id, amount)
VALUES (uuid(), 1000)
USING CONSISTENCY QUORUM;
```

### 4. Use Lightweight Transactions

```sql
-- Prevent duplicates
INSERT INTO orders (order_id, user_id, total)
VALUES (uuid(), 123, 100)
IF NOT EXISTS;
```

### 5. Monitor Consistency

```bash
# Check consistency level
nodetool consistency

# Monitor read latency
nodetool tpstats
```

---

## Further Reading

- [Cassandra Consistency](https://cassandra.apache.org/doc/latest/cassandra/consistency/index.html)
- [Consistency Levels](https://cassandra.apache.org/doc/latest/cassandra/consistency/consistency.html)
- [Tunable Consistency](https://cassandra.apache.org/doc/latest/cassandra/consistency/consistency.html)
