# Redis Version History

## Redis 1.0
- **Release Date:** March 15, 2009
- **Features:** Key-value data structures, strings, lists, sets, persistence (RDB), replication, pub/sub, transactions
- **Deprecated:** N/A (initial release)
- **Removed:** N/A
- **Performance:** In-memory operation for microsecond latency
- **Security:** Basic password authentication (requirepass)
- **Why Introduced:** Created by Salvatore Sanfilippo to provide a fast, in-memory data store for real-time applications

## Redis 1.2
- **Release Date:** March 20, 2009
- **Features:** Persistent storage improvements, better replication, Lua scripting (experimental)
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Improved persistence handling
- **Security:** Authentication improvements
- **Why Introduced:** Stability and persistence improvements

## Redis 2.0
- **Release Date:** August 15, 2010
- **Features:** Persistent AOF (Append-Only File), Lua scripting (stable), replication improvements, memory management, virtual memory (later removed), cluster mode (experimental)
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** AOF persistence for durability
- **Security:** Lua scripting sandbox
- **Why Introduced:** Lua scripting for server-side computation, AOF for durability

## Redis 2.2
- **Release Date:** September 11, 2010
- **Features:** Persistence improvements, better replication, Lua scripting improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Replication improvements
- **Security:** Lua sandbox improvements
- **Why Introduced:** Stability improvements

## Redis 2.4
- **Release Date:** November 11, 2011
- **Features:** Memory efficiency improvements, hash encoding optimizations, ziplist improvements, virtual memory (removed later)
- **Deprecated:** Virtual memory (deprecated)
- **Removed:** N/A
- **Performance:** Memory optimizations for large datasets
- **Security:** Improved authentication
- **Why Introduced:** Memory efficiency for larger datasets

## Redis 2.6
- **Release Date:** September 28, 2012
- **Features:** Lua scripting improvements, key expire improvements, memory diagnostics, client output buffer limits, QUIT/PING improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Lua scripting performance improvements
- **Security:** Script sandboxing improvements
- **Why Introduced:** Lua scripting maturity and diagnostics

## Redis 2.8
- **Release Date:** November 22, 2013
- **Features:** PSYNC (partial resynchronization), CLUSTER support (stable), keyspace notifications, CONFIG SET/GET improvements, command renamed
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** PSYNC reduces replication reconnection overhead
- **Security:** Command renaming for security
- **Why Introduced:** Cluster mode for horizontal scaling

## Redis 3.0
- **Release Date:** April 1, 2015
- **Features:** Redis Cluster (stable), improved cluster management, slot migration, cluster node failure detection, client-side caching, improved Lua scripting
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Redis Cluster for distributed performance
- **Security:** Cluster security improvements
- **Why Introduced:** Production-ready Redis Cluster for scalability

## Redis 3.2
- **Release Date:** April 25, 2016
- **Features:** Geo commands, bitmap improvements, memory usage tracking, client-side caching improvements, stream-like commands
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Geo commands for location-based services
- **Security:** Memory usage tracking for resource management
- **Why Introduced:** Geo commands for geospatial applications

## Redis 4.0
- **Release Date:** July 18, 2017
- **Features:** Modules API, LFU eviction, memory usage improvements, mixed RDB/AOF persistence, SWAPDB, active defragmentation, PSYNC2, cache-aside optimization
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Active defragmentation for memory optimization, LFU for better eviction
- **Security:** Module sandboxing
- **Why Introduced:** Module system for extensibility, memory management improvements

## Redis 5.0
- **Release Date:** October 17, 2018
- **Features:** Streams (XADD, XREAD, XLEN, XRANGE, consumer groups, XREADGROUP, XACK), cluster improvements, module API improvements, RDB improvements, CLIENT PAUSE improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Streams for high-throughput event streaming
- **Security:** Stream access control
- **Why Introduced:** Streams for real-time event processing, a major new data type

## Redis 6.0
- **Release Date:** July 22, 2020
- **Features:** ACL (Access Control Lists), TLS/SSL support, RESP3 protocol, multi-part AOF, client-side caching improvements, memory usage improvements, IO threads for performance
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** IO threads for multi-core performance, RESP3 protocol
- **Security:** ACL for fine-grained access control, TLS for encryption
- **Why Introduced:** ACL and TLS for production security, IO threads for performance

## Redis 6.2
- **Release Date:** February 24, 2021
- **Features:** GETDEL, GETEX, COPY, LMPOP, ZMPOP, CLIENT NO-EVICT, FUNCTION ( scripting improvements), SWAPDB improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** New commands for common operations
- **Security:** CLIENT NO-EVICT for protected workloads
- **Why Introduced:** New commands for common use cases

## Redis 7.0
- **Release Date:** April 27, 2022
- **Features:** Functions (REPLACE, LIST, DELETE), persistent ACL logs, client-side caching improvements, sharded pub/sub, cluster improvements, waitaof command, command improvements
- **Deprecated:** EVAL (replaced by FUNCTION)
- **Removed:** N/A
- **Performance:** Functions for server-side scripting improvements
- **Security:** Persistent ACL logs for audit trail
- **Why Introduced:** Functions for persistent server-side logic, security audit improvements

## Redis 7.2
- **Release Date:** August 15, 2023
- **Features:** Redis Functions improvements, Valkey compatibility preparation, multi-part AOF improvements, client-side caching improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** AOF improvements for durability
- **Security:** Continued security improvements
- **Why Introduced:** Stability and compatibility improvements
