# PostgreSQL Internals

## MVCC (Multi-Version Concurrency Control)

PostgreSQL implements MVCC to allow concurrent reads and writes without blocking. Each transaction sees a snapshot of the database at the start of the transaction. Inserts create new row versions (tuples); updates create a new version and mark the old one as deleted; deletes mark the existing version as dead. Each tuple has xmin (inserting transaction ID) and xmax (deleting transaction ID) fields.

Readers never block writers and writers never block readers. The visibility rule determines which tuples a transaction can see based on its snapshot and transaction ID comparison. Dead tuples accumulate until vacuumed. This eliminates read locks but requires periodic cleanup. Serializable isolation uses predicate locking to prevent write skew anomalies.

MVCC visibility uses a complex set of rules involving transaction IDs, subtransaction IDs, and command IDs. The `pg_visible_in_snapshot()` function tests tuple visibility. Transaction ID wraparound is handled by freezing old tuples. The `FREEZE` operation replaces old transaction IDs with a frozen ID to prevent wraparound issues.

The xmin and xmax fields are 32-bit unsigned integers. Transaction IDs wrap around after approximately 4 billion transactions. The `autovacuum_freeze_max_age` setting controls when tuples are frozen. The `vacuum_freeze_min_age` setting controls the minimum age for freezing. The `pg_database.datfrozenxid` column tracks the oldest unfrozen transaction ID.

## Write-Ahead Logging (WAL)

WAL records all changes before they are applied to the data pages. This ensures crash recovery: after a failure, the database replays WAL to bring pages to a consistent state. WAL segments are written sequentially, providing high throughput. Each WAL record contains the LSN (Log Sequence Number) identifying its position.

WAL serves multiple purposes: crash recovery, replication (streaming primary to replicas), and point-in-time recovery (PITR). WAL archiving copies completed segments to a standby location. Checkpointing writes dirty pages to disk, allowing old WAL to be recycled. The `max_wal_size` and `min_wal_size` settings control WAL lifecycle.

WAL records include full-page writes to prevent torn pages. The `full_page_writes` setting controls this behavior. WAL compression reduces disk usage at the cost of CPU. The `wal_level` setting determines the amount of information stored: minimal, replica, or logical. The `wal_buffers` setting controls the in-memory WAL buffer size.

WAL segment files are named using the timeline and segment number. The `pg_switch_wal()` function forces a WAL segment switch. The `pg_current_wal_lsn()` function returns the current WAL position. The `pg_walfile_name()` function converts an LSN to a WAL file name. WAL archiving uses `archive_command` or `archive_library`.

## Query Planner

The PostgreSQL query planner uses a cost-based optimizer. It generates a query plan by estimating costs for sequential scans, index scans, nested loop joins, hash joins, and merge joins. The planner uses table statistics (pg_statistic) to estimate selectivity and cardinality.

Planning phases include: parsing (SQL to parse tree), rewriting (views, rules), planning (cost estimation), and execution. The planner explores multiple paths and selects the cheapest. Index selection uses the cost model to determine if index access is cheaper than sequential scan. Parameterized plans use prepared statement values for more accurate cost estimation.

The planner uses statistics from pg_statistic to estimate selectivity. The `ANALYZE` command collects statistics. The `EXPLAIN` command shows the query plan. The `EXPLAIN ANALYZE` command executes the query and shows actual vs. estimated costs. The `enable_*` flags control which plan types are considered.

The planner supports parallel query execution. Parallel workers execute parts of the query plan concurrently. The `max_parallel_workers_per_gather` setting controls parallelism. The `parallel_tuple_cost` and `parallel_setup_cost` settings influence the planner's decision to use parallel plans. The `EXPLAIN (ANALYZE, VERBOSE)` output shows parallel execution details.

## Index Types

B-tree is the default index type, suitable for equality and range queries. It maintains a balanced tree structure with logarithmic lookup time. B-tree supports multi-column indexes, prefix compression, and covering indexes (INCLUDE clause). GIN (Generalized Inverted Index) indexes composite values (arrays, JSONB, full-text). GIN stores a posting list of row pointers per key value.

GiST (Generalized Search Tree) indexes support geometric data, full-text search, and range types. BRIN (Block Range Index) stores the min/max values for contiguous block ranges, useful for large tables with natural ordering. Hash indexes support only equality queries and are WAL-logged since PostgreSQL 10. Bloom filters provide probabilistic membership testing.

Index maintenance involves VACUUM to remove dead entries and REINDEX to rebuild indexes. Partial indexes include only rows matching a WHERE clause. Expression indexes use function calls in the index definition. The `pg_stat_user_indexes` view tracks index usage. The `EXPLAIN` command shows which indexes are used for query execution.

PostgreSQL supports index-only scans when all required columns are in the index. The visibility map tracks which pages are all-visible for index-only scans. The `pg_stat_user_indexes` view shows index scan counts. The `pg_indexes_size` function returns index size. The `REINDEX` command rebuilds indexes online.

## Connection Management

PostgreSQL uses a process-per-connection model. Each client connection spawns a backend process. The postmaster (main process) listens for connections and forks backend processes. Connection pooling is typically handled by PgBouncer or pgpool-II, since PostgreSQL does not natively support connection pooling.

Shared memory buffers hold frequently accessed data pages. The buffer manager uses a clock-sweep algorithm for eviction. The background writer writes dirty pages to reduce checkpoint I/O spikes. WAL writer flushes WAL buffers to disk. The autovacuum daemon cleans dead tuples and updates statistics without manual intervention.

The `max_connections` setting limits concurrent connections. The `superuser_reserved_connections` setting reserves connections for administrators. Connection limits can be set per-user or per-database. The `pg_stat_activity` view shows active connections and queries. The `pg_terminate_backend()` function kills connections.

PostgreSQL uses shared memory for buffer caching and inter-process communication. The `shared_buffers` setting controls the buffer cache size. The `work_mem` setting controls memory for sort and hash operations. The `maintenance_work_mem` setting controls memory for maintenance operations. The `effective_cache_size` setting influences the query planner's cost estimates.

## Replication

Streaming replication continuously ships WAL from primary to replicas. Replicas apply WAL in real-time, maintaining near-synchronous copies. Synchronous replication confirms that at least one replica has received each commit before acknowledging the client. Asynchronous replication provides higher throughput but risks data loss.

Logical replication decouples publisher and subscriber, replicating individual tables rather than entire clusters. It uses WAL decoding to extract logical changes. Logical replication supports selective replication, cross-version upgrades, and multi-master topologies via extensions like BDR. Cascade replication allows replicas to replicate from other replicas, reducing primary load.

Replication slots track the WAL position of replicas, preventing premature WAL removal. The `pg_replication_slots` view shows replication slot status. The `pg_stat_replication` view shows replica lag. The `pg_promote()` function promotes a standby to primary. The `pg_create_physical_replication_slot()` function creates replication slots.

PostgreSQL supports synchronous replication with multiple synchronous standbys. The `synchronous_standby_names` setting specifies synchronous standbys. The `synchronous_commit` setting controls synchronous behavior. The `pg_stat_replication` view shows sync state. The `pg_show_replication_slots()` function shows replication slot details.

## Partitioning

PostgreSQL supports declarative table partitioning. Range partitioning divides data by value ranges (e.g., dates). List partitioning divides by enumerated values. Hash partitioning distributes data uniformly across partitions. Partition pruning eliminates scanning irrelevant partitions based on query predicates.

Inheritance-based partitioning (legacy) creates child tables that inherit from a parent. Declarative partitioning (PostgreSQL 10+) is integrated with the query planner for automatic partition pruning. Partition-wise join and aggregation improve query performance for partitioned tables. Sub-partitioning allows partitions of partitions.

Partition management includes: ATTACH PARTITION for adding partitions, DETACH PARTITION for removing them, and DEFAULT partitions for handling out-of-range data. The `pg_partition_tree()` function shows partition hierarchy. The `pg_class.relkind` value 'p' indicates partitioned tables. Partition key columns cannot be altered after table creation.

PostgreSQL supports partition pruning for INSERT operations. The `plan_cache_mode` setting controls plan caching. The `pg_class.relpartbound` column shows partition bounds. The `pg_dump` command includes partition definitions. The `ALTER TABLE ... ATTACH PARTITION` command attaches partitions with bound validation.

## Extensions and Advanced Features

PostgreSQL supports extensions for additional functionality. The `CREATE EXTENSION` command installs extensions. The `pg_extension` view shows installed extensions. Popular extensions include: PostGIS (geospatial), pg_trgm (trigram similarity), btree_gist (GiST operator classes), and pg_stat_statements (query statistics).

PostgreSQL supports foreign data wrappers (FDW) for accessing external data sources. The `CREATE FOREIGN TABLE` command defines external tables. The `postgres_fdw` FDW accesses other PostgreSQL databases. The `file_fdw` FDW accesses files. The `mysql_fdw` and `tds_fdw` FDWs access MySQL and SQL Server.

PostgreSQL supports JSONB for semi-structured data. JSONB operators include: `->` (object field), `->>` (text field), `#>` (path), and `@>` (contains). The `jsonb_path_query` function evaluates JSONPath expressions. The `jsonb_set` function modifies JSONB values. The `jsonb_agg` function aggregates rows into JSONB arrays.

PostgreSQL supports full-text search using tsvector and tsquery types. The `to_tsvector` function converts text to tsvector. The `to_tsquery` function converts text to tsquery. The `@@` operator matches tsvector against tsquery. The `ts_rank` function calculates relevance scores. The `pg_ts_config` view shows text search configurations.
