# Java Platform Quiz

## Question 1 (Code Output)
What is the output of this code involving type erasure?

```java
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TypeErasureDemo {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        
        System.out.println(strings.getClass() == integers.getClass());
        System.out.println(strings.getClass().getName());
    }
}
```

A) false, java.util.ArrayList
B) true, java.util.ArrayList
C) true, java.util.ArrayList<java.lang.String>
D) false, java.util.ArrayList<java.lang.String>

**Answer: B**
**Explanation:** Due to type erasure, `List<String>` and `List<Integer>` have the same runtime class: `java.util.ArrayList`. Generic type parameters exist only at compile time. `getClass()` returns the raw class without generic information.

---

## Question 2 (Architecture)
Why is `String` immutable in Java?

A) To save memory through string interning
B) For thread safety and security (class loading, network connections)
C) Because the JVM requires all objects to be immutable
D) For faster garbage collection

**Answer: B**
**Explanation:** String immutability provides: (1) thread safety without synchronization, (2) security for class loading and network URLs, (3) safe hashing (hashCode cached), (4) string interning. The primary reason is security — if strings were mutable, class names and file paths could be changed at runtime, compromising the entire JVM security model.

---

## Question 3 (Code Output)
What does this JEP version check print?

```java
public class JepCheck {
    public static void main(String[] args) {
        System.out.println(Runtime.version().feature());
        System.out.println(System.getProperty("java.specification.version"));
    }
}
```
(Run on JDK 21)

A) 21, 21
B) 21.0, 21
C) 21, 21.0
D) 17, 17

**Answer: A**
**Explanation:** `Runtime.version().feature()` returns the major version number (21). `java.specification.version` also returns the major version "21" (not "21.0"). Both return the feature release number.

---

## Question 4 (JVM Internals)
What happens when the JVM encounters a `ClassNotFoundException` vs `NoClassDefFoundError`?

A) They are identical — both mean the class file is missing
B) `ClassNotFoundException` is thrown by `Class.forName()` at runtime; `NoClassDefFoundError` is thrown by the JVM when a referenced class cannot be found during linking
C) `ClassNotFoundException` is a checked exception; `NoClassDefFoundError` is an error
D) Both B and C are correct

**Answer: D**
**Explanation:** `ClassNotFoundException` is a checked exception thrown when dynamic class loading fails (e.g., `Class.forName()`). `NoClassDefFoundError` is an error thrown by the JVM when a class that was available at compile time cannot be found at runtime (e.g., missing dependency JAR). Both describe different failure modes of class loading.

---

## Question 5 (Code Output)
What is the output of this code using method handles?

```java
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class MethodHandleDemo {
    public static void main(String> args) throws Throwable {
        var lookup = MethodHandles.lookup();
        var method = lookup.findVirtual(String.class, "toUpperCase", MethodType.methodType(String.class));
        var result = (String) method.invokeExact("hello");
        System.out.println(result);
    }
}
```

A) HELLO
B) hello
C) Compilation error
D) Throws IllegalAccessException

**Answer: A**
**Explanation:** `MethodHandles.lookup()` creates a lookup context. `findVirtual` finds the instance method `toUpperCase()` on `String`. `invokeExact` calls the method with exact type matching. The result is "HELLO" — the string uppercased.

---

## Question 6 (JVM Internals)
Which GC algorithm is the default in JDK 21?

A) Parallel GC
B) G1GC
C) ZGC
D) Shenandoah

**Answer: B**
**Explanation:** G1GC (Garbage-First) has been the default since JDK 9. It balances throughput and latency by dividing the heap into regions and collecting the most garbage-filled regions first. ZGC and Shenandoah are low-latency alternatives but are not defaults.

---

## Question 7 (Code Output)
What is the output of this switch expression?

```java
sealed interface Shape permits Circle, Rectangle, Triangle {}
record Circle(double radius) implements Shape {}
record Rectangle(double w, double h) implements Shape {}
record Triangle(double a, double b, double c) implements Shape {}

public class Main {
    static String describe(Shape shape) {
        return switch (shape) {
            case Circle c -> "Circle: " + c.radius();
            case Rectangle r -> "Rectangle: " + r.w() + "x" + r.h();
            case Triangle t -> "Triangle";
        };
    }
    
    public static void main(String[] args) {
        System.out.println(describe(new Circle(5.0)));
    }
}
```

A) Circle: 5.0
B) Compilation error — switch not exhaustive
C) Runtime error — missing default
D) Circle

**Answer: A**
**Explanation:** Sealed interfaces with `permits` allow the compiler to verify exhaustiveness. The switch covers all three permitted types (Circle, Rectangle, Triangle), so no `default` is needed. Pattern matching extracts the record components. Output: "Circle: 5.0".

---

## Question 8 (JEP Awareness)
Which JEP introduced pattern matching for `instanceof`?

A) JEP 394
B) JEP 395
C) JEP 441
D) JEP 406

**Answer: A**
**Explanation:** JEP 394 (finalized in JDK 16) introduced pattern matching for `instanceof`. JEP 395 added records and sealed classes. JEP 441 added pattern matching for switch. JEP 406 was preview for pattern matching for switch.

---

## Question 9 (JVM Deep Dive)
What is the purpose of the Metaspace in the JVM?

A) Stores instance data for objects
B) Stores class metadata, method bytecode, and constant pool — off-heap, managed by the JVM
C) Caches frequently accessed strings
D) Stores thread-local variable data

**Answer: B**
**Explanation:** Metaspace (replaced PermGen in JDK 8) stores class metadata, method bytecodes, constant pool, and annotation data. It resides in native memory (off-heap), growing dynamically. This avoids the `PermGen space` errors common in older JVMs.

---

## Question 10 (Java Evolution)
What is the significance of Project Loom in the Java ecosystem?

A) It adds native compilation to Java
B) It introduces virtual threads, enabling millions of concurrent threads with blocking code
C) It adds a new garbage collector
D) It replaces the class loader subsystem

**Answer: B**
**Explanation:** Project Loom (finalized in JDK 21 as JEP 444) introduces virtual threads. Virtual threads are lightweight threads managed by the JVM (not the OS), enabling millions of concurrent threads. They use carrier (platform) threads for execution but are much cheaper to create and switch, bringing the simplicity of blocking code to high-concurrency scenarios.
