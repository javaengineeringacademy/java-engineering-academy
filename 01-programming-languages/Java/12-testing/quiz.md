# Debugging Quiz

## Question 1 (MCQ)
What is the purpose of a stack trace in exception handling?
- A) To fix the exception automatically
- B) To show the sequence of method calls that led to the exception
- C) To log all variable values
- D) To restart the application

**Answer: B**
**Explanation:** A stack trace shows the call hierarchy from the point of exception origin back to the main method, helping developers trace the execution path and identify the root cause.

---

## Question 2 (MCQ)
What is a memory leak in Java?
- A) When the JVM runs out of physical RAM
- B) When objects are no longer needed but are still referenced, preventing garbage collection
- C) When a thread is stuck in an infinite loop
- D) When a file handle is not closed

**Answer: B**
**Explanation:** A memory leak occurs when objects are unintentionally kept referenced (e.g., in static collections, unclosed resources), so the garbage collector cannot reclaim their memory even though they're no longer needed.

---

## Question 3 (MCQ)
What is the difference between System.out.println debugging and using a proper logging framework?
- A) println is faster
- B) Logging frameworks provide configurable log levels, structured output, and can be disabled without code changes
- C) println provides more information
- D) There is no difference

**Answer: B**
**Explanation:** Logging frameworks like SLF4J/Logback offer log levels (DEBUG, INFO, WARN, ERROR), structured formats, file rotation, and can be configured externally. println is hardcoded and lacks these features.

---

## Question 4 (MCQ)
Which JVM flag enables remote debugging on port 5005?
- A) `-XX:+RemoteDebug=5005`
- B) `-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005`
- C) `-Ddebug.port=5005`
- D) `-Xdebug:remote:5005`

**Answer: B**
**Explanation:** The `-agentlib:jdwp` flag loads the Java Debug Wire Protocol agent, which allows an external debugger to connect to the specified port for remote debugging.

---

## Question 5 (Code Output)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5};
        int sum = 0;

        for (int i = 0; i <= numbers.length; i++) {
            sum += numbers[i];
        }

        System.out.println("Sum: " + sum);
    }
}
```

**Answer:** ArrayIndexOutOfBoundsException at runtime
**Explanation:** The loop uses `i <= numbers.length` instead of `i < numbers.length`. When i reaches 5 (which equals numbers.length), accessing `numbers[5]` throws an ArrayIndexOutOfBoundsException because valid indices are 0-4.

---

## Question 6 (Code Output)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        String s1 = "hello";
        String s2 = new String("hello");
        String s3 = s2.intern();

        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
    }
}
```

**Answer:** false, true
**Explanation:** `s1` and `s2` are different objects (literal vs new), so `s1 == s2` is false. `s3` is the interned version from the string pool, which is the same object as `s1`, so `s1 == s3` is true.

---

## Question 7 (Bug Finding)
Find the bug:

```java
public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        for (String s : list) {
            if (s.equals("B")) {
                list.remove(s);
            }
        }

        System.out.println(list);
    }
}
```

**Bug:** Modifying a collection during for-each iteration throws `ConcurrentModificationException`. The for-each loop uses an iterator internally, and structural modifications invalidate it.
**Fix:** Use an iterator or removeIf:
```java
list.removeIf(s -> s.equals("B"));
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
public class Main {
    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("T1: holding lock1");
                try { Thread.sleep(100); } catch (Exception e) {}
                synchronized (lock2) {
                    System.out.println("T1: holding lock2");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("T2: holding lock2");
                try { Thread.sleep(100); } catch (Exception e) {}
                synchronized (lock1) {
                    System.out.println("T2: holding lock1");
                }
            }
        });

        t1.start();
        t2.start();
    }
}
```

**Bug:** Classic deadlock scenario. Thread 1 holds lock1 and waits for lock2. Thread 2 holds lock2 and waits for lock1. Both threads are stuck forever.
**Fix:** Always acquire locks in the same order:
```java
// Both threads should acquire lock1 first, then lock2
```

---

## Question 9 (Scenario-based)
Your application throws a ConcurrentModificationException in production, but only under high load. The stack trace points to a HashMap being iterated while another thread modifies it. How should you diagnose and fix this?

- A) Add try-catch to suppress the exception
- B) Use a thread-safe collection like ConcurrentHashMap, or synchronize access to the map
- C) Use a larger HashMap
- D) Disable concurrent access

**Answer: B**
**Explanation:** The root cause is unsynchronized concurrent access. ConcurrentHashMap provides thread-safe operations without full synchronization. Alternatively, synchronize all access to a regular HashMap.

---

## Question 10 (Architecture Decision)
You need to implement a logging system for a distributed microservice architecture. Logs from multiple services need to be correlated and searchable. How should you design this?

- A) Each service writes logs to its own file
- B) Use structured logging (JSON format) with correlation IDs, centralized log aggregation (ELK stack), and SLF4J with MDC for context propagation
- C) Print all logs to stdout
- D) Store logs in a relational database

**Answer: B**
**Explanation:** Structured logging (JSON) enables machine parsing. Correlation IDs (propagated via MDC) link related log entries across services. Centralized aggregation (ELK/Splunk) provides unified search and monitoring. This is the industry standard for distributed observability.

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MathTest {
    @Test
    void testAddition() {
        assertEquals(4, 2 + 2, "Basic addition failed");
        assertTrue(4 > 3, "4 should be greater than 3");
        assertNotEquals(5, 2 + 2, "5 should not equal 4");
    }

    @Test
    void testArray() {
        int[] expected = {1, 2, 3};
        int[] actual = {1, 2, 3};
        assertArrayEquals(expected, actual, "Arrays should match");
    }

    public static void main(String[] args) {
        new MathTest().testAddition();
        new MathTest().testArray();
        System.out.println("All tests passed");
    }
}
```

A) All tests passed
B) AssertionError: 5 should not equal 4
C) AssertionError: Basic addition failed
D) Compilation error

**Answer: A**
**Explanation:** All assertions pass: `assertEquals(4, 2+2)` → 4==4 ✓, `assertTrue(4>3)` → true ✓, `assertNotEquals(5, 4)` → 5≠4 ✓, `assertArrayEquals` → arrays match ✓. If run via main, all assertions pass and "All tests passed" prints. JUnit assertions throw AssertionError only when conditions fail.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class MockTest {
    interface UserDAO {
        String findUser(int id);
    }

    public static void main(String[] args) {
        UserDAO mockDAO = mock(UserDAO.class);
        when(mockDAO.findUser(1)).thenReturn("Alice");

        System.out.println(mockDAO.findUser(1));
        System.out.println(mockDAO.findUser(2));

        verify(mockDAO, times(1)).findUser(1);
        verify(mockDAO, times(1)).findUser(2);
        verifyNoMoreInteractions(mockDAO);
        System.out.println("Verification passed");
    }
}
```

A) Alice null Verification passed
B) Alice Alice Verification passed
C) Alice null AssertionError
D) null null Verification passed

**Answer: A**
**Explanation:** `when(mockDAO.findUser(1)).thenReturn("Alice")` stubs the mock to return "Alice" for id=1. `findUser(2)` returns null (default for unstubbed methods returning Object). `verify` confirms each method was called once. `verifyNoMoreInteractions` ensures no unexpected calls. Output: `Alice null Verification passed`.

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
import org.junit.jupiter.api.*;

public class OrderTest {
    static StringBuilder log = new StringBuilder();

    @BeforeAll
    static void initAll() { log.append("BeforeAll "); }

    @BeforeEach
    void init() { log.append("BeforeEach "); }

    @Test
    void test1() { log.append("Test1 "); }

    @Test
    void test2() { log.append("Test2 "); }

    @AfterEach
    void tearDown() { log.append("AfterEach "); }

    @AfterAll
    static void cleanupAll() { log.append("AfterAll "); }

    public static void main(String[] args) {
        OrderTest t = new OrderTest();
        t.initAll();
        t.init(); t.test1(); t.tearDown();
        t.init(); t.test2(); t.tearDown();
        t.cleanupAll();
        System.out.println(log);
    }
}
```

A) BeforeAll BeforeEach Test1 AfterEach BeforeEach Test2 AfterEach AfterAll
B) BeforeAll BeforeEach Test1 Test2 AfterEach AfterAll
C) BeforeEach BeforeAll Test1 AfterEach Test2 AfterAll
D) BeforeAll Test1 Test2 AfterAll

**Answer: A**
**Explanation:** JUnit execution order: `@BeforeAll` runs once → "BeforeAll ". Then for each test: `@BeforeEach` → test → `@AfterEach`. Test1: "BeforeEach Test1 AfterEach ". Test2: "BeforeEach Test2 AfterEach ". `@AfterAll` runs once at end → "AfterAll ". Output: `BeforeAll BeforeEach Test1 AfterEach BeforeEach Test2 AfterEach AfterAll`.

