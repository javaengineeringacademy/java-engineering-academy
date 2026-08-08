# Vector Internals

## Backing Array Structure

```java
protected Object[] elementData;
protected int elementCount;
```

Vector is structurally identical to ArrayList but with synchronized methods.

## How Vector Works

```
elementData: [A] [B] [C] [D] [E] [n] [n] [n] [n] [n] [n] [n] [n] [n] [n]
              ↑                                                       ↑
             index=0                                              capacity=15
                                        ↑
                                   elementCount=5
```

## Growth Algorithm

```
DEFAULT_CAPACITY = 10

newCapacity = oldCapacity * 2           // 2x growth (not 1.5x)

Increment-based:
newCapacity = oldCapacity + capacityIncrement
capacityIncrement defaults to 0 → uses 2x
```

### Growth Sequence (default)

```
Capacity:  10 → 20 → 40 → 80 → 160 → 320 → ...
```

### Growth Sequence (with capacityIncrement = 5)

```
Capacity:  10 → 15 → 20 → 25 → 30 → 35 → ...
```

## Resize Process

```
BEFORE GROWTH (elementCount=10, capacity=10):
+---+---+---+---+---+---+---+---+---+---+
| A | B | C | D | E | F | G | H | I | J |
+---+---+---+---+---+---+---+---+---+---+
                                                  Arrays.copyOf(elementData, 20)
AFTER GROWTH (elementCount=10, capacity=20):
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| A | B | C | D | E | F | G | H | I | J | n | n | n | n | n | n | n | n | n | n |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
```

## Thread Safety Mechanism

```java
// Every public method is synchronized
public synchronized void addElement(E obj) { ... }
public synchronized E elementAt(int index) { ... }
public synchronized boolean removeElement(Object obj) { ... }
```

### Monitor Lock

```
Thread 1: add("A")  ──► Acquires lock ──► Executes ──► Releases lock
Thread 2: get(0)    ──► Waits for lock ──────────────► Acquires lock → Executes
Thread 3: remove()  ──► Waits for lock ──────────────────────────────► Acquires lock
```

## Core Operations

### add(element)

```
1. synchronized(this)
2. Ensure capacity:  elementCount + 1 > elementData.length → grow()
3. elementData[elementCount] = element
4. elementCount++

Time: O(1) amortized
Lock: held during entire operation
```

### elementAt(index)

```
1. synchronized(this)
2. Range check
3. Return elementData[index]

Time: O(1)
Lock: held during operation
```

### removeElement(obj)

```
1. synchronized(this)
2. Find index of obj (linear scan)
3. If found: System.arraycopy to shift left
4. elementCount--
5. elementData[elementCount] = null

Time: O(n)
Lock: held during entire operation
```

## Memory Layout Diagram

```
Vector instance on heap:
┌──────────────────────────────────────┐
│  Object header       (12 bytes)      │
│  Monitor lock info    (8 bytes)      │
│  int elementCount     (4 bytes)      │
│  int capacityIncrement(4 bytes)      │
│  Object[] elementData (8 bytes)      │
└──────────────┬───────────────────────┘
               │
               ▼
     ┌──────────────────────────────────────┐
     │  Object[] backing array              │
     │  [0] → Element1                      │
     │  [1] → Element2                      │
     │  ...                                 │
     │  [n-1] → ElementN                    │
     │  [n] null                            │
     │  ...                                 │
     └──────────────────────────────────────┘
```

## Vector vs ArrayList

```
┌─────────────────┬────────────────┬────────────────┐
│ Feature         │    Vector      │   ArrayList    │
├─────────────────┼────────────────┼────────────────┤
│ Thread safety   │  Synchronized  │  Not safe      │
│ Growth          │  2x            │  1.5x          │
│ Performance     │  Slower (lock) │  Faster        │
│ Legacy          │  Since JDK 1.0 │  Since JDK 1.2 │
│ Recommended     │  No            │  Yes           │
└─────────────────┴────────────────┴────────────────┘
```

## Legacy Status

Vector is a legacy class from JDK 1.0:
- All methods synchronized → unnecessary overhead in single-threaded code
- Growth 2x wastes more memory than ArrayList's 1.5x
- Use ArrayList + Collections.synchronizedList() if needed
- Or better: use java.util.concurrent classes

## Key Implementation Details

1. **elementCount vs size** — Vector uses `elementCount`, ArrayList uses `size`.
   Same concept, different naming (legacy).

2. **capacityIncrement** — Optional growth increment. If set, grows by that
   amount instead of doubling. Allows fine-tuning growth.

3. **insertElementAt / removeElementAt** — Legacy methods with the same
   functionality as add(index, element) / remove(index).

4. **elements()** — Returns Enumeration (legacy Iterator). Returns a
   snapshot; concurrent modification during iteration is safe but may
   skip or duplicate elements.

5. **toArray()** — Returns Object[], not T[]. No type safety.
