# Examples: Custom Exceptions in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Custom Checked Exception

```java
public class CustomChecked {
    static class InsufficientFundsException extends Exception {
        private final double balance;
        private final double amount;

        InsufficientFundsException(double balance, double amount) {
            super("Insufficient funds: balance=" + balance + ", attempted=" + amount);
            this.balance = balance;
            this.amount = amount;
        }

        double getBalance() { return balance; }
        double getAmount() { return amount; }
        double getDeficit() { return amount - balance; }
    }

    static double withdraw(double balance, double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(balance, amount);
        }
        return balance - amount;
    }

    public static void main(String[] args) {
        try {
            double newBalance = withdraw(100, 50);
            System.out.println("New balance: " + newBalance);
            newBalance = withdraw(100, 150);
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Deficit: " + e.getDeficit());
        }
    }
}
```

**Output:**
```
New balance: 50.0
Error: Insufficient funds: balance=100.0, attempted=150.0
Deficit: 50.0
```

**Explanation:** Custom checked exceptions carry domain-specific fields. The caller can inspect the fields programmatically. Checked exceptions enforce that callers handle expected failure modes.

---

## Example 2: Custom Unchecked Exception

```java
public class CustomUnchecked {
    static class ValidationException extends RuntimeException {
        private final String field;

        ValidationException(String field, String message) {
            super(message);
            this.field = field;
        }

        String getField() { return field; }
    }

    static void validateUser(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name", "Name cannot be blank");
        }
        if (email == null || !email.contains("@")) {
            throw new ValidationException("email", "Invalid email format");
        }
        System.out.println("User validated: " + name);
    }

    public static void main(String[] args) {
        validateUser("Alice", "alice@example.com");
        try {
            validateUser("", "bob@example.com");
        } catch (ValidationException e) {
            System.out.println("Validation failed [" + e.getField() + "]: " + e.getMessage());
        }
    }
}
```

**Output:**
```
User validated: Alice
Validation failed [name]: Name cannot be blank
```

**Explanation:** Unchecked custom exceptions are for programming errors. The `field` attribute identifies which input failed. This pattern is common in validation frameworks.

---

## Example 3: Exception Hierarchy

```java
public class ExceptionHierarchy {
    static class ServiceException extends Exception {
        ServiceException(String msg) { super(msg); }
        ServiceException(String msg, Throwable cause) { super(msg, cause); }
    }

    static class ValidationException extends ServiceException {
        private final String field;
        ValidationException(String field, String msg) { super(msg); this.field = field; }
        String getField() { return field; }
    }

    static class NotFoundException extends ServiceException {
        private final String resourceId;
        NotFoundException(String resourceId) { super("Not found: " + resourceId); this.resourceId = resourceId; }
        String getResourceId() { return resourceId; }
    }

    public static void main(String[] args) {
        try {
            throw new ValidationException("email", "Invalid email");
        } catch (ValidationException e) {
            System.out.println("Validation [" + e.getField() + "]: " + e.getMessage());
        }

        try {
            throw new NotFoundException("ORD-123");
        } catch (NotFoundException e) {
            System.out.println("Not found: " + e.getResourceId());
        }

        try {
            throw new ServiceException("General error");
        } catch (ServiceException e) {
            System.out.println("Service: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Validation [email]: Invalid email
Not found: ORD-123
Service: General error
```

**Explanation:** The hierarchy allows catching at different levels. Specific catches handle individual exception types; general catches handle the base type. This provides flexibility in error handling.

---

## Example 4: Exception with Cause

```java
public class ExceptionWithCause {
    static class DataException extends Exception {
        DataException(String msg, Throwable cause) { super(msg, cause); }
    }

    static void loadAndProcess(String path) throws DataException {
        try {
            int value = Integer.parseInt(path);
            int result = 100 / value;
            System.out.println("Result: " + result);
        } catch (NumberFormatException | ArithmeticException e) {
            throw new DataException("Failed to process: " + path, e);
        }
    }

    public static void main(String[] args) {
        try {
            loadAndProcess("abc");
        } catch (DataException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getClass().getSimpleName());
        }
    }
}
```

**Output:**
```
Error: Failed to process: abc
Cause: NumberFormatException
```

**Explanation:** Wrapping low-level exceptions in domain exceptions preserves the cause chain. The caller gets a meaningful exception type and can still access the root cause for debugging.
