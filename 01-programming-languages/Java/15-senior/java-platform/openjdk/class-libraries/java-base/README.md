# java.base — Core Java APIs

The `java.base` module is the foundation of the Java platform. Every other module depends on it. It contains the most frequently used classes in Java.

## java.lang — Fundamental Classes

### Object

The root class of all Java objects:

- `equals()`, `hashCode()`, `toString()`
- `getClass()`, `notify()`, `notifyAll()`, `wait()`

### String and CharSequence

```java
String s = "hello";
int len = s.length();
String upper = s.toUpperCase();
String sub = s.substring(1, 3);
boolean eq = s.equals("hello");
```

String is immutable. Concatenation creates new String objects. Java 9+ uses `byte[]` backing for compact strings (Latin-1 or UTF-16).

### Primitives and Wrappers

| Primitive | Wrapper | Size |
|-----------|---------|------|
| `boolean` | `Boolean` | 1 bit |
| `byte` | `Byte` | 8 bits |
| `char` | `Character` | 16 bits |
| `short` | `Short` | 16 bits |
| `int` | `Integer` | 32 bits |
| `long` | `Long` | 64 bits |
| `float` | `Float` | 32 bits |
| `double` | `Double` | 64 bits |

### Math

```java
Math.max(a, b);
Math.min(a, b);
Math.abs(x);
Math.sqrt(x);
Math.random();         // [0.0, 1.0)
Math.PI;
Math.E;
```

### System

```java
System.out.println("Hello");
System.err.println("Error");
System.exit(0);
System.currentTimeMillis();
System.nanoTime();
System.getenv("HOME");
System.getProperty("java.version");
System.arraycopy(src, 0, dest, 0, len);
```

### Thread

```java
Thread t = new Thread(() -> { /* work */ });
t.start();
t.join();
t.interrupt();
t.isAlive();
Thread.currentThread();
Thread.sleep(1000);
```

## java.util — Collections and Utilities

### Collections Framework

```
Collection
├── List (ArrayList, LinkedList, Vector, CopyOnWriteArrayList)
├── Set (HashSet, TreeSet, LinkedHashSet, CopyOnWriteArraySet)
├── Queue (PriorityQueue, ArrayDeque)
└── Deque (ArrayDeque, LinkedList)

Map
├── HashMap
├── TreeMap
├── LinkedHashMap
├── ConcurrentHashMap
├── Hashtable (legacy)
└── WeakHashMap
```

### Stream API

```java
list.stream()
    .filter(x -> x > 0)
    .map(x -> x * 2)
    .sorted()
    .collect(Collectors.toList());
```

### Optional

```java
Optional<String> opt = Optional.ofNullable(value);
String result = opt.orElse("default");
opt.ifPresent(v -> System.out.println(v));
```

### Date/Time (java.time)

```java
LocalDate today = LocalDate.now();
LocalTime now = LocalTime.now();
Instant timestamp = Instant.now();
Duration d = Duration.ofHours(2);
Period p = Period.ofDays(30);
```

### Random and UUID

```java
Random rng = new Random();
int n = rng.nextInt(100);

UUID id = UUID.randomUUID();
```

## java.io — Input/Output

### Byte Streams

```
InputStream (abstract)
├── FileInputStream
├── ByteArrayInputStream
├── BufferedInputStream
├── DataInputStream
└── ObjectInputStream

OutputStream (abstract)
├── FileOutputStream
├── ByteArrayOutputStream
├── BufferedOutputStream
├── DataOutputStream
└── ObjectOutputStream
```

### Character Streams

```
Reader (abstract)
├── FileReader
├── StringReader
├── BufferedReader
└── InputStreamReader

Writer (abstract)
├── FileWriter
├── StringWriter
├── BufferedWriter
├── OutputStreamWriter
└── PrintWriter
```

### NIO (java.nio)

```
Channel (abstract)
├── FileChannel
├── SocketChannel
├── ServerSocketChannel
├── DatagramChannel

Buffer (abstract)
├── ByteBuffer
├── CharBuffer
├── IntBuffer
└── ...

Path
├── Paths.get("/path/to/file")
├── Path.resolve("subdir")
└── Path.normalize()
```

## java.math — Numeric Operations

```java
BigInteger big = new BigInteger("12345678901234567890");
BigDecimal dec = new BigDecimal("3.14159265358979");

big.add(another);
big.multiply(another);
dec.setScale(10, RoundingMode.HALF_UP);
```

## java.lang.invoke — Method Handles

```java
MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodHandle mh = lookup.findVirtual(String.class, "length",
    MethodType.methodType(int.class));
int len = (int) mh.invokeExact("hello");
```

Method handles provide a low-level, type-safe alternative to reflection for invoking methods dynamically.

## Key Source Files

| Path | Contents |
|------|----------|
| `src/java.base/share/classes/java/lang/` | java.lang classes |
| `src/java.base/share/classes/java/util/` | java.util classes |
| `src/java.base/share/classes/java/io/` | java.io classes |
| `src/java.base/share/classes/java/math/` | java.math classes |
| `src/java.base/share/classes/java/time/` | java.time classes |
| `src/java.base/share/classes/java/lang/invoke/` | Method handles |

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
