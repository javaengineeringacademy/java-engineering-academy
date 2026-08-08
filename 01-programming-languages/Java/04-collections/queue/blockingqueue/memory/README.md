# BlockingQueue Memory Usage

## ArrayBlockingQueue Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  Object[] items ref               8B   │
│  int takeIndex                   4B   │
│  int putIndex                    4B   │
│  int count                       4B   │
│  ReentrantLock ref               8B   │
│  Condition notEmpty ref          8B   │
│  Condition notFull ref           8B   │
│  Padding                        12B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 68B  │
└────────────────────────────────────────┘
```

## LinkedBlockingQueue Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  AtomicInteger count              8B   │
│  Node head ref                    8B   │
│  Node last ref                    8B   │
│  ReentrantLock putLock ref        8B   │
│  ReentrantLock takeLock ref       8B   │
│  Condition notEmpty ref           8B   │
│  Condition notFull ref            8B   │
│  int capacity                     4B   │
│  Padding                         12B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 84B  │
└────────────────────────────────────────┘
```

## Per-Element Cost

### ArrayBlockingQueue
```
4 bytes per slot in backing array
+ Object overhead per element
```

### LinkedBlockingQueue
```
Node object:
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  E item ref                      4B   │
│  Node next ref                   8B   │
│  Padding                         4B   │
├────────────────────────────────────────┤
│  TOTAL PER NODE:                 28B  │
└────────────────────────────────────────┘
```

## Scaling: 1000 Integer Elements

### ArrayBlockingQueue
```
Instance:                     68 bytes
Backing array:
  Header:                   16 bytes
  1024 slots:             4,096 bytes
  ──────────────────────────────────
  Array total:            4,112 bytes

1000 Integer objects:
  1000 × 16 bytes:       16,000 bytes

─────────────────────────────────────────
TOTAL:                    20,180 bytes ≈ 20 KB
```

### LinkedBlockingQueue
```
Instance:                     84 bytes
1000 Node objects:
  1000 × 28 bytes:       28,000 bytes

1000 Integer objects:
  1000 × 16 bytes:       16,000 bytes

─────────────────────────────────────────
TOTAL:                    44,084 bytes ≈ 43 KB
```

## Comparison: BlockingQueue Implementations

```
┌──────────────────┬──────────────┬──────────────┬──────────────┐
│                  │   ABQ        │    LBQ       │  ArrayDeque  │
├──────────────────┼──────────────┼──────────────┼──────────────┤
│ Instance         │     68 B     │     84 B     │     32 B     │
│ Structure        │   4,112 B    │      N/A     │   4,112 B    │
│ Nodes            │      None    │  28,000 B    │      None    │
│ Elements         │  16,000 B    │  16,000 B    │  16,000 B    │
│ Locks            │  1 lock      │  2 locks     │  None        │
├──────────────────┼──────────────┼──────────────┼──────────────┤
│ TOTAL (1000)     │   ≈ 20 KB   │   ≈ 43 KB   │   ≈ 20 KB   │
│ Blocking         │     Yes      │     Yes      │      No      │
│ Thread safe      │     Yes      │     Yes      │      No      │
└──────────────────┴──────────────┴──────────────┴──────────────┘
```

## Lock Overhead

```
ReentrantLock instance:
  Object header:      12 bytes
  sync state:          4 bytes
  owner thread:        8 bytes
  waiters queue:      16 bytes
  ────────────────────────
  Total:              40 bytes per lock

Condition instance:
  Object header:      12 bytes
  WaitQueue:          16 bytes
  ────────────────────────
  Total:              28 bytes per condition

ArrayBlockingQueue:  1 lock + 2 conditions = 96 bytes
LinkedBlockingQueue: 2 locks + 2 conditions = 136 bytes
```

## Empty BlockingQueue

```
ArrayBlockingQueue(100):
  Instance:           68 bytes
  items array:       416 bytes (100 slots)
  Total:             484 bytes

LinkedBlockingQueue():
  Instance:           84 bytes
  No nodes allocated
  Total:              84 bytes
```

## Producer-Consumer Memory Pattern

```
Steady state: 5 items in queue

ArrayBlockingQueue:
  [A][B][C][D][E][ ][ ][ ]  ← 8 slots, 5 used
  Memory: 32 + 336 + 80 = 448 bytes

LinkedBlockingQueue:
  A → B → C → D → E → null
  Memory: 84 + 5×(28+16) = 304 bytes
  (Each Integer: 16 bytes, Node: 28 bytes)
```
