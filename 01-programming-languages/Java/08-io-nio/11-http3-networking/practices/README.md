# HTTP/3 and QUIC - Practices

## Practice 1: HTTP/2 Client with Connection Pooling

Implement an HTTP/2 client that:
- Maintains a connection pool of 10 connections
- Routes requests to least-used connections
- Handles connection failures with automatic retry
- Measures and logs request latency

## Practice 2: Async File Download Manager

Build a download manager that:
- Downloads multiple files concurrently using HTTP/2 multiplexing
- Reports download progress per file
- Handles partial downloads (resume support)
- Limits concurrent downloads to 5

## Practice 3: HTTP/2 Server Push Simulator

Create a server push mechanism that:
- Analyzes request patterns
- Pushes related resources before client requests them
- Tracks pushed resources to avoid duplicates
- Measures bandwidth savings from push

## Practice 4: Performance Comparison

Compare HTTP/1.1 vs HTTP/2 performance:
- Download 100 small files sequentially
- Download 100 small files concurrently
- Measure total time, throughput, and connection count
- Generate a comparison report

## Practice 5: WebSocket over HTTP/2

Implement WebSocket communication over HTTP/2:
- Establish WebSocket connection using HTTP/2
- Send and receive messages bidirectionally
- Handle connection drops and reconnection
- Support binary and text frames

## Practice 6: QUIC Protocol Analysis

Simulate QUIC behavior:
- Implement connection ID tracking
- Simulate network migration (change source port)
- Demonstrate stream independence (one stream blocks, others continue)
- Compare with TCP behavior under same conditions

## Practice 7: Load Testing Tool

Build a simple HTTP/2 load testing tool:
- Configure target URL, concurrent connections, total requests
- Measure requests per second, latency percentiles (p50, p95, p99)
- Generate a performance report
- Support both HTTP/1.1 and HTTP/2 modes
