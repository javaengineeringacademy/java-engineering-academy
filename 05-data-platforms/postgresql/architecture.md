# PostgreSQL Architecture

## Process Model

PostgreSQL uses a client-server architecture with multiple processes.

### Main Processes

1. **Postmaster**: Parent process that manages connections
2. **Backend**: One process per client connection
3. **Background Workers**: Handle maintenance tasks
4. **WAL Writer**: Writes WAL records to disk
5. **Checkpointer**: Periodically writes dirty buffers
6. **Autovacuum**: Reclaims space from dead tuples

## Shared Memory

### Shared Buffers
- Default: 128MB (should be 25% of RAM)
- Stores frequently accessed data pages
- LRU-based eviction policy

### WAL Buffers
- Stores Write-Ahead Log records
- Default: 64MB
- Written to disk on commit

### CLOG Buffers
- Transaction commit status tracking
- Keeps visibility information

## Write-Ahead Logging (WAL)

### Purpose
- Ensures data durability
- Enables point-in-time recovery
- Supports replication

### WAL Record Flow

1. Transaction modifies data
2. WAL records generated
3. WAL buffers written to disk
4. Data buffers written later (checkpoint)

### WAL Configuration

```
wal_level = replica
wal_buffers = 64MB
max_wal_senders = 10
wal_keep_size = 1GB
```

## MVCC (Multi-Version Concurrency Control)

### How It Works
- Each row has transaction ID (xmin, xmax)
- Readers don't block writers
- Old versions kept until vacuum

### Transaction Visibility

```
xmin: Transaction that created the row
xmax: Transaction that deleted/updated the row
```

### MVCC Benefits
- Non-blocking reads
- Consistent snapshots
- No read locks needed

## Query Processing

### Query Pipeline

1. Parser: SQL to parse tree
2. Optimizer: Generate execution plan
3. Executor: Execute plan
4. Return results

### Memory Contexts

- Execution memory per query
- Temporary tables
- Sort operations

## Background Processes

### Autovacuum
- Removes dead tuples
- Updates statistics
- Prevents transaction ID wraparound

### Stats Collector
- Gathers usage statistics
- Feeds pg_stat views

## Best Practices

1. Tune shared_buffers appropriately
2. Monitor WAL generation
3. Configure autovacuum for workload
4. Use connection pooling
5. Monitor background processes
