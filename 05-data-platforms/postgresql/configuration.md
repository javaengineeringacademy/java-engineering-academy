# PostgreSQL Configuration

## postgresql.conf

### Memory Settings

```
# Shared Buffers (25% of RAM)
shared_buffers = 4GB

# Work Memory (per operation)
work_mem = 256MB

# Maintenance Work Memory
maintenance_work_mem = 1GB

# Effective Cache Size (75% of RAM)
effective_cache_size = 12GB
```

### Write-Ahead Log

```
wal_level = replica
wal_buffers = 64MB
max_wal_size = 4GB
min_wal_size = 1GB
checkpoint_completion_target = 0.9
```

### Connection Settings

```
max_connections = 200
superuser_reserved_connections = 3
```

### Logging

```
logging_collector = on
log_directory = 'log'
log_filename = 'postgresql-%Y-%m-%d.log'
log_min_duration_statement = 1000
log_statement = 'ddl'
log_connections = on
log_disconnections = on
```

### Autovacuum

```
autovacuum = on
autovacuum_max_workers = 3
autovacuum_naptime = 1min
autovacuum_vacuum_threshold = 50
autovacuum_analyze_threshold = 50
```

## pg_hba.conf

### Client Authentication

```
# TYPE  DATABASE  USER  ADDRESS      METHOD
local   all       all                peer
host    all       all   127.0.0.1/32 md5
host    all       all   ::1/128      md5
host    mydb      myuser 10.0.0.0/8  scram-sha-256
```

### Authentication Methods

- trust: No password required
- md5: Password hash
- scram-sha-256: SCRAM authentication
- cert: SSL certificate
- ldap: LDAP authentication
- pam: PAM authentication

### SSL Configuration

```
ssl = on
ssl_cert_file = 'server.crt'
ssl_key_file = 'server.key'
ssl_ca_file = 'ca.crt'
```

## Connection Pooling

### PgBouncer Configuration

```
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

## Performance Tuning

### Query Planning

```
random_page_cost = 1.1
effective_io_concurrency = 200
seq_page_cost = 1.0
```

### Parallel Query

```
max_worker_processes = 8
max_parallel_workers = 8
max_parallel_workers_per_gather = 4
```

## Runtime Configuration

### ALTER SYSTEM

```sql
ALTER SYSTEM SET shared_buffers = '8GB';
ALTER SYSTEM RESET work_mem;
SELECT pg_reload_conf();
```

### Per-Database Settings

```sql
ALTER DATABASE mydb SET work_mem = '512MB';
ALTER DATABASE mydb SET statement_timeout = '30s';
```

## Configuration Files

### File Locations

```
postgresql.conf: Main configuration
pg_hba.conf: Client authentication
pg_ident.conf: User name mapping
```

### Include Directories

```
include_dir = 'conf.d'
include = 'custom.conf'
```

## Best Practices

1. Use ALTER SYSTEM for persistent changes
2. Test changes in development first
3. Monitor configuration impact
4. Document configuration changes
5. Use connection pooling in production
