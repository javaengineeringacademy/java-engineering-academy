# Examples: Unchecked Exceptions in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Common Unchecked Exceptions

```java
public class CommonUnchecked {
    public static void main(String[] args) {
        // NullPointerException
        try {
            String s = null;
            s.length();
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e.getMessage());
        }

        // ArrayIndexOutOfBoundsException
        try {
            int[] arr = {1, 2, 3};
            arr[5] = 10;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException: Index " + e.getMessage());
        }

        // ClassCastException
        try {
            Object obj = "hello";
            Integer num = (Integer) obj;
        } catch (ClassCastException e) {
            System.out.println("ClassCastException: " + e.getMessage());
        }

        // NumberFormatException
        try {
            Integer.parseInt("abc");
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
        }
    }
}
```

**Output:**
```
NullPointerException: null
ArrayIndexOutOfBoundsException: Index 5
ClassCastException: class java.lang.String cannot be cast to class java.lang.Integer
NumberFormatException: For input string: "abc"
```

**Explanation:** Unchecked exceptions extend `RuntimeException`. They signal programming errors — null dereferences, invalid array access, bad casts, invalid parsing. These should be fixed in code, not caught.

---

## Example 2: Throwing Unchecked Exceptions

```java
public class ThrowingUnchecked {
    static double calculateDiscount(double price, double percent) {
        if (price < 0) throw new IllegalArgumentException("Price must be positive: " + price);
        if (percent < 0 || percent > 100) throw new IllegalArgumentException("Percent must be 0-100: " + percent);
        return price * (1 - percent / 100.0);
    }

    public static void main(String[] args) {
        System.out.println("Discount: " + calculateDiscount(100, 20));
        try {
            calculateDiscount(-10, 20);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Discount: 80.0
Error: Price must be positive: -10.0
```

**Explanation:** `IllegalArgumentException` is the standard exception for invalid method arguments. Include the actual value in the message. No `throws` declaration is needed.

---

## Example 3: Precondition Validation

```java
import java.util.Objects;

public class PreconditionValidation {
    static void processOrder(String orderId, int quantity) {
        Objects.requireNonNull(orderId, "orderId cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive: " + quantity);
        System.out.println("Processing order " + orderId + " x" + quantity);
    }

    public static void main(String[] args) {
        processOrder("ORD-1", 5);
        try {
            processOrder(null, 5);
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            processOrder("ORD-2", -1);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Processing order ORD-1 x5
Error: orderId cannot be null
Error: quantity must be positive: -1
```

**Explanation:** `Objects.requireNonNull` throws `NullPointerException` with a message. Combined with `IllegalArgumentException` for range checks, this creates robust precondition validation. These are developer errors, not recoverable conditions.
