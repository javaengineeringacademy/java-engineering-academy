# PostgreSQL Fundamentals

## Table of Contents

1. [Installation](#installation)
2. [Architecture](#architecture)
3. [SQL Basics](#sql-basics)
4. [Data Types](#data-types)
5. [MVCC (Multi-Version Concurrency Control)](#mvcc)
6. [Extensions](#extensions)
7. [Backup and Recovery](#backup-and-recovery)
8. [Best Practices](#best-practices)

---

## Installation

### Ubuntu/Debian

```bash
# Add PostgreSQL repository
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'

# Add repository key
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -

# Update and install
sudo apt-get update
sudo apt-get install postgresql-15 postgresql-client-15

# Start PostgreSQL
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Check status
sudo systemctl status postgresql
```

### CentOS/RHEL

```bash
# Install repository
sudo yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-8-x86_64/pgdg-redhat-repo-latest.noarch.rpm

# Install PostgreSQL
sudo yum install -y postgresql15-server postgresql15

# Initialize database
sudo /usr/pgsql-15/bin/postgresql-15-setup initdb

# Start PostgreSQL
sudo systemctl start postgresql-15
sudo systemctl enable postgresql-15
```

### Docker

```bash
# Run PostgreSQL container
docker run -d \
  --name postgres \
  -p 5432:5432 \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=mydb \
  -e POSTGRES_USER=user \
  -v pgdata:/var/lib/postgresql/data \
  postgres:15

# Connect to PostgreSQL
docker exec -it postgres psql -U user -d mydb
```

### Configuration

```ini
# postgresql.conf
listen_addresses = '*'
port = 5432
max_connections = 200
superuser_reserved_connections = 3

# Memory
shared_buffers = 4GB  # 25% of RAM
effective_cache_size = 12GB  # 75% of RAM
work_mem = 256MB
maintenance_work_mem = 1GB

# WAL
wal_level = replica
max_wal_senders = 10
wal_keep_size = 1GB
hot_standby = on

# Logging
logging_collector = on
log_directory = 'log'
log_filename = 'postgresql-%Y-%m-%d.log'
log_min_duration_statement = 1000

# Autovacuum
autovacuum = on
autovacuum_max_workers = 3
autovacuum_naptime = 1min

# Performance
random_page_cost = 1.1  # SSD
effective_io_concurrency = 200  # SSD
checkpoint_completion_target = 0.9
```

```ini
# pg_hba.conf - Client Authentication
# TYPE  DATABASE  USER  ADDRESS      METHOD
local   all       all                peer
host    all       all   127.0.0.1/32 md5
host    all       all   ::1/128      md5
host    mydb      user  192.168.1.0/24 md5
```

---

## Architecture

### PostgreSQL Process Model

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Application                        │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    Postmaster Process                        │
│  - Listens for connections                                  │
│  - Forks backend processes                                  │
│  - Manages shared memory                                    │
└──────────────────────────┬──────────────────────────────────┘
                           │
          ┌────────────────┼────────────────┐
          ▼                ▼                ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│  Backend        │ │  Backend        │ │  Backend        │
│  Process 1      │ │  Process 2      │ │  Process 3      │
│  (Client 1)     │ │  (Client 2)     │ │  (Client 3)     │
└─────────────────┘ └─────────────────┘ └─────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    Shared Memory                             │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐          │
│  │ Shared      │ │ Buffer      │ │ WAL Buffer  │          │
│  │ Buffers     │ │ Cache       │ │             │          │
│  └─────────────┘ └─────────────┘ └─────────────┘          │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐          │
│  │ Lock Tables │ │ Proc Array  │ │ CLOG        │          │
│  └─────────────┘ └─────────────┘ └─────────────┘          │
└─────────────────────────────────────────────────────────────┘
```

### Memory Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Memory Layout                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Shared Memory                          │    │
│  │  - shared_buffers (cache data pages)                │    │
│  │  - WAL buffers                                       │    │
│  │  - Lock tables                                       │    │
│  │  - Proc array                                        │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Per-Process Memory                     │    │
│  │  - work_mem (sorts, hashes)                         │    │
│  │  - maintenance_work_mem (VACUUM, CREATE INDEX)      │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## SQL Basics

### Database Operations

```sql
-- Create database
CREATE DATABASE mydb
  WITH ENCODING = 'UTF8'
  LC_COLLATE = 'en_US.UTF-8'
  LC_CTYPE = 'en_US.UTF-8'
  TEMPLATE = template0;

-- Connect to database
\c mydb

-- List databases
\l

-- Drop database
DROP DATABASE IF EXISTS mydb;
```

### Table Operations

```sql
-- Create table with advanced features
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  is_active BOOLEAN DEFAULT TRUE,
  profile JSONB,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Create table with partitioning
CREATE TABLE orders (
  id BIGSERIAL,
  user_id INTEGER NOT NULL,
  total DECIMAL(10,2),
  status VARCHAR(20),
  created_at TIMESTAMPTZ DEFAULT NOW(),
  PRIMARY KEY (id, created_at)
) PARTITION BY RANGE (created_at);

-- Create partitions
CREATE TABLE orders_2024 PARTITION OF orders
  FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');

CREATE TABLE orders_2025 PARTITION OF orders
  FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');

-- Alter table
ALTER TABLE users
  ADD COLUMN phone VARCHAR(20),
  ADD COLUMN age INTEGER CHECK (age >= 0 AND age <= 150),
  ALTER COLUMN is_active SET DEFAULT TRUE;

-- Create index
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_profile ON users USING GIN (profile);
CREATE INDEX idx_orders_created ON orders (created_at);
```

### CRUD Operations

```sql
-- INSERT with RETURNING
INSERT INTO users (username, email, password_hash, first_name, last_name)
VALUES ('john_doe', 'john@example.com', crypt('password', gen_salt('bf')), 'John', 'Doe')
RETURNING id, username;

-- INSERT multiple rows
INSERT INTO users (username, email, password_hash)
VALUES
  ('user1', 'user1@example.com', crypt('pass1', gen_salt('bf'))),
  ('user2', 'user2@example.com', crypt('pass2', gen_salt('bf')))
RETURNING id;

-- INSERT with ON CONFLICT (UPSERT)
INSERT INTO users (username, email, password_hash)
VALUES ('john_doe', 'john@example.com', crypt('password', gen_salt('bf')))
ON CONFLICT (username)
DO UPDATE SET email = EXCLUDED.email, updated_at = NOW();

-- SELECT with advanced features
SELECT
  id,
  username,
  email,
  profile->>'name' AS profile_name,
  profile->'hobbies' AS hobbies,
  created_at AT TIME ZONE 'UTC' AS created_utc
FROM users
WHERE is_active = TRUE
  AND created_at >= NOW() - INTERVAL '30 days'
ORDER BY created_at DESC
LIMIT 10 OFFSET 20;

-- UPDATE with RETURNING
UPDATE users
SET is_active = FALSE, updated_at = NOW()
WHERE id = 1
RETURNING *;

-- DELETE with RETURNING
DELETE FROM users
WHERE is_active = FALSE
  AND updated_at < NOW() - INTERVAL '1 year'
RETURNING id, username;
```

---

## Data Types

### Numeric Types

```sql
-- Integer types
SMALLINT      -- 2 bytes (-32768 to 32767)
INTEGER       -- 4 bytes (-2147483648 to 2147483647)
BIGINT        -- 8 bytes (-9223372036854775808 to 9223372036854775807)
SERIAL        -- Auto-incrementing integer
BIGSERIAL     -- Auto-incrementing bigint

-- Decimal types
NUMERIC(10,2) -- Exact precision (99999999.99)
REAL          -- 4 bytes (approximate)
DOUBLE PRECISION  -- 8 bytes (approximate)

-- Example
CREATE TABLE measurements (
  id SERIAL PRIMARY KEY,
  value NUMERIC(10,4) NOT NULL,
  average REAL,
  large_value BIGINT
);
```

### String Types

```sql
CHAR(n)       -- Fixed length (padded with spaces)
VARCHAR(n)    -- Variable length (up to n characters)
TEXT          -- Variable length (unlimited)
NAME          -- Internal type for identifiers

-- Example
CREATE TABLE documents (
  id SERIAL PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content TEXT,
  slug VARCHAR(100) GENERATED ALWAYS AS (
    LOWER(REPLACE(title, ' ', '-'))
  ) STORED
);
```

### Date/Time Types

```sql
DATE          -- '2024-01-15'
TIME          -- '14:30:00'
TIMESTAMP     -- '2024-01-15 14:30:00'
TIMESTAMPTZ   -- '2024-01-15 14:30:00-05' (timezone-aware)
INTERVAL      -- '1 year 2 months 3 days 4 hours'

-- Example
CREATE TABLE events (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  event_date DATE NOT NULL,
  event_time TIME,
  start_at TIMESTAMPTZ NOT NULL,
  duration INTERVAL,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Working with dates
SELECT
  NOW() AS current_datetime,
  CURRENT_DATE AS current_date,
  CURRENT_TIME AS current_time,
  EXTRACT(YEAR FROM created_at) AS year,
  EXTRACT(MONTH FROM created_at) AS month,
  DATE_TRUNC('month', created_at) AS month_start,
  AGE(NOW(), created_at) AS age,
  created_at + INTERVAL '7 days' AS next_week
FROM events;
```

### JSON/JSONB Types

```sql
-- JSON: Stores raw text, validates JSON syntax
-- JSONB: Stores binary, supports indexing

CREATE TABLE user_profiles (
  id SERIAL PRIMARY KEY,
  profile JSONB NOT NULL,
  CHECK (jsonb_typeof(profile) = 'object')
);

-- Insert JSON
INSERT INTO user_profiles (profile) VALUES
  ('{"name": "John", "age": 30, "hobbies": ["reading", "gaming"], "address": {"city": "NYC"}}');

-- Query JSON
SELECT
  profile->>'name' AS name,
  profile->'hobbies' AS hobbies,
  profile->'address'->>'city' AS city,
  jsonb_array_length(profile->'hobbies') AS hobby_count
FROM user_profiles;

-- JSON operators
SELECT * FROM user_profiles WHERE profile @> '{"name": "John"}';
SELECT * FROM user_profiles WHERE profile->>'age' = '30';
SELECT * FROM user_profiles WHERE profile ? 'hobbies';
SELECT * FROM user_profiles WHERE profile ?| ARRAY['name', 'email'];

-- Modify JSON
UPDATE user_profiles
SET profile = jsonb_set(profile, '{age}', '31')
WHERE id = 1;

UPDATE user_profiles
SET profile = profile - 'old_field'
WHERE id = 1;

-- GIN index for JSONB
CREATE INDEX idx_profile ON user_profiles USING GIN (profile);
```

### Array Types

```sql
CREATE TABLE products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  tags TEXT[],
  prices NUMERIC(10,2)[]
);

-- Insert arrays
INSERT INTO products (name, tags, prices)
VALUES ('Laptop', ARRAY['electronics', 'computers'], ARRAY[999.99, 899.99]);

-- Query arrays
SELECT * FROM products WHERE 'electronics' = ANY(tags);
SELECT * FROM products WHERE tags @> ARRAY['electronics'];
SELECT * FROM products WHERE array_length(tags, 1) > 1;

-- Array functions
SELECT
  name,
  tags[1] AS first_tag,
  array_append(tags, 'new_tag') AS with_new_tag,
  array_remove(tags, 'computers') AS without_computers,
  array_cat(prices, ARRAY[799.99]) AS all_prices
FROM products;
```

---

## MVCC

### How MVCC Works

```
┌─────────────────────────────────────────────────────────────┐
│                    MVCC Overview                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Transaction 1: BEGIN                                       │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Sees snapshot of database at transaction start     │    │
│  │  Read: version from before any concurrent changes   │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  Transaction 2: BEGIN (concurrent)                          │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Sees snapshot of database at its start time       │    │
│  │  May see different versions than Transaction 1     │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  Row Versioning:                                            │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  xmin: Transaction that created the row            │    │
│  │  xmax: Transaction that deleted/updated the row    │    │
│  │  ctid: Tuple ID (page, offset)                     │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Transaction Isolation Levels

```sql
-- Read Uncommitted (not truly uncommitted in PostgreSQL)
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

-- Read Committed (default)
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- Repeatable Read
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;

-- Serializable
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

-- Check current isolation level
SHOW transaction_isolation;

-- Transaction examples
BEGIN;
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
SELECT * FROM accounts WHERE id = 1;
-- Same query returns same result within transaction
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
COMMIT;
```

### VACUUM

```sql
-- VACUUM reclaims storage from dead tuples
VACUUM users;

-- VACUUM ANALYZE updates statistics
VACUUM ANALYZE users;

-- VACUUM FULL rewrites table (locks table)
VACUUM FULL users;

-- Autovacuum settings
SHOW autovacuum;
SHOW autovacuum_vacuum_threshold;
SHOW autovacuum_vacuum_scale_factor;

-- Manual vacuum for specific table
VACUUM (VERBOSE, ANALYZE) users;

-- Check table bloat
SELECT
  schemaname,
  tablename,
  pg_size_pretty(pg_total_relation_size(schemaname || '.' || tablename)) AS total_size,
  n_dead_tup,
  last_vacuum,
  last_autovacuum
FROM pg_stat_user_tables
WHERE n_dead_tup > 0
ORDER BY n_dead_tup DESC;
```

---

## Extensions

### Popular Extensions

```sql
-- Enable extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "btree_gist";

-- UUID generation
SELECT uuid_generate_v4();

-- Crypto functions
SELECT crypt('password', gen_salt('bf'));
SELECT crypt('password', '$2a$10$...') = '$2a$10$...' AS valid;

-- Trigram similarity
SELECT * FROM users
WHERE similarity(username, 'johndoe') > 0.3;

-- List installed extensions
SELECT * FROM pg_extension;

-- Available extensions
SELECT * FROM pg_available_extensions ORDER BY name;
```

### PostGIS Extension

```sql
-- Install PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

-- Create spatial table
CREATE TABLE locations (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  geom GEOMETRY(POINT, 4326)
);

-- Insert spatial data
INSERT INTO locations (name, geom)
VALUES ('Office', ST_SetSRID(ST_MakePoint(-73.9857, 40.7484), 4326));

-- Spatial queries
SELECT
  name,
  ST_AsText(geom) AS coordinates,
  ST_Distance(
    geom::geography,
    ST_SetSRID(ST_MakePoint(-73.9857, 40.7484), 4326)::geography
  ) AS distance_meters
FROM locations
ORDER BY geom <-> ST_SetSRID(ST_MakePoint(-73.9857, 40.7484), 4326);

-- Find nearby points
SELECT name
FROM locations
WHERE ST_DWithin(
  geom::geography,
  ST_SetSRID(ST_MakePoint(-73.9857, 40.7484), 4326)::geography,
  1000  -- 1 km radius
);
```

---

## Backup and Recovery

### Logical Backup

```bash
# Full backup
pg_dump -U user -d mydb -F p -f backup.sql

# Custom format (compressed, can be restored selectively)
pg_dump -U user -d mydb -F c -f backup.dump

# Parallel backup
pg_dump -U user -d mydb -F d -j 4 -f backup_dir/

# Backup all databases
pg_dumpall -U user -f all_databases.sql

# Backup globals (roles, tablespaces)
pg_dumpall -U user --globals-only -f globals.sql
```

### Physical Backup

```bash
# Base backup
pg_basebackup -U replicant -D /backup/base -Ft -z -P

# Continuous archiving
# postgresql.conf:
# archive_mode = on
# archive_command = 'cp %p /archive/%f'

# Restore from backup
pg_ctl stop -D /var/lib/postgresql/data
rm -rf /var/lib/postgresql/data/*
tar -xzf base.tar.gz -C /var/lib/postgresql/data/
tar -xzf pg_wal.tar.gz -C /var/lib/postgresql/data/pg_wal/
cp recovery.conf /var/lib/postgresql/data/
pg_ctl start -D /var/lib/postgresql/data
```

### Point-in-Time Recovery

```sql
-- Configure recovery
-- postgresql.conf:
-- restore_command = 'cp /archive/%f %p'
-- recovery_target_time = '2024-01-15 14:30:00'
-- recovery_target_action = 'promote'

-- Create recovery signal file
-- touch /var/lib/postgresql/data/recovery.signal

-- Start PostgreSQL
pg_ctl start -D /var/lib/postgresql/data
```

---

## Best Practices

### Naming Conventions

```sql
-- Tables: plural nouns, snake_case
CREATE TABLE user_orders (...);
CREATE TABLE product_categories (...);

-- Columns: snake_case
CREATE TABLE users (
  user_id SERIAL PRIMARY KEY,
  first_name VARCHAR(50),
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Indexes: idx_table_column
CREATE INDEX idx_users_email ON users (email);

-- Primary keys: table_name_id
-- Foreign keys: referenced_table_name_id
```

### Performance Tips

```sql
-- 1. Use appropriate data types
-- BAD: VARCHAR for numbers
-- GOOD: INTEGER for numeric values

-- 2. Create proper indexes
CREATE INDEX idx_orders_user_id ON orders (user_id);
CREATE INDEX idx_orders_created ON orders (created_at DESC);

-- 3. Use EXPLAIN ANALYZE
EXPLAIN ANALYZE
SELECT * FROM orders WHERE user_id = 1 AND created_at > '2024-01-01';

-- 4. Avoid SELECT *
SELECT id, username, email FROM users WHERE id = 1;

-- 5. Use connection pooling (PgBouncer)

-- 6. Tune shared_buffers and work_mem

-- 7. Use prepared statements
PREPARE user_query (int) AS SELECT * FROM users WHERE id = $1;
EXECUTE user_query(1);
```

### Monitoring

```sql
-- Check table sizes
SELECT
  schemaname,
  tablename,
  pg_size_pretty(pg_total_relation_size(schemaname || '.' || tablename))
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname || '.' || tablename) DESC;

-- Check index usage
SELECT
  schemaname,
  tablename,
  indexname,
  idx_scan,
  idx_tup_read,
  idx_tup_fetch
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;

-- Check slow queries
SELECT
  pid,
  now() - pg_stat_activity.query_start AS duration,
  query,
  state
FROM pg_stat_activity
WHERE (now() - pg_stat_activity.query_start) > interval '5 minutes'
  AND state = 'active';

-- Check locks
SELECT
  l.locktype,
  l.mode,
  l.granted,
  a.pid,
  a.query
FROM pg_locks l
JOIN pg_stat_activity a ON l.pid = a.pid
WHERE NOT l.granted;
```

---

## Summary

| Feature | Description |
|---------|-------------|
| MVCC | Non-blocking reads |
| JSONB | Native JSON support |
| Extensions | PostGIS, pg_trgm, etc. |
| Partitioning | Declarative partitioning |
| CTEs | WITH clause |
| Window Functions | Analytical queries |
| Full Text Search | Built-in FTS |

## Next Steps

- [PostgreSQL Queries](../queries/) - Advanced querying
- [PostgreSQL Optimization](../optimization/) - Performance tuning
- [PostgreSQL Replication](../replication/) - High availability
- [PostgreSQL Extensions](../extensions/) - Extension ecosystem
