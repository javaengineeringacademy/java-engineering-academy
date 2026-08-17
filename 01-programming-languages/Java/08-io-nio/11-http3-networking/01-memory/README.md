# HTTP/3 and QUIC - Memory Model

## QUIC Connection Memory Structure

### Connection Object Layout
```
QUICConnection
├── ConnectionID (16 bytes)
├── State (Initial/Handshake/Active/Draining/Closed)
├── Streams (Map<StreamID, QUICStream>)
├── FlowControl
│   ├── Connection-level window
│   └── Per-stream windows
├── Crypto
│   ├── TLS 1.3 state machine
│   ├── Encryption keys
│   └── Certificate chain
├── PacketManager
│   ├── Sent packets buffer
│   ├── Received packets buffer
│   └── ACK tracking
└── CongestionController
    ├── CUBIC state
    └── RTT estimates
```

### Memory Per Connection
| Component | Typical Size | Description |
|-----------|-------------|-------------|
| Connection state | 256-512 bytes | State, IDs, configuration |
| Stream map | 64+ bytes per stream | Stream state, buffers |
| TLS state | 1-4 KB | Keys, certificates, handshake |
| Packet buffers | 16-64 KB | Sent/received packet cache |
| Congestion state | 128-256 bytes | RTT, cwnd, pacing |

## Buffer Management

### Send Buffer
- Application writes data to send buffer
- QUIC fragments into packets (max ~1200 bytes)
- Packets queued for transmission
- Acknowledged data freed from buffer

### Receive Buffer
- Incoming packets reassembled into stream data
- Application reads from receive buffer
- Out-of-order data held until gaps filled
- Flow control limits total buffered data

## Memory Scaling Considerations

### Per-Connection Memory
- Base: ~50 KB per active connection
- Per stream: ~2-4 KB additional
- With 1000 connections: ~50 MB baseline
- With 10 streams each: ~90 MB total

### Packet Buffer Pool
- Pre-allocated pool of packet buffers
- Reduces GC pressure under high load
- Typical pool size: 10,000-100,000 packets
- Each packet: ~1.5 KB (1280 bytes MTU + overhead)

## Java-Specific Memory

### ByteBuffer Usage
- Direct ByteBuffers for network I/O
- Heap ByteBuffers for application processing
- Pooled ByteBuf (Netty) for efficiency

### GC Implications
- High connection churn creates GC pressure
- Connection pools reduce allocation rates
- Off-heap buffers avoid GC entirely
- Monitor GC pauses during high-throughput scenarios

## Memory Optimization Strategies
1. **Connection pooling** - Reuse connections to avoid allocation
2. **Stream multiplexing** - Fewer connections needed
3. **Buffer pooling** - Reuse ByteBuffers
4. **Off-heap buffers** - Reduce GC pressure
5. **Flow control tuning** - Prevent memory bloat
6. **Early data limits** - Cap 0-RTT data size
