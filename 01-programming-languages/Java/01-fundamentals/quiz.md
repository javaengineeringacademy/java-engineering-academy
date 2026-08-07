# Java Fundamentals Quiz

## Question 1 (Production Scenario)
Your application receives user input as Strings and needs to convert them to numeric types for calculations. A junior developer writes `int value = (int) someDoubleString;` and gets unexpected results. Which approach handles type conversion correctly?

- A) Use `Integer.parseInt()` for int conversion and `Double.parseDouble()` for double conversion
- B) Cast directly using `(int)` and `(double)` for all conversions
- C) Use `toString()` method on the string
- D) Use `Object` casting for all types

**Answer: A**
**Explanation:** String to numeric conversion requires parsing methods like `Integer.parseInt()` or `Double.parseDouble()`. Direct casting `(int)` only works between numeric primitives/wrappers (not from String). Using parsing methods also throws `NumberFormatException` for invalid input, allowing proper error handling in production.

---

## Question 2 (Production Scenario)
A financial application calculates interest using `double` arithmetic. Customers report that balances are off by pennies after thousands of transactions. What is the root cause and the fix?

- A) The JVM has a bug with double precision — use float instead
- B) Floating-point precision errors accumulate — use `BigDecimal` for monetary calculations
- C) The database is rounding values — change database settings
- D) Network latency is causing race conditions — add synchronization

**Answer: B**
**Explanation:** `double` uses IEEE 754 binary floating-point, which cannot exactly represent many decimal fractions (e.g., 0.1). Errors compound over many operations. `BigDecimal` provides arbitrary-precision decimal arithmetic, essential for financial calculations. This is a fundamental production issue in any money-handling system.

---

## Question 3 (Debugging)
Your application works in development but crashes in production with `NullPointerException` at an auto-unboxing line. The code is:

```java
Map<String, Integer> config = loadConfig();
int timeout = config.get("timeout");
```

What is the most likely cause?

- A) The HashMap is null
- B) The key "timeout" is missing from the map, so `get()` returns null, and auto-unboxing null throws NPE
- C) The int type cannot store the config value
- D) The loadConfig() method throws an exception

**Answer: B**
**Explanation:** `HashMap.get()` returns `null` if the key doesn't exist. When assigning `null` to `int` via auto-unboxing, Java calls `intValue()` on null, throwing `NullPointerException`. The fix is to use `config.getOrDefault("timeout", 30)` or check for null before unboxing.

---

## Question 4 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        Object obj = "Hello";
        String str = (String) obj;
        Integer num = (Integer) obj;
        System.out.println(str);
    }
}
```

A) Hello
B) ClassCastException at runtime
C) null
D) Compilation error

**Answer: B**
**Explanation:** `obj` is a String. The cast to `String` succeeds. However, `Integer num = (Integer) obj` attempts to cast a String to Integer, which throws a `ClassCastException` at runtime because String and Integer are unrelated types.

---

## Question 5 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        String s1 = "Java";
        String s2 = "Java";
        String s3 = new String("Java");
        String s4 = s3.intern();

        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1 == s4);
    }
}
```

A) true false true
B) false false true
C) true true true
D) true false false

**Answer: A**
**Explanation:** `s1` and `s2` are string literals that reference the same object in the string pool, so `s1 == s2` is true. `s3` is a new String object on the heap, so `s1 == s3` is false. `s3.intern()` returns the pool reference (same as `s1`), so `s1 == s4` is true.

---

## Question 6 (Production Scenario)
Your team maintains a legacy system where a method signature is `void process(int x, double y)`. A new requirement needs `void process(double x, int y)`. Adding the second overload causes a compilation error at call sites like `process(5, 10)`. How should you resolve this?

- A) Remove the first method and only keep the new one
- B) Rename the methods to `processIntDouble` and `processDoubleInt`
- C) Make both methods take `double` parameters instead
- D) Use varargs to handle both cases

**Answer: B**
**Explanation:** When `process(5, 10)` is called, Java cannot determine which overload is more specific — both require one widening conversion. This ambiguity causes a compilation error. Renaming methods with descriptive names eliminates ambiguity and makes the API clearer for other developers.

---

## Question 7 (Debugging)
A production service throws `NullPointerException` intermittently. The stack trace points to:

```java
Integer count = cache.get(key);
int total = count * factor;
```

The cache is a `ConcurrentHashMap<String, Integer>`. What is happening?

- A) ConcurrentHashMap is not thread-safe and loses entries
- B) Another thread removed the key between `get()` and the unboxing, so `count` is null
- C) The `factor` variable is null
- D) The cache is returning the wrong type

**Answer: B**
**Explanation:** This is a classic TOCTOU (time-of-check-time-of-use) race condition. Between `cache.get(key)` returning a value and the auto-unboxing on the next line, another thread could remove the key. The fix is to use `cache.getOrDefault(key, 0)` or store the result in a local variable and null-check before unboxing.

---

## Question 8 (Production Scenario)
You are building a data pipeline that processes millions of integer IDs from a CSV file. The IDs range from 1 to 100,000. You need fast lookups to check if an ID exists. Which approach is most memory-efficient?

- A) `ArrayList<Integer>` with `contains()` method
- B) `HashSet<Integer>` for O(1) lookups
- C) `boolean[]` array indexed by ID value
- D) `TreeSet<Integer>` for sorted access

**Answer: C**
**Explanation:** A `boolean[]` of size 100,001 uses ~100KB of memory (1 byte per boolean). A `HashSet<Integer>` would use ~4-8 bytes per entry plus overhead (~2-4MB). For dense integer IDs, a direct-mapped array provides O(1) lookup with minimal memory. This is a common optimization in production systems processing numeric identifiers.

---

## Question 9 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        int a = 5;
        int b = 2;
        float c = a / b;
        float d = (float) a / b;
        System.out.println(c + " " + d);
    }
}
```

A) 2.5 2.5
B) 2.0 2.5
C) 2.5 2.0
D) 2 2.5

**Answer: B**
**Explanation:** `a / b` performs integer division (5 / 2 = 2), then assigns to float `c` as 2.0. `(float) a / b` casts `a` to float first, so `5.0 / 2` performs floating-point division, resulting in 2.5. Output: `2.0 2.5`.
