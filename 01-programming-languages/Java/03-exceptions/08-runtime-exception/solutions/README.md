# Solutions: RuntimeException in Java

These are complete solutions for all three exercises. Review your own implementation before reading these.

---

## Solution 1: Null Check

```java
public class Exercise1 {
    static void greet(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        System.out.println("Hello, " + name + "!");
    }

    public static void main(String[] args) {
        greet("Alice");
        greet(null);
    }
}
```

**Output:**
```
Hello, Alice!
Exception in thread "main" java.lang.NullPointerException: Name cannot be null
```

**Key points:**
- Explicit null check with descriptive message.
- `NullPointerException` is unchecked — no `throws` declaration needed.
- The message helps debug where the null originated.

---

## Solution 2: Custom Unchecked Exception

```java
public class Exercise2 {
    static class InvalidAgeException extends RuntimeException {
        InvalidAgeException(String message) { super(message); }
    }

    static void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException("Invalid age: " + age);
        }
        System.out.println("Age " + age + " is valid");
    }

    public static void main(String[] args) {
        validateAge(25);
        validateAge(-5);
    }
}
```

**Output:**
```
Age 25 is valid
Exception in thread "main" InvalidAgeException: Invalid age: -5
```

**Key points:**
- Extends `RuntimeException` for unchecked behavior.
- Constructor delegates to `super(message)`.
- Message includes the actual invalid value.

---

## Solution 3: Try-Catch Recovery

```java
public class Exercise3 {
    static int safeParse(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println("42: " + safeParse("42"));
        System.out.println("abc: " + safeParse("abc"));
    }
}
```

**Output:**
```
42: 42
abc: -1
```

**Key points:**
- `Integer.parseInt` throws `NumberFormatException` for invalid input.
- The catch block returns a sentinel value.
- This pattern is common for parsing user input.
