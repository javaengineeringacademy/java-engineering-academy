# Collections Utilities in Java Collections Framework

## 1. Introduction

The `Collections` class provides static utility methods for operating on collections. These methods include sorting, reversing, shuffling, and creating unmodifiable or synchronized wrappers. The `Collections` class is a member of the Java Collections Framework and provides convenient methods for common collection operations.

```java
List<Integer> numbers = new ArrayList<>(List.of(3, 1, 4, 1, 5, 9));
Collections.sort(numbers);
Collections.reverse(numbers);
Collections.shuffle(numbers);
```

## 2. Learning Objectives

- Understand the `Collections` utility class and its methods
- Learn about unmodifiable and synchronized wrappers
- Master sorting, reversing, and shuffling operations
- Understand frequency and disjoint operations
- Learn about empty and singleton factory methods

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of collections (List, Set, Map)
- Familiarity with generics
- Basic understanding of thread safety (recommended)

## 4. Why This Concept Exists

The `Collections` class provides convenient utility methods that are commonly needed when working with collections. Instead of implementing sorting, reversing, or shuffling algorithms manually, developers can use these optimized, tested methods. They also provide wrappers for thread safety and immutability.

## 5. Problem Statement

Common collection operations like sorting, reversing, and shuffling require manual implementation without utility classes. Additionally, creating thread-safe or unmodifiable collections requires boilerplate code. The `Collections` class solves these problems by providing ready-made, optimized methods.

## 6. Theory

### Key Methods

#### Unmodifiable Wrappers
- `Collections.unmodifiableList(List<? extends T> list)`: Returns unmodifiable view
- `Collections.unmodifiableSet(Set<? extends T> set)`: Returns unmodifiable view
- `Collections.unmodifiableMap(Map<? extends K, ? extends V> map)`: Returns unmodifiable view

#### Synchronized Wrappers
- `Collections.synchronizedList(List<T> list)`: Returns synchronized wrapper
- `Collections.synchronizedSet(Set<T> set)`: Returns synchronized wrapper
- `Collections.synchronizedMap(Map<K, V> map)`: Returns synchronized wrapper

#### Sorting and Ordering
- `Collections.sort(List<T> list)`: Sorts list in natural order
- `Collections.sort(List<T> list, Comparator<? super T> c)`: Sorts with comparator
- `Collections.reverse(List<?> list)`: Reverses order
- `Collections.shuffle(List<?> list)`: Randomizes order
- `Collections.shuffle(List<?> list, Random rnd)`: Randomizes with seed

#### Searching
- `Collections.binarySearch(List<? extends Comparable<? super T>> list, T key)`: Binary search
- `Collections.binarySearch(List<? extends T> list, T key, Comparator<? super T> c)`: Binary search with comparator

#### Frequency and Disjoint
- `Collections.frequency(Collection<?> c, Object o)`: Counts occurrences
- `Collections.disjoint(Collection<?> c1, Collection<?> c2)`: Checks if collections have no common elements

#### Empty and Singleton
- `Collections.emptyList()`: Returns empty immutable list
- `Collections.emptySet()`: Returns empty immutable set
- `Collections.emptyMap()`: Returns empty immutable map
- `Collections.singletonList(T o)`: Returns immutable list with one element
- `Collections.singleton(T o)`: Returns immutable set with one element
- `Collections.singletonMap(K key, V value)`: Returns immutable map with one entry

## 7. Internal Working

### Unmodifiable Wrapper Implementation
```java
// Simplified unmodifiable list wrapper
private static class UnmodifiableList<E> implements List<E> {
    private final List<? extends E> list;
    
    UnmodifiableList(List<? extends E> list) {
        this.list = list;
    }
    
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }
    
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }
    
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }
    
    public E get(int index) {
        return list.get(index);  // Delegate to original
    }
    
    public int size() {
        return list.size();  // Delegate to original
    }
}
```

### Synchronized Wrapper Implementation
```java
// Simplified synchronized list wrapper
private static class SynchronizedList<E> implements List<E> {
    private final List<E> list;
    
    SynchronizedList(List<E> list) {
        this.list = list;
    }
    
    public synchronized boolean add(E e) {
        return list.add(e);
    }
    
    public synchronized E get(int index) {
        return list.get(index);
    }
    
    public synchronized E set(int index, E element) {
        return list.set(index, element);
    }
    
    public synchronized Iterator<E> iterator() {
        return list.iterator();  // Not synchronized!
    }
}
```

### Sort Implementation
```java
// Simplified sort using TimSort
public static <T extends Comparable<? super T>> void sort(List<T> list) {
    Object[] a = list.toArray();
    Arrays.sort(a);  // Uses TimSort algorithm
    ListIterator<T> i = list.listIterator();
    for (int j = 0; j < a.length; j++) {
        i.next();
        i.set((T) a[j]);
    }
}
```

## 8. Syntax

```java
// Import
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;

// Unmodifiable wrappers
List<String> unmodifiableList = Collections.unmodifiableList(list);
Set<Integer> unmodifiableSet = Collections.unmodifiableSet(set);
Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(map);

// Synchronized wrappers
List<String> synchronizedList = Collections.synchronizedList(list);
Set<Integer> synchronizedSet = Collections.synchronizedSet(set);
Map<String, Integer> synchronizedMap = Collections.synchronizedMap(map);

// Sorting and ordering
Collections.sort(list);
Collections.sort(list, Comparator.reverseOrder());
Collections.reverse(list);
Collections.shuffle(list);
Collections.shuffle(list, new Random());

// Searching
int index = Collections.binarySearch(sortedList, key);
int index = Collections.binarySearch(sortedList, key, comparator);

// Frequency and disjoint
int count = Collections.frequency(collection, element);
boolean disjoint = Collections.disjoint(collection1, collection2);

// Empty and singleton
List<String> emptyList = Collections.emptyList();
Set<Integer> emptySet = Collections.emptySet();
Map<String, Integer> emptyMap = Collections.emptyMap();
List<String> singleElementList = Collections.singletonList("element");
Set<Integer> singleElementSet = Collections.singleton(42);
Map<String, Integer> singleElementMap = Collections.singletonMap("key", 1);
```

## 9. Easy Example

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsBasic {
    public static void main(String[] args) {
        // Create a list
        List<Integer> numbers = new ArrayList<>();
        numbers.add(5);
        numbers.add(2);
        numbers.add(8);
        numbers.add(1);
        numbers.add(9);
        
        System.out.println("Original: " + numbers);
        
        // Sort
        Collections.sort(numbers);
        System.out.println("Sorted: " + numbers);
        
        // Reverse
        Collections.reverse(numbers);
        System.out.println("Reversed: " + numbers);
        
        // Shuffle
        Collections.shuffle(numbers);
        System.out.println("Shuffled: " + numbers);
        
        // Frequency
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Alice");
        names.add("Charlie");
        names.add("Alice");
        
        int aliceCount = Collections.frequency(names, "Alice");
        System.out.println("Alice count: " + aliceCount);  // 3
        
        // Disjoint
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        
        List<Integer> list2 = new ArrayList<>();
        list2.add(4);
        list2.add(5);
        list2.add(6);
        
        boolean disjoint = Collections.disjoint(list1, list2);
        System.out.println("Lists are disjoint: " + disjoint);  // true
    }
}
```

## 10. Medium Example

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

public class CollectionsAdvanced {
    public static void main(String[] args) {
        // Unmodifiable list
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        
        List<String> unmodifiableNames = Collections.unmodifiableList(names);
        System.out.println("Unmodifiable: " + unmodifiableNames);
        
        try {
            unmodifiableNames.add("David");  // Throws UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable list");
        }
        
        // Synchronized list
        List<Integer> synchronizedNumbers = Collections.synchronizedList(new ArrayList<>());
        synchronizedNumbers.add(1);
        synchronizedNumbers.add(2);
        synchronizedNumbers.add(3);
        
        System.out.println("Synchronized: " + synchronizedNumbers);
        
        // Sort with comparator
        List<String> cities = new ArrayList<>();
        cities.add("New York");
        cities.add("Los Angeles");
        cities.add("Chicago");
        cities.add("Houston");
        
        // Sort by length
        cities.sort(Comparator.comparingInt(String::length));
        System.out.println("Sorted by length: " + cities);
        
        // Sort reverse
        cities.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println("Sorted by length (reverse): " + cities);
        
        // Binary search (list must be sorted)
        List<Integer> sortedList = new ArrayList<>();
        sortedList.add(10);
        sortedList.add(20);
        sortedList.add(30);
        sortedList.add(40);
        sortedList.add(50);
        
        int index = Collections.binarySearch(sortedList, 30);
        System.out.println("Index of 30: " + index);  // 2
        
        // Empty and singleton collections
        List<String> emptyList = Collections.emptyList();
        System.out.println("Empty list: " + emptyList);
        System.out.println("Empty list size: " + emptyList.size());  // 0
        
        List<String> singleElement = Collections.singletonList("Only");
        System.out.println("Singleton list: " + singleElement);
        
        Map<String, Integer> singleEntry = Collections.singletonMap("key", 1);
        System.out.println("Singleton map: " + singleEntry);
    }
}
```

## 11. Hard Example

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class DataProcessor {
    private final List<String> data;
    private final Map<String, Integer> wordCount;
    
    public DataProcessor() {
        this.data = new ArrayList<>();
        this.wordCount = new HashMap<>();
    }
    
    public void loadData(String[] items) {
        for (String item : items) {
            data.add(item);
        }
    }
    
    public void processAndSort() {
        System.out.println("=== Processing Data ===");
        
        // Create unmodifiable view for safe processing
        List<String> immutableData = Collections.unmodifiableList(data);
        
        // Sort by natural order
        List<String> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData);
        System.out.println("Sorted: " + sortedData);
        
        // Sort by length
        sortedData.sort(Comparator.comparingInt(String::length));
        System.out.println("Sorted by length: " + sortedData);
        
        // Reverse
        Collections.reverse(sortedData);
        System.out.println("Reversed: " + sortedData);
        
        // Shuffle
        List<String> shuffledData = new ArrayList<>(data);
        Collections.shuffle(shuffledData);
        System.out.println("Shuffled: " + shuffledData);
    }
    
    public void countWords() {
        System.out.println("\n=== Word Count ===");
        
        for (String word : data) {
            wordCount.merge(word, 1, Integer::sum);
        }
        
        // Sort by frequency
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(wordCount.entrySet());
        sortedEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    
    public void findDuplicates() {
        System.out.println("\n=== Finding Duplicates ===");
        
        Set<String> unique = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        
        for (String item : data) {
            if (!unique.add(item)) {
                duplicates.add(item);
            }
        }
        
        if (duplicates.isEmpty()) {
            System.out.println("No duplicates found");
        } else {
            System.out.println("Duplicates: " + duplicates);
        }
    }
    
    public void analyzeCollections() {
        System.out.println("\n=== Collection Analysis ===");
        
        // Frequency analysis
        List<String> uniqueElements = new ArrayList<>(new HashSet<>(data));
        
        for (String element : uniqueElements) {
            int frequency = Collections.frequency(data, element);
            System.out.println(element + " appears " + frequency + " times");
        }
        
        // Disjoint check
        List<String> subset1 = new ArrayList<>();
        subset1.add(data.get(0));
        subset1.add(data.get(1));
        
        List<String> subset2 = new ArrayList<>();
        subset2.add(data.get(2));
        subset2.add(data.get(3));
        
        boolean disjoint = Collections.disjoint(subset1, subset2);
        System.out.println("First two subsets are disjoint: " + disjoint);
    }
    
    public static void main(String[] args) {
        DataProcessor processor = new DataProcessor();
        
        String[] data = {
            "apple", "banana", "apple", "cherry", "banana", "date",
            "elderberry", "fig", "grape", "apple", "banana", "cherry"
        };
        
        processor.loadData(data);
        processor.processAndSort();
        processor.countWords();
        processor.findDuplicates();
        processor.analyzeCollections();
    }
}
```

## 12. Enterprise Example

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ReportGenerator {
    private static final Logger LOGGER = Logger.getLogger(ReportGenerator.class.getName());
    private final List<ReportEntry> reportData;
    private final Map<String, Integer> categoryCounts;
    
    public ReportGenerator() {
        this.reportData = new CopyOnWriteArrayList<>();
        this.categoryCounts = new HashMap<>();
    }
    
    public void addEntry(String category, String description, double amount) {
        reportData.add(new ReportEntry(category, description, amount));
        categoryCounts.merge(category, 1, Integer::sum);
    }
    
    public void generateReport() {
        LOGGER.info("Generating report...");
        
        // Create unmodifiable view for safe processing
        List<ReportEntry> immutableData = Collections.unmodifiableList(reportData);
        
        // Sort by amount
        List<ReportEntry> sortedByAmount = new ArrayList<>(immutableData);
        Collections.sort(sortedByAmount, Comparator.comparingDouble(ReportEntry::getAmount).reversed());
        
        System.out.println("\n=== Report by Amount (Descending) ===");
        for (ReportEntry entry : sortedByAmount) {
            System.out.printf("%s: $%.2f - %s%n", 
                entry.getCategory(), entry.getAmount(), entry.getDescription());
        }
        
        // Sort by category then description
        List<ReportEntry> sortedByCategory = new ArrayList<>(immutableData);
        sortedByCategory.sort(Comparator
            .comparing(ReportEntry::getCategory)
            .thenComparing(ReportEntry::getDescription));
        
        System.out.println("\n=== Report by Category ===");
        String currentCategory = "";
        for (ReportEntry entry : sortedByCategory) {
            if (!entry.getCategory().equals(currentCategory)) {
                currentCategory = entry.getCategory();
                System.out.println("\n" + currentCategory + ":");
            }
            System.out.printf("  %s: $%.2f%n", entry.getDescription(), entry.getAmount());
        }
        
        // Category summary
        System.out.println("\n=== Category Summary ===");
        List<Map.Entry<String, Integer>> sortedCategories = new ArrayList<>(categoryCounts.entrySet());
        sortedCategories.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        
        for (Map.Entry<String, Integer> entry : sortedCategories) {
            System.out.printf("%s: %d entries%n", entry.getKey(), entry.getValue());
        }
        
        // Statistics
        System.out.println("\n=== Statistics ===");
        System.out.println("Total entries: " + reportData.size());
        System.out.println("Unique categories: " + categoryCounts.size());
        
        // Find min and max amounts
        if (!reportData.isEmpty()) {
            double maxAmount = Collections.max(reportData, Comparator.comparingDouble(ReportEntry::getAmount)).getAmount();
            double minAmount = Collections.min(reportData, Comparator.comparingDouble(ReportEntry::getAmount)).getAmount();
            System.out.printf("Max amount: $%.2f%n", maxAmount);
            System.out.printf("Min amount: $%.2f%n", minAmount);
        }
    }
    
    private static class ReportEntry {
        private final String category;
        private final String description;
        private final double amount;
        
        public ReportEntry(String category, String description, double amount) {
            this.category = category;
            this.description = description;
            this.amount = amount;
        }
        
        public String getCategory() { return category; }
        public String getDescription() { return description; }
        public double getAmount() { return amount; }
    }
    
    public static void main(String[] args) {
        ReportGenerator generator = new ReportGenerator();
        
        // Add sample data
        generator.addEntry("Marketing", "Social Media Campaign", 5000.00);
        generator.addEntry("Marketing", "Email Newsletter", 1500.00);
        generator.addEntry("Engineering", "Server Maintenance", 3000.00);
        generator.addEntry("Engineering", "Software License", 2500.00);
        generator.addEntry("Sales", "Client Meeting", 800.00);
        generator.addEntry("Sales", "Product Demo", 1200.00);
        generator.addEntry("Marketing", "SEO Optimization", 2000.00);
        generator.addEntry("Engineering", "Cloud Hosting", 4000.00);
        
        generator.generateReport();
    }
}
```

## 13. Performance

### Time Complexity
- **Collections.sort()**: O(n log n) using TimSort
- **Collections.reverse()**: O(n)
- **Collections.shuffle()**: O(n) using Fisher-Yates shuffle
- **Collections.binarySearch()**: O(log n) for sorted lists
- **Collections.frequency()**: O(n)
- **Collections.disjoint()**: O(min(m, n))

### Memory Usage
- **Unmodifiable wrappers**: O(1) - just references
- **Synchronized wrappers**: O(1) - just references
- **Sort operations**: O(n) for temporary array
- **Empty/Singleton**: O(1) - shared instances

### Comparison
| Operation | Time | Memory | Thread-safe |
|-----------|------|--------|-------------|
| sort() | O(n log n) | O(n) | No |
| reverse() | O(n) | O(1) | No |
| shuffle() | O(n) | O(1) | No |
| binarySearch() | O(log n) | O(1) | No |
| frequency() | O(n) | O(1) | No |
| disjoint() | O(min(m,n)) | O(1) | No |
| unmodifiableList() | O(1) | O(1) | No |
| synchronizedList() | O(1) | O(1) | Yes |

## 14. Best Practices

```java
// 1. Use unmodifiable wrappers for immutable views
List<String> immutableList = Collections.unmodifiableList(mutableList);

// 2. Use synchronized wrappers for thread safety
List<String> synchronizedList = Collections.synchronizedList(new ArrayList<>());

// 3. Sort with Comparator for custom ordering
list.sort(Comparator.comparingInt(String::length));

// 4. Use binarySearch on sorted lists
Collections.sort(list);
int index = Collections.binarySearch(list, key);

// 5. Use frequency for counting occurrences
int count = Collections.frequency(list, element);

// 6. Use disjoint to check for common elements
boolean hasCommon = !Collections.disjoint(set1, set2);

// 7. Use empty/singleton for immutable singletons
List<String> empty = Collections.emptyList();
List<String> single = Collections.singletonList("only");

// 8. Synchronize iteration on synchronized collections
synchronized (synchronizedList) {
    Iterator<String> iterator = synchronizedList.iterator();
    while (iterator.hasNext()) {
        // Safe iteration
    }
}

// 9. Use List.sort() instead of Collections.sort()
list.sort(Comparator.naturalOrder());

// 10. Prefer List.of() for immutable lists (Java 9+)
List<String> immutable = List.of("a", "b", "c");
```

## 15. Common Mistakes

```java
// Mistake 1: Modifying unmodifiable wrapper
// Bad
List<String> unmodifiable = Collections.unmodifiableList(list);
unmodifiable.add("new");  // UnsupportedOperationException
// Good
List<String> modifiable = new ArrayList<>(unmodifiable);
modifiable.add("new");

// Mistake 2: Not synchronizing iteration on synchronized collection
// Bad
List<String> synchronizedList = Collections.synchronizedList(new ArrayList<>());
Iterator<String> iterator = synchronizedList.iterator();
while (iterator.hasNext()) {
    // May throw ConcurrentModificationException
}
// Good
synchronized (synchronizedList) {
    Iterator<String> iterator = synchronizedList.iterator();
    while (iterator.hasNext()) {
        // Safe
    }
}

// Mistake 3: Using binarySearch on unsorted list
// Bad
List<Integer> unsorted = new ArrayList<>();
unsorted.add(3);
unsorted.add(1);
unsorted.add(4);
int index = Collections.binarySearch(unsorted, 1);  // Undefined behavior
// Good
Collections.sort(unsorted);
int index = Collections.binarySearch(unsorted, 1);  // Correct

// Mistake 4: Assuming unmodifiable wrapper prevents modification of original
// Bad
List<String> original = new ArrayList<>();
original.add("a");
List<String> unmodifiable = Collections.unmodifiableList(original);
original.add("b");  // Unmodifiable list also changes!
// Good
List<String> unmodifiable = Collections.unmodifiableList(new ArrayList<>(original));

// Mistake 5: Using Collections.sort() instead of List.sort()
// Bad
Collections.sort(list);  // Older API
// Good
list.sort(Comparator.naturalOrder());  // Modern API
```

## 16. Pitfalls

### Thread Safety Issues
- **Synchronized wrappers**: Individual methods are synchronized, but compound operations are not
- **Iteration**: Must synchronize externally when iterating over synchronized collections
- **Performance**: Synchronization overhead in high-concurrency scenarios

### Immutability Issues
- **Unmodifiable wrappers**: Only prevent modification through wrapper; original can still be modified
- **Shared instances**: Empty/singleton collections are shared; must not be modified
- **Defensive copies**: Create copies to ensure true immutability

### Performance Concerns
- **Sort operations**: Create temporary arrays; may be expensive for large collections
- **Frequency/disjoint**: Linear time; may be slow for large collections
- **Wrapper overhead**: Slight performance overhead for wrappers

### API Limitations
- **No primitive support**: Must use wrapper classes for primitives
- **Limited sorting**: Only works with Comparable or Comparator
- **No parallel sorting**: Collections.sort() is single-threaded

## 17. Interview Questions

### Q1: What is the difference between Collections.unmodifiableList() and List.of()?
**Answer**: `Collections.unmodifiableList()` creates a wrapper that throws `UnsupportedOperationException` on modification attempts. `List.of()` (Java 9+) creates a true immutable list that also prevents null elements. `List.of()` is more efficient and preferred in modern Java.

### Q2: When would you use Collections.synchronizedList() over CopyOnWriteArrayList?
**Answer**: Use `synchronizedList()` when you need a simple thread-safe list with moderate read/write operations. Use `CopyOnWriteArrayList` when you have read-heavy workloads with rare writes and need safe iteration without external synchronization.

### Q3: How does Collections.binarySearch() work?
**Answer**: It performs binary search on a sorted list, returning the index of the key if found, or the negative insertion point if not found. The list must be sorted; otherwise, results are undefined. Time complexity is O(log n).

### Q4: What is the difference between Collections.sort() and List.sort()?
**Answer**: `Collections.sort()` is a static method that sorts the list in place. `List.sort()` is an instance method (Java 8+) that also sorts in place but accepts a Comparator. `List.sort()` is the modern, preferred approach.

### Q5: How do you safely iterate over a synchronized collection?
**Answer**: Synchronize on the collection itself: `synchronized (collection) { Iterator<T> i = collection.iterator(); while (i.hasNext()) { ... } }`. Without this, you may get `ConcurrentModificationException`.

### Q6: What are the performance implications of Collections utilities?
**Answer**: Most utilities are O(n) or O(n log n) time. `binarySearch()` is O(log n) on sorted lists. Wrappers have minimal overhead. Frequency and disjoint operations are linear and may be slow on large collections.

### Q7: When would you use Collections.emptyXxx() or singletonXxx()?
**Answer**: Use `emptyXxx()` to return empty immutable collections from methods. Use `singletonXxx()` when you need a collection with exactly one element. Both return shared, immutable instances that are thread-safe and memory-efficient.

## 18. Exercises

### Exercise 1: Sorting and Searching
Create a list of integers, sort them using `Collections.sort()`, and then use `Collections.binarySearch()` to find specific elements. Handle the case where the element is not found.

### Exercise 2: Unmodifiable Collections
Create a method that returns an unmodifiable view of a list. Test that modification attempts throw `UnsupportedOperationException`. Create a copy that can be modified.

### Exercise 3: Thread-Safe Collections
Implement a thread-safe counter using `Collections.synchronizedList()`. Test with multiple threads incrementing and decrementing the counter.

### Exercise 4: Data Analysis
Given a list of strings, use `Collections.frequency()` to count occurrences of each unique string. Sort the results by frequency and display them.

## 19. Summary

- `Collections` class provides static utility methods for collection operations
- **Unmodifiable wrappers**: `unmodifiableList()`, `unmodifiableSet()`, `unmodifiableMap()` - prevent modification
- **Synchronized wrappers**: `synchronizedList()`, `synchronizedSet()`, `synchronizedMap()` - thread-safe operations
- **Sorting**: `sort()` using TimSort, O(n log n)
- **Ordering**: `reverse()`, `shuffle()` for reordering
- **Searching**: `binarySearch()` for sorted lists, O(log n)
- **Analysis**: `frequency()`, `disjoint()` for collection analysis
- **Factory methods**: `emptyList()`, `singletonList()` for immutable collections
- **Thread safety**: Synchronized wrappers require external synchronization for compound operations
- **Immutability**: Unmodifiable wrappers only prevent modification through wrapper

## 20. References

### Official Documentation
- [Java Collections Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collections.html)
- [Java List.sort() Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html#sort(java.util.Comparator))
- [Java Comparable Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Comparable.html)

### Books
- *Effective Java* by Joshua Bloch
- *Java: The Complete Reference* by Herbert Schildt
- *Java Concurrency in Practice* by Brian Goetz

### Online Resources
- [Baeldung - Collections Utility Methods](https://www.baeldung.com/java-collections-utilities)
- [GeeksforGeeks - Collections class in Java](https://www.geeksforgeeks.org/collections-class-java/)
- [Oracle - Collections Framework](https://docs.oracle.com/en/java/javase/21/docs/technotes/guides/collections/)

### Related Topics
- [List Interface](../05-list/README.md)
- [Set Interface](../06-set/README.md)
- [Map Interface](../07-map/README.md)
- [Comparable and Comparator](../17-comparable-comparator/README.md)
