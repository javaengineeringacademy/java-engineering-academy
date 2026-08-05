# TCP vs UDP

## What They Are

### TCP (Transmission Control Protocol)
A connection-oriented protocol that provides reliable, ordered delivery of data. Establishes connections through three-way handshake, maintains state, and ensures data arrives intact and in sequence.

### UDP (User Datagram Protocol)
A connectionless protocol that provides fast, best-effort delivery of data. No connection establishment, no delivery guarantees, no ordering. Minimal overhead for maximum speed.

## Key Difference Table

| Feature | TCP | UDP |
|---------|-----|-----|
| Connection | Connection-oriented | Connectionless |
| Reliability | Guaranteed delivery | Best-effort |
| Ordering | Ordered packets | Unordered |
| Error Checking | Yes + retransmission | Basic checksum |
| Flow Control | Yes | No |
| Congestion Control | Yes | No |
| Header Size | 20-60 bytes | 8 bytes |
| Speed | Slower | Faster |
| Use Case | Web, email, file transfer | Streaming, gaming, DNS |
| State | Maintains state | Stateless |

## When to Use Which

### Use TCP When
- Data integrity is critical (files, emails)
- Order matters (web pages, API responses)
- Reliable delivery needed (financial transactions)
- Flow control required (avoid overwhelming receiver)
- Error recovery important (retransmit lost packets)

### Use UDP When
- Speed is more important than reliability (gaming)
- Real-time data (video streaming, VoIP)
- Broadcasting to multiple recipients (DNS, DHCP)
- Small data packets with low overhead (IoT sensors)
- Retransmission is impractical (live events)

## Interview Trap

**Trap**: "UDP is unreliable and should never be used."

**Reality**: UDP provides "best-effort" delivery, not "no delivery." For many applications, the overhead of TCP's reliability mechanisms is unnecessary and harmful. UDP is essential for real-time applications.

**Follow-up Trap**: "HTTP always uses TCP."

**Reality**: HTTP/1.1 and HTTP/2 use TCP, but HTTP/3 uses QUIC, which is built on UDP. QUIC provides TCP-like reliability with UDP's performance benefits.

## Visual Diagram

```
TCP Three-Way Handshake:
┌─────────┐                    ┌─────────┐
│  Client  │                   │  Server  │
└─────────┘                    └─────────┘
    │                               │
    │──── SYN ────────────────────>│
    │                               │
    │<─── SYN-ACK ────────────────│
    │                               │
    │──── ACK ────────────────────>│
    │                               │
    │<───── Data Transfer ────────>│
    │                               │
    │──── FIN ────────────────────>│
    │                               │
    │<─── FIN-ACK ────────────────│
    │                               │

UDP Communication:
┌─────────┐                    ┌─────────┐
│  Client  │                   │  Server  │
└─────────┘                    └─────────┘
    │                               │
    │──── Data ───────────────────>│
    │                               │
    │──── Data ───────────────────>│
    │                               │
    │<──── Data ──────────────────│
    │                               │
    │──── Data ───────────────────>│
    │                               │
    (No connection establishment)
    (No guaranteed delivery)
```

## Protocol Comparison

| Protocol | Transport | Purpose |
|----------|-----------|---------|
| HTTP/1.1 | TCP | Web browsing |
| HTTP/2 | TCP | Modern web |
| HTTP/3 | UDP (QUIC) | Next-gen web |
| HTTPS | TCP | Secure web |
| DNS | UDP (TCP for large) | Domain resolution |
| SMTP | TCP | Email |
| FTP | TCP | File transfer |
| RTP | UDP | Real-time media |
| SIP | UDP/TCP | VoIP signaling |

## Performance Metrics

| Metric | TCP | UDP |
|--------|-----|-----|
| Latency | Higher (handshake) | Lower (no handshake) |
| Throughput | Good (flow control) | Excellent (no overhead) |
| Packet loss | Recovered | Lost forever |
| Bandwidth usage | Higher (headers) | Lower (minimal headers) |

## Key Insight

TCP and UDP are not competitors; they serve different needs:

**TCP**: When data integrity matters more than speed
**UDP**: When speed matters more than data integrity

Modern protocols like QUIC (HTTP/3) combine the best of both:
- UDP's speed and reduced latency
- TCP's reliability and ordering
- Built-in encryption (TLS 1.3)
- Connection migration (network changes)

The choice depends on your application's requirements:
- Web browsing, email, file transfer → TCP
- Video streaming, online gaming, DNS → UDP
- Next-generation web applications → QUIC (UDP-based)
