# LinkedList Internals

## Node Structure

```java
private static class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```

## Doubly-Linked List Layout

```
FIRST                                              LAST
  │                                                  │
  ▼                                                  ▼
┌──────┬──────┐    ┌──────┬──────┐    ┌──────┬──────┐
│ prev │ null │    │ prev │      │    │ prev │      │
│      │      │    │      │      │    │      │      │
│ next ─────────►│ next │      │────►│ next │ null │
└──────┴──────┘    └──────┴──────┘    └──────┴──────┘
  Node 1              Node 2              Node 3
  item="A"            item="B"            item="C"
```

### Concrete Example: addFirst / addLast

```
EMPTY LIST:
  first = null
  last  = null
  size  = 0

addFirst("A"):
  first ──►┌─────┬──────┬─────┐
           │prev │  "A" │next │
           │null │      │null │
           └─────┴──────┴─────┘
  last = first

addLast("B"):
  first ──►┌─────┬──────┬─────┐     ┌─────┬──────┬─────┐
           │prev │  "A" │next─────────│prev │  "B" │next │
           │null │      │     │     │     │      │null │
           └─────┴──────┴─────┘     └─────┴──────┴─────┘
                                    ▲
                                    │
                                   last

addFirst("C") (insert before first):
  first ──►┌─────┬──────┬─────┐     ┌─────┬──────┬─────┐     ┌─────┬──────┬─────┐
           │prev │  "C" │next─────────│prev │  "A" │next─────────│prev │  "B" │next │
           │null │      │     │     │     │      │     │     │     │      │null │
           └─────┴──────┴─────┘     └─────┴──────┴─────┘     └─────┴──────┴─────┘
                                    ▲
                                    │
                                   last
```

## Core Operations

### addFirst(element)

```
1. Create new node: Node(null, element, first)
2. If empty:  last = newNode
3. Else:      first.prev = newNode
4. first = newNode
5. size++

Time: O(1) — no shifting needed
```

### addLast(element)

```
1. Create new node: Node(last, element, null)
2. If empty:  first = newNode
3. Else:      last.next = newNode
4. last = newNode
5. size++

Time: O(1)
```

### get(index)

```
1. Range check
2. If index < size/2:  traverse from first
3. If index >= size/2: traverse from last
4. Return node.item

Time: O(n) — must walk links
```

### remove(node)

```
1. If node.prev != null: node.prev.next = node.next
2. Else:                 first = node.next
3. If node.next != null: node.next.prev = node.prev
4. Else:                 last = node.prev
5. node.item = null  (help GC)
6. size--

Time: O(1) if you have the node reference
```

### remove(index)

```
1. Range check
2. Find node at index (O(n))
3. unlink(node)  → O(1)

Time: O(n) — dominated by finding the node
```

## Traversal Direction

```
For index i:
  if i < (size >> 1):  start from first, walk forward
  else:                start from last, walk backward

       ◄─────── forward ───────►
       0    1    2    3    4    5
     [A]──[B]──[C]──[D]──[E]──[F]
       ▲                         ▲
     first                     last
       ◄── backward ──►
```

## Memory Layout Diagram

```
LinkedList instance on heap:
┌─────────────────────────────────┐
│  Object header     (12 bytes)   │
│  int size          (4 bytes)    │
│  Node first ref    (8 bytes)    │
│  Node last ref     (8 bytes)    │
└──────────────┬──────────────────┘
               │
               ▼
         Node1 (heap)      Node2 (heap)      Node3 (heap)
        ┌──────────┐      ┌──────────┐      ┌──────────┐
        │ prev: ───┼──X   │ prev: ───┼──┐   │ prev: ───┼──┐
        │ item: "A"│      │ item: "B"│  │   │ item: "C"│  │
        │ next: ───┼──┐   │ next: ───┼──┼──►│ next:null│  │
        └──────────┘  │   └──────────┘  │   └──────────┘  │
                      └─────────────────┘                  │
                       (prev of Node3 points to Node2)    │
                      └───────────────────────────────────┘
```

## Key Implementation Details

1. **No backing array** — Each element is a separate heap object with
   its own overhead. No capacity concept.

2. **Double links** — Both `prev` and `next` allow O(1) removal if you
   already have the node reference.

3. **first/last** — Direct references to both ends enable O(1) `addFirst`,
   `addLast`, `getFirst`, `getLast`.

4. **NOT random access** — Unlike ArrayList, `get(i)` requires traversal.
   Never use `linkedlist.get(i)` in a loop.

5. **Implements Deque** — LinkedList also implements `Deque`, so it can
   be used as a queue or stack.
