# Collections Framework - References

## Official Documentation
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/index.html)
- [Java API: java.util Package](https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html)
- [Java Language Specification: Generics](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html)

## Books
- *Effective Java* (Joshua Bloch) - Items on collections, generics, and streams
- *Java Generics and Collections* (Naftalin & Wadler) - Deep dive into collections internals

## Collection Hierarchy

```
Iterable
├── Collection
│   ├── List (ArrayList, LinkedList, Vector, Stack)
│   ├── Set (HashSet, LinkedHashSet, TreeSet)
│   ├── Queue (PriorityQueue, ArrayDeque)
│   └── Deque (ArrayDeque, LinkedList)
└── Map (HashMap, LinkedHashMap, TreeMap, Hashtable)
```

## Key Interfaces
| Interface | Methods | Description |
|-----------|---------|-------------|
| Collection | add, remove, contains, size | Base for all collections |
| List | get, set, indexOf | Ordered, index-accessible |
| Set | (no additional) | Unique elements |
| Queue | offer, poll, peek | FIFO processing |
| Deque | addFirst, removeLast | Double-ended queue |
| Map | get, put, containsKey | Key-value pairs |

## Implementation Guide
| Use Case | Recommended | Avoid |
|----------|-------------|-------|
| Fast random access | ArrayList | LinkedList |
| Frequent insert/delete | LinkedList | ArrayList |
| Unique elements | HashSet | TreeSet (unless sorted) |
| Sorted keys | TreeMap | HashMap |
| Thread-safe list | CopyOnWriteArrayList | Vector |
| Thread-safe map | ConcurrentHashMap | Hashtable |

## Related Topics
- [Stream API](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [Functional Interfaces](https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html)
- [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)
