# Locks - Memory Model

## Lock Memory Layout

### AQS Memory Structure

```
AbstractQueuedSynchronizer (AQS):
┌─────────────────────────────────────────┐
│ volatile int state                      │
│ Thread exclusiveOwnerThread             │
│ Node head (CLH queue)                   │
│ Node tail (CLH queue)                   │
└─────────────────────────────────────────┘

CLH Queue Node:
┌─────────────────────────────────────────┐
│ volatile int waitStatus                 │
│ volatile Node prev                      │
│ volatile Node next                      │
│ volatile Thread thread                  │
└─────────────────────────────────────────┘
```

### Lock State in Memory

| Lock | State Variable | Size | Description |
|------|---------------|------|-------------|
| ReentrantLock | `state` (int) | 32 bits | 0=unlocked, N=held N times |
| ReadWriteLock | `state` (int) | 32 bits | High=write count, Low=read count |
| StampedLock | `state` (long) | 64 bits | Stamp value for lock state |

### StampedLock Stamp Layout

```
Stamp (64 bits):
┌──────────────────────────────────────┐
│ Bit 63: Mode flag (0=read, 1=write) │
│ Bits 0-62: Sequence number          │
└──────────────────────────────────────┘
```

Optimistic read stamp:
```
┌──────────────────────────────────────┐
│ Bit 63: 0 (read mode)               │
│ Bits 0-62: Current sequence         │
└──────────────────────────────────────┘
```

### Memory Overhead Comparison

| Lock Type | Object Size | Per-Waiter | Total per thread |
|-----------|------------|------------|-----------------|
| synchronized | ~16 bytes | Monitor entry | Monitor only |
| ReentrantLock | ~48 bytes | ~40 bytes (Node) | ~88 bytes |
| ReadWriteLock | ~64 bytes | ~40 bytes | ~104 bytes |
| StampedLock | ~48 bytes | N/A (optimistic) | ~48 bytes |

### Why StampedLock is Memory-Efficient

Optimistic reads don't allocate queue nodes:
- Only the stamp (64 bits) is stored on the thread stack
- No CLH queue node is created
- Validation is a simple CAS comparison
- No parking/unparking overhead
