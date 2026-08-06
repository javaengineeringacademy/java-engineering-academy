# MySQL Replication

## Table of Contents

1. [Replication Overview](#replication-overview)
2. [Asynchronous Replication](#asynchronous-replication)
3. [GTID Replication](#gtid-replication)
4. [Semi-Synchronous Replication](#semi-synchronous-replication)
5. [Replication Topologies](#replication-topologies)
6. [Replication Monitoring](#replication-monitoring)
7. [Failover Procedures](#failover-procedures)
8. [Best Practices](#best-practices)

---

## Replication Overview

### How MySQL Replication Works

```
┌─────────────────────────────────────────────────────────────┐
│                    Master Server                             │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Binary Log (binlog)                                │    │
│  │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐         │    │
│  │  │Event│ │Event│ │Event│ │Event│ │Event│  ...      │    │
│  │  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘         │    │
│  └─────────────────────────────────────────────────────┘    │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ 1. Write events to binlog
                           │
                           │ 2. Dump thread reads binlog
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                   Slave Server                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  I/O Thread                                        │    │
│  │  - Receives binlog events from master               │    │
│  │  - Writes to relay log                              │    │
│  └─────────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  SQL Thread                                         │    │
│  │  - Reads relay log                                  │    │
│  │  - Applies events to slave database                 │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### Replication Formats

```sql
-- Statement-Based Replication (SBR)
-- Replicates SQL statements
SET GLOBAL binlog_format = 'STATEMENT';
-- Pros: Compact logs, less network traffic
-- Cons: Non-deterministic functions can cause issues

-- Row-Based Replication (RBR)
-- Replicates row changes
SET GLOBAL binlog_format = 'ROW';
-- Pros: Consistent results, safer
-- Cons: Larger binlog, more network traffic

-- Mixed-Based Replication (MBR)
-- Automatic switch between SBR and RBR
SET GLOBAL binlog_format = 'MIXED';
-- Default for MySQL 5.7

-- Recommended: ROW format for MySQL 8.0
SET GLOBAL binlog_format = 'ROW';
```

---

## Asynchronous Replication

### Basic Setup

```sql
-- MASTER SERVER CONFIGURATION
-- /etc/mysql/my.cnf

[mysqld]
server-id = 1
log-bin = mysql-bin
binlog-format = ROW
binlog-checksum = NONE
gtid-mode = ON
enforce-gtid-consistency = ON

-- Create replication user
CREATE USER 'repl_user'@'%' IDENTIFIED BY 'secure_password';
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'repl_user'@'%';
FLUSH PRIVILEGES;

-- Check master status
SHOW MASTER STATUS;
-- +------------------+----------+--------------+------------------+-------------------+
-- | File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
-- +------------------+----------+--------------+------------------+-------------------+
-- | mysql-bin.000003 |      785 |              |                  |                   |
-- +------------------+----------+--------------+------------------+-------------------+


-- SLAVE SERVER CONFIGURATION
-- /etc/mysql/my.cnf

[mysqld]
server-id = 2
relay-log = relay-bin
read-only = ON
super-read-only = ON
log-slave-updates = ON

-- Configure slave to connect to master
CHANGE MASTER TO
  MASTER_HOST = '192.168.1.100',
  MASTER_PORT = 3306,
  MASTER_USER = 'repl_user',
  MASTER_PASSWORD = 'secure_password',
  MASTER_LOG_FILE = 'mysql-bin.000003',
  MASTER_LOG_POS = 785;

-- Start replication
START SLAVE;

-- Check slave status
SHOW SLAVE STATUS\G
```

### GTID Replication

```sql
-- GTID Configuration
-- MASTER
[mysqld]
server-id = 1
log-bin = mysql-bin
binlog-format = ROW
gtid-mode = ON
enforce-gtid-consistency = ON
log-slave-updates = ON

-- SLAVE
[mysqld]
server-id = 2
relay-log = relay-bin
read-only = ON
super-read-only = ON
gtid-mode = ON
enforce-gtid-consistency = ON
log-slave-updates = ON

-- GTID-based CHANGE MASTER
CHANGE MASTER TO
  MASTER_HOST = '192.168.1.100',
  MASTER_PORT = 3306,
  MASTER_USER = 'repl_user',
  MASTER_PASSWORD = 'secure_password',
  MASTER_AUTO_POSITION = 1;

-- Start replication
START SLAVE;

-- Check GTID status
SHOW MASTER STATUS;
SELECT @@gtid_mode;
SELECT @@server_uuid;

-- Monitor GTID execution
SELECT * FROM mysql.gtid_executed;
SHOW SLAVE STATUS\G
-- Look for: Executed_Gtid_Set, Retrieved_Gtid_Set
```

---

## Semi-Synchronous Replication

### Configuration

```sql
-- MASTER SERVER
-- Install plugin
INSTALL PLUGIN rpl_semi_sync_master SONAME 'semisync_master.so';

-- Enable plugin
SET GLOBAL rpl_semi_sync_master_enabled = ON;
SET GLOBAL rpl_semi_sync_master_timeout = 5000;  -- 5 seconds

-- Check status
SHOW STATUS LIKE 'Rpl_semi_sync%';

-- SLAVE SERVER
-- Install plugin
INSTALL PLUGIN rpl_semi_sync_slave SONAME 'semisync_slave.so';

-- Enable plugin
SET GLOBAL rpl_semi_sync_slave_enabled = ON;

-- Restart replication
STOP SLAVE;
START SLAVE;

-- Check status
SHOW STATUS LIKE 'Rpl_semi_sync%';
```

### Semi-Synchronous Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    Master Server                             │
│  1. Write transaction to binlog                             │
│  2. Commit transaction                                      │
│  3. Wait for slave acknowledgment (timeout: 5s)            │
│     - If slave acknowledges: return success                 │
│     - If timeout: fall back to async, return success        │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ 4. Send binlog events
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                   Slave Server                               │
│  5. Receive binlog events                                   │
│  6. Write to relay log                                      │
│  7. Send ACK to master                                      │
└─────────────────────────────────────────────────────────────┘
```

### Monitoring Semi-Sync

```sql
-- Master status
SHOW STATUS LIKE 'Rpl_semi_sync_master_status';
-- Rpl_semi_sync_master_status: ON = semi-sync active
-- Rpl_semi_sync_master_status: OFF = fell back to async

SHOW STATUS LIKE 'Rpl_semi_sync_master_clients';
-- Number of connected slaves

SHOW STATUS LIKE 'Rpl_semi_sync_master_yes_tx';
-- Number of transactions that got acknowledgments

SHOW STATUS LIKE 'Rpl_semi_sync_master_no_tx';
-- Number of transactions that timed out

-- Slave status
SHOW STATUS LIKE 'Rpl_semi_sync_slave_status';
-- Rpl_semi_sync_slave_status: ON = semi-sync active
```

---

## Replication Topologies

### Master-Slave (1:Many)

```
                    ┌─────────────┐
                    │   Master    │
                    │  (Write)    │
                    └──────┬──────┘
                           │
            ┌──────────────┼──────────────┐
            │              │              │
            ▼              ▼              ▼
      ┌─────────┐    ┌─────────┐    ┌─────────┐
      │ Slave 1 │    │ Slave 2 │    │ Slave 3 │
      │ (Read)  │    │ (Read)  │    │ (Read)  │
      └─────────┘    └─────────┘    └─────────┘
```

```sql
-- Configure multiple slaves
-- Slave 1
CHANGE MASTER TO
  MASTER_HOST = '192.168.1.100',
  MASTER_USER = 'repl_user',
  MASTER_PASSWORD = 'password',
  MASTER_AUTO_POSITION = 1;

-- Slave 2
CHANGE MASTER TO
  MASTER_HOST = '192.168.1.100',
  MASTER_USER = 'repl_user',
  MASTER_PASSWORD = 'password',
  MASTER_AUTO_POSITION = 1;

-- Slave 3
CHANGE MASTER TO
  MASTER_HOST = '192.168.1.100',
  MASTER_USER = 'repl_user',
  MASTER_PASSWORD = 'password',
  MASTER_AUTO_POSITION = 1;
```

### Chain Replication (Master → Slave1 → Slave2)

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Master    │────▶│   Slave 1   │────▶│   Slave 2   │
│  (Write)    │     │  (Read)     │     │  (Read)     │
└─────────────┘     └─────────────┘     └─────────────┘
```

```sql
-- Slave 1 (from Master)
CHANGE MASTER TO
  MASTER_HOST = '192.168.1.100',
  MASTER_USER = 'repl_user',
  MASTER_PASSWORD = 'password',
  MASTER_AUTO_POSITION = 1;

-- Slave 2 (from Slave 1)
-- On Slave 1, enable log-slave-updates
SET GLOBAL log_slave_updates = ON;

-- On Slave 2
CHANGE MASTER TO
  MASTER_HOST = '192.168.1.101',  -- Slave 1's IP
  MASTER_USER = 'repl_user',
  MASTER_PASSWORD = 'password',
  MASTER_AUTO_POSITION = 1;
```

### Circular Replication (Multi-Master)

```
┌─────────────┐     ┌─────────────┐
│   Master 1  │────▶│   Master 2  │
│  (Active)   │     │  (Passive)  │
└─────────────┘     └─────────────┘
       ▲                   │
       └───────────────────┘
```

```sql
-- Master 1
[mysqld]
server-id = 1
log-bin = mysql-bin
binlog-format = ROW
gtid-mode = ON
enforce-gtid-consistency = ON
log-slave-updates = ON

-- Master 2
[mysqld]
server-id = 2
log-bin = mysql-bin
binlog-format = ROW
gtid-mode = ON
enforce-gtid-consistency = ON
log-slave-updates = ON

-- On Master 1
CHANGE MASTER TO
  MASTER_HOST = '192.168.1.101',
  MASTER_USER = 'repl_user',
  MASTER_PASSWORD = 'password',
  MASTER_AUTO_POSITION = 1;

-- On Master 2
CHANGE MASTER TO
  MASTER_HOST = '192.168.1.100',
  MASTER_USER = 'repl_user',
  MASTER_PASSWORD = 'password',
  MASTER_AUTO_POSITION = 1;
```

---

## Replication Monitoring

### Essential Commands

```sql
-- Check replication status
SHOW SLAVE STATUS\G

-- Key fields to monitor:
-- Slave_IO_Running: Should be Yes
-- Slave_SQL_Running: Should be Yes
-- Seconds_Behind_Master: Replication lag
-- Last_IO_Error: IO thread errors
-- Last_SQL_Error: SQL thread errors
-- Retrieved_Gtid_Set: GTIDs received
-- Executed_Gtid_Set: GTIDs applied

-- Check master status
SHOW MASTER STATUS;

-- Check binary log events
SHOW BINLOG EVENTS IN 'mysql-bin.000001' LIMIT 10;

-- Monitor replication lag
SELECT
  UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(MAX(ts)) AS lag_seconds
FROM mysql.slave_master_info;

-- Check GTID execution
SELECT * FROM mysql.gtid_executed;
```

### Performance Schema Monitoring

```sql
-- Replication status via Performance Schema
SELECT
  CHANNEL_NAME,
  SERVICE_STATE,
  SOURCE_USER,
  REMOTE_HOST,
  RECEIVED_TRANSACTION_SET
FROM performance_schema.replication_connection_status;

SELECT
  CHANNEL_NAME,
  SERVICE_STATE,
  LAST_APPLIED_TRANSACTION,
  APPLYING_TRANSACTION,
  LAST_APPLIED_TRANSACTION_END_APPLY_TIMESTAMP
FROM performance_schema.replication_applier_status;

-- Replication configuration
SELECT
  CHANNEL_NAME,
  SERVICE_STATE,
  THREAD_ID
FROM performance_schema.replication_applier_status_by_worker;
```

### Monitoring Scripts

```bash
#!/bin/bash
# check_replication.sh

MASTER_HOST="192.168.1.100"
SLAVE_HOST="192.168.1.101"

# Check slave status
SLAVE_STATUS=$(mysql -h $SLAVE_HOST -u root -p'password' -e "SHOW SLAVE STATUS\G" 2>/dev/null)

IO_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_IO_Running:" | awk '{print $2}')
SQL_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_SQL_Running:" | awk '{print $2}')
LAG=$(echo "$SLAVE_STATUS" | grep "Seconds_Behind_Master:" | awk '{print $2}')

if [ "$IO_RUNNING" != "Yes" ] || [ "$SQL_RUNNING" != "Yes" ]; then
    echo "CRITICAL: Replication broken on $SLAVE_HOST"
    # Send alert
fi

if [ "$LAG" -gt 60 ]; then
    echo "WARNING: Replication lag $LAG seconds on $SLAVE_HOST"
    # Send alert
fi
```

---

## Failover Procedures

### Manual Failover

```sql
-- 1. Stop writes to master
-- (Application should stop accepting writes)

-- 2. Check slave is caught up
SHOW SLAVE STATUS\G
-- Verify Seconds_Behind_Master = 0

-- 3. Stop slave
STOP SLAVE;
RESET SLAVE ALL;

-- 4. Promote slave to master
SET GLOBAL read_only = OFF;
SET GLOBAL super_read_only = OFF;

-- 5. Update application connection strings

-- 6. Configure other slaves to point to new master
-- On each remaining slave:
STOP SLAVE;
CHANGE MASTER TO
  MASTER_HOST = '192.168.1.101',  -- New master
  MASTER_USER = 'repl_user',
  MASTER_PASSWORD = 'password',
  MASTER_AUTO_POSITION = 1;
START SLAVE;
```

### Orchestrated Failover (MHA)

```bash
# Install MHA
apt-get install mha4mysql-manager mha4mysql-node

# Configuration file
cat > /etc/mha/app.cnf << 'EOF'
[server default]
manager_workdir=/var/log/mha/app
manager_log=/var/log/mha/app/manager.log
user=mha_user
password=mha_password
ssh_user=root
repl_user=repl_user
repl_password=repl_password

[server1]
hostname=192.168.1.100
candidate_master=1

[server2]
hostname=192.168.1.101
candidate_master=1

[server3]
hostname=192.168.1.102
no_master=1
EOF

# Check replication
masterha_check_repl --conf=/etc/mha/app.cnf

# Start MHA manager
masterha_manager --conf=/etc/mha/app.cnf &
```

### Failover Best Practices

```sql
-- 1. Use GTID for automatic position tracking
-- 2. Enable semi-sync to minimize data loss
-- 3. Monitor replication lag continuously
-- 4. Test failover procedures regularly
-- 5. Use orchestration tools (MHA, Orchestrator, Group Replication)

-- Pre-failover checklist:
-- [ ] All slaves caught up
-- [ ] No replication errors
-- [ ] Semi-sync ACKs received
-- [ ] Application can reconnect
-- [ ] Monitoring alerts configured

-- Post-failover checklist:
-- [ ] Verify data consistency
-- [ ] Check all applications connected
-- [ ] Monitor for errors
-- [ ] Rebuild old master as slave
```

---

## Best Practices

### Security

```sql
-- 1. Use SSL/TLS for replication
CHANGE MASTER TO
  MASTER_SSL = 1,
  MASTER_SSL_CA = '/etc/mysql/ssl/ca.pem',
  MASTER_SSL_CERT = '/etc/mysql/ssl/client-cert.pem',
  MASTER_SSL_KEY = '/etc/mysql/ssl/client-key.pem';

-- 2. Use dedicated replication user
CREATE USER 'repl_user'@'192.168.1.%'
  IDENTIFIED BY 'strong_password'
  REQUIRE SSL;
GRANT REPLICATION SLAVE ON *.* TO 'repl_user'@'192.168.1.%';

-- 3. Restrict replication user permissions
-- Only REPLICATION SLAVE, not SUPER or ALL
```

### Performance

```sql
-- 1. Use ROW format for consistency
SET GLOBAL binlog_format = 'ROW';

-- 2. Enable GTID for easier management
SET GLOBAL gtid_mode = ON;
SET GLOBAL enforce_gtid_consistency = ON;

-- 3. Optimize slave parallel replication
SET GLOBAL slave_parallel_workers = 4;
SET GLOBAL slave_parallel_type = 'LOGICAL_CLOCK';

-- 4. Monitor and tune replication lag
-- Use pt-heartbeat for accurate lag measurement
```

### Monitoring

```sql
-- 1. Set up alerts for:
-- - Replication lag > 10 seconds
-- - Slave_IO_Running != Yes
-- - Slave_SQL_Running != Yes
-- - Any replication errors

-- 2. Regular health checks
-- Monitor Seconds_Behind_Master
-- Monitor GTID execution
-- Check for replication errors

-- 3. Use performance schema
SELECT * FROM performance_schema.replication_connection_status;
SELECT * FROM performance_schema.replication_applier_status;
```

### Backup Strategy

```sql
-- 1. Backup slave (not master) to avoid impact
mysqldump --all-databases --master-data=2 --single-transaction \
  --user=repl_user --password > backup.sql

-- 2. Use GTID for consistent backups
mysqldump --all-databases --set-gtid-purged=ON \
  --single-transaction --user=repl_user --password > backup.sql

-- 3. Monitor backup size and duration
-- 4. Test backup restoration regularly
-- 5. Keep backups for at least 30 days
```

---

## Troubleshooting

### Common Issues

```sql
-- Issue 1: Slave lagging
-- Check network latency
-- Check slave hardware
-- Check for long-running transactions on slave
-- Enable parallel replication

-- Issue 2: Slave SQL thread stopped
-- Check Last_SQL_Error in SHOW SLAVE STATUS
-- Common causes:
-- - Duplicate key errors
-- - Table not found
-- - Constraint violations

-- Fix: Skip error (use with caution)
STOP SLAVE;
SET GLOBAL sql_slave_skip_counter = 1;
START SLAVE;

-- Issue 3: GTID conflicts
-- Check for duplicate GTIDs
SELECT * FROM mysql.gtid_executed;

-- Reset GTID (use with caution)
RESET MASTER;
-- Or
RESET SLAVE ALL;
```

### Debugging Commands

```sql
-- Check replication status
SHOW SLAVE STATUS\G

-- Check binary log
SHOW BINARY LOGS;
SHOW BINLOG EVENTS IN 'mysql-bin.000001';

-- Check relay log
SHOW RELAYLOG EVENTS;

-- Check replication user
SELECT * FROM mysql.user WHERE Repl_slave_priv = 'Y';

-- Check server-id
SELECT @@server_id;

-- Check GTID mode
SELECT @@gtid_mode;
SELECT @@enforce_gtid_consistency;
```

---

## Summary

| Feature | Description |
|---------|-------------|
| Async Replication | Standard MySQL replication |
| Semi-Sync | Wait for at least one slave ACK |
| GTID | Global Transaction IDs |
| Parallel Replication | Apply transactions in parallel |
| Group Replication | Built-in multi-master |

## Next Steps

- [MySQL High Availability](../HA/) - InnoDB Cluster and Group Replication
- [MySQL Optimization](../optimization/) - Performance tuning
- Replication Concepts - General replication concepts
