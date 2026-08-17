# Exercises: Try-with-Resources in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Basic TWR

### Problem

Write a method `readAll(String content)` that uses TWR with a `StringReader` and `BufferedReader` to read all lines and return them joined with newlines.

### Starter Code

```java
import java.io.*;

public class Exercise1 {
    static String readAll(String content) throws IOException {
        // TODO: Use TWR with StringReader and BufferedReader to read all lines
    }

    public static void main(String[] args) throws IOException {
        System.out.println(readAll("line1\nline2\nline3"));
    }
}
```

### Expected Output

```
line1
line2
line3
```

### Hints

1. Create `new StringReader(content)` and `new BufferedReader(...)`.
2. Read lines in a loop with `reader.readLine()`.
3. Join lines with `\n`.
4. The TWR handles closing automatically.

---

## Exercise 2: Multiple Resources

### Problem

Write a method `copyContent(String source)` that uses TWR with a `StringReader` and `StringWriter` to copy content, converting to uppercase.

### Starter Code

```java
import java.io.*;

public class Exercise2 {
    static String copyContent(String source) throws IOException {
        // TODO: Use TWR with StringReader (input) and StringWriter (output)
        // Read all characters, convert to uppercase, write to output
    }

    public static void main(String[] args) throws IOException {
        System.out.println(copyContent("hello world"));
    }
}
```

### Expected Output

```
HELLO WORLD
```

### Hints

1. Create both resources in the TWR header.
2. Use `reader.read()` to read characters one at a time.
3. Convert each character with `Character.toUpperCase()`.
4. Write to the StringWriter with `writer.write()`.
5. Return `writer.toString()`.

---

## Exercise 3: Custom AutoCloseable

### Problem

Create a `Counter` class implementing `AutoCloseable` that tracks open/close. Each call to `increment()` prints the count. `close()` prints `"Counter closed: <count>"`.

### Starter Code

```java
public class Exercise3 {
    // TODO: Create Counter implementing AutoCloseable
    // Constructor takes a name
    // increment() prints and increments count
    // close() prints "Counter closed: <count>"

    public static void main(String[] args) {
        try (var counter = new Exercise3().new Counter("items")) {
            counter.increment();
            counter.increment();
            counter.increment();
        }
    }
}
```

### Expected Output

```
items: 1
items: 2
items: 3
Counter closed: 3
```

### Hints

1. Implement `AutoCloseable` with a `close()` method.
2. Store the count as an instance variable.
3. `increment()` should print `name + ": " + count` and increment.
4. `close()` should print the final count.

---

## Exercise 4: Exception in TWR

### Problem

Write a method `safeRead(String content)` that uses TWR to read from a `StringReader`. If reading fails, catch the exception and return `"error"`. Otherwise return the content.

### Starter Code

```java
import java.io.*;

public class Exercise4 {
    static String safeRead(String content) {
        // TODO: Use TWR, catch IOException, return "error" on failure
    }

    public static void main(String[] args) {
        System.out.println(safeRead("hello"));
        System.out.println(safeRead(null));
    }
}
```

### Expected Output

```
hello
error
```

### Hints

1. Create the TWR with a null check on content.
2. If content is null, throw IOException.
3. Catch IOException and return "error".
4. The resource is closed automatically even when an exception occurs.
