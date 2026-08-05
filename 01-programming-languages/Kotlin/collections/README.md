# Kotlin Collections

This section covers working with collections in Kotlin, including Lists, Sets, Maps, and collection processing operations.

## Table of Contents

- [Collection Types](#collection-types)
- [Immutable vs Mutable](#immutable-vs-mutable)
- [List Operations](#list-operations)
- [Set Operations](#set-operations)
- [Map Operations](#map-operations)
- [Collection Transformation](#collection-transformation)
- [Sequences](#sequences)
- [Collection Processing](#collection-processing)
- [Performance Considerations](#performance-considerations)
- [Common Patterns](#common-patterns)

## Collection Types

### List

```kotlin
// Immutable list (read-only)
val numbers = listOf(1, 2, 3, 4, 5)
val strings = listOf("a", "b", "c")
val mixed = listOf(1, "hello", 2.0)  // List<Any>

// Empty list
val emptyList = emptyList<String>()
val emptyMutable = mutableListOf<String>()

// List with nulls
val withNulls = listOf(1, null, 3, null, 5)

// Typed list
val typedList: List<Int> = listOf(1, 2, 3)
val mutableTypedList: MutableList<Int> = mutableListOf(1, 2, 3)
```

### Set

```kotlin
// Immutable set (no duplicates)
val uniqueNumbers = setOf(1, 2, 3, 4, 5)
val uniqueStrings = setOf("a", "b", "c", "a")  // Only one "a"

// Empty set
val emptySet = emptySet<String>()

// Mutable set
val mutableSet = mutableSetOf(1, 2, 3)
mutableSet.add(4)
mutableSet.remove(1)

// Linked hash set (maintains insertion order)
val linkedSet = linkedSetOf(3, 1, 4, 1, 5, 9, 2, 6)
println(linkedSet)  // [3, 1, 4, 5, 9, 2, 6]
```

### Map

```kotlin
// Immutable map
val ages = mapOf("Alice" to 30, "Bob" to 25, "Charlie" to 35)
val numbers = mapOf(1 to "one", 2 to "two", 3 to "three")

// Empty map
val emptyMap = emptyMap<String, Int>()

// Mutable map
val mutableAges = mutableMapOf("Alice" to 30)
mutableAges["Bob"] = 25
mutableAges.putIfAbsent("Charlie", 35)

// Map with null values
val mapWithNulls = mapOf("a" to 1, "b" to null, "c" to 3)

// Hash map (unordered)
val hashMap = hashMapOf("x" to 1, "y" to 2, "z" to 3)

// Linked hash map (maintains insertion order)
val linkedMap = linkedMapOf("first" to 1, "second" to 2, "third" to 3)
```

## Immutable vs Mutable

### Creating Collections

```kotlin
// Immutable collections
val immutableList = listOf(1, 2, 3)
val immutableSet = setOf(1, 2, 3)
val immutableMap = mapOf(1 to "one", 2 to "two")

// Mutable collections
val mutableList = mutableListOf(1, 2, 3)
val mutableSet = mutableSetOf(1, 2, 3)
val mutableMap = mutableMapOf(1 to "one", 2 to "two")

// Converting between mutable and immutable
val immutable = listOf(1, 2, 3)
val mutable = immutable.toMutableList()
mutable.add(4)
val newImmutable = mutable.toList()  // [1, 2, 3, 4]
```

### Collection Operations

```kotlin
// Adding elements
val list = mutableListOf(1, 2, 3)
list.add(4)
list.addAll(listOf(5, 6))
list += 7  // Operator overload

// Removing elements
list.remove(3)
list.removeAll { it > 5 }
list -= 7  // Operator overload

// Checking elements
val contains = list.contains(4)
val hasElement = 4 in list  // Operator overload

// Updating elements
list[0] = 10  // Set element at index
list.set(0, 10)  // Same as above
```

## List Operations

### Accessing Elements

```kotlin
val list = listOf(1, 2, 3, 4, 5)

// By index
val first = list[0]
val second = list[1]

// First and last
val firstElement = list.first()
val lastElement = list.last()
val firstOrNull = list.firstOrNull()
val lastOrNull = list.lastOrNull()

// Safe access
val element = list.getOrElse(10) { -1 }  // -1
val elementOr = list.getOrNull(10)  // null

// Sublist
val sublist = list.subList(1, 3)  // [2, 3]

// Slicing
val sliced = list.slice(1..3)  // [2, 3, 4]
```

### Searching

```kotlin
val list = listOf(1, 2, 3, 4, 5)

// Find elements
val index = list.indexOf(3)  // 2
val lastIndex = list.lastIndexOf(3)  // 2
val first = list.find { it > 3 }  // 4
val last = list.findLast { it < 4 }  // 3

// Check existence
val contains = list.contains(3)
val any = list.any { it > 3 }
val all = list.all { it > 0 }
val none = list.none { it > 10 }

// Count
val count = list.count()
val countWithPredicate = list.count { it % 2 == 0 }
```

### Sorting

```kotlin
val list = listOf(3, 1, 4, 1, 5, 9, 2, 6)

// Basic sorting
val sorted = list.sorted()
val sortedDescending = list.sortedDescending()

// By property
data class Person(val name: String, val age: Int)
val people = listOf(
    Person("Alice", 30),
    Person("Bob", 25),
    Person("Charlie", 35)
)

val sortedByAge = people.sortedBy { it.age }
val sortedByAgeDescending = people.sortedByDescending { it.age }
val sortedByName = people.sortedBy { it.name }

// Custom comparator
val customSort = list.sortedWith(compareBy { it % 3 })

// Shuffling
val shuffled = list.shuffled()

// Reversing
val reversed = list.reversed()
```

### Filtering

```kotlin
val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

// Basic filtering
val evens = list.filter { it % 2 == 0 }
val odds = list.filterNot { it % 2 == 0 }

// Filtering with predicate
val greaterThanFive = list.filter { it > 5 }
val betweenThreeAndSeven = list.filter { it in 3..7 }

// Filtering nulls
val withNulls = listOf(1, null, 3, null, 5)
val withoutNulls = withNulls.filterNotNull()

// Filtering by type
val mixedList = listOf(1, "hello", 2.0, "world", 3)
val strings = mixedList.filterIsInstance<String>()

// Take and drop
val firstThree = list.take(3)
val lastThree = list.takeLast(3)
val withoutFirstThree = list.drop(3)
val withoutLastThree = list.dropLast(3)

// Take/drop while
val takeWhile = list.takeWhile { it < 5 }
val dropWhile = list.dropWhile { it < 5 }
```

### Transformation

```kotlin
val list = listOf(1, 2, 3, 4, 5)

// Map
val doubled = list.map { it * 2 }
val squared = list.map { it * it }

// Map with index
val withIndex = list.mapIndexed { index, value -> "$index: $value" }

// Flat map
val nested = listOf(listOf(1, 2), listOf(3, 4), listOf(5))
val flat = nested.flatMap { it }  // [1, 2, 3, 4, 5]

// Flat map with transformation
val words = listOf("hello world", "foo bar")
val letters = words.flatMap { it.split(" ") }

// Zip
val names = listOf("Alice", "Bob", "Charlie")
val ages = listOf(30, 25, 35)
val zipped = names.zip(ages)  // [(Alice, 30), (Bob, 25), (Charlie, 35)]

// Unzip
val (unzippedNames, unzippedAges) = zipped.unzip()

// Associate
val nameToAge = names.associateWith { name ->
    ages[names.indexOf(name)]
}

// Group by
val numbers = listOf(1, 2, 3, 4, 5, 6)
val grouped = numbers.groupBy { if (it % 2 == 0) "even" else "odd" }

// Partition
val (even, odd) = numbers.partition { it % 2 == 0 }

// Chunk
val chunked = numbers.chunked(3)  // [[1, 2, 3], [4, 5, 6]]

// Window
val windowed = numbers.windowed(3)  // [[1, 2, 3], [2, 3, 4], [3, 4, 5], [4, 5, 6]]
```

### Aggregation

```kotlin
val list = listOf(1, 2, 3, 4, 5)

// Sum
val sum = list.sum()
val sumOf = list.sumOf { it * 2 }

// Average
val average = list.average()

// Reduce
val product = list.reduce { acc, i -> acc * i }

// Fold
val sumWithInitial = list.fold(0) { acc, i -> acc + i }
val joinToString = list.fold("") { acc, i -> if (acc.isEmpty()) "$i" else "$acc, $i" }

// Min and Max
val min = list.min()
val max = list.max()
val minBy = list.minBy { it }
val maxBy = list.maxBy { it }

// First and last with predicate
val first = list.first { it > 3 }  // 4
val last = list.last { it < 4 }  // 3
```

## Set Operations

### Set Algebra

```kotlin
val set1 = setOf(1, 2, 3, 4)
val set2 = setOf(3, 4, 5, 6)

// Union
val union = set1.union(set2)  // [1, 2, 3, 4, 5, 6]
val unionOperator = set1 or set2  // Same as union

// Intersection
val intersection = set1.intersect(set2)  // [3, 4]
val intersectionOperator = set1 and set2  // Same as intersection

// Difference
val difference = set1.subtract(set2)  // [1, 2]
val differenceOperator = set1 - set2  // Same as difference

// Symmetric difference
val symmetricDifference = set1.xor(set2)  // [1, 2, 5, 6]
```

### Set Membership

```kotlin
val set = setOf(1, 2, 3, 4, 5)

// Contains
val contains = set.contains(3)
val containsAll = set.containsAll(listOf(1, 2))

// Subset and superset
val subset = setOf(1, 2, 3)
val superset = setOf(1, 2, 3, 4, 5, 6)

val isSubset = subset.all { it in set }  // true
val isSuperset = set.all { it in superset }  // true
```

## Map Operations

### Accessing Map Entries

```kotlin
val map = mapOf("a" to 1, "b" to 2, "c" to 3)

// Get values
val value = map["a"]  // 1
val valueOrNull = map["d"]  // null
val valueOrDefault = map.getOrDefault("d", 0)  // 0

// Safe access with let
map["a"]?.let { println(it) }

// Get or put
val mutableMap = mutableMapOf("a" to 1)
val value2 = mutableMap.getOrPut("b") { 2 }  // Returns 2, puts it in map

// Get or compute
val value3 = mutableMap.getOrPut("c") {
    // Compute expensive value
    3
}
```

### Map Transformation

```kotlin
val map = mapOf("a" to 1, "b" to 2, "c" to 3)

// Map values
val doubled = map.mapValues { it.value * 2 }
val keyed = map.mapKeys { it.key.uppercase() }

// Map entries
val entries = map.map { "${it.key}=${it.value}" }

// Filter
val filtered = map.filter { it.value > 1 }
val filteredKeys = map.filterKeys { it != "b" }

// Mutable map operations
val mutableMap = mutableMapOf("a" to 1, "b" to 2, "c" to 3)
mutableMap.replaceAll { key, value -> value * 2 }
mutableMap.computeIfAbsent("d") { 4 }
mutableMap.compute("a") { key, value -> (value ?: 0) + 10 }

// Merge
val map1 = mapOf("a" to 1, "b" to 2)
val map2 = mapOf("b" to 3, "c" to 4)
val merged = map1 + map2  // {a=1, b=3, c=4}

// Merge with function
val mergedWithFunction = map1.toMutableMap().apply {
    map2.forEach { (key, value) ->
        merge(key, value) { existing, new -> existing + new }
    }
}
```

## Collection Transformation

### Mapping and Flattening

```kotlin
// Map transformation
val numbers = listOf(1, 2, 3, 4, 5)

// Basic map
val doubled = numbers.map { it * 2 }

// Map with index
val withIndex = numbers.mapIndexed { index, value -> 
    "$index: $value" 
}

// MapNotNull
val withNulls = listOf(1, null, 3, null, 5)
val nonNullDoubled = withNulls.mapNotNull { it?.times(2) }

// Flat map
val nested = listOf(listOf(1, 2), listOf(3, 4))
val flat = nested.flatMap { it }

// Flat map with transformation
val sentences = listOf("Hello World", "Foo Bar")
val words = sentences.flatMap { it.split(" ") }
```

### Grouping and Partitioning

```kotlin
// Group by
val words = listOf("apple", "banana", "avocado", "blueberry", "cherry")
val byFirstLetter = words.groupBy { it.first() }

// Group by with value transformation
val groupedAndTransformed = words.groupBy(
    { it.first() },
    { it.uppercase() }
)

// Partition
val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
val (evens, odds) = numbers.partition { it % 2 == 0 }

// Chunk
val chunked = numbers.chunked(3)

// Window
val windowed = numbers.windowed(3)
val windowedWithStep = numbers.windowed(3, step = 2)
```

### Zipping and Unzipping

```kotlin
// Zip
val names = listOf("Alice", "Bob", "Charlie")
val ages = listOf(30, 25, 35)

val zipped = names.zip(ages)
// [(Alice, 30), (Bob, 25), (Charlie, 35)]

// Zip with transformation
val combined = names.zip(ages) { name, age ->
    "$name is $age years old"
}

// Unzip
val (unzippedNames, unzippedAges) = zipped.unzip()

// Zip with next
val list = listOf(1, 2, 3, 4, 5)
val pairs = list.zipWithNext()
// [(1, 2), (2, 3), (3, 4), (4, 5)]

// Zip with next and transformation
val differences = list.zipWithNext { a, b -> b - a }
// [1, 1, 1, 1]
```

## Sequences

### What are Sequences?

```kotlin
// Sequences provide lazy evaluation
val sequence = sequence {
    for (i in 1..10) {
        delay(100)  // Simulate computation
        yield(i)
    }
}

// Collecting sequences
fun sequenceExample() = runBlocking {
    sequence.collect { println(it) }
}

// Sequence from iterable
val list = listOf(1, 2, 3, 4, 5)
val seq = list.asSequence()

// Sequence operations
val result = list.asSequence()
    .map { it * 2 }
    .filter { it > 4 }
    .toList()
```

### Lazy Evaluation

```kotlin
// Eager evaluation (List)
val eagerList = (1..1000000).toList()
    .map { it * it }
    .filter { it % 2 == 0 }
    .take(5)
// Computes all operations on all elements

// Lazy evaluation (Sequence)
val lazySequence = (1..1000000).asSequence()
    .map { it * it }
    .filter { it % 2 == 0 }
    .take(5)
    .toList()
// Only computes for first 5 matching elements

// Performance comparison
fun comparePerformance() {
    val list = (1..1000000).toList()
    
    // List operations
    val listTime = measureTimeMillis {
        list.map { it * it }
            .filter { it % 2 == 0 }
            .take(5)
    }
    
    // Sequence operations
    val sequenceTime = measureTimeMillis {
        list.asSequence()
            .map { it * it }
            .filter { it % 2 == 0 }
            .take(5)
            .toList()
    }
    
    println("List: $listTime ms")
    println("Sequence: $sequenceTime ms")
}
```

### Sequence Operations

```kotlin
// Sequence transformation
val sequence = (1..100).asSequence()

// Map and filter
val result = sequence
    .map { it * it }
    .filter { it % 2 == 0 }
    .take(10)
    .toList()

// Reduce and fold
val sum = sequence.reduce { acc, i -> acc + i }
val product = sequence.fold(1) { acc, i -> acc * i }

// Take and drop
val first10 = sequence.take(10).toList()
val skip10 = sequence.drop(10).toList()

// Chunk and window
val chunked = sequence.chunked(10).toList()
val windowed = sequence.windowed(10).toList()
```

## Collection Processing

### Advanced Processing

```kotlin
// Complex data processing
data class Order(
    val id: Int,
    val customer: String,
    val items: List<String>,
    val total: Double
)

val orders = listOf(
    Order(1, "Alice", listOf("Book", "Pen"), 25.99),
    Order(2, "Bob", listOf("Laptop"), 999.99),
    Order(3, "Alice", listOf("Mouse", "Keyboard"), 49.99),
    Order(4, "Charlie", listOf("Monitor"), 299.99)
)

// Group by customer
val ordersByCustomer = orders.groupBy { it.customer }

// Calculate total per customer
val totalPerCustomer = ordersByCustomer.mapValues { 
    it.value.sumOf { order -> order.total }
}

// Find most expensive order
val mostExpensive = orders.maxByOrNull { it.total }

// Items bought by each customer
val itemsByCustomer = ordersByCustomer.mapValues { 
    it.value.flatMap { order -> order.items }.distinct()
}
```

### Performance Optimization

```kotlin
// Use sequence for large datasets
fun processLargeDataset() {
    val largeList = (1..1000000).toList()
    
    // Bad: Creates intermediate lists
    val badResult = largeList
        .map { it * it }
        .filter { it % 2 == 0 }
        .take(5)
    
    // Good: Uses sequence for lazy evaluation
    val goodResult = largeList.asSequence()
        .map { it * it }
        .filter { it % 2 == 0 }
        .take(5)
        .toList()
}

// Use appropriate collection type
fun chooseCollectionType() {
    // Use List for ordered, indexed access
    val list = listOf(1, 2, 3)
    
    // Use Set for unique elements
    val set = setOf(1, 2, 3)
    
    // Use Map for key-value pairs
    val map = mapOf(1 to "one", 2 to "two")
    
    // Use Mutable collections when needed
    val mutableList = mutableListOf(1, 2, 3)
    mutableList.add(4)
}
```

## Common Patterns

### Collection Patterns

```kotlin
// Builder pattern
fun buildList(): List<Int> {
    return buildList {
        for (i in 1..10) {
            if (i % 2 == 0) {
                add(i)
            }
        }
    }
}

// Destructuring
val pairs = listOf("a" to 1, "b" to 2, "c" to 3)
for ((key, value) in pairs) {
    println("$key: $value")
}

// Scope functions
val result = mutableListOf<Int>().apply {
    addAll(listOf(1, 2, 3))
    add(4)
    remove(2)
}.toList()

// Collection conversion
val stringList = listOf(1, 2, 3).map { it.toString() }
val intSet = listOf(1, 1, 2, 2, 3).toSet()
val intMap = listOf(1, 2, 3).associate { it to it * it }
```

### Null Safety with Collections

```kotlin
// Nullable collections
val nullableList: List<Int?> = listOf(1, null, 3, null, 5)
val nonNullList: List<Int> = nullableList.filterNotNull()

// Collection of nullable elements
val strings: List<String?> = listOf("hello", null, "world")
val nonNullStrings = strings.filterNotNull()

// Safe operations
val list: List<Int>? = null
val size = list?.size ?: 0
val first = list?.firstOrNull()

// Collection with default values
val map = mapOf("a" to 1)
val value = map["a"] ?: 0
```

## Summary

Kotlin collections provide powerful and expressive data manipulation:

- **Immutable by Default**: Prefer immutable collections for thread safety
- **Rich API**: Extensive operations for filtering, mapping, and aggregation
- **Sequences**: Lazy evaluation for performance with large datasets
- **Extension Functions**: Convenient operations on all collection types
- **Type Safety**: Compile-time type checking for collection operations
- **Interoperability**: Seamless conversion between Java and Kotlin collections

Mastering collections is essential for effective Kotlin programming.
