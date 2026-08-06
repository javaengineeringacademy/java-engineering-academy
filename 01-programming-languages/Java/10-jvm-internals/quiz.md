# JVM Internals Quiz

## Question 1 (MCQ)
What is the purpose of the JVM's Method Area?
- A) Stores object instances
- B) Stores class metadata, static variables, and constant pool
- C) Stores local method variables
- D) Stores native method implementations

**Answer: B**
**Explanation:** The Method Area stores class metadata, static variables, constant pool, and method bytecode. It is shared among all threads.

---

## Question 2 (MCQ)
Which GC algorithm became the default in Java 9 and is designed for large heaps with predictable pause times?
- A) Serial GC
- B) Parallel GC
- C) G1 GC
- D) ZGC

**Answer: C**
**Explanation:** G1 (Garbage-First) GC became the default in Java 9. It divides the heap into regions and prioritizes collecting regions with the most garbage, providing predictable pause times.

---

## Question 3 (MCQ)
What happens during the "Linking" phase of class loading?
- A) Reading the .class file from disk
- B) Executing static initializers
- C) Verifying bytecode, preparing memory, and resolving references
- D) Allocating heap memory for objects

**Answer: C**
**Explanation:** Linking consists of three sub-phases: Verification (checking bytecode validity), Preparation (allocating memory for static fields), and Resolution (replacing symbolic references with direct references).

---

## Question 4 (MCQ)
Which of the following is a GC Root?
- A) An object referenced by another unreferenced object
- B) A local variable in an active method
- C) An object in the old generation
- D) A weak reference

**Answer: B**
**Explanation:** GC Roots include local variables in active frames, static fields, JNI references, active threads, and monitors. Objects reachable from GC Roots are not eligible for garbage collection.

---

## Question 5 (Code Output)
What does this code print?

```java
public class Main {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Max Memory: " + runtime.maxMemory() / 1024 / 1024 + " MB");
        System.out.println("Available Processors: " + runtime.availableProcessors());
    }
}
```

**Answer:** Max Memory: [varies] MB and Available Processors: [varies]
**Explanation:** The output depends on the system configuration. `maxMemory()` returns the maximum heap size, and `availableProcessors()` returns the number of processors available to the JVM.

---

## Question 6 (Code Output)
What does this code print?

```java
public class Main {
    public static void main(String[] args) {
        System.out.println(int.class.getName());
        System.out.println(int[].class.getName());
        System.out.println(Object.class.getSuperclass());
    }
}
```

**Answer:** int, [I, null
**Explanation:** `int.class.getName()` returns "int" for primitive types. `int[].class.getName()` returns "[I" (JVM internal representation). `Object.class.getSuperclass()` returns null because Object is the root class.

---

## Question 7 (Bug Finding)
Find the bug:

```java
public class Main {
    public static void main(String[] args) {
        List<byte[]> memoryLeak = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            memoryLeak.add(new byte[1024 * 1024]); // 1MB each
        }
        System.out.println("Allocated " + memoryLeak.size() + " MB");
    }
}
```

**Bug:** This is a memory leak simulation. The list holds references to large byte arrays, preventing garbage collection. While not a "bug" in the traditional sense, it demonstrates how unintentional references cause OutOfMemoryError.
**Fix:** Clear the list when done, or use weak references if the data should be GC-eligible:
```java
memoryLeak.clear(); // Allow GC to reclaim memory
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
public class Main {
    public static void main(String[] args) {
        try {
            infiniteRecursion();
        } catch (StackOverflowError e) {
            System.out.println("Stack overflow caught!");
        }
    }

    static void infiniteRecursion() {
        infiniteRecursion();
    }
}
```

**Bug:** Catching `StackOverflowError` is generally not recommended. It's an `Error`, not an `Exception`, indicating a fatal condition. While the catch technically works, it's better to fix the recursion rather than catch it.
**Fix:** Add a base case to prevent infinite recursion:
```java
static void recursion(int depth) {
    if (depth <= 0) return;
    recursion(depth - 1);
}
```

---

## Question 9 (Scenario-based)
Your Java application is experiencing frequent Full GC pauses that last several seconds. The heap size is 4GB. Which diagnostic approach should you take first?

- A) Increase heap size to 8GB
- B) Enable GC logging and analyze heap usage patterns with JFR (Java Flight Recorder)
- C) Switch to ZGC immediately
- D) Add more RAM to the server

**Answer: B**
**Explanation:** Before tuning, you need data. GC logging and JFR provide insights into what's being collected, pause times, and allocation rates. This data-driven approach ensures you address the root cause rather than guessing.

---

## Question 10 (Architecture Decision)
You are deploying a latency-sensitive trading application that requires sub-millisecond GC pauses. The heap will be 16GB. Which GC algorithm should you choose?

- A) G1 GC (default)
- B) Serial GC
- C) ZGC or Shenandoah
- D) Parallel GC

**Answer: C**
**Explanation:** ZGC and Shenandoah are designed for ultra-low latency with pauses typically under 10ms regardless of heap size. They are ideal for latency-sensitive applications where even short pauses are unacceptable. G1 GC has longer pauses, and Parallel/Serial GC have stop-the-world pauses.
