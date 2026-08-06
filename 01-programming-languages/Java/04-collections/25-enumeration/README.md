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
        
        System.out.println("
Keys:");
        Enumeration<String> keyEnum = ages.keys();
        while (keyEnum.hasMoreElements()) {
            String key = keyEnum.nextElement();
            System.out.println("  " + key);
        }
        
        System.out.println("
Values:");
        Enumeration<Integer> valueEnum = ages.elements();
        while (valueEnum.hasMoreElements()) {
            Integer value = valueEnum.nextElement();
            System.out.println("  " + value);
        }
        
        // StringTokenizer
        StringTokenizer tokenizer = new StringTokenizer("Java,Collections,Framework");
        System.out.println("
Tokens:");
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
        System.out.println("
CSV parsing:");
        while (csvTokenizer.hasMoreTokens()) {
            System.out.println("  " + csvTokenizer.nextToken());
        }
        
        // StringTokenizer with return delimiters
        String sentence = "Hello, world! How are you?";
        StringTokenizer wordTokenizer = new StringTokenizer(sentence, " ,?!");
        System.out.println("
Word parsing:");
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
        
        System.out.println("
Student scores:");
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
            
            System.out.println("
Category: " + category);
            processRecords(records);
        }
        
        System.out.println("
Processed " + processedRecords.size() + " records");
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
        System.out.println("
=== Processing Report ===");
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
        
        System.out.println("
Records per category:");
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

## 📑 Continue Reading

**Part 1** of 2 | [Part 2](README-part2.md)

```
