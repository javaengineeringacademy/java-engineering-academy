# Java Collections Framework Hierarchy

The Collections Framework provides a unified architecture for representing and manipulating collections of objects.

## Complete Collections Hierarchy

```mermaid
classDiagram
    class Iterable {
        <<interface>>
        +iterator()
    }
    
    class Collection {
        <<interface>>
        +add()
        +remove()
        +contains()
        +size()
        +isEmpty()
    }
    
    class List {
        <<interface>>
        +get()
        +set()
        +indexOf()
        +listIterator()
    }
    
    class Set {
        <<interface>>
        +add()
        +contains()
    }
    
    class SortedSet {
        <<interface>>
        +first()
        +last()
    }
    
    class NavigableSet {
        <<interface>>
        +lower()
        +higher()
        +floor()
        +ceiling()
    }
    
    class Queue {
        <<interface>>
        +offer()
        +poll()
        +peek()
    }
    
    class Deque {
        <<interface>>
        +addFirst()
        +addLast()
        +removeFirst()
        +removeLast()
    }
    
    class Map {
        <<interface>>
        +put()
        +get()
        +containsKey()
        +containsValue()
    }
    
    class SortedMap {
        <<interface>>
        +firstKey()
        +lastKey()
    }
    
    class NavigableMap {
        <<interface>>
        +lowerKey()
        +higherKey()
    }
    
    Iterable <|-- Collection
    Collection <|-- List
    Collection <|-- Set
    Collection <|-- Queue
    Set <|-- SortedSet
    SortedSet <|-- NavigableSet
    Queue <|-- Deque
    Map <|-- SortedMap
    SortedMap <|-- NavigableMap
```

## Implementation Classes

```mermaid
classDiagram
    class ArrayList {
        +Object[] elementData
        +int size
        +add()
        +get()
        +remove()
    }
    
    class LinkedList {
        +Node first
        +Node last
        +int size
        +addFirst()
        +addLast()
        +removeFirst()
    }
    
    class HashSet {
        +HashMap map
        +add()
        +contains()
        +remove()
    }
    
    class TreeSet {
        +NavigableMap m
        +add()
        +first()
        +last()
    }
    
    class PriorityQueue {
        +Object[] queue
        +offer()
        +poll()
        +peek()
    }
    
    class ArrayDeque {
        +Object[] elements
        +int head
        +int tail
        +addFirst()
        +addLast()
    }
    
    class HashMap {
        +Node[] table
        +int size
        +put()
        +get()
        +remove()
    }
    
    class TreeMap {
        +Entry root
        +put()
        +get()
        +firstKey()
        +lastKey()
    }
    
    class LinkedHashMap {
        +Entry head
        +Entry tail
        +boolean accessOrder
        +removeEldestEntry()
    }
    
    List <|.. ArrayList
    List <|.. LinkedList
    Deque <|.. LinkedList
    Deque <|.. ArrayDeque
    Set <|.. HashSet
    SortedSet <|.. TreeSet
    Queue <|.. PriorityQueue
    Map <|.. HashMap
    SortedMap <|.. TreeMap
    Map <|.. LinkedHashMap
```

## Collection Types Comparison

```mermaid
graph TB
    subgraph "Ordered Collections"
        ArrayList[ArrayList<br/>Dynamic array<br/>Fast random access]
        LinkedList[LinkedList<br/>Doubly linked list<br/>Fast insertion/deletion]
    end
    
    subgraph "Unique Elements"
        HashSet[HashSet<br/>Hash table<br/>No ordering]
        TreeSet[TreeSet<br/>Red-black tree<br/>Sorted order]
    end
    
    subgraph "FIFO Queue"
        PriorityQueue[PriorityQueue<br/>Heap-based<br/>Priority ordering]
        ArrayDeque[ArrayDeque<br/>Resizable array<br/>Faster than Stack]
    end
    
    subgraph "Key-Value Pairs"
        HashMap[HashMap<br/>Hash table<br/>No ordering]
        TreeMap[TreeMap<br/>Red-black tree<br/>Sorted by key]
        LinkedHashMap[LinkedHashMap<br/>Hash table + linked list<br/>Insertion order]
    end
```

## Selection Guide

```mermaid
flowchart TD
    A[Need Collection] --> B{Elements?}
    
    B -->|Unique| C{Ordered?}
    B -->|Duplicate| D{Ordered?}
    B -->|Key-Value| E{Ordered?}
    
    C -->|No| F[HashSet]
    C -->|Sorted| G[TreeSet]
    C -->|Insertion| H[LinkedHashSet]
    
    D -->|Random access| I[ArrayList]
    D -->|Frequent insert| J[LinkedList]
    D -->|FIFO| K[ArrayDeque]
    D -->|Priority| L[PriorityQueue]
    
    E -->|No| M[HashMap]
    E -->|Sorted| N[TreeMap]
    E -->|Insertion| O[LinkedHashMap]
```

## Key Takeaways

- **List**: Ordered, allows duplicates, indexed access
- **Set**: No duplicates, unordered (HashSet) or sorted (TreeSet)
- **Queue**: FIFO ordering, elements added/removed from ends
- **Map**: Key-value pairs, keys must be unique
- **Deque**: Double-ended queue, add/remove from both ends