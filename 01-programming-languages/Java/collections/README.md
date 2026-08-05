# Java Collections Framework

## Table of Contents

1. [List](#list)
2. [Set](#set)
3. [Map](#map)
4. [Queue](#queue)
5. [Deque](#deque)
6. [Comparable and Comparator](#comparable-and-comparator)
7. [Iterator](#iterator)
8. [Collections Utility](#collections-utility)
9. [Concurrent Collections](#concurrent-collections)
10. [Performance Comparison](#performance-comparison)

---

## List

### ArrayList

```java
import java.util.*;

public class ArrayListExample {
    public static void main(String[] args) {
        // Creating ArrayList
        List<String> names = new ArrayList<>();
        
        // Adding elements
        names.add("Alice");           // Add at end
        names.add("Bob");
        names.add("Charlie");
        names.add(1, "David");       // Add at index
        
        // Accessing elements
        System.out.println("First: " + names.get(0));
        System.out.println("Size: " + names.size());
        
        // Updating elements
        names.set(0, "Alicia");
        
        // Removing elements
        names.remove("Bob");          // Remove by object
        names.remove(0);              // Remove by index
        
        // Searching
        System.out.println("Contains Charlie: " + names.contains("Charlie"));
        System.out.println("Index of Charlie: " + names.indexOf("Charlie"));
        
        // Iterating
        for (String name : names) {
            System.out.println(name);
        }
        
        // SubList
        List<String> subList = names.subList(0, 2);
        System.out.println("SubList: " + subList);
        
        // Sorting
        names.sort(Comparator.naturalOrder());
        System.out.println("Sorted: " + names);
        
        // Converting to array
        String[] array = names.toArray(new String[0]);
        System.out.println("Array: " + Arrays.toString(array));
        
        // Bulk operations
        List<String> moreNames = List.of("Eve", "Frank");
        names.addAll(moreNames);
        System.out.println("After addAll: " + names);
        
        // Removing if
        names.removeIf(name -> name.length() < 4);
        System.out.println("After removeIf: " + names);
        
        // Replace all
        names.replaceAll(String::toUpperCase);
        System.out.println("After replaceAll: " + names);
    }
}
```

### LinkedList

```java
import java.util.*;

public class LinkedListExample {
    public static void main(String[] args) {
        // Creating LinkedList
        LinkedList<String> linkedList = new LinkedList<>();
        
        // Adding elements
        linkedList.add("Alice");
        linkedList.add("Bob");
        linkedList.addFirst("Charlie");   // Add at beginning
        linkedList.addLast("David");      // Add at end
        
        // Accessing elements
        System.out.println("First: " + linkedList.getFirst());
        System.out.println("Last: " + linkedList.getLast());
        System.out.println("Element: " + linkedList.element()); // Same as getFirst()
        
        // Removing elements
        linkedList.removeFirst();
        linkedList.removeLast();
        
        // Deque operations (LinkedList implements Deque)
        linkedList.push("Eve");    // Add to beginning (stack)
        linkedList.pop();          // Remove from beginning (stack)
        linkedList.offer("Frank"); // Add to end (queue)
        linkedList.poll();         // Remove from beginning (queue)
        
        // Iterating
        System.out.println("List: " + linkedList);
        
        // Performance comparison with ArrayList
        // ArrayList: O(1) get, O(n) add/remove at beginning
        // LinkedList: O(n) get, O(1) add/remove at beginning
        
        // Use LinkedList when:
        // 1. Frequent add/remove at beginning
        // 2. Implementing queue/deque
        // Don't use LinkedList for random access
    }
}
```

### Vector and Stack

```java
import java.util.*;

public class VectorStackExample {
    public static void main(String[] args) {
        // Vector (thread-safe, legacy)
        Vector<String> vector = new Vector<>();
        vector.add("Alice");
        vector.add("Bob");
        vector.add("Charlie");
        
        // Vector-specific methods
        System.out.println("Capacity: " + vector.capacity());
        System.out.println("Element at 1: " + vector.elementAt(1));
        
        // Iterating
        Enumeration<String> enumeration = vector.elements();
        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement());
        }
        
        // Stack (extends Vector)
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        
        System.out.println("Peek: " + stack.peek());
        System.out.println("Pop: " + stack.pop());
        System.out.println("Search for 2: " + stack.search(2));
        
        System.out.println("Stack: " + stack);
        
        // Note: Use Deque interface instead of Stack class
        Deque<Integer> dequeStack = new ArrayDeque<>();
        dequeStack.push(1);
        dequeStack.push(2);
        dequeStack.push(3);
        
        System.out.println("Deque peek: " + dequeStack.peek());
        System.out.println("Deque pop: " + dequeStack.pop());
    }
}
```

---

## Set

### HashSet

```java
import java.util.*;

public class HashSetExample {
    public static void main(String[] args) {
        // Creating HashSet
        Set<String> set = new HashSet<>();
        
        // Adding elements
        set.add("Alice");
        set.add("Bob");
        set.add("Charlie");
        set.add("Alice");  // Duplicate, won't be added
        
        System.out.println("Set: " + set);
        System.out.println("Size: " + set.size());
        
        // Checking contains
        System.out.println("Contains Alice: " + set.contains("Alice"));
        
        // Removing elements
        set.remove("Bob");
        
        // Iterating
        for (String name : set) {
            System.out.println(name);
        }
        
        // Set operations
        Set<String> set1 = new HashSet<>(List.of("Alice", "Bob", "Charlie"));
        Set<String> set2 = new HashSet<>(List.of("Bob", "David", "Eve"));
        
        // Union
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);
        
        // Intersection
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);
        
        // Difference
        Set<String> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference: " + difference);
        
        // Converting from other collections
        List<String> list = List.of("Alice", "Bob", "Alice", "Charlie");
        Set<String> uniqueElements = new HashSet<>(list);
        System.out.println("Unique: " + uniqueElements);
    }
}
```

### TreeSet

```java
import java.util.*;

public class TreeSetExample {
    public static void main(String[] args) {
        // Creating TreeSet (sorted)
        TreeSet<String> treeSet = new TreeSet<>();
        
        // Adding elements
        treeSet.add("Charlie");
        treeSet.add("Alice");
        treeSet.add("Bob");
        treeSet.add("David");
        
        System.out.println("TreeSet: " + treeSet);
        
        // Navigation methods
        System.out.println("First: " + treeSet.first());
        System.out.println("Last: " + treeSet.last());
        System.out.println("Lower (before Alice): " + treeSet.lower("Alice"));
        System.out.println("Higher (after Alice): " + treeSet.higher("Alice"));
        System.out.println("Floor (<= Bob): " + treeSet.floor("Bob"));
        System.out.println("Ceiling (>= Bob): " + treeSet.ceiling("Bob"));
        
        // Subset operations
        NavigableSet<String> subset = treeSet.subSet("Alice", true, "Charlie", true);
        System.out.println("Subset: " + subset);
        
        // Head/Tail sets
        System.out.println("HeadSet (< Charlie): " + treeSet.headSet("Charlie"));
        System.out.println("TailSet (>= Charlie): " + treeSet.tailSet("Charlie"));
        
        // Descending order
        NavigableSet<String> descending = treeSet.descendingSet();
        System.out.println("Descending: " + descending);
        
        // Custom comparator
        TreeSet<String> customTreeSet = new TreeSet<>(Comparator.comparingInt(String::length));
        customTreeSet.add("Charlie");
        customTreeSet.add("Alice");
        customTreeSet.add("Bob");
        customTreeSet.add("David");
        
        System.out.println("By length: " + customTreeSet);
    }
}
```

### LinkedHashSet

```java
import java.util.*;

public class LinkedHashSetExample {
    public static void main(String[] args) {
        // Creating LinkedHashSet (maintains insertion order)
        Set<String> linkedHashSet = new LinkedHashSet<>();
        
        // Adding elements
        linkedHashSet.add("Charlie");
        linkedHashSet.add("Alice");
        linkedHashSet.add("Bob");
        linkedHashSet.add("David");
        
        System.out.println("LinkedHashSet: " + linkedHashSet);
        
        // Performance comparison
        // HashSet: O(1) add/remove/contains, no order
        // TreeSet: O(log n) add/remove/contains, sorted order
        // LinkedHashSet: O(1) add/remove/contains, insertion order
        
        // Use cases
        // HashSet: When order doesn't matter
        // TreeSet: When you need sorted elements
        // LinkedHashSet: When you need insertion order
    }
}
```

---

## Map

### HashMap

```java
import java.util.*;

public class HashMapExample {
    public static void main(String[] args) {
        // Creating HashMap
        Map<String, Integer> map = new HashMap<>();
        
        // Adding entries
        map.put("Alice", 30);
        map.put("Bob", 25);
        map.put("Charlie", 35);
        map.put("David", 28);
        
        System.out.println("Map: " + map);
        
        // Accessing values
        System.out.println("Alice's age: " + map.get("Alice"));
        System.out.println("Size: " + map.size());
        
        // putIfAbsent
        map.putIfAbsent("Eve", 22);
        map.putIfAbsent("Alice", 99);  // Won't update
        
        // getOrDefault
        int age = map.getOrDefault("Frank", 0);
        System.out.println("Frank's age: " + age);
        
        // computeIfAbsent
        map.computeIfAbsent("Grace", k -> k.length() * 10);
        
        // computeIfPresent
        map.computeIfPresent("Alice", (k, v) -> v + 1);
        
        // compute
        map.compute("Bob", (k, v) -> v == null ? 0 : v * 2);
        
        // merge
        map.merge("Charlie", 5, Integer::sum);
        
        // Removing entries
        map.remove("David");
        map.remove("Bob", 25);  // Remove only if value matches
        
        // Iterating
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
        // Keys and values
        Set<String> keys = map.keySet();
        Collection<Integer> values = map.values();
        
        // Replace operations
        map.replaceAll((key, value) -> value + 10);
        
        // forEach
        map.forEach((key, value) -> System.out.println(key + " -> " + value));
        
        // Converting to other maps
        TreeMap<String, Integer> treeMap = new TreeMap<>(map);
        LinkedHashMap<String, Integer> linkedMap = new LinkedHashMap<>(map);
    }
}
```

### TreeMap

```java
import java.util.*;

public class TreeMapExample {
    public static void main(String[] args) {
        // Creating TreeMap (sorted by keys)
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        
        // Adding entries
        treeMap.put("Charlie", 35);
        treeMap.put("Alice", 30);
        treeMap.put("Bob", 25);
        treeMap.put("David", 28);
        
        System.out.println("TreeMap: " + treeMap);
        
        // Navigation methods
        System.out.println("First key: " + treeMap.firstKey());
        System.out.println("Last key: " + treeMap.lastKey());
        System.out.println("Lower key (before Alice): " + treeMap.lowerKey("Alice"));
        System.out.println("Higher key (after Alice): " + treeMap.higherKey("Alice"));
        
        // SubMap operations
        SortedMap<String, Integer> subMap = treeMap.subMap("Alice", true, "Charlie", true);
        System.out.println("SubMap: " + subMap);
        
        // HeadMap/TailMap
        System.out.println("HeadMap (< Charlie): " + treeMap.headMap("Charlie"));
        System.out.println("TailMap (>= Charlie): " + treeMap.tailMap("Charlie"));
        
        // First/Last entries
        System.out.println("First entry: " + treeMap.firstEntry());
        System.out.println("Last entry: " + treeMap.lastEntry());
        
        // Poll operations
        System.out.println("Poll first: " + treeMap.pollFirstEntry());
        System.out.println("Poll last: " + treeMap.pollLastEntry());
        
        // Descending order
        NavigableMap<String, Integer> descending = treeMap.descendingMap();
        System.out.println("Descending: " + descending);
        
        // Custom comparator
        TreeMap<String, Integer> customTreeMap = new TreeMap<>(
            Comparator.comparingInt(String::length)
        );
        customTreeMap.put("Charlie", 35);
        customTreeMap.put("Alice", 30);
        customTreeMap.put("Bob", 25);
        
        System.out.println("By length: " + customTreeMap);
    }
}
```

### LinkedHashMap and EnumMap

```java
import java.util.*;

public class SpecialMaps {
    public static void main(String[] args) {
        // LinkedHashMap (maintains insertion order)
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Charlie", 35);
        linkedHashMap.put("Alice", 30);
        linkedHashMap.put("Bob", 25);
        
        System.out.println("LinkedHashMap: " + linkedHashMap);
        
        // Access-order LinkedHashMap (for LRU cache)
        LinkedHashMap<String, Integer> lruCache = new LinkedHashMap<>(16, 0.75f, true);
        lruCache.put("A", 1);
        lruCache.put("B", 2);
        lruCache.put("C", 3);
        
        // Access "A" to move it to end
        lruCache.get("A");
        
        System.out.println("LRU Cache: " + lruCache);
        
        // EnumMap (optimized for enum keys)
        enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
        
        EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
        schedule.put(Day.MONDAY, "Work");
        schedule.put(Day.TUESDAY, "Work");
        schedule.put(Day.WEDNESDAY, "Gym");
        schedule.put(Day.THURSDAY, "Work");
        schedule.put(Day.FRIDAY, "Work");
        schedule.put(Day.SATURDAY, "Rest");
        schedule.put(Day.SUNDAY, "Rest");
        
        System.out.println("Schedule: " + schedule);
        
        // IdentityHashMap (uses == instead of equals)
        IdentityHashMap<String, String> identityMap = new IdentityHashMap<>();
        String s1 = new String("hello");
        String s2 = new String("hello");
        
        identityMap.put(s1, "first");
        identityMap.put(s2, "second");  // Different keys because == is used
        
        System.out.println("IdentityHashMap size: " + identityMap.size());
        
        // WeakHashMap (entries can be garbage collected)
        WeakHashMap<Object, String> weakMap = new WeakHashMap<>();
        Object key = new Object();
        weakMap.put(key, "value");
        
        System.out.println("WeakHashMap before GC: " + weakMap.size());
        key = null;  // Allow key to be garbage collected
        System.gc();
        
        // Note: Entry may or may not be collected immediately
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("WeakHashMap after GC: " + weakMap.size());
    }
}
```

---

## Queue

### PriorityQueue

```java
import java.util.*;

public class PriorityQueueExample {
    public static void main(String[] args) {
        // Creating PriorityQueue (min-heap by default)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        // Adding elements
        minHeap.offer(5);
        minHeap.offer(2);
        minHeap.offer(8);
        minHeap.offer(1);
        minHeap.offer(3);
        
        System.out.println("Min Heap: " + minHeap);
        
        // Removing elements (always smallest first)
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");
        }
        System.out.println();
        
        // Max-heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(2);
        maxHeap.offer(8);
        maxHeap.offer(1);
        
        System.out.print("Max Heap: ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " ");
        }
        System.out.println();
        
        // Custom priority with objects
        PriorityQueue<int[]> taskQueue = new PriorityQueue<>(
            Comparator.comparingInt(a -> a[1])  // Sort by priority (second element)
        );
        
        taskQueue.offer(new int[]{1, 3});  // Task 1, priority 3
        taskQueue.offer(new int[]{2, 1});  // Task 2, priority 1
        taskQueue.offer(new int[]{3, 2});  // Task 3, priority 2
        
        System.out.println("Tasks by priority:");
        while (!taskQueue.isEmpty()) {
            int[] task = taskQueue.poll();
            System.out.println("Task " + task[0] + " with priority " + task[1]);
        }
        
        // Operations
        PriorityQueue<String> pq = new PriorityQueue<>();
        pq.offer("Charlie");
        pq.offer("Alice");
        pq.offer("Bob");
        
        System.out.println("Peek: " + pq.peek());
        System.out.println("Size: " + pq.size());
        System.out.println("Contains Alice: " + pq.contains("Alice"));
        
        // Convert to sorted list
        List<String> sortedList = new ArrayList<>(pq);
        Collections.sort(sortedList);
        System.out.println("Sorted list: " + sortedList);
    }
}
```

### ArrayDeque as Queue

```java
import java.util.*;

public class ArrayDequeQueue {
    public static void main(String[] args) {
        // Creating ArrayDeque as Queue
        Queue<String> queue = new ArrayDeque<>();
        
        // Adding elements
        queue.offer("Alice");
        queue.offer("Bob");
        queue.offer("Charlie");
        
        System.out.println("Queue: " + queue);
        
        // Removing elements
        System.out.println("Poll: " + queue.poll());
        System.out.println("Peek: " + queue.peek());
        
        // Adding to beginning
        queue.offerFirst("David");
        
        // Removing from end
        queue.offerLast("Eve");
        
        System.out.println("Queue after operations: " + queue);
        
        // Iterating
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
        
        // LinkedList can also be used as Queue
        Queue<Integer> linkedQueue = new LinkedList<>();
        linkedQueue.offer(1);
        linkedQueue.offer(2);
        linkedQueue.offer(3);
        
        System.out.println("LinkedList Queue: " + linkedQueue);
    }
}
```

---

## Deque

### ArrayDeque

```java
import java.util.*;

public class ArrayDequeExample {
    public static void main(String[] args) {
        // Creating ArrayDeque
        Deque<String> deque = new ArrayDeque<>();
        
        // Adding elements
        deque.offer("Alice");       // Add to end
        deque.offerFirst("Bob");    // Add to beginning
        deque.offerLast("Charlie"); // Add to end
        
        System.out.println("Deque: " + deque);
        
        // Accessing elements
        System.out.println("First: " + deque.peekFirst());
        System.out.println("Last: " + deque.peekLast());
        
        // Removing elements
        System.out.println("PollFirst: " + deque.pollFirst());
        System.out.println("PollLast: " + deque.pollLast());
        
        // Stack operations
        deque.push("David");    // Add to beginning
        deque.push("Eve");
        
        System.out.println("Stack: " + deque);
        System.out.println("Pop: " + deque.pop());
        System.out.println("Peek: " + deque.peek());
        
        // Iterating
        for (String s : deque) {
            System.out.println(s);
        }
        
        // Descending iterator
        Iterator<String> descIterator = deque.descendingIterator();
        while (descIterator.hasNext()) {
            System.out.println(descIterator.next());
        }
        
        // Size and contains
        System.out.println("Size: " + deque.size());
        System.out.println("Contains Alice: " + deque.contains("Alice"));
    }
}
```

### Deque Implementations

```java
import java.util.*;

public class DequeImplementations {
    public static void main(String[] args) {
        // ArrayDeque (recommended for most cases)
        Deque<String> arrayDeque = new ArrayDeque<>();
        
        // LinkedList (alternative implementation)
        Deque<String> linkedDeque = new LinkedList<>();
        
        // Performance comparison
        // ArrayDeque: O(1) for add/remove at both ends
        // LinkedList: O(1) for add/remove at both ends
        // ArrayDeque is faster due to cache locality
        
        // Use ArrayDeque unless you need:
        // 1. Thread safety (use ConcurrentLinkedDeque)
        // 2. Null elements (ArrayDeque doesn't allow nulls)
        // 3. Frequent iteration (LinkedList has better iteration performance for large lists)
        
        // Example: Using Deque as Stack
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        
        System.out.println("Stack: " + stack);
        System.out.println("Pop: " + stack.pop());
        System.out.println("Peek: " + stack.peek());
        
        // Example: Using Deque as Queue
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        
        System.out.println("\nQueue: " + queue);
        System.out.println("Poll: " + queue.poll());
        System.out.println("Peek: " + queue.peek());
    }
}
```

---

## Comparable and Comparator

### Comparable

```java
import java.util.*;

// Implementing Comparable
public class Student implements Comparable<Student> {
    private String name;
    private int age;
    private double gpa;
    
    public Student(String name, int age, double gpa) {
        this.name = name;
        this.age = age;
        this.gpa = gpa;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getGpa() { return gpa; }
    
    // Natural ordering (by name)
    @Override
    public int compareTo(Student other) {
        return this.name.compareTo(other.name);
    }
    
    @Override
    public String toString() {
        return name + " (Age: " + age + ", GPA: " + gpa + ")";
    }
}

// Comparable with multiple fields
public class Employee implements Comparable<Employee> {
    private String name;
    private String department;
    private double salary;
    
    public Employee(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }
    
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
    
    @Override
    public int compareTo(Employee other) {
        // Compare by department, then by salary
        int deptCompare = this.department.compareTo(other.department);
        if (deptCompare != 0) {
            return deptCompare;
        }
        return Double.compare(this.salary, other.salary);
    }
    
    @Override
    public String toString() {
        return name + " (" + department + ", $" + salary + ")";
    }
}

public class ComparableExample {
    public static void main(String[] args) {
        // Using Comparable
        List<Student> students = new ArrayList<>();
        students.add(new Student("Alice", 20, 3.8));
        students.add(new Student("Bob", 22, 3.5));
        students.add(new Student("Charlie", 21, 3.9));
        students.add(new Student("David", 23, 3.7));
        
        // Sort using natural ordering (Comparable)
        Collections.sort(students);
        System.out.println("Sorted by name:");
        students.forEach(System.out::println);
        
        // Using TreeSet (uses Comparable)
        Set<Student> studentSet = new TreeSet<>(students);
        System.out.println("\nTreeSet:");
        studentSet.forEach(System.out::println);
        
        // Using Arrays.sort
        Student[] studentArray = students.toArray(new Student[0]);
        Arrays.sort(studentArray);
        System.out.println("\nArray sorted:");
        Arrays.stream(studentArray).forEach(System.out::println);
    }
}
```

### Comparator

```java
import java.util.*;

public class ComparatorExample {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Charlie");
        names.add("Alice");
        names.add("Bob");
        names.add("David");
        
        // Comparator.comparing
        names.sort(Comparator.comparing(String::length));
        System.out.println("By length: " + names);
        
        // Comparator.comparingInt
        names.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println("By length reversed: " + names);
        
        // ThenComparing
        names.sort(Comparator.comparing(String::length)
                .thenComparing(Comparator.naturalOrder()));
        System.out.println("By length then natural: " + names);
        
        // nullsFirst/nullsLast
        names.add(null);
        names.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println("Nulls first: " + names);
        
        // Reversed
        names.sort(Comparator.comparing(String::length).reversed());
        System.out.println("Reversed: " + names);
        
        // Custom Comparator for complex objects
        List<Student> students = new ArrayList<>();
        students.add(new Student("Alice", 20, 3.8));
        students.add(new Student("Bob", 22, 3.5));
        students.add(new Student("Charlie", 21, 3.9));
        students.add(new Student("David", 23, 3.7));
        
        // Sort by GPA
        students.sort(Comparator.comparingDouble(Student::getGpa).reversed());
        System.out.println("\nBy GPA:");
        students.forEach(System.out::println);
        
        // Sort by age, then by GPA
        students.sort(Comparator.comparingInt(Student::getAge)
                .thenComparingDouble(Student::getGpa).reversed());
        System.out.println("\nBy age then GPA:");
        students.forEach(System.out::println);
        
        // Multiple comparators
        Comparator<Student> byName = Comparator.comparing(Student::getName);
        Comparator<Student> byAge = Comparator.comparingInt(Student::getAge);
        Comparator<Student> byGpa = Comparator.comparingDouble(Student::getGpa);
        
        // Combine comparators
        students.sort(byName);
        System.out.println("\nBy name:");
        students.forEach(System.out::println);
        
        // thenComparing chaining
        students.sort(Comparator.comparing(Student::getName)
                .thenComparingInt(Student::getAge)
                .thenComparingDouble(Student::getGpa));
        System.out.println("\nBy name, age, GPA:");
        students.forEach(System.out::println);
        
        // Comparator from Comparable
        Comparator<Student> naturalOrder = Comparator.naturalOrder();
        students.sort(naturalOrder);
        System.out.println("\nNatural order:");
        students.forEach(System.out::println);
        
        // Reversed comparator
        Comparator<Student> reverseOrder = Comparator.reverseOrder();
        students.sort(reverseOrder);
        System.out.println("\nReverse order:");
        students.forEach(System.out::println);
    }
}
```

---

## Iterator

### Basic Iterator

```java
import java.util.*;

public class IteratorExample {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        names.add("David");
        
        // Using Iterator
        Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            System.out.println(name);
        }
        
        // Removing elements while iterating
        Iterator<String> removeIterator = names.iterator();
        while (removeIterator.hasNext()) {
            String name = removeIterator.next();
            if (name.startsWith("B")) {
                removeIterator.remove();  // Safe removal
            }
        }
        System.out.println("After removal: " + names);
        
        // ListIterator (for List only)
        List<String> list = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));
        ListIterator<String> listIterator = list.listIterator();
        
        // Forward iteration
        while (listIterator.hasNext()) {
            int index = listIterator.nextIndex();
            String name = listIterator.next();
            System.out.println(index + ": " + name);
            
            // Replace
            if (name.equals("Bob")) {
                listIterator.set("Robert");
            }
            
            // Add
            if (name.equals("Charlie")) {
                listIterator.add("David");
            }
        }
        
        System.out.println("After modifications: " + list);
        
        // Backward iteration
        System.out.println("Backward:");
        while (listIterator.hasPrevious()) {
            int index = listIterator.previousIndex();
            String name = listIterator.previous();
            System.out.println(index + ": " + name);
        }
        
        // Creating ListIterator from index
        ListIterator<String> fromIndex = list.listIterator(2);
        while (fromIndex.hasNext()) {
            System.out.println(fromIndex.next());
        }
    }
}
```

### Spliterator (Java 8+)

```java
import java.util.*;
import java.util.stream.*;

public class SpliteratorExample {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Using Spliterator
        Spliterator<Integer> spliterator = numbers.spliterator();
        
        // Characteristics
        System.out.println("Characteristics:");
        System.out.println("  Ordered: " + (spliterator.hasCharacteristics(Spliterator.ORDERED)));
        System.out.println("  Distinct: " + (spliterator.hasCharacteristics(Spliterator.DISTINCT)));
        System.out.println("  Sorted: " + (spliterator.hasCharacteristics(Spliterator.SORTED)));
        System.out.println("  Sized: " + (spliterator.hasCharacteristics(Spliterator.SIZED)));
        
        // Processing elements
        spliterator.forEachRemaining(System.out::println);
        
        // Try split
        Spliterator<Integer> spliterator2 = numbers.spliterator();
        Spliterator<Integer> half1 = spliterator2.trySplit();
        
        if (half1 != null) {
            System.out.println("\nFirst half:");
            half1.forEachRemaining(n -> System.out.print(n + " "));
            System.out.println();
            
            System.out.println("Second half:");
            spliterator2.forEachRemaining(n -> System.out.print(n + " "));
            System.out.println();
        }
        
        // Estimate size
        Spliterator<Integer> sizedSpliterator = numbers.spliterator();
        System.out.println("\nEstimated size: " + sizedSpliterator.estimateSize());
        System.out.println("Exact size (if known): " + sizedSpliterator.getExactSizeIfKnown());
        
        // Parallel streams use Spliterator
        long sum = numbers.parallelStream().mapToInt(Integer::intValue).sum();
        System.out.println("\nParallel sum: " + sum);
    }
}
```

---

## Collections Utility

### Collections Class Methods

```java
import java.util.*;

public class CollectionsUtility {
    public static void main(String[] args) {
        // Creating unmodifiable collections
        List<String> unmodifiableList = Collections.unmodifiableList(
                new ArrayList<>(List.of("Alice", "Bob", "Charlie")));
        // unmodifiableList.add("David");  // Throws UnsupportedOperationException
        
        Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(
                new HashMap<>(Map.of("Alice", 30, "Bob", 25)));
        
        // Creating synchronized collections
        List<String> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        Map<String, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        
        // Creating checked collections (runtime type checking)
        List<String> checkedList = Collections.checkedList(new ArrayList<>(), String.class);
        
        // Creating singleton collections
        List<String> singletonList = Collections.singletonList("Alice");
        Set<String> singletonSet = Collections.singleton("Bob");
        Map<String, Integer> singletonMap = Collections.singletonMap("Charlie", 35);
        
        // Sorting and searching
        List<Integer> numbers = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3));
        Collections.sort(numbers);
        System.out.println("Sorted: " + numbers);
        
        int index = Collections.binarySearch(numbers, 8);
        System.out.println("Index of 8: " + index);
        
        // Min and max
        System.out.println("Min: " + Collections.min(numbers));
        System.out.println("Max: " + Collections.max(numbers));
        
        // Frequency
        List<String> names = List.of("Alice", "Bob", "Alice", "Charlie", "Alice");
        System.out.println("Frequency of Alice: " + Collections.frequency(names, "Alice"));
        
        // Disjoint
        List<Integer> list1 = List.of(1, 2, 3);
        List<Integer> list2 = List.of(4, 5, 6);
        System.out.println("Disjoint: " + Collections.disjoint(list1, list2));
        
        // Fill and copy
        List<String> fillList = new ArrayList<>(Arrays.asList(new String[5]));
        Collections.fill(fillList, "Hello");
        System.out.println("Filled: " + fillList);
        
        List<String> copyList = new ArrayList<>(Arrays.asList(new String[3]));
        Collections.copy(copyList, List.of("A", "B", "C"));
        System.out.println("Copied: " + copyList);
        
        // Reverse and shuffle
        Collections.reverse(numbers);
        System.out.println("Reversed: " + numbers);
        
        Collections.shuffle(numbers);
        System.out.println("Shuffled: " + numbers);
        
        // Rotate
        Collections.rotate(numbers, 2);
        System.out.println("Rotated: " + numbers);
        
        // Swap
        Collections.swap(numbers, 0, 1);
        System.out.println("Swapped: " + numbers);
        
        // Enumeration
        List<String> list = List.of("Alice", "Bob", "Charlie");
        Enumeration<String> enumeration = Collections.enumeration(list);
        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement());
        }
    }
}
```

### Arrays Utility

```java
import java.util.*;

public class ArraysUtility {
    public static void main(String[] args) {
        // Sorting
        int[] numbers = {5, 2, 8, 1, 9, 3};
        Arrays.sort(numbers);
        System.out.println("Sorted: " + Arrays.toString(numbers));
        
        // Sorting with range
        int[] partial = {5, 2, 8, 1, 9, 3};
        Arrays.sort(partial, 1, 4);  // Sort from index 1 to 3
        System.out.println("Partial sort: " + Arrays.toString(partial));
        
        // Parallel sort
        int[] largeArray = new int[1000];
        Random random = new Random();
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = random.nextInt(1000);
        }
        Arrays.parallelSort(largeArray);
        
        // Binary search
        int index = Arrays.binarySearch(numbers, 8);
        System.out.println("Index of 8: " + index);
        
        // Fill
        int[] filled = new int[5];
        Arrays.fill(filled, 10);
        System.out.println("Filled: " + Arrays.toString(filled));
        
        // CopyOf
        int[] copied = Arrays.copyOf(numbers, numbers.length);
        System.out.println("Copied: " + Arrays.toString(copied));
        
        // CopyOfRange
        int[] range = Arrays.copyOfRange(numbers, 2, 5);
        System.out.println("Range: " + Arrays.toString(range));
        
        // Equals
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        System.out.println("Equal: " + Arrays.equals(arr1, arr2));
        
        // DeepEquals (for multi-dimensional arrays)
        int[][] m1 = {{1, 2}, {3, 4}};
        int[][] m2 = {{1, 2}, {3, 4}};
        System.out.println("Deep Equal: " + Arrays.deepEquals(m1, m2));
        
        // Compare (Java 9+)
        System.out.println("Compare: " + Arrays.compare(arr1, arr2));
        
        // Mismatch (Java 9+)
        System.out.println("Mismatch: " + Arrays.mismatch(arr1, arr2));
        
        // toString
        System.out.println("ToString: " + Arrays.toString(numbers));
        
        // DeepToString
        System.out.println("DeepToString: " + Arrays.deepToString(m1));
        
        // hashCode
        System.out.println("HashCode: " + Arrays.hashCode(arr1));
        
        // setAll (Java 8+)
        int[] setAll = new int[5];
        Arrays.setAll(setAll, i -> i * 2);
        System.out.println("SetAll: " + Arrays.toString(setAll));
        
        // parallelPrefix (Java 8+)
        int[] prefix = {1, 2, 3, 4, 5};
        Arrays.parallelPrefix(prefix, Integer::sum);
        System.out.println("Prefix: " + Arrays.toString(prefix));
        
        // stream
        IntStream stream = Arrays.stream(numbers);
        int sum = stream.sum();
        System.out.println("Sum: " + sum);
    }
}
```

---

## Concurrent Collections

### ConcurrentHashMap

```java
import java.util.concurrent.*;
import java.util.*;

public class ConcurrentHashMapExample {
    public static void main(String[] args) throws InterruptedException {
        // Creating ConcurrentHashMap
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        
        // Thread-safe operations
        map.put("Alice", 30);
        map.put("Bob", 25);
        map.put("Charlie", 35);
        
        // Atomic operations
        map.putIfAbsent("David", 28);
        map.compute("Alice", (key, value) -> value + 1);
        map.merge("Bob", 5, Integer::sum);
        
        // Parallel operations
        map.forEach(1, (key, value) -> 
            System.out.println(Thread.currentThread().getName() + ": " + key + " = " + value));
        
        // Search
        String result = map.search(1, (key, value) -> 
            value > 30 ? key : null);
        System.out.println("Found: " + result);
        
        // Reduce
        int maxValue = map.reduceValues(1, Integer::max);
        System.out.println("Max value: " + maxValue);
        
        // Count
        long count = map.mappingCount();
        System.out.println("Count: " + count);
        
        // Multi-threaded update
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            final int threadNum = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    concurrentMap.merge("counter", 1, Integer::sum);
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        System.out.println("Final counter: " + concurrentMap.get("counter"));
    }
}
```

### Other Concurrent Collections

```java
import java.util.concurrent.*;
import java.util.*;

public class OtherConcurrentCollections {
    public static void main(String[] args) throws InterruptedException {
        // ConcurrentLinkedQueue (unbounded, non-blocking)
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        queue.offer("Alice");
        queue.offer("Bob");
        queue.offer("Charlie");
        
        System.out.println("Poll: " + queue.poll());
        System.out.println("Peek: " + queue.peek());
        
        // ConcurrentLinkedDeque
        ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();
        deque.offer("Alice");
        deque.offerFirst("Bob");
        deque.offerLast("Charlie");
        
        System.out.println("Deque: " + deque);
        
        // CopyOnWriteArrayList (thread-safe, copy on write)
        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();
        cowList.add("Alice");
        cowList.add("Bob");
        cowList.add("Charlie");
        
        // Iteration is safe even with modifications
        for (String name : cowList) {
            System.out.println(name);
            cowList.add("David");  // Won't affect current iteration
        }
        
        System.out.println("After additions: " + cowList);
        
        // CopyOnWriteArraySet
        CopyOnWriteArraySet<String> cowSet = new CopyOnWriteArraySet<>();
        cowSet.add("Alice");
        cowSet.add("Bob");
        cowSet.add("Alice");  // Duplicate, won't be added
        
        System.out.println("Set: " + cowSet);
        
        // ConcurrentSkipListMap (sorted, concurrent)
        ConcurrentSkipListMap<String, Integer> skipMap = new ConcurrentSkipListMap<>();
        skipMap.put("Charlie", 35);
        skipMap.put("Alice", 30);
        skipMap.put("Bob", 25);
        
        System.out.println("SkipListMap: " + skipMap);
        
        // ConcurrentSkipListSet
        ConcurrentSkipListSet<String> skipSet = new ConcurrentSkipListSet<>();
        skipSet.add("Charlie");
        skipSet.add("Alice");
        skipSet.add("Bob");
        
        System.out.println("SkipListSet: " + skipSet);
        
        // BlockingQueue implementations
        // ArrayBlockingQueue (bounded)
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(5);
        arrayBlockingQueue.offer("Alice");
        arrayBlockingQueue.offer("Bob");
        arrayBlockingQueue.offer("Charlie");
        
        // LinkedBlockingQueue (optionally bounded)
        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
        linkedBlockingQueue.offer("Alice");
        linkedBlockingQueue.offer("Bob");
        
        // PriorityBlockingQueue
        PriorityBlockingQueue<Integer> priorityQueue = new PriorityBlockingQueue<>();
        priorityQueue.offer(5);
        priorityQueue.offer(2);
        priorityQueue.offer(8);
        
        System.out.println("Priority poll: " + priorityQueue.poll());
        
        // SynchronousQueue (zero capacity)
        SynchronousQueue<String> syncQueue = new SynchronousQueue<>();
        
        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                syncQueue.put("Message");
                System.out.println("Sent: Message");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                String message = syncQueue.take();
                System.out.println("Received: " + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
    }
}
```

---

## Performance Comparison

### List Performance

```java
import java.util.*;
import java.time.*;

public class ListPerformance {
    
    public static void main(String[] args) {
        int size = 100000;
        
        // ArrayList vs LinkedList
        System.out.println("=== List Performance ===");
        
        // ArrayList
        List<Integer> arrayList = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
        }
        long end = System.nanoTime();
        System.out.println("ArrayList add: " + (end - start) / 1_000_000 + " ms");
        
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            arrayList.get(i);
        }
        end = System.nanoTime();
        System.out.println("ArrayList get: " + (end - start) / 1_000_000 + " ms");
        
        // LinkedList
        List<Integer> linkedList = new LinkedList<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedList.add(i);
        }
        end = System.nanoTime();
        System.out.println("LinkedList add: " + (end - start) / 1_000_000 + " ms");
        
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            linkedList.get(i);
        }
        end = System.nanoTime();
        System.out.println("LinkedList get: " + (end - start) / 1_000_000 + " ms");
        
        // Vector
        List<Integer> vector = new Vector<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            vector.add(i);
        }
        end = System.nanoTime();
        System.out.println("Vector add: " + (end - start) / 1_000_000 + " ms");
        
        // CopyOnWriteArrayList
        List<Integer> cowList = new CopyOnWriteArrayList<>();
        start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            cowList.add(i);
        }
        end = System.nanoTime();
        System.out.println("CopyOnWriteArrayList add: " + (end - start) / 1_000_000 + " ms");
    }
}
```

### Set Performance

```java
import java.util.*;
import java.time.*;

public class SetPerformance {
    
    public static void main(String[] args) {
        int size = 100000;
        System.out.println("=== Set Performance ===");
        
        // HashSet
        Set<Integer> hashSet = new HashSet<>();
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            hashSet.add(i);
        }
        long end = System.nanoTime();
        System.out.println("HashSet add: " + (end - start) / 1_000_000 + " ms");
        
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            hashSet.contains(i);
        }
        end = System.nanoTime();
        System.out.println("HashSet contains: " + (end - start) / 1_000_000 + " ms");
        
        // TreeSet
        Set<Integer> treeSet = new TreeSet<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            treeSet.add(i);
        }
        end = System.nanoTime();
        System.out.println("TreeSet add: " + (end - start) / 1_000_000 + " ms");
        
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            treeSet.contains(i);
        }
        end = System.nanoTime();
        System.out.println("TreeSet contains: " + (end - start) / 1_000_000 + " ms");
        
        // LinkedHashSet
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedHashSet.add(i);
        }
        end = System.nanoTime();
        System.out.println("LinkedHashSet add: " + (end - start) / 1_000_000 + " ms");
        
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            linkedHashSet.contains(i);
        }
        end = System.nanoTime();
        System.out.println("LinkedHashSet contains: " + (end - start) / 1_000_000 + " ms");
        
        // EnumSet (for enum types)
        enum Color { RED, GREEN, BLUE, YELLOW, PURPLE }
        
        Set<Color> enumSet = EnumSet.of(Color.RED, Color.GREEN, Color.BLUE);
        start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            enumSet.contains(Color.RED);
        }
        end = System.nanoTime();
        System.out.println("EnumSet contains: " + (end - start) / 1_000_000 + " ms");
    }
}
```

### Map Performance

```java
import java.util.*;
import java.util.concurrent.*;
import java.time.*;

public class MapPerformance {
    
    public static void main(String[] args) {
        int size = 100000;
        System.out.println("=== Map Performance ===");
        
        // HashMap
        Map<Integer, Integer> hashMap = new HashMap<>();
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            hashMap.put(i, i);
        }
        long end = System.nanoTime();
        System.out.println("HashMap put: " + (end - start) / 1_000_000 + " ms");
        
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            hashMap.get(i);
        }
        end = System.nanoTime();
        System.out.println("HashMap get: " + (end - start) / 1_000_000 + " ms");
        
        // TreeMap
        Map<Integer, Integer> treeMap = new TreeMap<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            treeMap.put(i, i);
        }
        end = System.nanoTime();
        System.out.println("TreeMap put: " + (end - start) / 1_000_000 + " ms");
        
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            treeMap.get(i);
        }
        end = System.nanoTime();
        System.out.println("TreeMap get: " + (end - start) / 1_000_000 + " ms");
        
        // LinkedHashMap
        Map<Integer, Integer> linkedHashMap = new LinkedHashMap<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedHashMap.put(i, i);
        }
        end = System.nanoTime();
        System.out.println("LinkedHashMap put: " + (end - start) / 1_000_000 + " ms");
        
        // Hashtable (legacy, thread-safe)
        Map<Integer, Integer> hashtable = new Hashtable<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            hashtable.put(i, i);
        }
        end = System.nanoTime();
        System.out.println("Hashtable put: " + (end - start) / 1_000_000 + " ms");
        
        // ConcurrentHashMap
        Map<Integer, Integer> concurrentMap = new ConcurrentHashMap<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            concurrentMap.put(i, i);
        }
        end = System.nanoTime();
        System.out.println("ConcurrentHashMap put: " + (end - start) / 1_000_000 + " ms");
        
        // IdentityHashMap
        Map<Integer, Integer> identityMap = new IdentityHashMap<>();
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            identityMap.put(i, i);
        }
        end = System.nanoTime();
        System.out.println("IdentityHashMap put: " + (end - start) / 1_000_000 + " ms");
        
        // EnumMap
        enum Color { RED, GREEN, BLUE, YELLOW, PURPLE }
        Map<Color, Integer> enumMap = new EnumMap<>(Color.class);
        start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            enumMap.put(Color.RED, i);
        }
        end = System.nanoTime();
        System.out.println("EnumMap put: " + (end - start) / 1_000_000 + " ms");
    }
}
```

### Performance Summary Table

| Collection | Add | Remove | Contains | Get | Ordered | Thread-Safe |
|------------|-----|--------|----------|-----|---------|-------------|
| ArrayList | O(1)* | O(n) | O(n) | O(1) | Yes | No |
| LinkedList | O(1) | O(1) | O(n) | O(n) | Yes | No |
| HashSet | O(1) | O(1) | O(1) | N/A | No | No |
| TreeSet | O(log n) | O(log n) | O(log n) | N/A | Sorted | No |
| LinkedHashSet | O(1) | O(1) | O(1) | N/A | Insertion | No |
| HashMap | O(1) | O(1) | O(1) | O(1) | No | No |
| TreeMap | O(log n) | O(log n) | O(log n) | O(log n) | Sorted | No |
| LinkedHashMap | O(1) | O(1) | O(1) | O(1) | Insertion | No |
| ConcurrentHashMap | O(1) | O(1) | O(1) | O(1) | No | Yes |
| CopyOnWriteArrayList | O(n) | O(n) | O(n) | O(1) | Yes | Yes |
| ConcurrentSkipListMap | O(log n) | O(log n) | O(log n) | O(log n) | Sorted | Yes |

*Amortized

---

## Summary

The Java Collections Framework provides:

1. **List**: Ordered collection with duplicates (ArrayList, LinkedList, Vector)
2. **Set**: Unique elements (HashSet, TreeSet, LinkedHashSet)
3. **Map**: Key-value pairs (HashMap, TreeMap, LinkedHashMap)
4. **Queue**: FIFO ordering (PriorityQueue, ArrayDeque)
5. **Deque**: Double-ended queue (ArrayDeque, LinkedList)
6. **Comparable/Comparator**: Custom ordering
7. **Iterator**: Safe traversal and modification
8. **Collections**: Utility methods for collection manipulation
9. **Concurrent Collections**: Thread-safe collections for concurrent access
10. **Performance**: Choose the right collection for your use case

Understanding these collections and their performance characteristics is essential for writing efficient Java applications.

---

*Next: [Concurrency](../concurrency/README.md)*
