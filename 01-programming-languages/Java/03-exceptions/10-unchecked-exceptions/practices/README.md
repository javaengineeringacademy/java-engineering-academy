# Exercises: Unchecked Exceptions in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Null Check

### Problem

Write a method `processUser(String name)` that throws `NullPointerException` with message `"Name is required"` if name is null, and prints `"Processing: <name>"` otherwise.

### Starter Code

```java
public class Exercise1 {
    static void processUser(String name) {
        // TODO: Check for null, throw NullPointerException with message
    }

    public static void main(String[] args) {
        processUser("Alice");
        processUser(null);
    }
}
```

### Expected Output

```
Processing: Alice
Exception in thread "main" java.lang.NullPointerException: Name is required
```

### Hints

1. Use `Objects.requireNonNull(name, "Name is required")`.
2. Or check `if (name == null)` and throw manually.

---

## Exercise 2: Argument Validation

### Problem

Write a method `setPercentage(int value)` that throws `IllegalArgumentException` if value is not between 0 and 100.

### Starter Code

```java
public class Exercise2 {
    static void setPercentage(int value) {
        // TODO: Validate range, throw IllegalArgumentException
    }

    public static void main(String[] args) {
        setPercentage(50);
        setPercentage(-10);
    }
}
```

### Expected Output

```
Percentage set to: 50
Exception in thread "main" java.lang.IllegalArgumentException: Percentage must be 0-100: -10
```

### Hints

1. Check `value < 0 || value > 100`.
2. Include the actual value in the message.
3. Print the value if valid.

---

## Exercise 3: Array Bounds Check

### Problem

Write a method `getElement(int[] arr, int index)` that returns the element at the given index, or throws `ArrayIndexOutOfBoundsException` with a descriptive message.

### Starter Code

```java
public class Exercise3 {
    static int getElement(int[] arr, int index) {
        // TODO: Return element or throw with descriptive message
    }

    public static void main(String[] args) {
        int[] data = {10, 20, 30};
        System.out.println("Element: " + getElement(data, 1));
        System.out.println("Element: " + getElement(data, 5));
    }
}
```

### Expected Output

```
Element: 20
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 3
```

### Hints

1. Check if `index < 0 || index >= arr.length`.
2. Throw with a message including index and array length.
3. Return `arr[index]` if valid.
