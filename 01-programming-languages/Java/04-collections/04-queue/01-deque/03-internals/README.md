# Deque Internals

## Interface Definition

```
Deque (Double-Ended Queue) is an interface for element access
at both ends. Implemented by ArrayDeque and LinkedList.
```

## ArrayDeque Implementation

```
ArrayDeque uses a circular array:

private transient Object[] elements;
private transient int head;
private transient int tail;
```

## Circular Array Structure

```
Array with capacity 8:
  indices: 0  1  2  3  4  5  6  7

Initial state (empty):
  head = 0, tail = 0
  [ ] [ ] [ ] [ ] [ ] [ ] [ ] [ ]
   0   1   2   3   4   5   6   7

addLast("A"):
  tail = (0 + 1) % 8 = 1
  [A] [ ] [ ] [ ] [ ] [ ] [ ] [ ]
   0   1   2   3   4   5   6   7
   ↑
  head=0   tail=1

addFirst("B"):
  head = (0 - 1 + 8) % 8 = 7
  [A] [ ] [ ] [ ] [ ] [ ] [ ] [B]
   0   1   2   3   4   5   6   7
   ↑                           ↑
  head=7                   tail=1

addLast("C"):
  tail = (1 + 1) % 8 = 2
  [A] [ ] [C] [ ] [ ] [ ] [ ] [B]
   0   1   2   3   4   5   6   7
   ↑           ↑               ↑
  head=7    tail=2          (wraps around)
```

## Wrapping Behavior

```
The circular array wraps around:

After several operations:
  [E] [F] [ ] [ ] [ ] [ ] [C] [D]
   0   1   2   3   4   5   6   7
   ↑                       ↑
  tail=1                head=6

Elements: D(head) → E → F → ... → C(tail-1)
Iteration: start at head, follow indices modulo capacity
```

## Core Operations

### addFirst(element)

```
1. elements[head = (head - 1 + elements.length) % elements.length] = element
2. If head == tail: resize()

Time: O(1) amortized
```

### addLast(element)

```
1. elements[tail] = element
2. tail = (tail + 1) % elements.length
3. If head == tail: resize()

Time: O(1) amortized
```

### pollFirst()

```
1. if head == tail: return null (empty)
2. E result = elements[head]
3. elements[head] = null  (help GC)
4. head = (head + 1) % elements.length
5. Return result

Time: O(1)
```

### pollLast()

```
1. if head == tail: return null (empty)
2. tail = (tail - 1 + elements.length) % elements.length
3. E result = elements[tail]
4. elements[tail] = null
5. Return result

Time: O(1)
```

### peekFirst() / peekLast()

```
1. if head == tail: return null
2. Return elements[head] or elements[(tail - 1 + length) % length]

Time: O(1)
```

## Memory Layout Diagram

```
ArrayDeque instance:
┌──────────────────────────────────────┐
│  Object header         (12 bytes)    │
│  Object[] elements ref  (8 bytes)    │
│  int head                (4 bytes)   │
│  int tail                (4 bytes)   │
│  Padding                 (4 bytes)   │
└──────────────┬───────────────────────┘
               │
               ▼
     ┌──────────────────────────────────────────┐
     │  Circular array                          │
     │  ┌────┬────┬────┬────┬────┬────┬────┐   │
     │  │ D  │ E  │ F  │    │    │    │ A  │   │
     │  └────┴────┴────┴────┴────┴────┴────┘   │
     │   ↑                              ↑       │
     │  head=0                      tail=6      │
     └──────────────────────────────────────────┘
```

## Capacity Management

```
Default capacity: 16 (power of 2)
Growth: double capacity when full

Why power of 2?
  index = (head + offset) & (elements.length - 1)
  Bitwise AND instead of modulo for speed
```

## Thread Safety

ArrayDeque is **not** synchronized. Concurrent access can cause:
- Corrupted head/tail pointers
- Lost elements
- ArrayIndexOutOfBoundsException

## Key Implementation Details

1. **Circular array** — No shifting needed. Head and tail pointers
   wrap around using modulo arithmetic.

2. **No null elements** — ArrayDeque does not allow null elements.
   addFirst(null) throws NullPointerException.

3. **Power of 2 capacity** — Always rounds up to next power of 2.
   Enables fast index calculation with bitwise AND.

4. **Faster than LinkedList** — Better cache locality, no node allocation,
   simpler operations.

5. **Deque interface** — Can be used as stack, queue, or deque:
   - Stack: push()/pop() (addFirst/removeFirst)
   - Queue: offer()/poll() (addLast/removeFirst)
   - Deque: addFirst/addLast/pollFirst/pollLast

6. **Iterator order** — Elements iterate from head to tail:
   head → head+1 → ... → tail-1
