# MySQL High Availability

## Table of Contents

1. [High Availability Overview](#high-availability-overview)
2. [InnoDB Cluster](#innodb-cluster)
3. [Group Replication](#group-replication)
4. [ProxySQL](#proxysql)
5. [MySQL Router](#mysql-router)
6. [MHA (Master High Availability)](#mha)
7. [Orchestrator](#orchestrator)
8. [Best Practices](#best-practices)

---

## High Availability Overview

### Availability Concepts

```
┌─────────────────────────────────────────────────────────────┐
│              High Availability Architecture                  │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌─────────────────┐     ┌─────────────────┐               │
│  │  Load Balancer  │     │   Application   │               │
│  │  (ProxySQL)     │────▶│   Servers       │               │
│  └────────┬────────┘     └─────────────────┘               │
│           │                                                  │
│           ▼                                                  │
│  ┌─────────────────┐                                       │
│  │   MySQL Router  │                                       │
│  └────────┬────────┘                                       │
│           │                                                  │
│    ┌──────┴──────┐                                         │
│    │             │                                          │
│    ▼             ▼                                          │
│ ┌─────────┐ ┌─────────┐                                   │
│ │ Primary │ │Secondary│                                   │
│ │ (Write) │ │ (Read)  │                                   │
│ └─────────┘ └─────────┘                                   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Availability Tiers

| Tier | Uptime | Description |
|------|--------|-------------|
| 99.9% | 8.76 hours/year | Basic HA |
| 99.99% | 52.56 minutes/year | High HA |
| 99.999% | 5.26 minutes/year | Mission Critical |

---

## InnoDB Cluster

### Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                   InnoDB Cluster                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Group Replication                      │    │
│  │  ┌─────────┐    ┌─────────┐    ┌─────────┐        │    │
│  │  │  Node 1 │◄──▶│  Node 2 │◄──▶│  Node 3 │        │    │
│  │  │ PRIMARY │    │SECONDARY│    │SECONDARY│        │    │
│  │  └─────────┘    └─────────┘    └─────────┘        │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              MySQL Router                           │    │
│  │  - Automatic routing                                │    │
│  │  - Read/Write splitting                             │    │
│  │  - Connection pooling                               │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              MySQL Shell                            │    │
│  │  - Cluster management                               │    │
│  │  - Configuration                                    │    │
│  │  - Monitoring                                       │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Setup with MySQL Shell

```javascript
// Connect to first node
mysqlsh root@node1:3306

// Configure instance
dba.configureInstance('root@node1:3306', {
  password: 'password',
  interactive: true,
  restart: true
})

// Create cluster
var cluster = dba.createCluster('myCluster')

// Add nodes
cluster.addInstance('root@node2:3306', {password: 'password'})
cluster.addInstance('root@node3:3306', {password: 'password'})

// Check cluster status
cluster.status()

// Status output:
// {
//   "clusterName": "myCluster",
//   "status": "OK",
//   "topology": {
//     "node1:3306": {
//       "status": "ONLINE",
//       "role": "HA",
//       "version": "8.0.35"
//     },
//     "node2:3306": {
//       "status": "ONLINE",
//       "role": "HA",
//       "version": "8.0.35"
//     },
//     "node3:3306": {
//       "status": "ONLINE",
//       "role": "HA",
//       "version": "8.0.35"
//     }
//   }
// }
```

### Group Replication Configuration

```ini
# /etc/mysql/my.cnf for all nodes

[mysqld]
# Server configuration
server-id = 1  # Unique for each node
gtid-mode = ON
enforce-gtid-consistency = ON
binlog-checksum = NONE
log-slave-updates = ON
binlog-format = ROW

# Group Replication
plugin-load-add = group_replication.so
group_replication_group_name = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
group_replication_start_on_boot = ON
group_replication_local_address = "192.168.1.100:33061"
group_replication_group_seeds = "192.168.1.100:33061,192.168.1.101:33061,192.168.1.102:33061"
group_replication_bootstrap_group = OFF

# For primary node, set bootstrap = ON initially
```

### Cluster Operations

```javascript
// Check cluster status
cluster.status()
cluster.status({extended: 1})  // Detailed status

// Remove a node
cluster.removeInstance('root@node3:3306')

// Rejoin a node
cluster.rejoinInstance('root@node3:3306')

// Switch primary
cluster.setPrimaryInstance('root@node2:3306')

// Dissolve cluster
cluster.dissolve()

// Check cluster errors
cluster.checkInstanceState('root@node3:3306')

// Force node reconfiguration
cluster.reconfigureInstance('root@node3:3306', {force: true})
```

---

## Group Replication

### Configuration

```sql
-- Install plugin
INSTALL PLUGIN group_replication SONAME 'group_replication.so';

-- Check plugin
SELECT * FROM mysql.plugins WHERE name = 'group_replication';

-- Configure group
SET GLOBAL group_replication_group_name = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
SET GLOBAL group_replication_start_on_boot = ON;
SET GLOBAL group_replication_local_address = "192.168.1.100:33061";
SET GLOBAL group_replication_group_seeds = "192.168.1.100:33061,192.168.1.101:33061";
SET GLOBAL group_replication_bootstrap_group = OFF;

-- Bootstrap first node
SET GLOBAL group_replication_bootstrap_group = ON;
START GROUP_REPLICATION;
SET GLOBAL group_replication_bootstrap_group = OFF;

-- Other nodes
START GROUP_REPLICATION;

-- Check group replication status
SELECT * FROM performance_schema.replication_group_members;
-- +---------------------------+--------------------------------------+--------------+-------------+--------------+-------------+
-- | CHANNEL_NAME              | MEMBER_ID                            | MEMBER_HOST  | MEMBER_PORT | MEMBER_STATE | MEMBER_ROLE |
-- +---------------------------+--------------------------------------+--------------+-------------+--------------+-------------+
-- | group_replication_applier | xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx | 192.168.1.100|        3306 | ONLINE       | PRIMARY     |
-- | group_replication_applier | xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx | 192.168.1.101|        3306 | ONLINE       | SECONDARY   |
-- | group_replication_applier | xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx | 192.168.1.102|        3306 | ONLINE       | SECONDARY   |
-- +---------------------------+--------------------------------------+--------------+-------------+--------------+-------------+
```

### Single Primary Mode

```sql
-- Configure single primary mode
SET GLOBAL group_replication_single_primary_mode = ON;
SET GLOBAL group_replication_enforce_update_everywhere_checks = OFF;

-- Check primary
SELECT MEMBER_ID, MEMBER_HOST, MEMBER_PORT, MEMBER_STATE, MEMBER_ROLE
FROM performance_schema.replication_group_members;

-- Force primary election
SELECT group_replication_set_as_primary('member_uuid');

-- Switch to multi-primary mode
SELECT group_replication_switch_to_multi_primary_mode();

-- Switch to single primary mode
SELECT group_replication_switch_to_single_primary_mode('member_uuid');
```

### Multi-Primary Mode

```sql
-- Configure multi-primary mode
SET GLOBAL group_replication_single_primary_mode = OFF;
SET GLOBAL group_replication_enforce_update_everywhere_checks = ON;

-- All nodes can accept writes
-- Conflict detection is automatic
-- Last-write-wins conflict resolution

-- Check mode
SELECT @@group_replication_single_primary_mode;
```

### Group Replication Monitoring

```sql
-- Member status
SELECT * FROM performance_schema.replication_group_members;

-- Member stats
SELECT * FROM performance_schema.replication_group_member_stats;

-- Channel status
SELECT * FROM performance_schema.replication_connection_status
WHERE CHANNEL_NAME = 'group_replication_applier';

-- Applier status
SELECT * FROM performance_schema.replication_applier_status;

-- Worker status
SELECT * FROM performance_schema.replication_applier_status_by_worker;
```

---

## ProxySQL

### Installation

```bash
# Install ProxySQL
wget https://repo.proxysql.org/ProxySQL/proxysql-2.6.x/$(lsb_release -s -c)/pool/main/p/proxysql/proxysql_2.6.0-ubuntu22_amd64.deb
sudo dpkg -i proxysql_2.6.0-ubuntu22_amd64.deb

# Start ProxySQL
sudo systemctl start proxysql
sudo systemctl enable proxysql

# Connect to admin interface
mysql -u admin -padmin -h 127.0.0.1 -P 6032 --prompt='ProxySQL> '
```

### Backend Configuration

```sql
-- Add MySQL servers
INSERT INTO mysql_servers(
  hostgroup_id, hostname, port, weight, max_connections, max_replication_lag
) VALUES
  (10, '192.168.1.100', 3306, 1000, 200, 30),  -- Primary (write)
  (20, '192.168.1.101', 3306, 1000, 200, 30),  -- Secondary (read)
  (20, '192.168.1.102', 3306, 1000, 200, 30);  -- Secondary (read)

-- View servers
SELECT * FROM mysql_servers;

-- Add monitoring user
INSERT INTO mysql_users(username, password, default_hostgroup)
VALUES ('monitor', 'monitor_password', 10);

-- Load to runtime
LOAD MYSQL SERVERS TO RUNTIME;
LOAD MYSQL USERS TO RUNTIME;

-- Save to disk
SAVE MYSQL SERVERS TO DISK;
SAVE MYSQL USERS TO DISK;
```

### Read/Write Splitting

```sql
-- Create query rules for read/write splitting
INSERT INTO mysql_query_rules(
  rule_id, active, match_pattern, destination_hostgroup, apply
) VALUES
  (1, 1, '^SELECT .* FOR UPDATE$', 10, 1),    -- SELECT FOR UPDATE -> Primary
  (2, 1, '^SELECT', 20, 1),                     -- SELECT -> Secondary
  (3, 1, '.*', 10, 1);                          -- Everything else -> Primary

-- Load rules
LOAD MYSQL QUERY RULES TO RUNTIME;
SAVE MYSQL QUERY RULES TO DISK;

-- Test query routing
SELECT * FROM mysql_query_rules WHERE rule_id IN (1, 2, 3);
```

### Connection Pooling

```sql
-- Configure connection pooling
UPDATE global_variables
SET variable_value = 2000
WHERE variable_name = 'mysql-max_connections';

UPDATE global_variables
SET variable_value = 1000
WHERE variable_name = 'mysql-default_max_latency_ms';

-- Connection pool stats
SELECT * FROM stats_mysql_connection_pool;

-- Reset connection pool stats
SELECT * FROM stats_mysql_connection_pool_reset;
```

### Monitoring with ProxySQL

```sql
-- Query statistics
SELECT
  hostgroup,
  digest_text,
  count_star,
  sum_time,
  sum_no_index_used
FROM stats_mysql_query_digest
ORDER BY sum_time DESC
LIMIT 10;

-- Server stats
SELECT
  hostgroup,
  srv_host,
  srv_port,
  status,
  ConnUsed,
  ConnFree,
  ConnOK,
  ConnERR
FROM stats_mysql_connection_pool;

-- Global stats
SELECT * FROM stats_mysql_global
WHERE Variable_Name IN (
  'Client_Connections_connected',
  'Server_Connections_connected',
  'Queries_frontended',
  'Queries_backended'
);
```

---

## MySQL Router

### Installation

```bash
# Install MySQL Router
apt-get install mysql-router

# Configure for InnoDB Cluster
mysqlrouter --bootstrap root@node1:3306 \
  --directory /etc/mysqlrouter \
  --conf-use-sockets \
  --user=mysqlrouter

# Start MySQL Router
mysqlrouter -c /etc/mysqlrouter/mysqlrouter.conf &
```

### Configuration

```ini
# /etc/mysqlrouter/mysqlrouter.conf

[routing:myCluster_rw]
bind_address = 0.0.0.0
bind_port = 6446
destinations = metadata-cache://myCluster/?role=PRIMARY
routing_strategy = first-available
protocol = classic

[routing:myCluster_ro]
bind_address = 0.0.0.0
bind_port = 6447
destinations = metadata-cache://myCluster/?role=SECONDARY
routing_strategy = round-robin-with-fallback
protocol = classic

[routing:myCluster_x_rw]
bind_address = 0.0.0.0
bind_port = 6448
destinations = metadata-cache://myCluster/?role=PRIMARY
routing_strategy = first-available
protocol = x

[routing:myCluster_x_ro]
bind_address = 0.0.0.0
bind_port = 6449
destinations = metadata-cache://myCluster/?role=SECONDARY
routing_strategy = round-robin-with-fallback
protocol = x
```

### Connecting Through Router

```bash
# Connect to read/write port (Primary)
mysql -u app_user -p -h 127.0.0.1 -P 6446

# Connect to read-only port (Secondary)
mysql -u app_user -p -h 127.0.0.1 -P 6447

# Connect with X Protocol
mysqlx -u app_user -p -h 127.0.0.1 -P 6448
```

---

## MHA (Master High Availability)

### Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    MHA Architecture                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              MHA Manager                            │    │
│  │  - Monitors master                                  │    │
│  │  - Detects failures                                 │    │
│  │  - Orchestrates failover                            │    │
│  └─────────────────────────────────────────────────────┘    │
│                           │                                  │
│                           ▼                                  │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Master Server                          │    │
│  └─────────────────────────────────────────────────────┘    │
│                           │                                  │
│            ┌──────────────┼──────────────┐                 │
│            ▼              ▼              ▼                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │  Slave 1    │  │  Slave 2    │  │  Slave 3    │        │
│  │  (Candidate)│  │  (Candidate)│  │  (Relay)    │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Configuration

```ini
# /etc/mha/app.cnf

[server default]
manager_log=/var/log/mha/app/manager.log
manager_workdir=/var/log/mha/app
master_binlog_dir=/var/lib/mysql
master_ip_failover_script=/usr/local/bin/master_ip_failover
master_ip_online_change_script=/usr/local/bin/master_ip_online_change
password=mha_password
ping_interval=1
repl_password=repl_password
repl_user=repl_user
ssh_user=root
user=mha_user

[server1]
hostname=192.168.1.100
candidate_master=1

[server2]
hostname=192.168.1.101
candidate_master=1

[server3]
hostname=192.168.1.102
no_master=1
```

### Operations

```bash
# Check replication health
masterha_check_repl --conf=/etc/mha/app.cnf

# Check SSH connectivity
masterha_check_ssh --conf=/etc/mha/app.cnf

# Start MHA manager
masterha_manager --conf=/etc/mha/app.cnf --remove_dead_master_conf &

# Check MHA status
masterha_check_status --conf=/etc/mha/app.cnf

# Stop MHA
masterha_stop --conf=/etc/mha/app.cnf

# Online master switch (planned failover)
masterha_master_switch --conf=/etc/mha/app.cnf \
  --master_state=alive \
  --new_master_host=192.168.1.101 \
  --orig_master_is_new_slave \
  --running_updates_limit=10000
```

---

## Orchestrator

### Installation

```bash
# Download Orchestrator
wget https://github.com/openark/orchestrator/releases/download/v3.2.6/orchestrator-3.2.6-linux-amd64.tar.gz
tar -xzf orchestrator-3.2.6-linux-amd64.tar.gz

# Configure
cat > /etc/orchestrator.conf.json << 'EOF'
{
  "Debug": false,
  "ListenAddress": ":3000",
  "MySQLTopologyUser": "orchestrator",
  "MySQLTopologyPassword": "password",
  "MySQLOrchestratorHost": "127.0.0.1",
  "MySQLOrchestratorPort": 3306,
  "MySQLOrchestratorDatabase": "orchestrator",
  "MySQLOrchestratorCredentialsConfigFile": "/etc/orchestrator-credentials.json"
}
EOF

# Start Orchestrator
./orchestrator http &
```

### Web Interface

```bash
# Access web interface
http://orchestrator-server:3000

# Features:
# - Visual cluster topology
# - Drag-and-drop failover
# - Audit trail
# - Instance discovery
# - Maintenance mode
```

### Failover Operations

```bash
# Discover instances
orchestrator-client -c discover -i 192.168.1.100:3306

# Graceful master takeover
orchestrator-client -c graceful-master-takeover \
  -i 192.168.1.100:3306 \
  -d 192.168.1.101:3306

# Force master failover
orchestrator-client -c force-master-failover -i 192.168.1.100:3306

# Check topology
orchestrator-client -c topology -i 192.168.1.100:3306
```

---

## Best Practices

### Architecture Design

```sql
-- 1. Use InnoDB Cluster for built-in HA
-- 2. Configure ProxySQL for load balancing
-- 3. Deploy MySQL Router for automatic routing
-- 4. Use GTID for consistent replication
-- 5. Enable semi-synchronous replication

-- Server configuration
[mysqld]
# Group Replication
plugin-load-add = group_replication.so
group_replication_group_name = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
group_replication_start_on_boot = ON
group_replication_local_address = "192.168.1.100:33061"
group_replication_group_seeds = "192.168.1.100:33061,192.168.1.101:33061,192.168.1.102:33061"

# GTID
gtid-mode = ON
enforce-gtid-consistency = ON
log-slave-updates = ON

# Semi-Synchronous
plugin-load-add = semisync_master.so;semisync_slave.so
rpl_semi_sync_master_enabled = ON
rpl_semi_sync_slave_enabled = ON
```

### Monitoring

```sql
-- 1. Monitor cluster status
SELECT * FROM performance_schema.replication_group_members;

-- 2. Monitor replication lag
SELECT * FROM performance_schema.replication_connection_status;

-- 3. Monitor query statistics
SELECT * FROM performance_schema.events_statements_summary_by_digest
ORDER BY SUM_TIMER_WAIT DESC
LIMIT 10;

-- 4. Monitor InnoDB status
SHOW ENGINE INNODB STATUS\G

-- 5. Set up alerts for:
-- - Cluster node failures
-- - Replication lag > 10 seconds
-- - Connection pool exhaustion
-- - Query latency spikes
```

### Failover Testing

```sql
-- Regular failover testing
-- 1. Stop primary node
-- 2. Verify automatic failover
-- 3. Check application connectivity
-- 4. Verify data consistency
-- 5. Rebuild failed node

-- Test scripts
#!/bin/bash
# test_failover.sh

# Simulate primary failure
mysql -h primary-node -u root -p'password' -e "SHUTDOWN"

# Wait for failover
sleep 10

# Check new primary
mysql -h secondary-node -u root -p'password' -e "SHOW MASTER STATUS"

# Verify application can connect
mysql -h proxy-server -u app_user -p'password' -e "SELECT 1"
```

### Disaster Recovery

```sql
-- 1. Regular backups
-- Logical backup
mysqldump --all-databases --single-transaction \
  --master-data=2 --flush-logs > full_backup.sql

-- Physical backup (Percona XtraBackup)
xtrabackup --backup --target-dir=/backup/

-- 2. Test restoration regularly
-- 3. Keep backups off-site
-- 4. Document recovery procedures
-- 5. Practice disaster recovery drills
```

---

## Summary

| Component | Purpose |
|-----------|---------|
| InnoDB Cluster | Built-in HA solution |
| Group Replication | Multi-master replication |
| MySQL Router | Automatic routing |
| ProxySQL | Load balancing and caching |
| MHA | Manual failover management |
| Orchestrator | Visual cluster management |

## Next Steps

- [MySQL Optimization](../optimization/) - Performance tuning
- [MySQL Replication](../replication/) - Detailed replication setup
- [HA Concepts](../../../replication/) - General HA concepts
