# HTTP/3 and QUIC Networking - References

## Official Documentation
- [RFC 9114: HTTP/3](https://www.rfc-editor.org/rfc/rfc9114)
- [RFC 9000: QUIC Transport Protocol](https://www.rfc-editor.org/rfc/rfc9000)
- [RFC 9001: Using TLS to Secure QUIC](https://www.rfc-editor.org/rfc/rfc9001)
- [RFC 9002: QUIC Loss Detection and Congestion Control](https://www.rfc-editor.org/rfc/rfc9002)
- [Java HttpClient (Java 11+)](https://docs.oracle.com/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html)

## Books
- *HTTP/3 in Action* (Robin Marx) - Comprehensive HTTP/3 reference
- *High Performance Browser Networking* (Ilya Grigorik) - Networking fundamentals including QUIC

## Key Concepts

### QUIC Protocol Stack
```
Application (HTTP/3)
      |
   QUIC Layer
      |
     UDP
      |
   IP Layer
```

### Connection States
| State | Description |
|-------|-------------|
| Initial | First handshake (ClientHello) |
| Handshake | Key exchange and encryption setup |
| Active | Connection ready for data |
| Draining | Connection closing, no new data |
| Closed | Connection fully terminated |

### Stream Types
| Type | Direction | Use Case |
|------|-----------|----------|
| Bidirectional | Both | Request/response pairs |
| Unidirectional | One-way | Control messages, settings |
| Server-initiated | Server→Client | Push promises (limited) |

## Java HTTP/3 Libraries

| Library | HTTP/3 Support | Status |
|---------|---------------|--------|
| Java HttpClient (java.net.http) | HTTP/2 only | Built-in (Java 11+) |
| Jetty | HTTP/3 via Jetty-Quic | Active development |
| Netty | HTTP/3 via incubator | Active development |
| Vert.x | HTTP/3 via Vertx-Quic | Active development |
| Apache HttpClient | HTTP/2 only | Stable |

## Tools and Testing
- [curl with HTTP/3](https://curl.se/docs/http3.html) - Test HTTP/3 endpoints
- [wireshark](https://www.wireshark.org/) - Packet capture and QUIC analysis
- [qvis](https://qvis.quictools.info/) - QUIC performance visualization
- [ nghttp2](https://nghttp2.org/) - HTTP/2 and HTTP/3 testing tools

## Related Topics
- [Java NIO Channels](https://docs.oracle.com/javase/8/docs/api/java/nio/channels/package-summary.html)
- [Java SSL/TLS](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/package-summary.html)
- [Asynchronous Socket Channels](https://docs.oracle.com/javase/8/docs/api/java/nio/channels/AsynchronousSocketChannel.html)
- [Connection-Oriented vs Connectionless Protocols](https://www.baeldung.com/cs/connection-oriented-vs-connectionless)
