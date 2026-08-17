# Solutions: Try-with-Resources in Java

These are complete solutions for all four exercises. Review your own implementation before reading these.

---

## Solution 1: Basic TWR

```java
import java.io.*;

public class Exercise1 {
    static String readAll(String content) throws IOException {
        try (var reader = new BufferedReader(new StringReader(content))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (sb.length() > 0) sb.append("\n");
                sb.append(line);
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(readAll("line1\nline2\nline3"));
    }
}
```

**Output:**
```
line1
line2
line3
```

**Key points:**
- TWR automatically closes the reader when the block exits.
- `BufferedReader` adds `readLine()` capability.
- StringBuilder efficiently builds the result.

---

## Solution 2: Multiple Resources

```java
import java.io.*;

public class Exercise2 {
    static String copyContent(String source) throws IOException {
        try (var reader = new StringReader(source);
             var writer = new StringWriter()) {
            int ch;
            while ((ch = reader.read()) != -1) {
                writer.write(Character.toUpperCase(ch));
            }
            return writer.toString();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(copyContent("hello world"));
    }
}
```

**Output:**
```
HELLO WORLD
```

**Key points:**
- Both resources are declared in the TWR header.
- They are closed in reverse order (writer first, then reader).
- `Character.toUpperCase()` converts each character.

---

## Solution 3: Custom AutoCloseable

```java
public class Exercise3 {
    class Counter implements AutoCloseable {
        private final String name;
        private int count = 0;

        Counter(String name) { this.name = name; }

        void increment() {
            count++;
            System.out.println(name + ": " + count);
        }

        @Override
        public void close() {
            System.out.println("Counter closed: " + count);
        }
    }

    public static void main(String[] args) {
        try (var counter = new Exercise3().new Counter("items")) {
            counter.increment();
            counter.increment();
            counter.increment();
        }
    }
}
```

**Output:**
```
items: 1
items: 2
items: 3
Counter closed: 3
```

**Key points:**
- `AutoCloseable` requires a `close()` method.
- The close method is called automatically by TWR.
- The counter tracks state across increments.

---

## Solution 4: Exception in TWR

```java
import java.io.*;

public class Exercise4 {
    static String safeRead(String content) {
        try (var reader = new BufferedReader(new StringReader(
                content != null ? content : ""))) {
            if (content == null) {
                throw new IOException("Null content");
            }
            return reader.readLine();
        } catch (IOException e) {
            return "error";
        }
    }

    public static void main(String[] args) {
        System.out.println(safeRead("hello"));
        System.out.println(safeRead(null));
    }
}
```

**Output:**
```
hello
error
```

**Key points:**
- TWR handles resource closure even when exceptions occur.
- The catch block returns a default value on failure.
- Null content triggers an IOException that is caught.
