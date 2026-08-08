# Collections Comprehensive Quiz - 50 Questions

## Section A: Fundamentals (10 questions, easy)

1. What is the difference between Collection and Collections?
   A) Collection is a framework, Collections is an interface
   B) Collection is an interface, Collections is a utility class
   C) They are the same thing
   D) Collection is for lists, Collections is for maps
   Answer: B

2. Which collection maintains insertion order?
   A) HashSet
   B) TreeSet
   C) LinkedHashSet
   D) PriorityQueue
   Answer: C

3. What is the time complexity of ArrayList.get(index)?
   A) O(n)
   B) O(log n)
   C) O(1)
   D) O(n^2)
   Answer: C

4. Which collection does NOT allow null elements?
   A) ArrayList
   B) HashSet
   C) TreeMap
   D) LinkedList
   Answer: C

5. What interface does HashMap implement?
   A) Collection
   B) Map
   C) List
   D) Set
   Answer: B

6. Which is faster for random access?
   A) LinkedList
   B) ArrayList
   C) Both are same
   D) Depends on data
   Answer: B

7. What is the default capacity of ArrayList?
   A) 5
   B) 10
   C) 15
   D) 20
   Answer: B

8. Which collection is synchronized by default?
   A) ArrayList
   B) HashMap
   C) Vector
   D) HashSet
   Answer: C

9. What does Iterator.remove() do?
   A) Removes all elements
   B) Removes last element returned by next()
   C) Removes first element
   D) Throws exception
   Answer: B

10. Which is NOT a Collection subinterface?
    A) List
    B) Set
    C) Map
    D) Queue
    Answer: C

## Section B: List Implementations (10 questions, easy-medium)

11. When should you use LinkedList over ArrayList?
    A) When you need fast random access
    B) When you need frequent insertions/deletions at ends
    C) When you need thread safety
    D) When you need sorted data
    Answer: B

12. What is CopyOnWriteArrayList best for?
    A) Frequent writes
    B) Read-heavy, write-rare scenarios
    C) Thread-safe random access
    D) Memory-efficient storage
    Answer: B

13. What is the growth factor of ArrayList?
    A) 2x
    B) 1.5x
    C) 1.25x
    D) 3x
    Answer: B

14. What is the growth factor of Vector?
    A) 2x
    B) 1.5x
    C) 1.25x
    D) 3x
    Answer: A

15. Which List implementation is best for stack behavior?
    A) ArrayList
    B) LinkedList
    C) Stack
    D) ArrayDeque
    Answer: D

16. What happens when you add null to CopyOnWriteArrayList?
    A) Throws NullPointerException
    B) Adds null successfully
    C) Throws UnsupportedOperationException
    D) Returns false
    Answer: B

17. How does ArrayList internally store elements?
    A) Linked nodes
    B) Object[] array
    C) Tree nodes
    D) Hash table
    Answer: B

18. Which is better for memory: ArrayList or LinkedList?
    A) LinkedList (no array overhead)
    B) ArrayList (no node pointers)
    C) Same
    D) Depends on size
    Answer: B

19. What is Collections.synchronizedList()?
    A) A new List implementation
    B) A wrapper that adds synchronization
    C) A method to create Vector
    D) A thread pool
    Answer: B

20. Which method does NOT exist on List?
    A) get()
    B) set()
    C) put()
    D) remove()
    Answer: C

## Section C: Set and Map (10 questions, medium)

21. What is the difference between HashSet and TreeSet?
    A) HashSet is faster, TreeSet is sorted
    B) HashSet allows null, TreeSet doesn't
    C) Both
    D) Neither
    Answer: C

22. How does HashMap handle collisions?
    A) It doesn't
    B) Linked list → tree at 8 entries
    C) Chaining only
    D) Open addressing
    Answer: B

23. What is the load factor of HashMap?
    A) 0.5
    B) 0.75
    C) 1.0
    D) 0.25
    Answer: B

24. Which Map allows null keys?
    A) TreeMap
    B) Hashtable
    C) HashMap
    D) ConcurrentHashMap
    Answer: C

25. What is LinkedHashMap used for?
    A) Fastest O(1) access
    B) Maintaining insertion/access order
    C) Thread safety
    D) Sorted keys
    Answer: B

26. Which is thread-safe Map?
    A) HashMap
    B) LinkedHashMap
    C) ConcurrentHashMap
    D) TreeMap
    Answer: C

27. What does EnumSet use internally?
    A) Array
    B) Linked list
    C) Bit vector
    D) Tree
    Answer: C

28. What is WeakHashMap used for?
    A) Fast access
    B) Cache that allows GC
    C) Thread safety
    D) Sorted entries
    Answer: B

29. How does TreeMap maintain order?
    A) Insertion order
    B) Red-black tree
    C) Hash table
    D) Array
    Answer: B

30. Which Map is fastest for single-threaded use?
    A) ConcurrentHashMap
    B) Hashtable
    C) HashMap
    D) TreeMap
    Answer: C

## Section D: Queue and Deque (5 questions, medium-hard)

31. What is the difference between Queue and Deque?
    A) Queue is FIFO, Deque is both ends
    B) Queue is thread-safe, Deque isn't
    C) Deque is faster
    D) No difference
    Answer: A

32. What does PriorityQueue use internally?
    A) Array
    B) Binary heap
    C) Tree
    D) Linked list
    Answer: B

33. Which is better for stack: Stack or ArrayDeque?
    A) Stack (built for it)
    B) ArrayDeque (no synchronization)
    C) Same
    D) Neither
    Answer: B

34. What happens when you poll() empty queue?
    A) Throws exception
    B) Returns null
    C) Blocks
    D) Returns Optional
    Answer: B

35. What happens when you remove() empty queue?
    A) Returns null
    B) Throws NoSuchElementException
    C) Blocks
    D) Returns Optional
    Answer: B

## Section E: Streams and Lambda (10 questions, medium-hard)

36. What is a Predicate?
    A) T → R
    B) T → boolean
    C) () → T
    D) T → void
    Answer: B

37. What is a Function?
    A) T → boolean
    B) T → void
    C) T → R
    D) () → T
    Answer: C

38. What does flatMap do?
    A) Transforms each element
    B) Flattens nested streams
    C) Filters elements
    D) Sorts elements
    Answer: B

39. What is the difference between map and flatMap?
    A) map is faster
    B) map is one-to-one, flatMap can be one-to-many
    C) flatMap is for primitives
    D) No difference
    Answer: B

40. What does collect(Collectors.toList()) do?
    A) Returns stream
    B) Collects to List
    C) Counts elements
    D) Filters elements
    Answer: B

41. What is the difference between reduce and collect?
    A) reduce returns single value, collect builds collection
    B) reduce is faster
    C) collect is for primitives
    D) No difference
    Answer: A

42. When does a stream execute?
    A) When created
    B) When terminal operation called
    C) When parallel
    D) Never
    Answer: B

43. What is a method reference?
    A) Variable reference
    B) Shorthand for lambda
    C) Reference to class
    D) Pointer
    Answer: B

44. What does parallelStream() use?
    A) Single thread
    B) ForkJoinPool
    C) ExecutorService
    D) Thread pool
    Answer: B

45. What is the difference between forEach and for-each loop?
    A) forEach is faster
    B) for-each loop is functional
    C) forEach is functional, for-each is imperative
    D) No difference
    Answer: C

## Section F: Advanced (5 questions, hard)

46. What is the difference between spliterator and iterator?
    A) spliterator is faster
    B) spliterator supports parallel traversal
    C) iterator supports parallel
    D) No difference
    Answer: B

47. What is the time complexity of ConcurrentHashMap.get()?
    A) O(n)
    B) O(log n)
    C) O(1)
    D) O(n log n)
    Answer: C

48. What happens during HashMap resize?
    A) Elements are copied to new array
    B) Elements are removed
    C) Capacity decreases
    D) Nothing
    Answer: A

49. What is the purpose of identity in reduce(BinaryOperator)?
    A) Comparison
    B) Default value when empty
    C) Thread safety
    D) Sorting
    Answer: B

50. What is the difference between terminal and intermediate operations?
    A) Terminal is lazy, intermediate is eager
    B) Intermediate returns Stream, terminal returns value/collection
    C) Terminal is faster
    D) Intermediate executes immediately
    Answer: B
