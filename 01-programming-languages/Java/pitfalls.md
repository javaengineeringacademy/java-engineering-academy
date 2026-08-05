# Java Pitfalls

> Common gotchas: autoboxing, string comparison, resource leaks, and more.

## 1. String Comparison with ==

```java
// Bad: comparing references
String a = "hello";
String b = "hello";
System.out.println(a == b);  // true (same pool reference)

String c = new String("hello");
System.out.println(a == c);  // false (different objects)

// Good: use equals()
System.out.println(a.equals(c));  // true

// Also: case-insensitive comparison
System.out.println(a.equalsIgnoreCase("HELLO"));
```

## 2. Autoboxing Pitfalls

```java
// Bad: integer comparison
Integer a = 127;
Integer b = 127;
System.out.println(a == b);  // true (cached range -128 to 127)

Integer c = 128;
Integer d = 128;
System.out.println(c == d);  // false (different objects)

// Bad: null autoboxing
Integer val = null;
int result = val + 1;  // NullPointerException

// Good: use equals() for comparison
System.out.println(c.equals(d));  // true

// Good: null-safe operations
Optional<Integer> opt = Optional.ofNullable(val);
int result = opt.orElse(0) + 1;
```

## 3. Mutable Field in Lambda

```java
// Bad: side effects in parallel stream
int[] counter = {0};
list.parallelStream().forEach(item -> counter[0]++);
System.out.println(counter[0]);  // undefined count

// Good: use reduce or count
long count = list.parallelStream()
    .filter(this::isValid)
    .count();
```

## 4. Resource Leaks

```java
// Bad: resource not closed
InputStream is = new FileInputStream("file.txt");
// if exception occurs, file not closed

// Good: try-with-resources
try (InputStream is = new FileInputStream("file.txt")) {
    // process file
}

// Bad: non-closeable resources
try (var executor = Executors.newFixedThreadPool(10)) {
    // executor is not AutoCloseable
}

// Good: shutdown explicitly
ExecutorService executor = Executors.newFixedThreadPool(10);
try {
    // process
} finally {
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);
}
```

## 5. ConcurrentModificationException

```java
// Bad: modifying collection during iteration
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
for (String s : list) {
    if (s.equals("b")) list.remove(s);  // ConcurrentModificationException
}

// Good: use Iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("b")) it.remove();
}

// Good: use removeIf
list.removeIf(s -> s.equals("b"));
```

## 6. HashMap Key Issues

```java
// Bad: mutable key
Map<byte[], String> map = new HashMap<>();
byte[] key = "key".getBytes();
map.put(key, "value");
System.out.println(map.get("key".getBytes()));  // null (different array reference)

// Good: use immutable key
Map<String, String> map = new HashMap<>();
map.put("key", "value");

// Bad: overriding equals without hashCode
class BadKey {
    String value;
    @Override public boolean equals(Object o) { /* ... */ }
    // missing hashCode override
}

// Good: override both
class GoodKey {
    String value;
    @Override public boolean equals(Object o) { /* ... */ }
    @Override public int hashCode() { return Objects.hash(value); }
}
```

## 7. NullPointer with Optional

```java
// Bad: null in Optional
Optional<User> opt = Optional.ofNullable(nullUser);  // null in Optional
opt.ifPresent(user -> process(user));  // NPE when accessing user

// Good: proper Optional creation
Optional<User> opt = Optional.ofNullable(userRepository.findById(id));
opt.ifPresent(this::process);
```

## 8. Integer Overflow

```java
// Bad: overflow
int max = Integer.MAX_VALUE;
int result = max + 1;  // wraps to Integer.MIN_VALUE

// Good: check for overflow
if (a > Integer.MAX_VALUE - b) {
    throw new ArithmeticException("Integer overflow");
}

// Good: use Math.addExact
try {
    int result = Math.addExact(a, b);
} catch (ArithmeticException e) {
    // overflow occurred
}
```

## 9. Unchecked Cast Warning

```java
// Bad: unchecked cast
List<String> list = (List<String>) object;  // warning

// Good: proper generic type checking
if (object instanceof List<?> list) {
    // use list safely
}
```

## 10. Exception Swallowing

```java
// Bad: swallowing exception
try {
    process();
} catch (Exception e) {
    // do nothing - bug hidden
}

// Bad: catching too broadly
try {
    specificOperation();
} catch (Exception e) {
    // catching unrelated exceptions
}

// Good: catch specific exceptions
try {
    specificOperation();
} catch (IOException e) {
    logger.error("IO error", e);
    throw new ServiceException("Failed", e);
}
```

## 11. Thread.sleep in Tests

```java
// Bad: unreliable timing
@Test
void shouldProcess() {
    service.processAsync();
    Thread.sleep(1000);  // flaky!
    verify(service).completed();
}

// Good: use Awaitility
@Test
void shouldProcess() {
    service.processAsync();
    await().atMost(5, SECONDS)
        .untilAsserted(() -> verify(service).completed());
}
```

## 12. Static Initialization Order

```java
// Bad: static initialization order dependency
class A {
    static int x = 10;
}

class B {
    static int y = A.x + 5;  // depends on A.x being initialized
}

// Good: avoid static initialization dependencies
class B {
    private static int y;
    static {
        y = A.x + 5;
    }
}
```

## 13. equals/hashCode Contract Violation

```java
// Bad: equals uses fields not in hashCode
class User {
    String name;
    String email;
    
    @Override
    public boolean equals(Object o) {
        return o instanceof User u && 
               name.equals(u.name) && email.equals(u.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);  // missing email
    }
}

// Good: use same fields
@Override
public int hashCode() {
    return Objects.hash(name, email);
}
```

## 14. Integer Cache Issue

```java
// Bad: integer caching
Integer a = 200;
Integer b = 200;
System.out.println(a == b);  // false (outside cache range)

// Good: use equals
System.out.println(a.equals(b));  // true

// Or use int
int a = 200;
int b = 200;
System.out.println(a == b);  // true (primitive comparison)
```

## 15. Uninitialized Fields

```java
// Bad: uninitialized final field
class User {
    final String name;  // compiler error if not initialized
}

// Good: initialize in constructor
class User {
    final String name;
    public User(String name) {
        this.name = Objects.requireNonNull(name);
    }
}
```

## References

- [Java Pitfalls - Michael C. Daconta](https://www.wiley.com/en-us/Java+Pitfalls%3A+Time-Saving+Tips+and+Tricks%2C+Second+Edition-p-9780471237518)
- [Common Java Mistakes - Baeldung](https://www.baeldung.com/java-common-mistakes)

---
**Prerequisites:** [Java core-concepts](core-concepts.md)
**Related:** [Java best-practices](best-practices.md) | [Java debugging](debugging.md)
**Next:** [Java debugging](debugging.md)
