# Text Processing Quiz

## Question 1 (Production Scenario)
Your application processes log files containing millions of lines. Each line must be parsed, filtered, and transformed. A developer uses `String +=` in a loop to build output, and the application becomes extremely slow after processing 100K lines. What is the root cause?

- A) The JVM garbage collector is overloaded
- B) String concatenation with `+=` creates new String objects each iteration, causing O(n²) time and excessive memory allocation
- C) The file I/O is the bottleneck
- D) The regex patterns are too complex

**Answer: B**
**Explanation:** Each `+=` creates a new String object and copies all previous characters. For n iterations, this results in O(n²) time. `StringBuilder.append()` modifies a mutable buffer in-place with amortized O(1) per append, resulting in O(n) total time. For 100K iterations, this difference is dramatic (seconds vs. minutes).

---

## Question 2 (Production Scenario)
Your application generates HTTP response headers. You need to concatenate 50 header key-value pairs into a single string. The concatenation happens once per request, serving 10,000 requests per second. Which approach is optimal?

- A) Use `String +=` in a loop
- B) Use `StringBuilder` with initial capacity set to expected size
- C) Use `String.concat()` method
- D) Use `StringBuffer` for thread safety

**Answer: B**
**Explanation:** Pre-sizing `StringBuilder` avoids repeated resizing. For 50 headers, estimating ~100 chars each means setting capacity to ~5000. `String +=` creates intermediate objects. `StringBuffer` has synchronization overhead. `String.concat()` only joins two strings. For single-threaded concatenation with known size, `StringBuilder` with pre-allocated capacity is optimal.

---

## Question 3 (Debugging)
A production service processes user input and throws `StackOverflowError`. The code builds a string recursively:

```java
public String buildString(List<String> parts) {
    if (parts.isEmpty()) return "";
    return parts.get(0) + buildString(parts.subList(1, parts.size()));
}
```

What is the bug?

- A) The method should use `StringBuilder` instead of recursion
- B) `subList()` creates a view that causes infinite recursion
- C) The base case is incorrect
- D) String concatenation is not allowed in recursion

**Answer: B**
**Explanation:** `subList(1, parts.size())` creates a view of the original list. The `isEmpty()` check works correctly, but for large lists, deep recursion exhausts the stack. The fix: use an iterative approach with `StringBuilder`, or use `String.join()` with `Collectors.joining()`.

---

## Question 5 (Code Output)
What does this code print?

```java
public class Main {
    public static void main(String[] args) {
        String s = "Hello";
        String t = "Hello";
        String u = new String("Hello");

        System.out.println(s == t);
        System.out.println(s == u);
        System.out.println(s.equals(u));
    }
}
```

**Answer:** true, false, true
**Explanation:** `s` and `t` both reference the same string pool literal "Hello", so `s == t` is true. `u` is a new object on the heap, so `s == u` is false. `equals()` compares content, so `s.equals(u)` is true.

---

## Question 6 (Code Output)
What does this code print?

```java
public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("abc");
        sb.append("def").reverse().insert(0, "X");
        System.out.println(sb);
    }
}
```

**Answer:** Xfedcba
**Explanation:** Append "def" → "abcdef". Reverse → "fedcba". Insert "X" at index 0 → "Xfedcba".

---

## Question 7 (Bug Finding)
Find the bug:

```java
public class Main {
    public static void main(String[] args) {
        String result = "";
        for (int i = 0; i < 10000; i++) {
            result += "a";
        }
        System.out.println(result.length());
    }
}
```

**Bug:** Using `+=` on String in a loop creates a new String object every iteration, resulting in O(n²) time complexity and excessive memory allocation. This causes significant performance degradation.
**Fix:** Use StringBuilder:
```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append("a");
}
System.out.println(sb.length());
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
public class Main {
    public static void main(String[] args) {
        String input = "  Hello, World!  ";
        String trimmed = input.trim();
        if (input == trimmed) {
            System.out.println("Same object");
        } else {
            System.out.println("Different objects");
        }
    }
}
```

**Bug:** The code compares String references with `==`. Since `trim()` returns a new String when whitespace is removed, the references will differ. If the intent was to check if trimming changed anything, use `.equals()` or check `input.length() != trimmed.length()`.
**Fix:**
```java
if (input.equals(trimmed)) {
    System.out.println("No whitespace to trim");
} else {
    System.out.println("Whitespace was trimmed");
}
```

---

## Question 9 (Scenario-based)
You need to process a large text file (2GB) line by line, filtering lines containing a specific keyword, and writing matches to an output file. Which approach is most memory-efficient?

- A) Read the entire file into a String, split by newlines, filter, and write
- B) Use BufferedReader with try-with-resources to process line by line
- C) Use Scanner to read the entire file into memory
- D) Convert the file to a byte array and process

**Answer: B**
**Explanation:** BufferedReader reads lines lazily without loading the entire file into memory. This approach uses constant memory (O(1)) regardless of file size, making it suitable for large files.

---

## Question 10 (Architecture Decision)
You are building a text processing pipeline that needs to: (1) read millions of records from CSV, (2) validate each field using regex patterns, (3) transform data, and (4) write results to a database. How should you design this?

- A) Load all records into memory, process, then write to DB
- B) Use a streaming approach: BufferedReader for input, Stream API for processing, batch inserts for DB writes
- C) Process records one at a time with single DB inserts
- D) Use parallel threads without synchronization

**Answer: B**
**Explanation:** Streaming approach minimizes memory usage. BufferedReader lazily reads lines, Stream API chains operations with lazy evaluation, and batch inserts reduce DB round-trips. This provides optimal throughput while keeping memory usage constant.

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        String result = "";
        for (int i = 0; i < 5; i++) {
            result += (char)('A' + i);
        }
        System.out.println(result);
    }
}
```

A) ABCDE
B) A B C D E
C) Compilation error
D) 6566676869

**Answer: A**
**Explanation:** String concatenation with `+=` builds the string by appending characters. `('A' + 0)` is 'A', `('A' + 1)` is 'B', etc. The `(char)` cast converts the int arithmetic result to a character. After 5 iterations, result is "ABCDE". String concatenation works with characters.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        StringBuilder sb1 = new StringBuilder("Hello");
        StringBuffer sb2 = new StringBuffer("Hello");

        sb1.append(" World");
        sb2.append(" World");

        System.out.println(sb1 == sb1.toString());
        System.out.println(sb2 == sb2.toString());
    }
}
```

A) true true
B) false false
C) true false
D) false true

**Answer: B**
**Explanation:** Both StringBuilder and StringBuffer return a new String object from `toString()`. `sb1 == sb1.toString()` compares a StringBuilder reference with a String reference — these are different types, so `==` returns false. Same for `sb2 == sb2.toString()`. Output: `false false`.

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        String s1 = new String("hello");
        String s2 = new String("hello");
        String s3 = "hello";

        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1.intern() == s3);
    }
}
```

A) false false true
B) false true true
C) true false true
D) false false false

**Answer: A**
**Explanation:** `s1` and `s2` are two different objects created with `new`, so `s1 == s2` is false. `s3` is a string literal from the pool, and `s1` is a new object, so `s1 == s3` is false. `s1.intern()` returns the pool reference for "hello", which is the same object as `s3`, so `s1.intern() == s3` is true. Output: `false false true`.
