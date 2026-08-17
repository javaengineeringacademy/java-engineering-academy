# Solutions: Unchecked Exceptions in Java

These are complete solutions for all three exercises. Review your own implementation before reading these.

---

## Solution 1: Null Check

```java
import java.util.Objects;

public class Exercise1 {
    static void processUser(String name) {
        Objects.requireNonNull(name, "Name is required");
        System.out.println("Processing: " + name);
    }

    public static void main(String[] args) {
        processUser("Alice");
        processUser(null);
    }
}
```

**Output:**
```
Processing: Alice
Exception in thread "main" java.lang.NullPointerException: Name is required
```

**Key points:**
- `Objects.requireNonNull` is the idiomatic way to check for null.
- It throws `NullPointerException` with a descriptive message.
- No `throws` declaration needed for unchecked exceptions.

---

## Solution 2: Argument Validation

```java
public class Exercise2 {
    static void setPercentage(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Percentage must be 0-100: " + value);
        }
        System.out.println("Percentage set to: " + value);
    }

    public static void main(String[] args) {
        setPercentage(50);
        setPercentage(-10);
    }
}
```

**Output:**
```
Percentage set to: 50
Exception in thread "main" java.lang.IllegalArgumentException: Percentage must be 0-100: -10
```

**Key points:**
- Range check at the start of the method (fail-fast).
- Message includes the actual value for debugging.
- `IllegalArgumentException` is the standard for invalid arguments.

---

## Solution 3: Array Bounds Check

```java
public class Exercise3 {
    static int getElement(int[] arr, int index) {
        if (index < 0 || index >= arr.length) {
            throw new ArrayIndexOutOfBoundsException(
                "Index " + index + " out of bounds for length " + arr.length);
        }
        return arr[index];
    }

    public static void main(String[] args) {
        int[] data = {10, 20, 30};
        System.out.println("Element: " + getElement(data, 1));
        System.out.println("Element: " + getElement(data, 5));
    }
}
```

**Output:**
```
Element: 20
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 3
```

**Key points:**
- Check bounds before accessing the array.
- Include both the index and array length in the message.
- This provides more context than the default JVM error message.
