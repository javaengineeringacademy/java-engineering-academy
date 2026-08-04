# Replication Fundamentals

## Comprehensive Guide to Database Replication

Replication copies data between databases for redundancy and performance. This guide covers replication types, topologies, and configuration.

---

## Table of Contents

1. [Replication Types](#replication-types)
2. [Replication Topologies](#replication-topologies)
3. [Configuration](#configuration)
4. [Best Practices](#best-practices)

---

## Replication Types

### Synchronous Replication

```
- Write waits for all replicas
- Strong consistency
- Higher latency
- Used for critical data
```

```sql
-- PostgreSQL synchronous replication
ALTER SYSTEM SET synchronous_standby_names = 'replica1';
SELECT pg_reload_conf();
```

### Asynchronous Replication

```
- Write doesn't wait for replicas
- Lower latency
- Eventual consistency
- Used for read scaling
```

```sql
-- MySQL asynchronous replication
CHANGE MASTER TO
  MASTER_HOST='master_host',
  MASTER_USER='repl_user',
  MASTER_PASSWORD='password',
  MASTER_LOG_FILE='mysql-bin.000001',
  MASTER_LOG_POS=0;
START SLAVE;
```

### Semi-Synchronous Replication

```
- Write waits for one replica
- Balance of consistency and performance
- Used for HA setups
```

```sql
-- MySQL semi-synchronous
INSTALL PLUGIN rpl_semi_sync_master SONAME 'semisync_master.so';
SET GLOBAL rpl_semi_sync_master_enabled = 1;
```

---

## Replication Topologies

### Master-Slave

```
+----------------+     +----------------+
|   Master       |---->|   Slave        |
+----------------+     +----------------+
        |
        v
+----------------+
|   Slave        |
+----------------+
```

### Master-Master

```
+----------------+     +----------------+
|   Master 1     |<--->|   Master 2     |
+----------------+     +----------------+
```

### Chain Replication

```
+----------------+     +----------------+     +----------------+
|   Master       |---->|   Replica 1    |---->|   Replica 2    |
+----------------+     +----------------+     +----------------+
```

---

## Configuration

### PostgreSQL Replication

```sql
-- Primary configuration
ALTER SYSTEM SET wal_level = replica;
ALTER SYSTEM SET max_wal_senders = 3;
ALTER SYSTEM SET synchronous_standby_names = 'replica1';

-- Replica configuration
primary_conninfo = 'host=primary_host port=5432 user=repl_user password=password'
```

### MySQL Replication

```sql
-- Primary configuration
[mysqld]
server-id=1
log-bin=mysql-bin
binlog-format=ROW

-- Replica configuration
[mysqld]
server-id=2
relay-log=relay-bin
read-only=1
```

### MongoDB Replication

```javascript
// Replica set initialization
rs.initiate({
  _id: "rs0",
  members: [
    { _id: 0, host: "mongo1:27017" },
    { _id: 1, host: "mongo2:27017" },
    { _id: 2, host: "mongo3:27017" }
  ]
})
```

---

## Best Practices

### 1. Monitor Replication Lag

```sql
-- PostgreSQL
SELECT now() - pg_last_xact_replay_timestamp() AS replication_lag;

-- MySQL
SHOW SLAVE STATUS\G
```

### 2. Use Heartbeat

```sql
-- PostgreSQL
ALTER SYSTEM SET wal_receiver_status_interval = 10;
ALTER SYSTEM SET hot_standby_feedback = on;
```

### 3. Configure Retention

```sql
-- PostgreSQL
ALTER SYSTEM SET wal_keep_segments = 100;

-- MySQL
expire_logs_days = 7
```

### 4. Use Read Replicas

```sql
-- Good - Read from replicas
SELECT * FROM orders;  -- On replica

-- Good - Write to primary
INSERT INTO orders VALUES (1, 100);  -- On primary
```

### 5. Monitor Health

```sql
-- Check replication status
SHOW SLAVE STATUS;

-- Check replication lag
SELECT now() - pg_last_xact_replay_timestamp();

-- Check replication slots
SELECT * FROM pg_replication_slots;
```

---

## Further Reading

- [PostgreSQL Replication](https://www.postgresql.org/docs/current/runtime-replication.html)
- [MySQL Replication](https://dev.mysql.com/doc/refman/8.0/en/replication.html)
- [MongoDB Replication](https://www.mongodb.com/docs/manual/replication/)
