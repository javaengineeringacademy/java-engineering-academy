# Cassandra Fundamentals

## Comprehensive Guide to Apache Cassandra

Cassandra is a distributed NoSQL database designed for high availability and scalability. This guide covers CQL, keyspaces, tables, and data modeling.

---

## Table of Contents

1. [Cassandra Architecture](#cassandra-architecture)
2. [CQL Basics](#cql-basics)
3. [Keyspaces](#keyspaces)
4. [Tables](#tables)
5. [Data Types](#data-types)
6. [Best Practices](#best-practices)

---

## Cassandra Architecture

### Architecture

```
+------------------+     +------------------+
|   Node 1         |     |   Node 2         |
|   (Coordinator)  |<--->|                  |
+------------------+     +------------------+
        ^                         ^
        |                         |
        v                         v
+------------------+     +------------------+
|   Node 3         |     |   Node 4         |
|                  |<--->|                  |
+------------------+     +------------------+
```

### Features

```
- Masterless architecture
- Linear scalability
- Tunable consistency
- Multi-datacenter replication
- CQL (Cassandra Query Language)
```

---

## CQL Basics

### Keyspace Operations

```sql
-- Create keyspace
CREATE KEYSPACE IF NOT EXISTS myapp
WITH replication = {
    'class': 'NetworkTopologyStrategy',
    'datacenter1': 3
}
AND durable_writes = true;

-- Use keyspace
USE myapp;

-- Describe keyspace
DESCRIBE KEYSPACE myapp;

-- Drop keyspace
DROP KEYSPACE IF EXISTS myapp;
```

### Table Operations

```sql
-- Create table
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    username TEXT,
    email TEXT,
    created_at TIMESTAMP
);

-- Create table with clustering key
CREATE TABLE user_events (
    user_id UUID,
    event_time TIMESTAMP,
    event_type TEXT,
    event_data MAP<TEXT, TEXT>,
    PRIMARY KEY (user_id, event_time)
) WITH CLUSTERING ORDER BY (event_time DESC);

-- Alter table
ALTER TABLE users ADD phone TEXT;
ALTER TABLE users DROP phone;

-- Truncate table
TRUNCATE users;

-- Drop table
DROP TABLE IF EXISTS users;
```

### CRUD Operations

```sql
-- Insert
INSERT INTO users (user_id, username, email, created_at)
VALUES (uuid(), 'john_doe', 'john@example.com', toTimestamp(now()));

-- Insert with TTL (expires in 1 hour)
INSERT INTO sessions (session_id, user_id, data)
VALUES (uuid(), 123, 'session data')
USING TTL 3600;

-- Select
SELECT * FROM users;
SELECT username, email FROM users WHERE user_id = ?;

-- Update
UPDATE users
SET email = 'newemail@example.com'
WHERE user_id = ?;

-- Delete
DELETE FROM users WHERE user_id = ?;

-- Delete with timestamp
DELETE phone FROM users
WHERE user_id = ?
USING TIMESTAMP 1234567890;
```

---

## Keyspaces

### Replication Strategies

```sql
-- SimpleStrategy (single datacenter)
CREATE KEYSPACE myapp
WITH replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 3
};

-- NetworkTopologyStrategy (multi-datacenter)
CREATE KEYSPACE myapp
WITH replication = {
    'class': 'NetworkTopologyStrategy',
    'us-east': 3,
    'eu-west': 2
};
```

---

## Tables

### Primary Key Design

```sql
-- Simple primary key
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    name TEXT,
    email TEXT
);

-- Composite primary key (partition key + clustering key)
CREATE TABLE user_posts (
    user_id UUID,
    post_id UUID,
    title TEXT,
    content TEXT,
    created_at TIMESTAMP,
    PRIMARY KEY (user_id, created_at)
) WITH CLUSTERING ORDER BY (created_at DESC);

-- Multiple clustering keys
CREATE TABLE sensor_data (
    sensor_id UUID,
    date DATE,
    time TIME,
    value DOUBLE,
    PRIMARY KEY (sensor_id, date, time)
) WITH CLUSTERING ORDER BY (date DESC, time DESC);
```

### Secondary Indexes

```sql
-- Create secondary index
CREATE INDEX ON users (email);

-- Create custom index
CREATE CUSTOM INDEX ON users (user_metadata)
USING 'org.apache.cassandra.index.sASI.SASIIndex'
WITH OPTIONS = {
    'mode': 'CONTAINS',
    'analyzer_class': 'org.apache.cassandra.index.sASI.StandardAnalyzer',
    'case_sensitive': 'false'
};
```

---

## Data Types

### Built-in Types

```sql
-- Text
name TEXT

-- Numeric
age INT
salary BIGINT
price DECIMAL
rating FLOAT
is_active BOOLEAN

-- Date/Time
created_at TIMESTAMP
event_date DATE
event_time TIME

-- UUID
user_id UUID

-- Binary
avatar BLOB

-- Collections
tags LIST<TEXT>
metadata MAP<TEXT, TEXT>
email_addresses SET<TEXT>

-- Tuple
location TUPLE<DOUBLE, DOUBLE>
```

### User-Defined Types

```sql
-- Create UDT
CREATE TYPE address (
    street TEXT,
    city TEXT,
    state TEXT,
    zip TEXT
);

-- Use UDT
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    name TEXT,
    home_address FROZEN<ADDRESS>,
    mailing_addresses LIST<FROZEN<ADDRESS>>
);
```

---

## Best Practices

### 1. Design for Queries

```sql
-- Design tables based on query patterns
-- Query: Get all posts by user, ordered by date
CREATE TABLE user_posts (
    user_id UUID,
    created_at TIMESTAMP,
    title TEXT,
    content TEXT,
    PRIMARY KEY (user_id, created_at)
) WITH CLUSTERING ORDER BY (created_at DESC);
```

### 2. Use Proper Partition Keys

```sql
-- Good - Even distribution
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    name TEXT
);

-- Bad - Hot partition
CREATE TABLE users (
    region TEXT,
    user_id UUID,
    name TEXT,
    PRIMARY KEY (region, user_id)
);
```

### 3. Avoid Large Partitions

```sql
-- Bad - Can create large partitions
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

### 4. Use TTL for Expiring Data

```sql
-- Sessions expire after 1 hour
INSERT INTO sessions (session_id, data)
VALUES (uuid(), 'session data')
USING TTL 3600;
```

### 5. Use Lightweight Transactions

```sql
-- Conditional insert
INSERT INTO users (user_id, username)
VALUES (uuid(), 'john')
IF NOT EXISTS;
```

---

## Further Reading

- [Cassandra Documentation](https://cassandra.apache.org/doc/)
- [CQL Reference](https://cassandra.apache.org/doc/latest/cql/)
- [Data Modeling](https://cassandra.apache.org/doc/latest/datamodeling/)
