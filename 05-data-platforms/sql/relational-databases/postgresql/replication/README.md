# PostgreSQL Replication

## Table of Contents

1. [Replication Overview](#replication-overview)
2. [Streaming Replication](#streaming-replication)
3. [Logical Replication](#logical-replication)
4. [Replication Slots](#replication-slots)
5. [Synchronous Replication](#synchronous-replication)
6. [Cascading Replication](#cascading-replication)
7. [Monitoring](#monitoring)
8. [Best Practices](#best-practices)

---

## Replication Overview

### How PostgreSQL Replication Works

```
┌─────────────────────────────────────────────────────────────┐
│                    Primary Server                            │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  WAL (Write-Ahead Log)                              │    │
│  │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐         │    │
│  │  │WAL  │ │WAL  │ │WAL  │ │WAL  │ │WAL  │  ...     │    │
│  │  │Seg 1│ │Seg 2│ │Seg 3│ │Seg 4│ │Seg 5│         │    │
│  │  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘         │    │
│  └─────────────────────────────────────────────────────┘    │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ 1. WAL sender process
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                   Standby Server                             │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  WAL Receiver                                      │    │
│  │  - Receives WAL from primary                        │    │
│  │  - Writes to local WAL files                        │    │
│  └─────────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Startup Process                                    │    │
│  │  - Applies WAL to data files                        │    │
│  │  - Handles replay                                    │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### Replication Types

| Type | Description | Use Case |
|------|-------------|----------|
| Streaming | Physical replication of WAL | HA, read replicas |
| Logical | Selective replication of changes | Data integration, migration |
| Synchronous | Wait for standby confirmation | Zero data loss |
| Asynchronous | Don't wait for standby | Performance |

---

## Streaming Replication

### Primary Setup

```ini
# postgresql.conf on primary

# WAL configuration
wal_level = replica
max_wal_senders = 10
wal_keep_size = 1GB

# Synchronous replication (optional)
# synchronous_standby_names = 'standby1'

# Archiving (optional)
archive_mode = on
archive_command = 'cp %p /archive/%f'
```

```sql
-- Create replication user
CREATE USER replicant WITH REPLICATION LOGIN PASSWORD 'password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE mydb TO replicant;
```

```ini
# pg_hba.conf on primary
# TYPE  DATABASE  USER       ADDRESS        METHOD
host    replication replicant 192.168.1.0/24 md5
```

### Standby Setup

```bash
# Base backup from primary
pg_basebackup -h primary-host -U replicant -D /var/lib/postgresql/data -Fp -Xs -P -R

# The -R flag automatically creates standby.signal and configures primary_conninfo

# Or manually create standby.signal
touch /var/lib/postgresql/data/standby.signal

# Configure primary_conninfo in postgresql.auto.conf
# (pg_basebackup -R does this automatically)
```

```ini
# postgresql.conf on standby
hot_standby = on
primary_conninfo = 'host=primary-host port=5432 user=replicant password=password'
```

```bash
# Start standby
pg_ctl start -D /var/lib/postgresql/data

# Check replication status on primary
SELECT * FROM pg_stat_replication;
```

---

## Logical Replication

### Setup

```ini
# postgresql.conf on publisher
wal_level = logical
max_replication_slots = 10
max_wal_senders = 10
```

```sql
-- Create publication on publisher
CREATE PUBLICATION my_pub FOR TABLE users, orders;

-- Or for all tables
CREATE PUBLICATION all_tables_pub FOR ALL TABLES;

-- Check publications
SELECT * FROM pg_publication;
SELECT * FROM pg_publication_tables;
```

### Subscription

```sql
-- Create subscription on subscriber
CREATE SUBSCRIPTION my_sub
  CONNECTION 'host=publisher-host port=5432 dbname=mydb user=replicant password=password'
  PUBLICATION my_pub;

-- Or with options
CREATE SUBSCRIPTION my_sub
  CONNECTION 'host=publisher-host port=5432 dbname=mydb user=replicant password=password'
  PUBLICATION my_pub
  WITH (
    copy_data = true,
    create_slot = true,
    synchronous_commit = 'off',
    binary = true
  );

-- Check subscriptions
SELECT * FROM pg_subscription;
SELECT * FROM pg_stat_subscription;
```

### Logical Replication Operations

```sql
-- Refresh subscription
ALTER SUBSCRIPTION my_sub REFRESH PUBLICATION;

-- Disable/enable subscription
ALTER SUBSCRIPTION my_sub DISABLE;
ALTER SUBSCRIPTION my_sub ENABLE;

-- Drop subscription
DROP SUBSCRIPTION my_sub;

-- Drop publication
DROP PUBLICATION my_pub;
```

---

## Replication Slots

### Physical Replication Slots

```sql
-- Create physical replication slot
SELECT pg_create_physical_replication_slot('standby1_slot');

-- Use slot on standby
-- postgresql.conf:
-- primary_slot_name = 'standby1_slot'

-- Check slot status
SELECT
  slot_name,
  slot_type,
  active,
  xmin,
  restart_lsn,
  confirmed_flush_lsn
FROM pg_replication_slots;

-- Drop slot
SELECT pg_drop_replication_slot('standby1_slot');
```

### Logical Replication Slots

```sql
-- Create logical replication slot
SELECT pg_create_logical_replication_slot('my_slot', 'pgoutput');

-- Check slot status
SELECT
  slot_name,
  slot_type,
  active,
  database,
  xmin,
  restart_lsn,
  confirmed_flush_lsn
FROM pg_replication_slots;

-- Advance slot (mark WAL as consumed)
SELECT pg_replication_slot_advance('my_slot', '0/1234567');

-- Drop slot
SELECT pg_drop_replication_slot('my_slot');
```

### Slot Monitoring

```sql
-- Check slot activity
SELECT
  slot_name,
  active,
  restart_lsn,
  confirmed_flush_lsn,
  pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), restart_lsn)) AS slot_lag
FROM pg_replication_slots;

-- Monitor WAL retention
SELECT
  slot_name,
  pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), restart_lsn)) AS retained_wal
FROM pg_replication_slots
WHERE active = false;
```

---

## Synchronous Replication

### Configuration

```ini
# postgresql.conf on primary
synchronous_standby_names = 'FIRST 1 (standby1, standby2)'

# Options:
# ANY 1 (standby1, standby2) - Any one standby
# FIRST 1 (standby1, standby2) - First standby
# FIRST 2 (standby1, standby2) - First two standbys
```

```sql
-- Check synchronous replication status
SELECT
  pid,
  usename,
  application_name,
  client_addr,
  sync_state,
  sync_priority,
  sent_lsn,
  write_lsn,
  flush_lsn,
  replay_lsn
FROM pg_stat_replication;

-- sync_state values:
-- async: Asynchronous replication
-- potential: Could be synchronous
-- sync: Currently synchronous
-- quorum: Part of quorum
```

### Synchronous Commit

```sql
-- Control synchronous commit per transaction
BEGIN;
SET LOCAL synchronous_commit = on;  -- Wait for standby
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
COMMIT;

BEGIN;
SET LOCAL synchronous_commit = off;  -- Don't wait
INSERT INTO logs (message) VALUES ('test');
COMMIT;

-- Global setting
SET synchronous_commit = on;
```

---

## Cascading Replication

### Setup

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Primary   │────▶│  Standby 1  │────▶│  Standby 2  │
│             │     │  (Cascader) │     │             │
└─────────────┘     └─────────────┘     └─────────────┘
```

```sql
-- On Standby 1 (cascader)
-- postgresql.conf:
hot_standby = on
primary_conninfo = 'host=primary-host port=5432 user=replicant password=password'
primary_slot_name = 'standby1_slot'

-- Create slot for Standby 2
SELECT pg_create_physical_replication_slot('standby2_slot');

-- On Standby 2
-- postgresql.conf:
hot_standby = on
primary_conninfo = 'host=standby1-host port=5432 user=replicant password=password'
primary_slot_name = 'standby2_slot'
```

### Monitoring Cascading

```sql
-- Check replication chain on Standby 1
SELECT * FROM pg_stat_replication;

-- Check Standby 2 is replicating from Standby 1
SELECT
  pid,
  usename,
  application_name,
  client_addr,
  sync_state,
  sent_lsn,
  write_lsn,
  replay_lsn
FROM pg_stat_replication
WHERE client_addr = 'standby2-host';
```

---

## Monitoring

### Replication Status

```sql
-- Check replication status on primary
SELECT
  pid,
  usename,
  application_name,
  client_addr,
  client_port,
  backend_start,
  state,
  sent_lsn,
  write_lsn,
  flush_lsn,
  replay_lsn,
  sync_priority,
  sync_state,
  pg_size_pretty(pg_wal_lsn_diff(sent_lsn, replay_lsn)) AS replication_lag
FROM pg_stat_replication;

-- Check WAL generation
SELECT
  pg_current_wal_lsn() AS current_wal_lsn,
  pg_current_wal_insert_lsn() AS insert_lsn,
  pg_current_wal_flush_lsn() AS flush_lsn;

-- Check replication lag
SELECT
  application_name,
  pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), replay_lsn)) AS lag
FROM pg_stat_replication;
```

### Replication Slots

```sql
-- Check slot status
SELECT
  slot_name,
  slot_type,
  active,
  database,
  xmin,
  restart_lsn,
  confirmed_flush_lsn,
  pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), restart_lsn)) AS retained_wal
FROM pg_replication_slots;

-- Monitor slot lag
SELECT
  slot_name,
  active,
  pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), restart_lsn)) AS wal_retained
FROM pg_replication_slots
WHERE NOT active;
```

### Standby Status

```sql
-- Check if standby is in recovery
SELECT pg_is_in_recovery();

-- Check standby configuration
SELECT
  name,
  setting,
  unit
FROM pg_settings
WHERE name IN (
  'hot_standby',
  'primary_conninfo',
  'primary_slot_name',
  'wal_receiver_status_interval'
);

-- Check WAL receiver
SELECT
  pid,
  status,
  received_start_lsn,
  received_tli,
  last_msg_send_time,
  last_msg_receipt_time
FROM pg_stat_wal_receiver;
```

---

## Best Practices

### Configuration

```sql
-- 1. Use replication slots to prevent WAL removal
SELECT pg_create_physical_replication_slot('standby1_slot');

-- 2. Set appropriate wal_keep_size
ALTER SYSTEM SET wal_keep_size = '1GB';

-- 3. Enable hot_standby for read replicas
ALTER SYSTEM SET hot_standby = on;

-- 4. Configure synchronous replication for zero data loss
ALTER SYSTEM SET synchronous_standby_names = 'FIRST 1 (standby1)';

-- 5. Monitor replication lag regularly
```

### Monitoring

```sql
-- Set up alerts for:
-- - Replication lag > 10 seconds
-- - Standby disconnection
-- - WAL retention > threshold
-- - Slot inactivity

-- Regular health checks
SELECT * FROM pg_stat_replication;
SELECT * FROM pg_replication_slots;
SELECT * FROM pg_stat_wal_receiver;
```

### Failover Procedures

```sql
-- 1. Stop writes to primary
-- 2. Verify standby is caught up
SELECT
  pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), replay_lsn)) AS lag
FROM pg_stat_replication
WHERE client_addr = 'standby-host';

-- 3. Promote standby
SELECT pg_promote();

-- 4. Update application connection strings

-- 5. Rebuild old primary as standby
```

### Backup Strategy

```sql
-- 1. Take backups from standby (not primary)
pg_basebackup -h standby-host -U replicant -D /backup -Ft -z -P

-- 2. Use pg_dump for logical backups
pg_dump -h standby-host -U replicant -d mydb -F c -f backup.dump

-- 3. Test backup restoration regularly

-- 4. Keep backups for at least 30 days
```

---

## Summary

| Feature | Description |
|---------|-------------|
| Streaming | Physical replication of WAL |
| Logical | Selective replication |
| Slots | Prevent WAL removal |
| Synchronous | Zero data loss |
| Cascading | Chain of standbys |

## Next Steps

- [PostgreSQL Fundamentals](../fundamentals/) - PostgreSQL basics
- [PostgreSQL Optimization](../optimization/) - Performance tuning
- [PostgreSQL Extensions](../extensions/) - Extension ecosystem
