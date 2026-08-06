# String.java Source Code Walkthrough

A deep dive into OpenJDK's `String.java` implementation.

## Internal Storage (Java 9+)

```java
public final class String implements java.io.Serializable, Comparable<String>, CharSequence {

    @Stable
    private final byte[] value;
    private final byte coder;

    static final byte LATIN1 = 0;
    static final byte UTF16 = 1;
}
```

**Key points:**
- `value`: byte array (not char array since Java 9 — Compact Strings)
- `coder`: identifies encoding (LATIN1 = 0, UTF16 = 1)
- Both fields are `final` — String is immutable
- `@Stable` annotation hints to JVM the value won't change

## String Pool Mechanism

```java
private static class StringTable extends StringTableLoader {
    // A fixed-size hash table for deduplication
    private static final int DEFAULT_SIZE = 256;
    private static int size;
    private static int count;
    private static final StringEntry[] elements;
}
```

**Interning process:**
1. String literals are automatically interned (placed in pool)
2. `intern()` method explicitly adds to pool
3. Pool is a hash table in native memory (not heap)
4. Pool reduces memory for duplicate literals
5. `String.intern()` has O(n) cost for first insertion, O(1) for lookups

**Pre-Java 7 vs Post-Java 7:**
- Pre-Java 7: Pool lived in PermGen (limited space, risk of OOM)
- Post-Java 7+: Pool lives in heap (unlimited, GC-managed)

## hashCode() Implementation

```java
private int hash;

public int hashCode() {
    int h = hash;
    if (h == 0 && !hashIsZero) {
        h = isLatin1() ? hashLatin1() : hashUTF16();
        if (h == 0) {
            hashIsZero = true;
        } else {
            hash = h;
        }
    }
    return h;
}

private int hashLatin1() {
    byte[] val = this.value;
    int h = 0;
    for (byte v : val) {
        h = 31 * h + (v & 0xff);
    }
    return h;
}

private int hashUTF16() {
    byte[] val = this.value;
    int h = 0;
    for (int i = 0; i < val.length; i += 2) {
        h = 31 * h + ((val[i] & 0xff) << 8 | (val[i + 1] & 0xff));
    }
    return h;
}
```

**Hash formula:** `h = 31 * h + char[i]`

**Why 31?**
- Odd prime — even number would lose bits via multiplication
- 31 = 32 - 1 → `31 * i = (i << 5) - i` (bit shift optimization)
- Good distribution of hash values
- Historically shown to produce fewer collisions than other small primes

**Caching:** Hash is computed once and stored in `hash` field. Subsequent calls return cached value instantly.

## equals() Implementation

```java
public boolean equals(Object anObject) {
    if (this == anObject) return true;
    if (anObject instanceof String aString) {
        if (coder() == aString.coder()) {
            return isLatin1() ? StringLatin1.equals(value, aString.value)
                              : StringUTF16.equals(value, aString.value);
        }
    }
    return false;
}

// Latin1 comparison (optimized path)
static boolean equals(byte[] value, byte[] other) {
    if (value.length == other.length) {
        for (int i = 0; i < value.length; i++) {
            if (value[i] != other[i]) return false;
        }
        return true;
    }
    return false;
}
```

**Comparison strategy:**
1. Reference check (`this == anObject`) — O(1)
2. Type check (`instanceof`) — O(1)
3. Length check (inside coder-specific method) — O(1)
4. Byte-by-byte comparison — O(n)

**Optimization:** Latin1 strings (all ASCII-compatible) use simpler, faster comparison than UTF-16 strings.

## substring() Implementation

**Pre-Java 7 (O(1) but dangerous):**
```java
public String substring(int beginIndex, int endIndex) {
    return new String(value, beginIndex, endIndex - beginIndex, true);
    // Shared the backing array — memory leak risk!
}
```

**Post-Java 7 (O(n) but safe):**
```java
public String substring(int beginIndex, int endIndex) {
    if (beginIndex == 0) {
        if (endIndex == value.length) return this;
        return newString(value, beginIndex, endIndex);
    }
    return (isLatin1())
        ? StringLatin1.substring(value, beginIndex, endIndex)
        : StringUTF16.substring(value, beginIndex, endIndex);
}
```

**Why the change?**
- Pre-Java 7: Substrings shared the backing array (saves memory but leaks entire array)
- Post-Java 7: Substrings copy data (uses more memory but prevents memory leaks)
- The old approach caused OOM errors in long-running apps with many substrings

## concat() Implementation

```java
public String concat(String str) {
    if (!str.isEmpty()) {
        if (isLatin1()) {
            return StringLatin1.concat(value, str.value());
        } else {
            return StringUTF16.concat(value, str.value());
        }
    }
    return this;
}

// Latin1 concat path
static byte[] concat(byte[] value, byte[] other) {
    int len = value.length + other.length;
    byte[] result = Arrays.copyOf(value, len);
    System.arraycopy(other, 0, result, value.length, other.length);
    return result;
}
```

**Note:** `concat` creates a new String each time. For many concatenations, use `StringBuilder` instead (O(n²) vs O(n)).

## indexOf() Implementation

```java
public int indexOf(int ch, int fromIndex) {
    if (isLatin1()) {
        return StringLatin1.indexOf(value, ch, fromIndex);
    } else {
        return StringUTF16.indexOf(value, ch, fromIndex);
    }
}

// Latin1 indexOf (byte-level)
static int indexOf(byte[] value, int ch, int fromIndex) {
    if (ch < 0 || ch > 0xff) return -1;
    for (int i = fromIndex; i < value.length; i++) {
        if (value[i] == (byte) ch) return i;
    }
    return -1;
}
```

**Search strategies:**
- `indexOf(String str)`: brute force O(n*m)
- `lastIndexOf(String str)`: searches from end
- Two-string `indexOf`: uses Boyer-Moore-like optimizations internally

## valueOf() Factory Methods

```java
public static String valueOf(Object obj) {
    return (obj == null) ? "null" : obj.toString();
}

public static String valueOf(char[] data) {
    return new String(data);
}

public static String valueOf(boolean b) {
    return b ? "true" : "false";
}

public static String valueOf(int i) {
    return Integer.toString(i);
}

public static String valueOf(float f) {
    return Float.toString(f);
}
```

**Pattern:** Factory methods hide constructor complexity and can cache results.

## Why String is Final

```java
public final class String implements java.io.Serializable, Comparable<String>, CharSequence {
```

**Reasons:**
1. **Security:** Prevents subclass from overriding methods to corrupt strings
2. **Integrity:** Guarantees immutability cannot be broken by inheritance
3. **Optimization:** JVM can inline String methods and cache hashes safely
4. **Trust:** Security managers rely on string immutability for class loading, network URLs, etc.

## Compact Strings (Java 9+)

**Before Java 9:** Every `char` was 16 bits (UTF-16), even for pure ASCII text.

**After Java 9:** Strings use Latin-1 (8-bit) when possible, UTF-16 only when needed.

```java
// Memory savings example:
// "Hello" before: 10 bytes (5 chars × 2 bytes each)
// "Hello" after:  5 bytes (5 bytes Latin-1) + 1 byte coder
```

**Impact:**
- ~50% memory reduction for ASCII-heavy strings
- ~50% memory reduction for many internationalized strings (when only Latin chars)
- Faster `equals()` and `hashCode()` for Latin-1 strings
- Measurable GC pause time reductions

**How it works:**
1. `String` checks if all characters fit in Latin-1 range (0-255)
2. If yes, stores as single-byte array (LATIN1)
3. If any char exceeds 255, stores as double-byte array (UTF16)
4. `coder` field tracks which encoding is used
5. Operations branch based on `coder` value

## Summary

String is one of the most critical classes in Java. Understanding its internals helps with:
- Memory-efficient coding (avoiding unnecessary concatenation)
- Performance tuning (StringBuilder vs String)
- Debugging (hashCode caching, pool behavior)
- API design (immutability benefits)

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
