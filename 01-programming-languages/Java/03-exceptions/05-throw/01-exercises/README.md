# Exercises: The throw Keyword

## Exercise 1: Throw a Specific Exception

Write a method `validateAge(int age)` that:
- Throws `IllegalArgumentException` if `age` is negative
- Throws `IllegalArgumentException` if `age` exceeds 150
- Includes the actual value in the exception message
- Returns normally if the age is valid

**Starter code:**
```java
public class Exercise1 {
    static void validateAge(int age) {
        // TODO: Implement validation with throw
    }

    public static void main(String[] args) {
        validateAge(25);       // should return normally
        validateAge(-5);       // should throw
        validateAge(200);      // should throw
    }
}
```

**Hints:**
- Use `if` statements to check conditions before doing anything else
- The message should be descriptive: `"Age cannot be negative: " + age`
- `IllegalArgumentException` is unchecked, so no `throws` declaration needed

---

## Exercise 2: Create and Throw a Custom Exception

Create a custom unchecked exception `InvalidUsernameException` and a method `validateUsername(String username)` that:
- Throws `NullPointerException` if `username` is null
- Throws `InvalidUsernameException` if `username` is shorter than 3 characters
- Throws `InvalidUsernameException` if `username` contains spaces
- Returns normally if valid

**Starter code:**
```java
public class Exercise2 {
    // TODO: Create InvalidUsernameException extending RuntimeException
    // Include a constructor that accepts a message

    static void validateUsername(String username) {
        // TODO: Implement validation
    }

    public static void main(String[] args) {
        validateUsername("alice");    // valid
        validateUsername(null);       // NullPointerException
        validateUsername("ab");      // InvalidUsernameException
        validateUsername("alice bob"); // InvalidUsernameException
    }
}
```

**Hints:**
- Define `InvalidUsernameException` as a static inner class or a separate class
- Call `super(message)` in the constructor
- Check for null first, then check length, then check for spaces
- Use `username.contains(" ")` to detect spaces

---

## Exercise 3: Exception Chaining

Write a method `loadUserProfile(String userId)` that:
- Catches a simulated `UserNotFoundException` (use `RuntimeException` with message `"User not found: " + userId`)
- Wraps it in a `ProfileException` (custom unchecked exception)
- The `ProfileException` must preserve the original cause
- The `ProfileException` message should be `"Failed to load profile for user: " + userId`

**Starter code:**
```java
public class Exercise3 {
    // TODO: Create ProfileException extending RuntimeException
    // Include constructors for message and message+cause

    static Object fetchUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new RuntimeException("User not found: " + userId);
        }
        return new Object(); // simulated user object
    }

    static Object loadUserProfile(String userId) {
        // TODO: Call fetchUser, catch exception, wrap in ProfileException
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

**Hints:**
- Use `try-catch` inside `loadUserProfile`
- Pass the caught exception as the second argument: `new ProfileException(message, caughtException)`
- The cause is accessible via `getCause()`

---

## Exercise 4: Rethrow with Context

Write a method `processOrder(String orderId)` that:
- Catches any `RuntimeException` thrown during processing
- Logs the error message to `System.err` (use format: `"ERROR: " + e.getMessage()`)
- Rethrows the **same** exception (do not wrap it)

**Starter code:**
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
        // TODO: Call validateOrder inside try-catch
        // Catch RuntimeException, log it, rethrow it
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

**Hints:**
- Use `catch (RuntimeException e)` to catch the validation error
- Print the error message with `System.err.println`
- Use `throw e;` (not `throw new RuntimeException(e)`) to rethrow the same object
- This preserves the original stack trace and type

---

## Exercise 5: Multi-Catch with Rethrow

Write a method `transferFunds(String from, String to, double amount)` that:
- Validates `from` and `to` are not null (throw `NullPointerException`)
- Validates `amount` is positive (throw `IllegalArgumentException`)
- If any exception occurs, wraps it in `TransferException` (custom unchecked) with the original cause
- The `TransferException` message should describe what was being attempted

**Starter code:**
```java
public class Exercise5 {
    // TODO: Create TransferException extending RuntimeException
    // Include constructor for message + cause

    static void transferFunds(String from, String to, double amount) {
        // TODO: Validate parameters and throw appropriate exceptions
        // Wrap any exception in TransferException
    }

    public static void main(String[] args) {
        try {
            transferFunds("account-1", "account-2", 100.0);
            System.out.println("Transfer successful");
        } catch (TransferException e) {
            System.err.println("Transfer failed: " + e.getMessage());
            System.err.println("Root cause: " + e.getCause().getMessage());
        }

        try {
            transferFunds(null, "account-2", 100.0);
        } catch (TransferException e) {
            System.err.println("Transfer failed: " + e.getMessage());
        }

        try {
            transferFunds("account-1", "account-2", -50.0);
        } catch (TransferException e) {
            System.err.println("Transfer failed: " + e.getMessage());
        }
    }
}
```

**Hints:**
- Use `Objects.requireNonNull(from, "From account cannot be null")` for null checks
- The `NullPointerException` from `Objects.requireNonNull` will be caught and wrapped
- Alternatively, throw manually: `if (from == null) throw new NullPointerException("...")`
- Wrap the entire validation block in `try-catch` and rethrow as `TransferException`

---

## Instructions

- Implement each exercise in a single Java file
- Test each method with both valid and invalid inputs
- Verify that exception messages are descriptive
- Verify that exception types match the requirements
- Run each exercise and confirm the output matches expectations
