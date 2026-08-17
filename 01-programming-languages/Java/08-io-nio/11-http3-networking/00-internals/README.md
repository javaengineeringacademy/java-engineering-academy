# HTTP/3 and QUIC - Internals

## QUIC Protocol Internals

### Connection Establishment Flow
```
Client                          Server
  |                               |
  |--- Initial (ClientHello) --->|
  |<-- Initial (ServerHello) ----|
  |<-- Handshake (Finished) -----|
  |--- Handshake (Finished) ---->|
  |                               |
  |=== 0-RTT / 1-RTT Ready =====|
```

### QUIC Packet Types
| Type | Purpose |
|------|---------|
| Initial | Connection establishment, carries ClientHello/ServerHello |
| Handshake | Key exchange completion, certificates |
| 0-RTT | Early data on resumed connections |
| 1-RTT | Regular data after handshake |
| Retry | Address validation, cookie exchange |

### Frame Types
| Frame | Purpose |
|-------|---------|
| STREAM | Carries application data |
| CRYPTO | Carries TLS handshake data |
| ACK | Acknowledges received packets |
| PING | Keep-alive probe |
| PADDING | Pad packets to minimum size |
| CONNECTION_CLOSE | Graceful connection termination |
| MAX_STREAMS | Flow control for streams |
| NEW_CONNECTION_ID | Connection migration support |

## Stream State Machine
```
Ready → Send → Data Sent → Reset Sent
  |                           |
  v                           v
Receive ← Data Received ← Reset Received
```

### Stream States
| State | Description |
|-------|-------------|
| Ready | Stream created, no data sent/received |
| Send | Stream can send data |
| Data Sent | All data sent, waiting for acknowledgment |
| Reset Sent | Stream reset sent to peer |
| Receive | Stream can receive data |
| Data Received | All data received |
| Reset Received | Stream reset received from peer |

## Flow Control

### Connection-Level Flow Control
- Controls total data across all streams
- Window size advertised via MAX_DATA frame
- Prevents receiver overflow

### Stream-Level Flow Control
- Controls data per stream
- Window size advertised via MAX_STREAM_DATA frame
- Independent of connection-level control

## Connection Migration
1. Client changes network (WiFi → cellular)
2. Client sends packets from new IP with same Connection ID
3. Server recognizes Connection ID, updates path
4. No reconnection needed - seamless transition

## Loss Detection and Recovery
- QUIC uses acknowledgment-based loss detection
- PTO (Probe Timeout) for detecting packet loss
- No retransmission timeout like TCP
- Early loss detection via redundant ACKs

## Encryption Integration
- TLS 1.3 integrated directly into QUIC handshake
- No separate TLS layer needed
- All QUIC packets (except Initial) are encrypted
- Forward secrecy by default
