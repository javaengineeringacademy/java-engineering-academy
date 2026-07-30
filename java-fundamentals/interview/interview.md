# Sprint 1 Interview Questions - Java Fundamentals

---

## 📋 Core Java Basics

### Q1: Why is Java called "Platform Independent"?
**Answer:** Java compiles to bytecode (`.class` files) which runs on the Java Virtual Machine (JVM). The JVM is platform-specific, but the bytecode is the same across all platforms. This is "Write Once, Run Anywhere" (WORA).

**Follow-up:** What's the difference between JDK, JRE, and JVM?
- **JVM:** Abstract machine that executes bytecode
- **JRE:** JVM + standard libraries (runtime environment)
- **JDK:** JRE + development tools (compiler, debugger, etc.)

---

### Q2: What is the difference between `==` and `.equals()`?
**Answer:** 
- `==` compares **references** (memory addresses) for objects, values for primitives
- `.equals()` compares **content** (can be overridden)

```java
String a = "hello";
String b = new String("hello");
a == b        // false (different objects)
a.equals(b)   // true (same content)
```

**Key point:** For String, always use `.equals()`. For primitives, use `==`.

---

### Q3: Explain the `main` method signature.
```java
public static void main(String[] args)
```
- `public`: Accessible by JVM (outside class)
- `static`: Callable without instance (JVM doesn't create object)
- `void`: No return value to JVM
- `main`: Specific name JVM looks for
- `String[] args`: Command-line arguments

---

### Q4: What are the 8 primitive types in Java?
| Type | Size | Default | Wrapper |
|------|------|---------|---------|
| `byte` | 8-bit | 0 | Byte |
| `short` | 16-bit | 0 | Short |
| `int` | 32-bit | 0 | Integer |
| `long` | 64-bit | 0L | Long |
| `float` | 32-bit | 0.0f | Float |
| `double` | 64-bit | 0.0d | Double |
| `char` | 16-bit | '\u0000' | Character |
| `boolean` | ~1-bit | false | Boolean |

---

## 🔢 Data Types & Conversion

### Q5: What is the output of `System.out.println(10 / 3)`?
**Answer:** `3` (integer division truncates decimal)

**Follow-up:** How to get `3.333...`?
```java
10 / 3.0        // 3.333... (double)
(double) 10 / 3 // 3.333...
10.0 / 3        // 3.333...
```

---

### Q6: What is autoboxing and unboxing?
**Answer:**
- **Autoboxing:** Primitive → Wrapper (automatic) `Integer i = 10;`
- **Unboxing:** Wrapper → Primitive `int j = i;`

**Danger:** NullPointerException when unboxing null
```java
Integer n = null;
int x = n; // NPE at runtime!
```

---

### Q7: Why is `char` 16-bit in Java?
**Answer:** Java uses UTF-16 for `char`. Originally designed for Unicode 1.0 (16-bit was enough). Now supplementary characters (emojis) need 2 `char`s (surrogate pairs).

---

## 🧮 Operators

### Q8: Explain short-circuit evaluation.
**Answer:** 
- `&&`: If left is `false`, right is **not evaluated**
- `||`: If left is `true`, right is **not evaluated**

```java
if (str != null && str.length() > 0) // Safe! NPE avoided
if (false && expensiveMethod())      // expensiveMethod() never called
```

---

### Q9: What is the difference between `>>` and `>>>`?
**Answer:**
- `>>` (Signed right shift): Preserves sign bit (fills with sign bit)
- `>>>` (Unsigned right shift): Always fills with 0

```java
-8 >> 1  = -4  (11111000 → 11111100)
-8 >>> 1 = 2147483644 (11111000 → 01111100)
```

---

## 🔄 Control Flow

### Q10: What is a switch expression (Java 12+)?
**Answer:** Returns a value, no fall-through, exhaustive for enums.

```java
String day = switch (d) {
    case MONDAY, FRIDAY, SUNDAY -> "Weekend";
    case TUESDAY -> "Meeting";
    default -> "Workday";
};
```

---

### Q11: Can you use `break` without a loop or switch?
**Answer:** Only with **labels** (labeled break):

```java
outer: for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 1) break outer; // Breaks OUTER loop
    }
}
```

---

## 📦 Arrays & Strings

### Q12: What is the output?
```java
String s1 = "hello";
String s2 = "hello";
String s3 = new String("hello");
System.out.println(s1 == s2); // true (String pool)
System.out.println(s1 == s3); // false (heap)
System.out.println(s1.equals(s3)); // true
```

---

### Q13: How do you compare two arrays for equality?
**Answer:**
```java
// 1D arrays
Arrays.equals(arr1, arr2);

// Multi-dimensional
Arrays.deepEquals(arr1, arr2);
```

---

### Q14: Why is String immutable?
**Answer:**
1. **String Pool:** Sharing requires immutability
2. **Security:** Parameters (file paths, URLs) can't be changed
3. **Thread-safety:** Immutable objects are inherently thread-safe
4. **Performance:** Hashcode caching, substring optimization (pre-Java 7u6)
5. **Class Loading:** Security in class names

---

### Q15: String vs StringBuilder vs StringBuffer?
| Class | Mutable | Thread-Safe | Performance |
|-------|---------|-------------|-------------|
| String | No | Yes (immutable) | Fast for reads |
| StringBuilder | Yes | No | **Fastest for building** |
| StringBuffer | Yes | Yes | Slower (synchronized) |

**Rule:** Use `StringBuilder` (single-threaded), `StringBuffer` (legacy multi-threaded)

---

## ⚙️ Methods

### Q16: Is Java pass-by-value or pass-by-reference?
**Answer:** **Pass-by-value ALWAYS.**

```java
void modify(int x) { x = 20; }           // Primitive: copy of value
void modify(StringBuilder sb) { sb.append("!"); } // Reference: copy of reference
```

For objects: The reference is passed by value. You can modify the object, but not reassign the caller's reference.

---

### Q17: What is method overloading vs overriding?
| Aspect | Overloading | Overriding |
|--------|-------------|------------|
| **Signature** | Different params | Same signature |
| **Return** | Can differ | Must be covariant |
| **Access** | Can be more restrictive | Can't be more restrictive |
| **Static** | Can overload static | Can't override static |
| **Binding** | Compile-time (static) | Runtime (dynamic) |

---

### Q18: What are varargs? Any restrictions?
**Answer:** Variable arguments `type... args`. Treated as array inside method.

**Restrictions:**
- Only ONE varargs per method
- Must be LAST parameter
- Can pass zero arguments

```java
void print(int... nums) { } // OK
// void print(int... a, int... b) { } // ERROR!
```

---

## 🧠 Advanced / Tricky

### Q19: What is the output?
```java
Integer a = 127;
Integer b = 127;
Integer c = 128;
Integer d = 128;
System.out.println(a == b); // true
System.out.println(c == d); // false
```

**Explanation:** Integer caches -128 to 127. Outside range = new objects.

---

### Q20: What is the output?
```java
String s = "hello";
s.toUpperCase();
System.out.println(s); // "hello" (unchanged!)
```

**Explanation:** String is immutable. `toUpperCase()` returns NEW string.

---

### Q21: Explain the difference between `break` and `continue`.
- `break`: Exits the loop/switch entirely
- `continue`: Skips to next iteration of loop

---

### Q22: What happens if you don't put `break` in switch?
**Answer:** **Fall-through** - executes all subsequent cases until break or end.

```java
switch (2) {
    case 1: System.out.print("1");
    case 2: System.out.print("2");
    case 3: System.out.print("3");
}
// Output: "23"
```

---

### Q23: Can a method have multiple return statements?
**Answer:** Yes, but only one executes per call. All paths must return (except void).

---

### Q24: What is the "dangling else" problem?
**Answer:** `else` matches nearest `if`. Use braces to avoid ambiguity:

```java
if (x > 0)
    if (y > 0) System.out.println("A");
else System.out.println("B"); // Which if? The inner one!
```

---

### Q25: How do you reverse a string efficiently?
**Answer:** 
```java
// Best: StringBuilder
new StringBuilder(str).reverse().toString();

// Or manual (no extra object for very large strings)
char[] chars = str.toCharArray();
for (int i = 0, j = chars.length - 1; i < j; i++, j--) {
    char temp = chars[i];
    chars[i] = chars[j];
    chars[j] = temp;
}
return new String(chars);
```

---

## 🎯 Behavioral / Design

### Q26: When would you use a `do-while` instead of `while`?
**Answer:** When loop body must execute **at least once** (e.g., menu display, input validation).

---

### Q27: What is the time complexity of `StringBuilder.append()`?
**Answer:** Amortized **O(1)**. Internally uses array that doubles when full.

---

### Q28: How would you swap two integers without a temp variable?
**Answer:** 
```java
a = a + b;
b = a - b;
a = a - b;

// Or XOR swap (only for integers)
a ^= b; b ^= a; a ^= b;
```

**Note:** Not recommended in practice (readability, overflow risk).

---

### Q29: What is the maximum size of an array in Java?
**Answer:** `Integer.MAX_VALUE - 5` (≈ 2.14 billion elements). Limited by `int` index and JVM header overhead.

---

### Q30: Explain the difference between `length`, `length()`, and `size()`.
| Usage | Method/Field |
|-------|--------------|
| Array | `arr.length` (field) |
| String | `str.length()` (method) |
| Collection | `list.size()` (method) |

---

## 💡 Pro Tips for Interviews

1. **Always clarify:** "May I assume input is valid?" "What's the expected input size?"
2. **Think aloud:** Explain your approach before coding
3. **Edge cases:** Empty, null, single element, duplicates, overflow
4. **Complexity:** State time/space complexity
5. **Trade-offs:** "StringBuilder is faster but not thread-safe"

---

## 📝 Quick Reference Card

| Concept | Key Point |
|---------|-----------|
| `main` signature | `public static void main(String[] args)` |
| Integer division | `10/3 = 3` (not 3.33) |
| String comparison | `.equals()` not `==` |
| String mutability | Immutable (use StringBuilder) |
| Pass-by-value | Always (references passed by value) |
| Switch expression | Returns value, no fall-through |
| Varargs | `type...` last parameter only |
| Array equality | `Arrays.equals()` / `deepEquals()` |
| Loop else | `else` belongs to nearest `if` |
| Default char | `'\u0000'` |