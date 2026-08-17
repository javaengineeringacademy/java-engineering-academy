# Stack Internals

## Class Hierarchy

```
Object → Vector → Stack
```

Stack extends Vector, inheriting its synchronized array-based structure.

## LIFO Behavior

```
push("A"):
  ┌─────┐
  │  A  │  ← top (elementCount=1)
  └─────┘

push("B"):
  ┌─────┐
  │  B  │  ← top (elementCount=2)
  ├─────┤
  │  A  │
  └─────┘

push("C"):
  ┌─────┐
  │  C  │  ← top (elementCount=3)
  ├─────┤
  │  B  │
  ├─────┤
  │  A  │
  └─────┘

pop():
  returns "C"
  ┌─────┐
  │  B  │  ← top (elementCount=2)
  ├─────┤
  │  A  │
  └─────┘
```

## Core Operations

### push(element)

```
1. addElement(element)   // Vector method
2. Increases elementCount
3. Returns element

Time: O(1) amortized
```

### pop()

```
1. synchronized(this)
2. obj = peek()          // get top element
3. removeElementAt(size()-1)  // remove top
4. Return obj

Time: O(1) amortized
Exception: EmptyStackException if empty
```

### peek()

```
1. synchronized(this)
2. if empty: throw EmptyStackException
3. Return elementAt(size() - 1)

Time: O(1)
```

### empty()

```
Return: elementCount == 0

Time: O(1)
```

### search(object)

```
1. synchronized(this)
2. Scan from top to bottom (last pushed first)
3. Return 1-based position from top
4. Return -1 if not found

Time: O(n)
```

## Stack Memory Layout

```
Stack instance on heap:
┌──────────────────────────────────────────┐
│  Object header          (12 bytes)       │
│  Monitor lock info       (8 bytes)       │
│  int elementCount        (4 bytes)       │
│  int capacityIncrement   (4 bytes)       │
│  Object[] elementData    (8 bytes)       │
└──────────────┬───────────────────────────┘
               │
               ▼
     ┌──────────────────────────────────────────┐
     │  Backing array (inherited from Vector)   │
     │  [0] → "A"   ← bottom                   │
     │  [1] → "B"                               │
     │  [2] → "C"   ← top (index = size-1)      │
     │  [3] null                                 │
     │  ...                                      │
     └──────────────────────────────────────────┘
```

## Thread Safety

```
All Stack methods are synchronized (inherited from Vector):

synchronized push(E item)
synchronized pop()
synchronized peek()
synchronized boolean empty()
synchronized int search(Object o)

Same lock contention issues as Vector.
```

## Legacy Status

Stack is a legacy class from JDK 1.0. Recommended alternatives:

```
┌──────────────────────────────────────────────────┐
│  Instead of Stack, use:                          │
│                                                  │
│  1. ArrayDeque<E>   — fastest, no synchronization│
│  2. LinkedList<E>   — if you need Deque interface│
│                                                  │
│  ArrayDeque is ~2x faster than Stack for LIFO.   │
└──────────────────────────────────────────────────┘
```

### Why ArrayDeque Is Better

```
Stack (Vector-based):
  - Synchronized overhead on every operation
  - 2x growth wastes memory
  - Legacy, no improvements since JDK 1.0

ArrayDeque (circular array):
  - No synchronization overhead
  - 2x growth but no monitor lock
  - Modern, optimized implementation
  - Can also be used as queue
```

## Key Implementation Details

1. **Inherits Vector** — All Vector internals apply: elementData array,
   elementCount, capacityIncrement, synchronized methods.

2. **push = addElement** — push() is just a wrapper for Vector.addElement().

3. **pop = removeElementAt** — pop() calls peek() then removes the last element.

4. **search() is 1-based** — Returns distance from top, not index.
   search("C") returns 1 for the top element.

5. **EmptyStackException** — pop() and peek() throw this if the stack is empty.
   Unlike Vector, which would return null or throw IndexOutOfBoundsException.

6. **Serialization** — Custom serialization handles the Vector fields.
   The stack-specific behavior is just method wrappers.
