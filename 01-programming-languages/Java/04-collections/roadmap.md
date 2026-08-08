# Collections Learning Roadmap

## Phase 1: Foundation
```
Collection interface → List → ArrayList
                ↓
               Set → HashSet
                ↓
               Map → HashMap
```

## Phase 2: Implementations
```
List: LinkedList, Vector, Stack, CopyOnWriteArrayList
Set: LinkedHashSet, TreeSet, EnumSet
Map: TreeMap, LinkedHashMap, Hashtable, WeakHashMap, ConcurrentHashMap
Queue: PriorityQueue, ArrayDeque, BlockingQueue
```

## Phase 3: Operations
```
Iteration (8 methods) → Sorting → Searching
                         ↓
                    Streams (filter, map, reduce, collect)
                         ↓
                    Lambda Expressions
```

## Phase 4: Concurrent
```
ConcurrentHashMap → CopyOnWriteArrayList
        ↓
   BlockingQueue → ForkJoinPool
```

## Decision Tree
```
Need key-value? → Map
Need unique? → Set
Need ordered? → List
Need FIFO? → Queue
Need LIFO? → Deque (ArrayDeque)
Need priority? → PriorityQueue
Need sorted? → TreeMap/TreeSet
Need fast? → HashMap/HashSet
Need thread-safe? → ConcurrentHashMap
```