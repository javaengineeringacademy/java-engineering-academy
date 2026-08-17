# HTTP/3 and QUIC Networking - Decision Guide

## When to Use HTTP/3
- **High-latency networks** - QUIC eliminates TCP head-of-line blocking
- **Mobile applications** - Faster connection migration across networks (WiFi → cellular)
- **Real-time applications** - Lower connection establishment latency
- **Microservices** - Better performance for service-to-service communication
- **CDN and web servers** - Improved content delivery performance

## HTTP/3 vs HTTP/2

| Feature | HTTP/2 | HTTP/3 |
|---------|--------|--------|
| Transport | TCP + TLS 1.2+ | QUIC (UDP-based) |
| Head-of-line blocking | Yes (TCP) | No (stream independence) |
| Connection establishment | 2-3 RTTs | 0-1 RTT (0-RTT resume) |
| Connection migration | Not supported | Built-in (connection IDs) |
| Encryption | Optional (TLS) | Mandatory (built-in) |
| Server push | Supported | Removed in HTTP/3 |

## QUIC Protocol Decisions

### Connection Establishment
- **0-RTT**: Resume a previous connection immediately
- **1-RTT**: New connection with QUIC + TLS handshake combined
- Consider security tradeoffs of 0-RTT (replay attacks)

### Stream Management
- Each stream is independent (no head-of-line blocking)
- Use unidirectional streams for control messages
- Use bidirectional streams for request/response pairs
- Set appropriate flow control windows per stream

### Connection Migration
- QUIC uses connection IDs instead of IP/port tuples
- Connections survive network changes automatically
- No need for application-level session management

## Java HTTP Client with HTTP/3
- Java 11+ HttpClient supports HTTP/2 and HTTP/1.1
- HTTP/3 support requires external libraries (Netty, Jetty)
- Use `java.net.http.HttpClient` for HTTP/2 (built-in)
- Consider Jetty or Netty for HTTP/3 QUIC support

## When NOT to Use HTTP/3
- **Simple internal services** - HTTP/2 may suffice
- **Environments blocking UDP** - Firewalls may block QUIC
- **Legacy infrastructure** - Existing TCP-based load balancers
- **Debugging requirements** - QUIC encryption makes packet inspection harder

## Performance Considerations
- QUIC reduces connection setup from 2-3 RTT to 0-1 RTT
- Stream independence prevents TCP head-of-line blocking
- Connection migration eliminates reconnection overhead
- Built-in encryption removes separate TLS handshake cost
