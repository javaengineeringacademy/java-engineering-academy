# PostgreSQL Production

## Backup Strategies

### pg_dump

```bash
# Backup single database
pg_dump -U postgres -d mydb -F c -f backup.dump

# Backup all databases
pg_dumpall -U postgres -f all_backup.sql

# Backup schema only
pg_dump -U postgres -d mydb -s -f schema.sql
```

### pg_basebackup

```bash
# Full backup
pg_basebackup -U replicator -D /backup/base -Fp -Xs -P

# Backup with WAL
pg_basebackup -U replicator -D /backup/base -Ft -z -Xs -P
```

### Continuous Archiving

```
# In postgresql.conf
wal_level = replica
archive_mode = on
archive_command = 'cp %p /archive/%f'
```

## Replication

### Streaming Replication

```bash
# On primary
CREATE USER replicator WITH REPLICATION PASSWORD 'password';

# In postgresql.conf
wal_level = replica
max_wal_senders = 5
wal_keep_size = 1GB

# In pg_hba.conf
host replication replicator 10.0.0.0/8 scram-sha-256
```

### Standby Setup

```bash
# On standby
pg_basebackup -h primary-host -U replicator -D /var/lib/postgresql/data -P

# Create standby.signal
touch /var/lib/postgresql/data/standby.signal

# Configure recovery
cat > /var/lib/postgresql/data/postgresql.auto.conf << EOF
primary_conninfo = 'host=primary-host user=replicator password=password'
EOF
```

## High Availability

### Patroni

```yaml
# patroni.yml
scope: postgres-cluster
name: node1

etcd3:
  hosts: etcd1:2379,etcd2:2379,etcd3:2379

bootstrap:
  dcs:
    ttl: 30
    loop_wait: 10
    retry_timeout: 10
    maximum_lag_on_failover: 1048576

postgresql:
  listen: 0.0.0.0:5432
  data_dir: /var/lib/postgresql/data
  authentication:
    replication:
      username: replicator
      password: password
    superuser:
      username: postgres
      password: password
```

### pgBouncer

```ini
[databases]
mydb = host=localhost port=5432 dbname=mydb

[pgbouncer]
listen_port = 6432
listen_addr = 0.0.0.0
auth_type = md5
auth_file = /etc/pgbouncer/userlist.txt
pool_mode = transaction
max_client_conn = 1000
default_pool_size = 25
```

## Monitoring

### Health Checks

```sql
-- Connection count
SELECT count(*) FROM pg_stat_activity;

-- Replication lag
SELECT client_addr, state, sent_lsn, replay_lsn,
       replay_lag
FROM pg_stat_replication;
```

### Alerting

```bash
# Check replication lag
psql -c "SELECT replay_lag FROM pg_stat_replication;" | grep "00:0"
```

## Point-in-Time Recovery

```bash
# Restore to specific time
pg_restore -D /var/lib/postgresql/data backup.dump

# Create recovery config
cat > /var/lib/postgresql/data/postgresql.auto.conf << EOF
restore_command = 'cp /archive/%f %p'
recovery_target_time = '2024-01-15 10:00:00'
EOF

# Create recovery.signal
touch /var/lib/postgresql/data/recovery.signal
```

## Scaling

### Read Replicas

```bash
# Create replica
pg_basebackup -h primary -D /replica/data -P

# Configure as read-only
ALTER SYSTEM SET default_transaction_read_only = on;
SELECT pg_reload_conf();
```

## Best Practices

1. Test backups regularly
2. Monitor replication lag
3. Use connection pooling
4. Set up automated failover
5. Plan for disaster recovery
6. Document recovery procedures
