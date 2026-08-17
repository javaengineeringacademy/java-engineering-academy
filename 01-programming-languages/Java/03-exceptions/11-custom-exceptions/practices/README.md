# Exercises: Custom Exceptions in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Custom Checked Exception

### Problem

Create a custom checked exception `AuthenticationException` with a `username` field. Write a method `login(String username, String password)` that throws it if the password doesn't match `"secret"`.

### Starter Code

```java
public class Exercise1 {
    // TODO: Create AuthenticationException extending Exception
    // Field: String username
    // Constructor: (String username, String message)

    static void login(String username, String password) throws AuthenticationException {
        // TODO: Validate password, throw if incorrect
    }

    public static void main(String[] args) {
        try {
            login("alice", "secret");
            System.out.println("Login successful");
            login("alice", "wrong");
        } catch (AuthenticationException e) {
            System.out.println("Auth failed for " + e.getUsername() + ": " + e.getMessage());
        }
    }
}
```

### Expected Output

```
Login successful
Auth failed for alice: Invalid password
```

### Hints

1. Extend `Exception` for checked behavior.
2. Store `username` as a field with a getter.
3. Check `!password.equals("secret")` and throw with username.

---

## Exercise 2: Custom Unchecked Exception

### Problem

Create a custom unchecked exception `DuplicateKeyException` with a `key` field. Write a method `insertIntoMap(String key, String value)` that throws it if the key already exists.

### Starter Code

```java
import java.util.*;

public class Exercise2 {
    // TODO: Create DuplicateKeyException extending RuntimeException
    // Field: String key

    static Map<String, String> map = new HashMap<>();

    static void insertIntoMap(String key, String value) {
        // TODO: Check if key exists, throw DuplicateKeyException
    }

    public static void main(String[] args) {
        insertIntoMap("name", "Alice");
        System.out.println("Inserted name");
        insertIntoMap("name", "Bob");
    }
}
```

### Expected Output

```
Inserted name
Exception in thread "main" DuplicateKeyException: Duplicate key: name
```

### Hints

1. Extend `RuntimeException`.
2. Store `key` as a field with a getter.
3. Check `map.containsKey(key)` and throw with the key.

---

## Exercise 3: Exception with Cause

### Problem

Write a method `parseAndDouble(String input)` that parses input as an integer, doubles it, and returns the result. If parsing fails, wrap the `NumberFormatException` in a custom checked `ParsingException`.

### Starter Code

```java
public class Exercise3 {
    // TODO: Create ParsingException extending Exception
    // Include constructor for message and message+cause

    static int parseAndDouble(String input) throws ParsingException {
        // TODO: Parse input, wrap NumberFormatException in ParsingException
    }

    public static void main(String[] args) {
        try {
            System.out.println("Result: " + parseAndDouble("21"));
            System.out.println("Result: " + parseAndDouble("abc"));
        } catch (ParsingException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getClass().getSimpleName());
        }
    }
}
```

### Expected Output

```
Result: 42
Error: Failed to parse: abc
Cause: NumberFormatException
```

### Hints

1. Use `Integer.parseInt(input)` in a try block.
2. Catch `NumberFormatException` and wrap in `ParsingException`.
3. Pass the caught exception as the cause.
