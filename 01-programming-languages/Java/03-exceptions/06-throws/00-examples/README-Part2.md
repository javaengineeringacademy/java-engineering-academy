# Examples: The throws Declaration (Part 2)

> Examples 5–6. See [Part 1](README-Part1.md) for Examples 1–4.

---

## Example 5: Method Chain Exception Handling

When methods are chained, each method in the chain must either handle exceptions or declare them. The `throws` clause propagates up the call stack until caught.

```java
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MethodChain {

    static class ProcessingException extends Exception {
        ProcessingException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    static String readSource(String path) throws IOException {
        if (path == null) {
            throw new IOException("Path cannot be null");
        }
        if (!path.endsWith(".txt")) {
            throw new IOException("Only .txt files supported: " + path);
        }
        return "content-from-" + path;
    }

    static List<String> parseLines(String data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data cannot be empty");
        }
        List<String> lines = new ArrayList<>();
        for (String s : data.split("-")) {
            lines.add(s);
        }
        return lines;
    }

    static String transform(String line) {
        return line.toUpperCase();
    }

    static String processFile(String path) throws ProcessingException {
        try {
            String data = readSource(path);
            List<String> lines = parseLines(data);
            StringBuilder result = new StringBuilder();
            for (String line : lines) {
                result.append(transform(line)).append(" ");
            }
            return result.toString().trim();
        } catch (IOException e) {
            throw new ProcessingException(
                "Failed to process file: " + path, e);
        }
    }

    public static void main(String[] args) {
        // Happy path
        try {
            String result = processFile("data.txt");
            System.out.println("Result: " + result);
        } catch (ProcessingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // IOException path
        try {
            String result = processFile("data.csv");
            System.out.println("Result: " + result);
        } catch (ProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }

        // Null path
        try {
            String result = processFile(null);
            System.out.println("Result: " + result);
        } catch (ProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
    }
}
```

**Output:**
```
Result: CONTENT FROM DATA TXT
Error: Failed to process file: data.csv
Cause: Only .txt files supported: data.csv
Error: Failed to process file: null
Cause: Path cannot be null
```

**Explanation:** `processFile` handles `IOException` from `readSource` by translating it to `ProcessingException`. The unchecked `IllegalArgumentException` from `parseLines` propagates naturally without being declared. This demonstrates the standard pattern: checked exceptions are caught and translated at the layer boundary, while unchecked exceptions pass through.

---

## Example 6: throws with Interface and Implementation

An interface declares `throws` to establish a contract. Implementations must honor that contract — they can declare fewer exceptions but not more.

```java
import java.io.IOException;

public class InterfaceThrows {

    interface DataSource {
        String read() throws IOException;
        void write(String data) throws IOException;
    }

    static class FileDataSource implements DataSource {
        private final String basePath;

        FileDataSource(String basePath) {
            this.basePath = basePath;
        }

        @Override
        public String read() throws IOException {
            throw new IOException("Simulated read failure from " + basePath);
        }

        @Override
        public void write(String data) throws IOException {
            throw new IOException("Simulated write failure to " + basePath);
        }
    }

    static class StaticDataSource implements DataSource {
        private final String data;

        StaticDataSource(String data) {
            this.data = data;
        }

        @Override
        public String read() {
            return data;
        }

        @Override
        public void write(String data) {
            // no-op for static source
        }
    }

    static void processSource(DataSource source) {
        try {
            String content = source.read();
            System.out.println("Read: " + content);
            source.write("processed-" + content);
            System.out.println("Write complete");
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("--- FileDataSource ---");
        processSource(new FileDataSource("/tmp/data"));

        System.out.println("--- StaticDataSource ---");
        processSource(new StaticDataSource("hardcoded-value"));
    }
}
```

**Output:**
```
--- FileDataSource ---
IO error: Simulated read failure from /tmp/data
--- StaticDataSource ---
Read: hardcoded-value
Write complete
```

**Explanation:** The `DataSource` interface declares `throws IOException`. `FileDataSource` honors the contract by declaring it. `StaticDataSource` does not need to declare it because its methods never throw `IOException` — this is allowed. The consumer `processSource` handles the checked exception, demonstrating how interface contracts propagate through implementations.

---

*See also: [Decision Guide](../decision.md) | [Part 1: Examples 1–4](README-Part1.md) | [Solutions](../02-solutions/README.md)*
