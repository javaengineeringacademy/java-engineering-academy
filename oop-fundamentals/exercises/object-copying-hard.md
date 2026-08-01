# Object Copying - Hard Exercises

## Exercise 1: Deep Copy Using Serialization

### Problem Statement
Implement deep copy for a complex object graph using Java serialization.

### Requirements
1. Create a class hierarchy with at least 3 levels of nesting
2. Implement `Serializable` for all classes
3. Create a utility method that performs deep copy using serialization
4. Handle all serialization exceptions properly
5. Ensure the copied object is completely independent

### Starter Code
```java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Create your own class hierarchy here
// Example: Company -> Department -> Team -> Employee

public class SerializationCopyUtil {

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepCopy(T object) throws IOException, ClassNotFoundException {
        // TODO: Implement deep copy using serialization
        // 1. Create ByteArrayOutputStream
        // 2. Create ObjectOutputStream
        // 3. Serialize the object
        // 4. Create ByteArrayInputStream
        // 5. Create ObjectInputStream
        // 6. Deserialize and return
    }
}
```

### Expected Behavior
```java
// Create complex object graph
Employee emp1 = new Employee(1, "Alice", 50000);
Employee emp2 = new Employee(2, "Bob", 60000);
Team team = new Team("Frontend", List.of(emp1, emp2));
Department dept = new Department("Engineering", List.of(team));

// Deep copy
Department copy = SerializationCopyUtil.deepCopy(dept);

// Verify independence
copy.getTeams().get(0).getEmployees().get(0).setName("Charlie");
System.out.println(dept.getTeams().get(0).getEmployees().get(0).getName()); // Should print "Alice"
```

### Hints
- All classes in the hierarchy must implement `Serializable`
- Use try-with-resources for stream management
- Consider using a wrapper class if the object graph has non-serializable parts

### Evaluation Criteria
- [ ] All classes implement Serializable
- [ ] Deep copy utility handles complex object graphs
- [ ] All exceptions are properly handled
- [ ] Copied object is completely independent
- [ ] Works with circular references (if handled)

---

## Exercise 2: Generic Deep Copy Utility

### Problem Statement
Create a generic deep copy utility that can handle any object type, including collections and arrays.

### Requirements
1. Create a utility class `DeepCopyUtil`
2. Implement generic `deepCopy(T object)` method
3. Handle special cases:
   - Primitives and wrapper types
   - Strings
   - Arrays (both primitive and object arrays)
   - Collections (List, Set, Map)
   - Custom objects with fields
4. Use reflection for custom objects

### Starter Code
```java
import java.lang.reflect.*;
import java.util.*;

public class DeepCopyUtil {

    public static <T> T deepCopy(T object) {
        // TODO: Implement generic deep copy
        // Handle different types appropriately
    }

    private static Object deepCopyArray(Object array) {
        // TODO: Implement array deep copy
    }

    private static Object deepCopyCollection(Object collection) {
        // TODO: Implement collection deep copy
    }

    private static Object deepCopyObject(Object obj) {
        // TODO: Implement object deep copy using reflection
    }
}
```

### Expected Behavior
```java
// Should work with any type
int[] intArray = {1, 2, 3};
int[] copiedIntArray = DeepCopyUtil.deepCopy(intArray);

List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
List<String> copiedList = DeepCopyUtil.deepCopy(list);

Map<String, List<Integer>> map = new HashMap<>();
map.put("key1", Arrays.asList(1, 2, 3));
Map<String, List<Integer>> copiedMap = DeepCopyUtil.deepCopy(map);

// All copies should be independent
copiedIntArray[0] = 99;
copiedList.set(0, "z");
copiedMap.get("key1").set(0, 99);

// Originals should be unchanged
System.out.println(intArray[0]); // Should print 1
System.out.println(list.get(0)); // Should print "a"
System.out.println(map.get("key1").get(0)); // Should print 1
```

### Hints
- Use `instanceof` to check object types
- For arrays, use `Array.newInstance()` and `Array.set()`
- For collections, create new collection and deep copy each element
- For custom objects, use reflection to access fields

### Evaluation Criteria
- [ ] Handles primitives and wrapper types
- [ ] Handles String correctly
- [ ] Handles primitive arrays
- [ ] Handles object arrays
- [ ] Handles List, Set, and Map collections
- [ ] Handles custom objects with nested fields
- [ ] All copies are truly independent

---

## Exercise 3: Performance Comparison of Copy Methods

### Problem Statement
Create a performance benchmark to compare different copy methods.

### Requirements
1. Implement 4 different copy methods:
   - Copy constructor
   - Clone method
   - Serialization
   - Reflection-based copy
2. Create test objects of varying complexity
3. Measure and compare execution time
4. Analyze results and document findings

### Starter Code
```java
import java.io.*;
import java.lang.reflect.*;

public class CopyPerformanceBenchmark {

    // Test class with various field types
    static class TestObject implements Serializable, Cloneable {
        private int id;
        private String name;
        private double value;
        private int[] data;
        private List<String> list;

        // Constructor

        // TODO: Implement copy constructor

        // TODO: Implement clone()
    }

    // TODO: Implement serialization copy

    // TODO: Implement reflection copy

    public static void main(String[] args) {
        int iterations = 100000;
        TestObject original = createTestObject();

        // Warm up JVM
        for (int i = 0; i < 1000; i++) {
            copyByConstructor(original);
            copyByClone(original);
            copyBySerialization(original);
            copyByReflection(original);
        }

        // Benchmark each method
        long startTime, endTime;

        // TODO: Time copy constructor
        // TODO: Time clone
        // TODO: Time serialization
        // TODO: Time reflection

        // TODO: Print results in a table format
    }
}
```

### Expected Behavior
```
Benchmark Results (100000 iterations):
+-------------------+------------+-------------+
| Method            | Time (ms)  | Avg (ns/op) |
+-------------------+------------+-------------+
| Copy Constructor  | 45         | 450         |
| Clone             | 52         | 520         |
| Serialization     | 1250       | 12500       |
| Reflection        | 380        | 3800        |
+-------------------+------------+-------------+
```

### Hints
- Use `System.nanoTime()` for precise timing
- Run benchmarks multiple times and take averages
- Consider JIT compilation effects (warm-up runs)
- Test with objects of different sizes

### Evaluation Criteria
- [ ] All 4 copy methods are implemented correctly
- [ ] Performance measurements are accurate
- [ ] Results are presented in a readable format
- [ ] Analysis includes trade-offs (speed, memory, safety)
- [ ] Code is well-organized and reusable
