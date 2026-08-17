# HTTP/3 and QUIC Networking - Quiz

## Questions

### Q1: What transport protocol does HTTP/3 use?
- A) TCP
- B) UDP (QUIC)
- C) SCTP
- D) WebSocket

### Q2: How many round trips does a new HTTP/3 connection require?
- A) 3 RTTs (TCP + TLS + HTTP)
- B) 2 RTTs (TCP + TLS)
- C) 1 RTT (QUIC + TLS combined)
- D) 0 RTTs always

### Q3: What eliminates head-of-line blocking in HTTP/3?
- A) Connection pooling
- B) Multiplexing with independent streams
- C) Larger buffer sizes
- D) TLS 1.3

### Q4: What is 0-RTT in QUIC?
- A) A connection that takes zero time to establish
- B) Ability to send data immediately when resuming a connection
- C) A protocol for real-time gaming
- D) A compression algorithm

### Q5: How does QUIC handle connection migration?
- A) Using IP address changes
- B) Using connection IDs independent of IP/port
- C) Using DNS rebinding
- D) It does not support connection migration

### Q6: Which Java version introduced the built-in HttpClient?
- A) Java 8
- B) Java 9
- C) Java 11
- D) Java 17

### Q7: What is a key difference between HTTP/2 and HTTP/3 stream multiplexing?
- A) HTTP/3 uses fewer streams
- B) HTTP/3 streams are independent (no head-of-line blocking)
- C) HTTP/2 streams are more reliable
- D) HTTP/3 does not support multiplexing

### Q8: Why is encryption mandatory in QUIC?
- A) To improve performance
- B) To prevent eavesdropping and ensure privacy
- C) Because UDP requires encryption
- D) It is optional, not mandatory

### Q9: What is a unidirectional stream used for in QUIC?
- A) Bidirectional request/response
- B) Control messages and server push
- C) File transfers only
- D) DNS queries

### Q10: What makes HTTP/3 particularly beneficial for mobile applications?
- A) Smaller payload sizes
- B) Built-in connection migration across network changes
- C) Better compression
- D) Lower battery usage

## Answers

1. **B** - HTTP/3 runs over QUIC, which is built on UDP
2. **C** - New connections require 1 RTT; resumed connections can use 0-RTT
3. **B** - Independent stream multiplexing prevents one blocked stream from affecting others
4. **B** - 0-RTT allows sending application data immediately during connection resumption
5. **B** - Connection IDs allow the connection to survive IP/port changes
6. **C** - Java 11 introduced java.net.http.HttpClient
7. **B** - HTTP/3 streams are independent; TCP-level blocking is eliminated
8. **B** - QUIC integrates TLS 1.3 and mandates encryption for all data
9. **B** - Unidirectional streams carry control frames, headers, and settings
10. **B** - Connection migration transparently handles WiFi ↔ cellular transitions
