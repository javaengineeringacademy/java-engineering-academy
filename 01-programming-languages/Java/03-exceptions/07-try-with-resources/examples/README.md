# Examples: Try-with-Resources in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Basic TWR

```java
import java.io.*;

public class BasicTWR {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new StringReader("hello\nworld"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
hello
world
```

**Explanation:** The resource is declared in the try header. It is automatically closed after the try block completes. The `catch` block handles any `IOException` that occurs during resource operations.

---

## Example 2: Multiple Resources

```java
import java.io.*;

public class MultipleResources {
    public static void main(String[] args) {
        try (var in = new BufferedReader(new StringReader("line1\nline2"));
             var out = new StringWriter()) {
            String line;
            while ((line = in.readLine()) != null) {
                out.write(line.toUpperCase() + "\n");
            }
            System.out.println(out.toString());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
LINE1
LINE2
```

**Explanation:** Multiple resources are declared separated by semicolons. They are closed in reverse declaration order (last declared, first closed). This ensures all resources are cleaned up.

---

## Example 3: Suppressed Exceptions in TWR

```java
import java.io.*;

public class SuppressedInTWR {
    static class FlakyResource implements AutoCloseable {
        String name;
        FlakyResource(String name) { this.name = name; }
        void use() { System.out.println("Using " + name); }
        @Override public void close() {
            System.out.println("Closing " + name);
            if ("resource2".equals(name)) {
                throw new RuntimeException("Close failed for " + name);
            }
        }
    }

    public static void main(String[] args) {
        try (var r1 = new FlakyResource("resource1");
             var r2 = new FlakyResource("resource2")) {
            r1.use();
            r2.use();
            throw new RuntimeException("Primary failure");
        } catch (RuntimeException e) {
            System.out.println("Primary: " + e.getMessage());
            for (Throwable t : e.getSuppressed()) {
                System.out.println("Suppressed: " + t.getMessage());
            }
        }
    }
}
```

**Output:**
```
Using resource1
Using resource2
Closing resource2
Closing resource1
Primary: Primary failure
Suppressed: Close failed for resource2
```

**Explanation:** When `r2.close()` throws during cleanup, that exception is added as suppressed to the primary exception. Both exceptions are preserved — the primary and the cleanup failure. This is the standard behavior of TWR.

---

## Example 4: Custom AutoCloseable

```java
public class CustomAutoCloseable {
    static class Connection implements AutoCloseable {
        private final String url;
        private boolean open = true;

        Connection(String url) { this.url = url; }
        void query(String sql) {
            if (!open) throw new IllegalStateException("Connection closed");
            System.out.println("Query on " + url + ": " + sql);
        }
        @Override public void close() {
            open = false;
            System.out.println("Connection closed: " + url);
        }
    }

    public static void main(String[] args) {
        try (var conn = new Connection("jdbc:hsqldb:mem:test")) {
            conn.query("SELECT 1");
            conn.query("SELECT 2");
        }
    }
}
```

**Output:**
```
Query on jdbc:hsqldb:mem:test: SELECT 1
Query on jdbc:hsqldb:mem:test: SELECT 2
Connection closed: jdbc:hsqldb:mem:test
```

**Explanation:** Any class implementing `AutoCloseable` can be used in TWR. The `close()` method is called automatically. The resource must implement `AutoCloseable.close()` which declares `throws Exception`.

---

## Example 5: TWR with Catch and Finally

```java
import java.io.*;

public class TWRWithCatchFinally {
    public static void main(String[] args) {
        try (var reader = new BufferedReader(new StringReader("data"))) {
            String line = reader.readLine();
            System.out.println("Read: " + line);
            throw new RuntimeException("Simulated error");
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("Finally block executes");
        }
    }
}
```

**Output:**
```
Read: data
Caught: Simulated error
Finally block executes
```

**Explanation:** TWR works with catch and finally blocks. The resource is closed first, then catch/finally execute. The order is: try body → resource close → catch → finally.
