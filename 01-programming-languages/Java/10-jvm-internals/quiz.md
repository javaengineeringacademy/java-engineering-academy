# JVM Internals Quiz

## Question 1 (Production Scenario)
Your Java application is experiencing frequent Full GC pauses that last several seconds. The heap size is 4GB. Users report timeouts during these pauses. Which diagnostic approach should you take first?

- A) Increase heap size to 8GB
- B) Enable GC logging and analyze heap usage patterns with JFR (Java Flight Recorder)
- C) Switch to ZGC immediately
- D) Add more RAM to the server

**Answer: B**
**Explanation:** Before tuning, you need data. GC logging and JFR provide insights into what's being collected, pause times, and allocation rates. This data-driven approach ensures you address the root cause rather than guessing. Increasing heap without understanding the problem may worsen pause times.

---

## Question 2 (Production Scenario)
You are deploying a latency-sensitive trading application that requires sub-millisecond GC pauses. The heap will be 16GB. Which GC algorithm should you choose?

- A) G1 GC (default)
- B) Serial GC
- C) ZGC or Shenandoah
- D) Parallel GC

**Answer: C**
**Explanation:** ZGC and Shenandoah are designed for ultra-low latency with pauses typically under 10ms regardless of heap size. They are ideal for latency-sensitive applications where even short pauses are unacceptable. G1 GC has longer pauses, and Parallel/Serial GC have stop-the-world pauses.

---

## Question 3 (Debugging)
A production application throws `OutOfMemoryError: Java heap space` even though heap usage appears low in monitoring. The code uses:

```java
public byte[] processData(byte[] input) {
    return Arrays.copyOf(input, input.length * 2);
}
```

When processing 10,000 concurrent requests, each with 10MB input, the application crashes. What is the bug?

- A) The JVM heap is too small
- B) Each request allocates 20MB (2x input), so 10,000 requests need 200GB — far exceeding heap
- C) `Arrays.copyOf()` is inefficient
- D) The input data is corrupted

**Answer: B**
**Explanation:** This is a memory estimation error. Each request allocates 20MB (input + copy). With 10,000 concurrent requests, peak memory is ~200GB. The fix: process requests in batches, use streaming (NIO), or implement backpressure to limit concurrency based on available memory.

---

## Question 4 (Production Scenario)
Your application uses many short-lived objects that become garbage quickly. GC logs show frequent young generation collections but long Full GC pauses. Which tuning approach helps?

- A) Increase old generation size
- B) Increase young generation size to reduce promotion rate
- C) Disable garbage collection
- D) Use a smaller heap

**Answer: B**
**Explanation:** Increasing the young generation allows objects to live longer before promotion to old generation. Short-lived objects die in the young generation, avoiding Full GC. This reduces the frequency of expensive Full GC pauses at the cost of slightly longer young GC pauses.

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

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
class Parent {
    static { System.out.print("Parent-static "); }
    { System.out.print("Parent-instance "); }
    Parent() { System.out.print("Parent-ctor "); }
}

class Child extends Parent {
    static { System.out.print("Child-static "); }
    { System.out.print("Child-instance "); }
    Child() { System.out.print("Child-ctor "); }
}

public class Main {
    public static void main(String[] args) {
        new Child();
        System.out.println();
        new Child();
    }
}
```

A) Parent-static Child-static Parent-instance Parent-ctor Child-instance Child-ctor then Parent-instance Parent-ctor Child-instance Child-ctor
B) Parent-static Child-static Parent-ctor Child-ctor then Parent-ctor Child-ctor
C) Parent-static Child-static Parent-instance Parent-ctor Child-instance Child-ctor then Parent-instance Parent-ctor Child-instance Child-ctor
D) Parent-static Child-static Parent-instance Parent-ctor Child-instance Child-ctor then Child-ctor

**Answer: A**
**Explanation:** Class loading: static blocks execute once when class is loaded → "Parent-static Child-static". First `new Child()`: Parent instance init → Parent constructor → Child instance init → Child constructor → "Parent-instance Parent-ctor Child-instance Child-ctor". Second `new Child()`: static blocks don't repeat, only instance init and constructors run → "Parent-instance Parent-ctor Child-instance Child-ctor". Output: static blocks once, then constructors twice.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
import java.lang.ref.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Object strong = new Object();
        WeakReference<Object> weak = new WeakReference<>(strong);

        System.out.println("Before GC: " + weak.get());
        strong = null;
        System.gc();
        Thread.sleep(100);
        System.out.println("After GC: " + weak.get());
    }
}
```

A) Before GC: java.lang.Object@... After GC: null
B) Before GC: null After GC: null
C) Before GC: java.lang.Object@... After GC: java.lang.Object@...
D) Compilation error

**Answer: A**
**Explanation:** Before GC, `strong` holds a strong reference to the object, so `weak.get()` returns the object. Setting `strong = null` removes the strong reference. `System.gc()` suggests the JVM run garbage collection. If GC runs, the weakly-referenced object is eligible for collection, so `weak.get()` returns null. Output: `Before GC: java.lang.Object@... After GC: null`.

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        int x = 10;
        Integer y = x; // autoboxing
        int z = y;     // unboxing

        System.out.println(x + " " + y + " " + z);
        System.out.println(x == y);
        System.out.println(y.equals(x));
    }
}
```

A) 10 10 10 true true
B) 10 10 10 true false
C) 10 Integer@... 10 false true
D) Compilation error

**Answer: A**
**Explanation:** `Integer y = x` autoboxes int 10 to Integer (using Integer cache for -128 to 127). `int z = y` unboxes Integer back to int 10. `x == y` compares int with Integer — Integer is unboxed for comparison, 10 == 10 is true. `y.equals(x)` autoboxes x to Integer, then compares values — true. Output: `10 10 10 true true`.

