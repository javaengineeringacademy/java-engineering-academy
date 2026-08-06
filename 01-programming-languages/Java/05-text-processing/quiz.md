# Text Processing Quiz

## Question 1 (MCQ)
Why is StringBuilder preferred over String concatenation in loops?
- A) StringBuilder uses less memory and avoids creating intermediate String objects
- B) StringBuilder is always faster in all scenarios
- C) String concatenation doesn't work in loops
- D) StringBuilder is thread-safe while String is not

**Answer: A**
**Explanation:** String concatenation in a loop creates a new String object each iteration (O(n²) time). StringBuilder modifies a mutable buffer in-place, resulting in O(n) time complexity.

---

## Question 2 (MCQ)
What is the difference between `String.equals()` and `==` for comparing two strings?
- A) They are identical in behavior
- B) `==` compares object references, while `equals()` compares the actual character content
- C) `equals()` compares references, `==` compares content
- D) `==` is faster and should always be used

**Answer: B**
**Explanation:** `==` checks if two references point to the same object in memory. `equals()` compares the actual string content character by character. Always use `equals()` for string content comparison.

---

## Question 3 (MCQ)
What is the default capacity of a StringBuilder and how does it grow?
- A) Capacity 8, doubles on resize
- B) Capacity 16, grows by doubling current capacity + 2
- C) Capacity 32, grows by adding 16
- D) Capacity is unlimited

**Answer: B**
**Explanation:** StringBuilder starts with a capacity of 16 characters. When it needs to grow, the new capacity is calculated as `(oldCapacity + 1) * 2`, ensuring amortized O(1) append operations.

---

## Question 4 (MCQ)
What does `String.intern()` return?
- A) A new String object
- B) A canonical representation from the string pool
- C) The hash code of the string
- D) A lowercase version of the string

**Answer: B**
**Explanation:** `intern()` checks the string pool for an equal string. If found, it returns the pool reference. If not, it adds the string to the pool and returns that reference. This saves memory for repeated strings.

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
