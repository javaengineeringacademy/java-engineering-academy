# Solutions: The throw Keyword

## Solution 1: Throw a Specific Exception

```java
public class Exercise1 {
    static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        if (age > 150) {
            throw new IllegalArgumentException("Age cannot exceed 150: " + age);
        }
    }

    public static void main(String[] args) {
        validateAge(25);
        System.out.println("Age 25 is valid");

        try {
            validateAge(-5);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        try {
            validateAge(200);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Age 25 is valid
Error: Age cannot be negative: -5
Error: Age cannot exceed 150: 200
```

**Key points:**
- Two separate `if` statements with distinct messages
- Each message includes the actual value
- No `throws` declaration needed for unchecked exception

---

## Solution 2: Create and Throw a Custom Exception

```java
public class Exercise2 {
    static class InvalidUsernameException extends RuntimeException {
        InvalidUsernameException(String message) {
            super(message);
        }
    }

    static void validateUsername(String username) {
        if (username == null) {
            throw new NullPointerException("Username cannot be null");
        }
        if (username.length() < 3) {
            throw new InvalidUsernameException(
                "Username must be at least 3 characters, got: " + username.length());
        }
        if (username.contains(" ")) {
            throw new InvalidUsernameException(
                "Username cannot contain spaces: '" + username + "'");
        }
    }

    public static void main(String[] args) {
        validateUsername("alice");
        System.out.println("Username 'alice' is valid");

        try {
            validateUsername(null);
        } catch (NullPointerException e) {
            System.err.println("Error: " + e.getMessage());
        }

        try {
            validateUsername("ab");
        } catch (InvalidUsernameException e) {
            System.err.println("Error: " + e.getMessage());
        }

        try {
            validateUsername("alice bob");
        } catch (InvalidUsernameException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Username 'alice' is valid
Error: Username cannot be null
Error: Username must be at least 3 characters, got: 2
Error: Username cannot contain spaces: 'alice bob'
```

**Key points:**
- Custom exception is a static inner class with a message constructor
- `NullPointerException` for null (standard practice)
- Custom exception for domain-specific validation rules
- Each message includes the specific violation

---

## Solution 3: Exception Chaining

```java
public class Exercise3 {
    static class ProfileException extends RuntimeException {
        ProfileException(String message) {
            super(message);
        }
        ProfileException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static Object fetchUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new RuntimeException("User not found: " + userId);
        }
        return new Object();
    }

    static Object loadUserProfile(String userId) {
        try {
            return fetchUser(userId);
        } catch (RuntimeException e) {
            throw new ProfileException(
                "Failed to load profile for user: " + userId, e);
        }
    }

    public static void main(String[] args) {
        try {
            loadUserProfile("");
        } catch (ProfileException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Cause: " + e.getCause().getMessage());
        }
    }
}
```

**Output:**
```
Error: Failed to load profile for user: 
Cause: User not found: 
```

**Key points:**
- `ProfileException` has both message-only and message+cause constructors
- The caught exception is passed as the second constructor argument
- `getCause()` returns the original exception
- The wrapper message adds context about what operation failed

---

## Solution 4: Rethrow with Context

```java
public class Exercise4 {
    static void validateOrder(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be blank");
        }
        if (!orderId.startsWith("ORD-")) {
            throw new IllegalArgumentException(
                "Order ID must start with 'ORD-', got: " + orderId);
        }
    }

    static void processOrder(String orderId) {
        try {
            validateOrder(orderId);
            System.out.println("Processing order: " + orderId);
        } catch (RuntimeException e) {
            System.err.println("ERROR: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            processOrder("ORD-1234");
            System.out.println("Order processed successfully");
        } catch (Exception e) {
            System.err.println("Caught in main: " + e.getMessage());
        }

        try {
            processOrder("INVALID");
        } catch (Exception e) {
            System.err.println("Caught in main: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Processing order: ORD-1234
Order processed successfully
ERROR: Order ID must start with 'ORD-', got: INVALID
Caught in main: Order ID must start with 'ORD-', got: INVALID
```

**Key points:**
- `throw e;` rethrows the exact same exception object (preserves stack trace and type)
- The logging happens in the catch block before rethrowing
- The caller sees the original exception, not a wrapper
- This pattern is useful for adding logging or metrics without hiding the failure

---

## Solution 5: Multi-Catch with Rethrow

```java
import java.util.Objects;

public class Exercise5 {
    static class TransferException extends RuntimeException {
        TransferException(String message) {
            super(message);
        }
        TransferException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static void transferFunds(String from, String to, double amount) {
        try {
            Objects.requireNonNull(from, "From account cannot be null");
            Objects.requireNonNull(to, "To account cannot be null");
            if (amount <= 0) {
                throw new IllegalArgumentException(
                    "Transfer amount must be positive, got: " + amount);
            }
            System.out.printf("Transferring %.2f from %s to %s%n",
                amount, from, to);
        } catch (RuntimeException e) {
            throw new TransferException(
                "Failed to transfer " + amount + " from " + from + " to " + to, e);
        }
    }

    public static void main(String[] args) {
        try {
            transferFunds("account-1", "account-2", 100.0);
            System.out.println("Transfer successful");
        } catch (TransferException e) {
            System.err.println("Transfer failed: " + e.getMessage());
            System.err.println("Root cause: " + e.getCause().getMessage());
        }

        System.out.println();

        try {
            transferFunds(null, "account-2", 100.0);
        } catch (TransferException e) {
            System.err.println("Transfer failed: " + e.getMessage());
        }

        System.out.println();

        try {
            transferFunds("account-1", "account-2", -50.0);
        } catch (TransferException e) {
            System.err.println("Transfer failed: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Transferring 100.00 from account-1 to account-2
Transfer successful

Transfer failed: Failed to transfer 100.0 from null to account-2
Root cause: From account cannot be null

Transfer failed: Failed to transfer -50.0 from account-1 to account-2
Root cause: Transfer amount must be positive, got: -50.0
```

**Key points:**
- `Objects.requireNonNull` throws `NullPointerException` with a message
- The entire validation block is wrapped in `try-catch`
- Any validation failure is caught and wrapped in `TransferException`
- The wrapper message includes the operation context (amount, from, to)
- The cause chain preserves the original exception details

---

## Instructions

- Compare your solutions to these implementations
- Focus on exception chaining in Solutions 3 and 5
- Verify that exception messages match the expected format
- Run each solution and confirm the output
