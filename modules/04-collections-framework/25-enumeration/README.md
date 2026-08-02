# Enumeration in Java Collections Framework

## 1. Introduction

`Enumeration` is a legacy interface in Java that provides a way to traverse elements in a collection one at a time. Introduced in JDK 1.0, it was the original traversal mechanism before `Iterator`. `Enumeration` is simpler than `Iterator` but lacks some features like removal and bidirectional traversal.

```java
Enumeration<String> enumeration = Collections.enumeration(list);
while (enumeration.hasMoreElements()) {
    String element = enumeration.nextElement();
    System.out.println(element);
}
```

## 2. Learning Objectives

- Understand the `Enumeration` interface and its legacy status
- Learn about `StringTokenizer` and its use cases
- Understand `Hashtable.elements()` and `Hashtable.keys()`
- Compare `Enumeration` with `Iterator`
- Recognize when to use `Enumeration` in modern applications

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of collections (List, Set, Map)
- Familiarity with legacy Java APIs
- Knowledge of Iterator interface (recommended)

## 4. Why This Concept Exists

`Enumeration` was created in JDK 1.0 as the standard way to traverse collections. It provided a simple interface for sequential access to elements. With JDK 1.2, `Iterator` was introduced as a more powerful alternative, making `Enumeration` largely obsolete. It still exists for backward compatibility and in some legacy APIs.

## 5. Problem Statement

Before Java 1.2, there was no standard way to traverse collections. `Enumeration` provided a simple interface for sequential access, but it lacked important features:
- No removal of elements during traversal
- No bidirectional traversal
- No `forEachRemaining()` method
- Only available for legacy classes like `Vector`, `Hashtable`, `StringTokenizer`

## 6. Theory

### Enumeration Interface Methods
- `boolean hasMoreElements()`: Returns true if more elements exist
- `E nextElement()`: Returns the next element

### StringTokenizer
- Legacy class for tokenizing strings
- Uses `Enumeration<String>` for traversal
- Replaced by `String.split()` and `Pattern` class

### Hashtable.elements() and keys()
- `elements()`: Returns `Enumeration<V>` of values
- `keys()`: Returns `Enumeration<K>` of keys
- Legacy methods for traversing `Hashtable`

### Enumeration vs Iterator
| Feature | Enumeration | Iterator |
|---------|-------------|----------|
| Methods | `hasMoreElements()`, `nextElement()` | `hasNext()`, `next()`, `remove()` |
| Removal | Not supported | Supported |
| Bidirectional | No | ListIterator only |
| Legacy | Yes (JDK 1.0) | No (JDK 1.2) |
| Modern usage | Rare | Common |

## 7. Internal Working

### Enumeration Implementation in Vector
```java
// Simplified Vector enumeration
class Vector<E> {
    private E[] elementData;
    private int elementCount;
    
    public Enumeration<E> elements() {
        return new Enumeration<E>() {
            int count = 0;
            
            public boolean hasMoreElements() {
                return count < elementCount;
            }
            
            public E nextElement() {
                if (count < elementCount) {
                    return (E) elementData[count++];
                }
                throw new NoSuchElementException("Vector Enumeration");
            }
        };
    }
}
```

### Hashtable Enumeration
```java
// Simplified Hashtable enumeration
class Hashtable<K, V> {
    private Entry<K, V>[] table;
    
    public Enumeration<V> elements() {
        return new Enumeration<V>() {
            int index = 0;
            Entry<K, V> entry = null;
            
            public boolean hasMoreElements() {
                if (entry != null) return true;
                while (index < table.length) {
                    if (table[index] != null) {
                        entry = table[index];
                        return true;
                    }
                    index++;
                }
                return false;
            }
            
            public V nextElement() {
                if (hasMoreElements()) {
                    V value = entry.value;
                    entry = entry.next;
                    if (entry == null) index++;
                    return value;
                }
                throw new NoSuchElementException("Hashtable Enumeration");
            }
        };
    }
}
```

## 8. Syntax

```java
// Import
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Collections;

// Creating Enumeration from Vector
Vector<String> vector = new Vector<>();
Enumeration<String> enumeration = vector.elements();

// Creating Enumeration from Hashtable
Hashtable<String, Integer> hashtable = new Hashtable<>();
Enumeration<String> keys = hashtable.keys();
Enumeration<Integer> values = hashtable.elements();

// Creating Enumeration from List (using Collections utility)
Enumeration<String> listEnum = Collections.enumeration(list);

// Using StringTokenizer
StringTokenizer tokenizer = new StringTokenizer("Hello,World,Java");
while (tokenizer.hasMoreTokens()) {
    String token = tokenizer.nextToken();
    System.out.println(token);
}

// Enumeration methods
boolean hasMore = enumeration.hasMoreElements();
E element = enumeration.nextElement();
```

## 9. Easy Example

```java
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

public class EnumerationBasic {
    public static void main(String[] args) {
        // Vector enumeration
        Vector<String> fruits = new Vector<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        
        Enumeration<String> fruitEnum = fruits.elements();
        System.out.println("Fruits:");
        while (fruitEnum.hasMoreElements()) {
            String fruit = fruitEnum.nextElement();
            System.out.println("  " + fruit);
        }
        
        // Hashtable enumeration
        Hashtable<String, Integer> ages = new Hashtable<>();
        ages.put("Alice", 25);
        ages.put("Bob", 30);
        ages.put("Charlie", 35);
        
        System.out.println("\nKeys:");
        Enumeration<String> keyEnum = ages.keys();
        while (keyEnum.hasMoreElements()) {
            String key = keyEnum.nextElement();
            System.out.println("  " + key);
        }
        
        System.out.println("\nValues:");
        Enumeration<Integer> valueEnum = ages.elements();
        while (valueEnum.hasMoreElements()) {
            Integer value = valueEnum.nextElement();
            System.out.println("  " + value);
        }
        
        // StringTokenizer
        StringTokenizer tokenizer = new StringTokenizer("Java,Collections,Framework");
        System.out.println("\nTokens:");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            System.out.println("  " + token);
        }
    }
}
```

## 10. Medium Example

```java
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class EnumerationAdvanced {
    public static void main(String[] args) {
        // Converting List to Enumeration
        List<String> names = new ArrayList<>();
        names.add("David");
        names.add("Eve");
        names.add("Frank");
        
        Enumeration<String> nameEnum = Collections.enumeration(names);
        System.out.println("Names from List:");
        while (nameEnum.hasMoreElements()) {
            System.out.println("  " + nameEnum.nextElement());
        }
        
        // StringTokenizer with delimiters
        String csv = "John,Doe,30,Engineer";
        StringTokenizer csvTokenizer = new StringTokenizer(csv, ",");
        System.out.println("\nCSV parsing:");
        while (csvTokenizer.hasMoreTokens()) {
            System.out.println("  " + csvTokenizer.nextToken());
        }
        
        // StringTokenizer with return delimiters
        String sentence = "Hello, world! How are you?";
        StringTokenizer wordTokenizer = new StringTokenizer(sentence, " ,?!");
        System.out.println("\nWord parsing:");
        while (wordTokenizer.hasMoreTokens()) {
            System.out.println("  " + wordTokenizer.nextToken());
        }
        
        // Nested Hashtable enumeration
        Hashtable<String, Vector<Integer>> studentScores = new Hashtable<>();
        Vector<Integer> aliceScores = new Vector<>();
        aliceScores.add(95);
        aliceScores.add(87);
        aliceScores.add(92);
        studentScores.put("Alice", aliceScores);
        
        Vector<Integer> bobScores = new Vector<>();
        bobScores.add(88);
        bobScores.add(91);
        bobScores.add(85);
        studentScores.put("Bob", bobScores);
        
        System.out.println("\nStudent scores:");
        Enumeration<String> studentEnum = studentScores.keys();
        while (studentEnum.hasMoreElements()) {
            String student = studentEnum.nextElement();
            Vector<Integer> scores = studentScores.get(student);
            
            System.out.println("  " + student + ":");
            Enumeration<Integer> scoreEnum = scores.elements();
            while (scoreEnum.hasMoreElements()) {
                System.out.println("    Score: " + scoreEnum.nextElement());
            }
        }
    }
}
```

## 11. Hard Example

```java
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;

public class LegacyDataProcessor {
    private final Hashtable<String, Vector<String>> dataStore;
    private final List<String> processedRecords;
    
    public LegacyDataProcessor() {
        this.dataStore = new Hashtable<>();
        this.processedRecords = new ArrayList<>();
    }
    
    public void loadData(String category, String[] records) {
        Vector<String> recordVector = new Vector<>();
        for (String record : records) {
            recordVector.add(record);
        }
        dataStore.put(category, recordVector);
    }
    
    public void processData() {
        System.out.println("Processing legacy data...");
        
        Enumeration<String> categoryEnum = dataStore.keys();
        while (categoryEnum.hasMoreElements()) {
            String category = categoryEnum.nextElement();
            Vector<String> records = dataStore.get(category);
            
            System.out.println("\nCategory: " + category);
            processRecords(records);
        }
        
        System.out.println("\nProcessed " + processedRecords.size() + " records");
    }
    
    private void processRecords(Vector<String> records) {
        Enumeration<String> recordEnum = records.elements();
        
        while (recordEnum.hasMoreElements()) {
            String record = recordEnum.nextElement();
            String processed = processRecord(record);
            processedRecords.add(processed);
            System.out.println("  Processed: " + processed);
        }
    }
    
    private String processRecord(String record) {
        // Simulate complex processing
        StringTokenizer tokenizer = new StringTokenizer(record, "|");
        StringBuilder processed = new StringBuilder();
        
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            processed.append(token.toUpperCase());
            if (tokenizer.hasMoreTokens()) {
                processed.append(" | ");
            }
        }
        
        return processed.toString();
    }
    
    public void generateReport() {
        System.out.println("\n=== Processing Report ===");
        System.out.println("Total categories: " + dataStore.size());
        System.out.println("Total records processed: " + processedRecords.size());
        
        // Count records per category
        Hashtable<String, Integer> categoryCounts = new Hashtable<>();
        Enumeration<String> categoryEnum = dataStore.keys();
        
        while (categoryEnum.hasMoreElements()) {
            String category = categoryEnum.nextElement();
            int count = dataStore.get(category).size();
            categoryCounts.put(category, count);
        }
        
        System.out.println("\nRecords per category:");
        Enumeration<String> countEnum = categoryCounts.keys();
        while (countEnum.hasMoreElements()) {
            String category = countEnum.nextElement();
            Integer count = categoryCounts.get(category);
            System.out.println("  " + category + ": " + count);
        }
    }
    
    public static void main(String[] args) {
        LegacyDataProcessor processor = new LegacyDataProcessor();
        
        // Load sample data
        processor.loadData("Users", new String[]{
            "1|John|Doe|john@example.com",
            "2|Jane|Smith|jane@example.com",
            "3|Bob|Johnson|bob@example.com"
        });
        
        processor.loadData("Orders", new String[]{
            "1001|1|2024-01-15|99.99",
            "1002|2|2024-01-16|149.99",
            "1003|1|2024-01-17|79.99"
        });
        
        processor.loadData("Products", new String[]{
            "P001|Laptop|999.99|Electronics",
            "P002|Phone|699.99|Electronics",
            "P003|Book|29.99|Education"
        });
        
        // Process data
        processor.processData();
        
        // Generate report
        processor.generateReport();
    }
}
```

## 12. Enterprise Example

```java
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LegacySystemAdapter {
    private static final Logger LOGGER = Logger.getLogger(LegacySystemAdapter.class.getName());
    private final Hashtable<String, Vector<String>> legacyData;
    private final List<String> modernData;
    
    public LegacySystemAdapter() {
        this.legacyData = new Hashtable<>();
        this.modernData = new ArrayList<>();
    }
    
    // Adapter method to convert legacy Enumeration to modern Iterator
    public <T> java.util.Iterator<T> enumerationToIterator(Enumeration<T> enumeration) {
        List<T> list = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list.iterator();
    }
    
    // Process legacy data using Enumeration
    public void processLegacyData() {
        LOGGER.info("Processing legacy data with Enumeration...");
        
        Enumeration<String> categoryEnum = legacyData.keys();
        while (categoryEnum.hasMoreElements()) {
            String category = categoryEnum.nextElement();
            Vector<String> records = legacyData.get(category);
            
            processCategory(category, records);
        }
    }
    
    private void processCategory(String category, Vector<String> records) {
        LOGGER.info("Processing category: " + category);
        
        Enumeration<String> recordEnum = records.elements();
        while (recordEnum.hasMoreElements()) {
            String record = recordEnum.nextElement();
            String processed = transformRecord(record);
            modernData.add(processed);
        }
    }
    
    private String transformRecord(String record) {
        // Parse legacy format and transform to modern format
        StringTokenizer tokenizer = new StringTokenizer(record, "|");
        StringBuilder modern = new StringBuilder();
        
        while (tokenizer.hasMoreTokens()) {
            String field = tokenizer.nextToken().trim();
            modern.append(field);
            if (tokenizer.hasMoreTokens()) {
                modern.append(",");
            }
        }
        
        return modern.toString();
    }
    
    // Export data using modern Iterator
    public void exportData() {
        LOGGER.info("Exporting data using modern Iterator...");
        
        java.util.Iterator<String> iterator = modernData.iterator();
        while (iterator.hasNext()) {
            String record = iterator.next();
            exportRecord(record);
        }
    }
    
    private void exportRecord(String record) {
        // Simulate export to modern system
        LOGGER.info("Exported: " + record);
    }
    
    // Load legacy data
    public void loadLegacyData(String category, String[] records) {
        Vector<String> recordVector = new Vector<>();
        for (String record : records) {
            recordVector.add(record);
        }
        legacyData.put(category, recordVector);
    }
    
    // Get statistics
    public Hashtable<String, Integer> getStatistics() {
        Hashtable<String, Integer> stats = new Hashtable<>();
        
        Enumeration<String> categoryEnum = legacyData.keys();
        while (categoryEnum.hasMoreElements()) {
            String category = categoryEnum.nextElement();
            int count = legacyData.get(category).size();
            stats.put(category, count);
        }
        
        stats.put("total_legacy", getLegacyRecordCount());
        stats.put("total_modern", modernData.size());
        
        return stats;
    }
    
    private int getLegacyRecordCount() {
        int count = 0;
        Enumeration<String> categoryEnum = legacyData.keys();
        while (categoryEnum.hasMoreElements()) {
            String category = categoryEnum.nextElement();
            count += legacyData.get(category).size();
        }
        return count;
    }
    
    public static void main(String[] args) {
        LegacySystemAdapter adapter = new LegacySystemAdapter();
        
        // Load legacy data
        adapter.loadLegacyData("customers", new String[]{
            "C001|Acme Corp|contact@acme.com",
            "C002|TechStart|info@techstart.com",
            "C003|GlobalInc|support@globalinc.com"
        });
        
        adapter.loadLegacyData("products", new String[]{
            "P001|Widget A|19.99|2024-01-01",
            "P002|Widget B|29.99|2024-01-15",
            "P003|Widget C|39.99|2024-02-01"
        });
        
        // Process legacy data
        adapter.processLegacyData();
        
        // Export to modern system
        adapter.exportData();
        
        // Get statistics
        Hashtable<String, Integer> stats = adapter.getStatistics();
        System.out.println("\n=== Migration Statistics ===");
        System.out.println("Legacy records: " + stats.get("total_legacy"));
        System.out.println("Modern records: " + stats.get("total_modern"));
    }
}
```

## 13. Performance

### Time Complexity
- **hasMoreElements()**: O(1)
- **nextElement()**: O(1)
- **StringTokenizer.nextToken()**: O(1)
- **Collections.enumeration()**: O(n) for conversion

### Memory Usage
- **Enumeration**: Minimal overhead, references original collection
- **StringTokenizer**: Stores tokens in internal array
- **Vector.elements()**: References existing vector

### Comparison with Iterator
| Aspect | Enumeration | Iterator |
|--------|-------------|----------|
| Creation | O(1) | O(1) |
| Traversal | O(1) per element | O(1) per element |
| Removal | Not supported | O(1) amortized |
| Memory | Low | Low |
| Thread safety | Synchronized (Vector/Hashtable) | Not synchronized |

## 14. Best Practices

```java
// 1. Use Iterator instead of Enumeration for new code
Iterator<String> iterator = list.iterator();

// 2. Use Enumeration only for legacy APIs
Enumeration<String> legacyEnum = vector.elements();

// 3. Convert Enumeration to Iterator for modern code
public <T> Iterator<T> toIterator(Enumeration<T> enumeration) {
    List<T> list = new ArrayList<>();
    while (enumeration.hasMoreElements()) {
        list.add(enumeration.nextElement());
    }
    return list.iterator();
}

// 4. Use String.split() instead of StringTokenizer
String[] tokens = "Hello,World".split(",");

// 5. Use Collections.enumeration() to create Enumeration from List
Enumeration<String> enumFromList = Collections.enumeration(list);

// 6. Avoid using Enumeration for new implementations
// Bad
public Enumeration<String> getElements() { /* ... */ }
// Good
public Iterator<String> getElements() { /* ... */ }

// 7. Use try-with-resources for StringTokenizer (implements AutoCloseable since Java 21)
try (StringTokenizer tokenizer = new StringTokenizer("Hello,World")) {
    while (tokenizer.hasMoreTokens()) {
        System.out.println(tokenizer.nextToken());
    }
}
```

## 15. Common Mistakes

```java
// Mistake 1: Using Enumeration for new code
// Bad
public Enumeration<String> getItems() { /* ... */ }
// Good
public Iterator<String> getItems() { /* ... */ }

// Mistake 2: Using StringTokenizer instead of String.split()
// Bad
StringTokenizer tokenizer = new StringTokenizer("Hello,World", ",");
while (tokenizer.hasMoreTokens()) {
    System.out.println(tokenizer.nextToken());
}
// Good
String[] tokens = "Hello,World".split(",");
for (String token : tokens) {
    System.out.println(token);
}

// Mistake 3: Not converting Enumeration to Iterator
// Bad
Enumeration<String> enum = vector.elements();
while (enum.hasMoreElements()) {
    String element = enum.nextElement();
    // Can't use Iterator methods
}
// Good
Iterator<String> iterator = Collections.list(vector.elements()).iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    // Can use Iterator methods
}

// Mistake 4: Using Enumeration for thread safety
// Bad
Enumeration<String> enum = vector.elements();  // Vector is synchronized, but Enumeration isn't
// Good
List<String> list = Collections.synchronizedList(new ArrayList<>());
synchronized (list) {
    Iterator<String> iterator = list.iterator();
    while (iterator.hasNext()) {
        // Safe iteration
    }
}

// Mistake 5: Assuming Enumeration is thread-safe
// Bad
Enumeration<String> enum = hashtable.elements();
// Multiple threads can call hasMoreElements() and nextElement()
// Good
Enumeration<String> enum = hashtable.elements();
synchronized (hashtable) {
    while (enum.hasMoreElements()) {
        // Safe iteration
    }
}
```

## 16. Pitfalls

### Legacy Limitations
- **No removal**: Cannot remove elements during traversal
- **No bidirectional traversal**: Only forward iteration
- **Limited APIs**: Only available for Vector, Hashtable, StringTokenizer
- **No forEachRemaining**: Cannot process remaining elements in one call

### Thread Safety Issues
- **Not truly thread-safe**: Enumeration itself is not synchronized
- **Vector/Hashtable synchronization**: Methods are synchronized, but iteration is not atomic
- **Concurrent access**: Multiple threads can cause issues

### Performance Concerns
- **Conversion overhead**: Converting to Iterator has O(n) cost
- **StringTokenizer inefficiency**: Less efficient than regular expressions
- **Memory usage**: Internal storage for StringTokenizer

### Migration Challenges
- **Code modernization**: Replacing Enumeration with Iterator requires changes
- **API compatibility**: Some legacy APIs only support Enumeration
- **Testing**: Need to ensure backward compatibility

## 17. Interview Questions

### Q1: What is the difference between Enumeration and Iterator?
**Answer**: `Enumeration` is a legacy interface (JDK 1.0) with only `hasMoreElements()` and `nextElement()`. `Iterator` (JDK 1.2) adds `remove()` and `forEachRemaining()`. `Iterator` is the modern choice; `Enumeration` is only for legacy code.

### Q2: When would you use Enumeration over Iterator?
**Answer**: Only when working with legacy APIs that return Enumeration (Vector, Hashtable, StringTokenizer). For new code, always use Iterator or for-each loop.

### Q3: What is StringTokenizer and why is it legacy?
**Answer**: `StringTokenizer` is a legacy class for parsing strings. It uses `Enumeration` for traversal. It's replaced by `String.split()` and `Pattern` class, which are more powerful and flexible.

### Q4: How do you convert Enumeration to Iterator?
**Answer**: Use `Collections.list(enumeration)` to convert to List, then call `iterator()`. Or create a custom adapter that wraps Enumeration.

### Q5: Why doesn't Enumeration support removal?
**Answer**: It was designed for simple traversal in JDK 1.0. Removal was added later in Iterator to support more complex use cases while maintaining backward compatibility.

### Q6: Is Enumeration thread-safe?
**Answer**: The Enumeration itself is not synchronized. However, the collections that return Enumeration (Vector, Hashtable) have synchronized methods. For thread-safe iteration, you still need external synchronization.

### Q7: What are modern alternatives to StringTokenizer?
**Answer**: `String.split()`, `Pattern` and `Matcher` classes, `Scanner` class, and third-party libraries like Apache Commons Lang's `StringUtils`.

## 18. Exercises

### Exercise 1: Basic Enumeration
Create a `Vector` of strings and use `Enumeration` to print all elements. Then convert the `Enumeration` to an `Iterator` and remove elements starting with "A".

### Exercise 2: String Tokenization
Implement a CSV parser using `StringTokenizer`. Parse a string with multiple fields and handle different delimiters.

### Exercise 3: Legacy Data Migration
Create a `Hashtable` with legacy data and use `Enumeration` to migrate it to a modern `HashMap`. Include data transformation during migration.

### Exercise 4: Enumeration Adapter
Implement an adapter class that converts `Enumeration` to `Iterator`. Test with `Vector` and `Hashtable`.

## 19. Summary

- `Enumeration` is a legacy interface from JDK 1.0 for sequential traversal
- Supports only `hasMoreElements()` and `nextElement()` methods
- Used by legacy classes: `Vector`, `Hashtable`, `StringTokenizer`
- `StringTokenizer` is legacy; prefer `String.split()` or `Pattern`
- `Iterator` is the modern replacement with more features
- Use `Collections.enumeration()` to create Enumeration from List
- Convert Enumeration to Iterator for modern code
- Only use Enumeration for backward compatibility with legacy APIs

## 20. References

### Official Documentation
- [Java Enumeration Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Enumeration.html)
- [Java StringTokenizer Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/StringTokenizer.html)
- [Java Collections.enumeration() Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collections.html#enumeration(java.util.Collection))

### Books
- *Effective Java* by Joshua Bloch
- *Java: The Complete Reference* by Herbert Schildt
- *Java Foundation Classes* by James Gosling

### Online Resources
- [Baeldung - Enumeration in Java](https://www.baeldung.com/java-enumeration)
- [GeeksforGeeks - Enumeration in Java](https://www.geeksforgeeks.org/enumeration-in-java/)
- [Oracle - Legacy Collections](https://docs.oracle.com/javase/tutorial/collections/legacy/index.html)

### Related Topics
- [Iterator Interface](../24-iterator/README.md)
- [Hashtable](../23-hashtable/README.md)
- [Vector](../04-vector/README.md)
