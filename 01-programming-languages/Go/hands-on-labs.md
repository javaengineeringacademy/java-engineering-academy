# Go Hands-on Labs

## Lab 1: HTTP Server

Build a REST API with routing, middleware, and JSON responses.

- Implement `/users` endpoint with GET, POST, PUT, DELETE
- Add logging middleware
- Add authentication middleware
- Store data in memory with sync.RWMutex
- Handle errors properly

## Lab 2: Concurrent File Processor

Process files concurrently with goroutines.

- Read multiple files in parallel
- Count words, lines, and characters
- Aggregate results using channels
- Implement graceful shutdown
- Limit concurrent operations with semaphore

## Lab 3: Chat Application

Build a WebSocket chat server.

- Accept WebSocket connections
- Broadcast messages to all connected clients
- Handle user join/leave events
- Store message history
- Implement rate limiting

## Lab 4: Worker Pool

Implement a generic worker pool.

- Create fixed number of workers
- Submit tasks via channel
- Collect results from workers
- Handle errors and timeouts
- Implement dynamic scaling

## Lab 5: Cache Implementation

Build an in-memory cache with expiration.

- Implement Get, Set, Delete operations
- Add TTL-based expiration
- Use sync.RWMutex for thread safety
- Add eviction policies (LRU)
- Implement statistics tracking

## Lab 6: CLI Tool

Build a command-line application with cobra.

- Implement multiple subcommands
- Add flags and arguments
- Read configuration from file
- Add progress bars for long operations
- Handle signals for graceful shutdown

## Lab 7: Database Connection Pool

Implement a database connection pool.

- Create reusable connections
- Implement connection validation
- Add connection timeout
- Track connection statistics
- Handle connection errors

## Lab 8: Rate Limiter

Implement multiple rate limiting algorithms.

- Token bucket algorithm
- Sliding window algorithm
- Per-user rate limiting
- Distributed rate limiting
- Rate limit headers in responses

## Lab 9: Service Discovery

Build a simple service registry.

- Register services with metadata
- Discover services by name
- Health check registration
- Watch for service changes
- TTL-based deregistration

## Lab 10: Event-Driven Architecture

Implement an event system.

- Define event types and handlers
- Implement publish/subscribe pattern
- Add event serialization
- Support event replay
- Implement event sourcing
