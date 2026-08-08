# Collections Interview Questions - Part 1: List, Set, Map, Iterator (Q1-50)

## Section A: ArrayList (Q1-10)

1. What is the default capacity of ArrayList?
   Answer: 10. It grows by 1.5x when full.

2. How does ArrayList grow internally?
   Answer: Creates new array of 1.5x size (oldCapacity + oldCapacity >> 1), copies elements using Arrays.copyOf().

3. Is ArrayList thread-safe?
   Answer: No. Use Collections.synchronizedList() or CopyOnWriteArrayList for thread safety.

4. What is the time complexity of get() and add() in ArrayList?
   Answer: get() is O(1), add() is O(1) amortized, add(index) is O(n).

5. When would you use ArrayList over LinkedList?
   Answer: When you need frequent random access (get by index). ArrayList is cache-friendly.

6. Can ArrayList hold null values?
   Answer: Yes, ArrayList can hold multiple null values.

7. What happens when you create ArrayList with capacity 0?
   Answer: It creates an empty array. First add() creates array with capacity 1.

8. How to remove elements while iterating ArrayList?
   Answer: Use Iterator.remove() or ListIterator.remove(). For-each loop throws ConcurrentModificationException.

9. What is the difference between ArrayList and Vector?
   Answer: Vector is synchronized (slower), grows 2x. ArrayList is not synchronized, grows 1.5x.

10. How to make ArrayList read-only?
    Answer: Collections.unmodifiableList(list). Attempting to modify throws UnsupportedOperationException.

## Section B: LinkedList (Q11-18)

11. What data structure does LinkedList use?
    Answer: Doubly-linked list. Each node has prev, next, and item.

12. What is the time complexity of get(i) in LinkedList?
    Answer: O(n). Must traverse from head or tail.

13. When is LinkedList better than ArrayList?
    Answer: When you need frequent insertions/deletions at both ends. addFirst/addLast are O(1).

14. Can LinkedList be used as Queue?
    Answer: Yes, LinkedList implements Queue and Deque interfaces.

15. What is the memory overhead of LinkedList per element?
    Answer: 32 bytes per node (12 header + 4 item + 8 next + 8 prev).

16. How does LinkedList handle null values?
    Answer: Allows null values. Can add null elements.

17. What is the difference between LinkedList and ArrayDeque?
    Answer: ArrayDeque is faster (cache-friendly), no nulls. LinkedList allows null, implements List.

18. Why is LinkedList rarely used in practice?
    Answer: Poor cache locality, memory overhead. ArrayList is faster for most operations.

## Section C: CopyOnWriteArrayList (Q19-23)

19. What is CopyOnWriteArrayList?
    Answer: Thread-safe List where writes copy entire array. Reads are lock-free.

20. When should you use CopyOnWriteArrayList?
    Answer: Read-heavy, write-rare scenarios. Multiple threads reading, few writing.

21. What happens to Iterator during write in CopyOnWriteArrayList?
    Answer: Iterator sees snapshot at creation time. Write creates new array, iterator remains on old.

22. Is CopyOnWriteArrayList memory efficient?
    Answer: No. Each write creates full copy. Use only when reads >> writes.

23. What is the time complexity of add() in CopyOnWriteArrayList?
    Answer: O(n) because it copies entire array.

## Section D: HashSet (Q24-29)

24. What is the underlying data structure of HashSet?
    Answer: HashMap. Values are stored as keys with dummy PRESENT object.

25. Can HashSet hold null values?
    Answer: Yes, one null value allowed.

26. What happens when you add duplicate to HashSet?
    Answer: Returns false, set remains unchanged.

27. Is HashSet ordered?
    Answer: No, HashSet does not maintain any order.

28. What is the time complexity of add/remove/contains in HashSet?
    Answer: O(1) for all operations.

29. How does HashSet handle hash collisions?
    Answer: Uses HashMap which handles collisions with linked list → tree (8+ entries).

## Section E: TreeSet (Q30-35)

30. What is the underlying data structure of TreeSet?
    Answer: TreeMap (Red-Black tree).

31. What is the time complexity of add/remove/contains in TreeSet?
    Answer: O(log n) for all operations.

32. Can TreeSet hold null values?
    Answer: No, TreeSet throws NullPointerException for null (in most implementations).

33. How does TreeSet maintain sorted order?
    Answer: Uses Red-Black tree. Elements must implement Comparable or provide Comparator.

34. What is the difference between TreeSet and HashSet?
    Answer: TreeSet is sorted O(log n), HashSet is unordered O(1).

35. When would you use TreeSet over HashSet?
    Answer: When you need sorted elements or range operations (subSet, headSet, tailSet).

## Section F: HashMap (Q36-45)

36. What is the underlying data structure of HashMap?
    Answer: Array of Node (bucket array). Each Node has hash, key, value, next.

37. What is the load factor of HashMap?
    Answer: 0.75 (75%). Triggers resize when 75% full.

38. How does HashMap handle collisions?
    Answer: Linked list in each bucket. When 8+ entries, converts to Red-Black tree.

39. Can HashMap hold null keys?
    Answer: Yes, one null key allowed. Null key hashes to bucket 0.

40. What happens during HashMap resize?
    Answer: Doubles capacity, rehashes all entries using new capacity.

41. What is the time complexity of get/put in HashMap?
    Answer: O(1) average, O(n) worst case (all same hash), O(log n) with treeification.

42. How does HashMap calculate bucket index?
    Answer: hash(key) & (n-1) where n is array length.

43. What is the difference between HashMap and Hashtable?
    Answer: Hashtable is synchronized, no nulls. HashMap is not synchronized, allows nulls.

44. What is ConcurrentHashMap?
    Answer: Thread-safe HashMap using CAS + per-node locking. No null keys/values.

45. When would you use TreeMap over HashMap?
    Answer: When you need sorted keys or range operations.

## Section G: LinkedHashMap (Q46-48)

46. What is LinkedHashMap?
    Answer: HashMap + doubly-linked list maintaining insertion or access order.

47. How to create LRU cache with LinkedHashMap?
    Answer: new LinkedHashMap<>(capacity, 0.75f, true) and override removeEldestEntry().

48. What is the difference between insertion order and access order?
    Answer: Insertion order keeps add order. Access order moves accessed entry to end (for LRU).

## Section H: Iterator (Q49-50)

49. What is the difference between Iterator and ListIterator?
    Answer: Iterator is forward-only, works on all Collections. ListIterator is bidirectional, List-only.

50. What is ConcurrentModificationException?
    Answer: Thrown when collection modified during iteration without using Iterator.remove(). Fail-fast behavior.

## Section I: Streams (Q51-65)

51. What is a Stream in Java?
    Answer: A sequence of elements supporting functional-style operations. Not a data structure.

52. What is the difference between intermediate and terminal operations?
    Answer: Intermediate returns Stream (lazy), terminal produces result (triggers execution).

53. What is a Predicate in Stream?
    Answer: Functional interface T → boolean. Used for filtering: stream.filter(x -> x > 5).

54. What is the difference between map() and flatMap()?
    Answer: map() is one-to-one, flatMap() flattens nested streams (one-to-many).

55. What does collect(Collectors.toList()) do?
    Answer: Collects stream elements into a new List.

56. What is the difference between reduce() and collect()?
    Answer: reduce() combines to single value. collect() builds collection using Collector.

57. What is a Collector?
    Answer: Object that accumulates input elements into a mutable result container.

58. How does parallelStream() work?
    Answer: Uses ForkJoinPool.commonPool(). Splits stream, processes in parallel, combines results.

59. What is the difference between forEach() and for-each loop?
    Answer: forEach is functional (Stream API). for-each is imperative (language construct).

60. What is Collectors.groupingBy()?
    Answer: Groups elements by classifier function. Returns Map<K, List<T>>.

61. What is Collectors.partitioningBy()?
    Answer: Partitions by predicate into two groups: true and false. Returns Map<Boolean, List<T>>.

62. What is the difference between findFirst() and findAny()?
    Answer: findFirst() returns first element (ordered). findAny() returns any (faster in parallel).

63. What is the difference between anyMatch(), allMatch(), noneMatch()?
    Answer: anyMatch: any element matches. allMatch: all match. noneMatch: none match.

64. When should you use parallelStream()?
    Answer: Large datasets, CPU-intensive operations, no shared mutable state.

65. What is Spliterator used for?
    Answer: Splittable iterator for parallel traversal. Used internally by parallel streams.

## Section J: Lambda Expressions (Q66-75)

66. What is a lambda expression?
    Answer: Anonymous function implementing a functional interface. Syntax: (params) -> body.

67. What is a functional interface?
    Answer: Interface with exactly one abstract method. @FunctionalInterface annotation optional.

68. What is the difference between Predicate and Function?
    Answer: Predicate: T → boolean (test). Function: T → R (transform).

69. What is a Consumer?
    Answer: Functional interface T → void. Performs action, no return value.

70. What is a Supplier?
    Answer: Functional interface () → T. Provides value, no input.

71. What are method references?
    Answer: Shorthand for lambdas. Types: ClassName::staticMethod, object::instanceMethod, ClassName::new.

72. What is the difference between lambda and anonymous class?
    Answer: Lambda is concise, implicit this. Anonymous class is verbose,有自己的 this.

73. Can lambda access local variables?
    Answer: Yes, but must be effectively final (not reassigned).

74. What is BiFunction?
    Answer: Functional interface (T, U) → R. Takes two arguments, returns result.

75. What is UnaryOperator?
    Answer: Functional interface T → T. Special case of Function where input equals output.

## Section K: Queue and Deque (Q76-82)

76. What is the difference between Queue and Deque?
    Answer: Queue is FIFO (addLast, removeFirst). Deque is double-ended (both ends).

77. What is PriorityQueue?
    Answer: Binary min-heap. Elements sorted by natural order or Comparator.

78. What is the difference between ArrayDeque and LinkedList?
    Answer: ArrayDeque is faster (cache-friendly), no nulls. LinkedList allows null, implements List.

79. What is the difference between poll() and remove()?
    Answer: poll() returns null if empty. remove() throws NoSuchElementException.

80. What is BlockingQueue?
    Answer: Queue that blocks when full (put) or empty (take). Used in producer-consumer.

81. What is the difference between ArrayBlockingQueue and LinkedBlockingQueue?
    Answer: Array is fixed-size, linked is optionally bounded. Both use locks.

82. When would you use Deque over Stack?
    Answer: Always. ArrayDeque is faster, not synchronized, better API.

## Section L: Concurrent Collections (Q83-90)

83. Why not use synchronized HashMap?
    Answer: Entire map locked. ConcurrentHashMap uses per-node locking, better concurrency.

84. What is the difference between ConcurrentHashMap in Java 7 and 8?
    Answer: Java 7: Segment locking. Java 8: CAS + per-node synchronized. Better scalability.

85. Can ConcurrentHashMap have null keys or values?
    Answer: No. Nulls ambiguous in concurrent context (contains vs get).

86. What is CopyOnWriteArraySet?
    Answer: Set backed by CopyOnWriteArrayList. Thread-safe, read-heavy scenarios.

87. What is ConcurrentLinkedQueue?
    Answer: Non-blocking thread-safe Queue using CAS. Lock-free.

88. What is BlockingQueue used for?
    Answer: Producer-consumer pattern. take() blocks until available, put() blocks until space.

89. What is ForkJoinPool?
    Answer: Thread pool for divide-and-conquer. Used by parallel streams.

90. What is work-stealing?
    Answer: Idle thread steals tasks from busy thread's queue. ForkJoinPool feature.

## Section M: Advanced (Q91-100)

91. What is the difference between HashMap and TreeMap?
    Answer: HashMap O(1) unordered. TreeMap O(log n) sorted by key.

92. What is EnumMap?
    Answer: Specialized Map for enum keys. Uses ordinal as index, very fast and memory efficient.

93. What is IdentityHashMap?
    Answer: Uses == instead of equals() for key comparison. Reference equality.

94. What is WeakHashMap?
    Answer: Map with weak reference keys. Entries GC'd when key has no strong references.

95. What is the difference between fail-fast and fail-safe iterators?
    Answer: fail-fast: throw ConcurrentModificationException. fail-safe: work on snapshot (CopyOnWrite).

96. How to create unmodifiable collection?
    Answer: Collections.unmodifiableList(), Collections.unmodifiableMap(), Collections.unmodifiableSet().

97. What is the difference between Collection and Collections?
    Answer: Collection is root interface. Collections is utility class with static methods.

98. What is the difference between List.of() and Arrays.asList()?
    Answer: List.of() is immutable, no nulls. Arrays.asList() is fixed-size, allows nulls, backed by array.

99. How to sort a Map by values?
    Answer: map.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(...)

100. What is the best practice for choosing collection?
    Answer: Need key-value? Map. Need unique? Set. Need ordered? List. Need FIFO? Queue. Need priority? PriorityQueue.
