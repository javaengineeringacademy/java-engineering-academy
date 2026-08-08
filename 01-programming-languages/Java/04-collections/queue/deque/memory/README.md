# Deque Memory Usage

## ArrayDeque Per-Instance Overhead

```
┌────────────────────────────────────────┐
│  Object header (Mark + Klass)     12B  │
│  Object[] elements ref             8B  │
│  int head                         4B   │
│  int tail                         4B   │
│  Padding                          4B   │
├────────────────────────────────────────┤
│  TOTAL INSTANCE:                 32B  │
└────────────────────────────────────────┘
```

## Backing Array Overhead

```
Header:   16 bytes
Per slot: 4 bytes (reference)

Default capacity: 16 slots = 64 bytes
```

## Per-Element Cost

```
4 bytes per slot in circular array
+ Object overhead per element
```

## Scaling: 1000 Integer Elements

```
ArrayDeque instance:         32 bytes
Backing array:
  Header:                   16 bytes
  1024 slots (power of 2): 4,096 bytes
  ──────────────────────────────────
  Array total:            4,112 bytes

1000 Integer objects:
  1000 × 16 bytes:       16,000 bytes

─────────────────────────────────────────
TOTAL:                    20,144 bytes ≈ 20 KB
```

## Comparison: ArrayDeque vs LinkedList (1000 Integers)

```
┌──────────────────┬──────────────┬──────────────┐
│                  │  ArrayDeque  │  LinkedList  │
├──────────────────┼──────────────┼──────────────┤
│ Instance         │     32 B     │     32 B     │
│ Array/Nodes      │   4,112 B    │  32,000 B    │
│ Elements         │  16,000 B    │  16,000 B    │
├──────────────────┼──────────────┼──────────────┤
│ TOTAL            │   ≈ 20 KB   │   ≈ 48 KB   │
│ addFirst/addLast │     O(1)     │     O(1)     │
│ Cache friendly   │     Yes      │      No      │
│ Memory ratio     │     1x       │    2.4x      │
└──────────────────┴──────────────┴──────────────┘
```

## Circular Array Visualization

```
ArrayDeque with 5 elements, capacity 8:

Circular view:
         ┌────┐
    ┌────┤ D  ├────┐
    │    └────┘    │
┌───┴──┐       ┌───┴──┐
│  C   │       │  E   │
└───┬──┘       └───┬──┘
    │    ┌────┐    │
    └────┤ A  ├────┘
         └────┘
    head points to A
    tail points to next empty slot

Linear memory:
[A] [B] [C] [D] [E] [ ] [ ] [ ]
 0   1   2   3   4   5   6   7
 ↑                       ↑
head=0                 tail=5
```

## Empty ArrayDeque

```
new ArrayDeque<>()
  Instance:            32 bytes
  elements: empty shared array
  head = 0, tail = 0
  Total:               32 bytes
```

## Growth Pattern

```
Default capacity: 16
Growth: 2x

Capacity: 16 → 32 → 64 → 128 → 256 → 512 → 1024

At size 1000:
  capacity = 1024
  wasted: 24 slots × 4 = 96 bytes
```

## Memory During Operations

```
addFirst + addLast interleaved:

After addLast("A"):  [A] [ ] [ ] [ ] [ ] [ ] [ ] [ ]
After addFirst("B"): [A] [ ] [ ] [ ] [ ] [ ] [ ] [B]
After addLast("C"):  [A] [ ] [C] [ ] [ ] [ ] [ ] [B]
After addFirst("D"): [A] [ ] [C] [ ] [ ] [ ] [D] [B]

Head and tail converge toward each other.
When they meet: resize to double capacity.
```

## Deque vs Stack vs Queue

```
┌──────────────────┬──────────────┬──────────────┬──────────────┐
│                  │  ArrayDeque  │    Stack     │  LinkedList  │
├──────────────────┼──────────────┼──────────────┼──────────────┤
│ As Stack         │     Yes      │     Yes      │     Yes      │
│ As Queue         │     Yes      │      No      │     Yes      │
│ As Deque         │     Yes      │      No      │     Yes      │
│ Thread safe      │      No      │     Yes      │      No      │
│ Performance      │   Fastest    │    Slow      │   Medium     │
│ Memory (1000)    │   ≈ 20 KB   │   ≈ 24 KB   │   ≈ 48 KB   │
└──────────────────┴──────────────┴──────────────┴──────────────┘
```
