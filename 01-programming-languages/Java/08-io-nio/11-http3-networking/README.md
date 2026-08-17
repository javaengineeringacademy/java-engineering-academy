# 11 - HTTP/3 Networking (Java 26 - JEP 512)

## 1. Introduction

HTTP/3 is the third major version of the Hypertext Transfer Protocol used to exchange data on the World Wide Web. Unlike its predecessors, HTTP/3 is built on QUIC (Quick UDP Internet Connections) instead of TCP, providing significant performance improvements. Java 26 introduces standard HTTP/3 support in the `java.net.http` package, enabling developers to build high-performance networking applications without external libraries.

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Understand HTTP/3 and QUIC protocol fundamentals
- Create HTTP/3 clients using `java.net.http.HttpClient`
- Build HTTP/3 servers with `com.sun.net.httpserver`
- Configure SSL/TLS for QUIC-based connections
- Compare HTTP/2 vs HTTP/3 performance characteristics
- Apply HTTP/3 in production scenarios

## 3. Prerequisites

- Understanding of HTTP/1.1 and HTTP/2 protocols
- Familiarity with Java Streams API and NIO basics
- Knowledge of SSL/TLS and certificate management
- Basic networking concepts (TCP, UDP, sockets)

## 4. Why HTTP/3 Exists

| Problem in HTTP/2 | HTTP/3 Solution |
|-------------------|-----------------|
| TCP head-of-line blocking | QUIC eliminates HOL blocking at transport layer |
| Slow connection establishment | 0-RTT connection resumption |
| TCP overhead | Built on UDP with kernel-bypass |
| Connection migration | Connections survive IP address changes |
| Encryption overhead | QUIC mandates encryption by default |

## 5. Core Concepts

### 5.1 QUIC Protocol
- Built on UDP instead of TCP
- Multiplexed streams without head-of-line blocking
- Built-in TLS 1.3 encryption
- 0-RTT connection establishment

### 5.2 HTTP/3 Client
```java
HttpClient client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_3)
    .connectTimeout(Duration.ofSeconds(5))
    .build();
```

### 5.3 HTTP/3 Server
```java
HttpsServer server = HttpsServer.create(new InetSocketAddress(8443), 0);
server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
```

## 6. File Structure

```
11-http3-networking/
├── README.md                    # This file
├── HTTP3ClientDemo.java         # HTTP/3 client demonstration
└── HTTP3ServerDemo.java         # HTTP/3 server demonstration
```

## 7. Demo Files

### HTTP3ClientDemo.java
Demonstrates HTTP/3 client operations:
- Basic GET/POST requests
- Async request handling
- Custom timeout configuration
- Response version detection

### HTTP3ServerDemo.java
Demonstrates HTTP/3 server setup:
- SSL/TLS context creation
- Route handler configuration
- Virtual thread executor
- Graceful server shutdown

## 8. Key API Changes in Java 26

| API | Description |
|-----|-------------|
| `HttpClient.Version.HTTP_3` | HTTP/3 version constant |
| `HttpResponse.version()` | Returns HTTP version used |
| `HttpsServer` | Enhanced for QUIC support |
| Virtual thread executor | Optimal for HTTP/3 connections |

## 9. Performance Benefits

| Metric | HTTP/2 (TCP) | HTTP/3 (QUIC) |
|--------|-------------|---------------|
| Connection setup | 1-3 RTT | 0-1 RTT |
| Handshake overhead | TCP + TLS | Single QUIC handshake |
| HOL blocking | Yes (TCP level) | No |
| Connection migration | No | Yes |
| Encryption | Optional | Mandatory |

## 10. Production Use Cases

- High-performance web services requiring low latency
- Mobile applications with unreliable network connections
- Microservices communication in cloud environments
- Real-time data streaming applications
- API gateways handling high concurrent connections
- CDN edge servers

## 11. References

- [JEP 512: HTTP/3 for HTTP Client](https://openjdk.org/jeps/512)
- [QUIC Protocol RFC 9000](https://datatracker.ietf.org/doc/html/rfc9000)
- [Java HttpClient Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.net.http/java/net/http/HttpClient.html)
- [HTTP/3 Specification](https://www.rfc-editor.org/rfc/rfc9114)
