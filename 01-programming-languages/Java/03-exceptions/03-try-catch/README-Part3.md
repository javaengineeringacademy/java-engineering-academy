# 03 - Try-Catch Exception Handling (Part 3)

[← Part 2](README-Part2.md) | [Part 4 →](README-Part4.md)

---

### Wrap and Rethrow

Add context to the exception before rethrowing.

```java
try {
    parseConfig(configFile);
} catch (IOException e) {
    throw new RuntimeException("Failed to initialize application", e);
}
```

The original exception is preserved as the `cause`. The caller can access it with `getCause()`.

```java
try {
    startApplication();
} catch (RuntimeException e) {
    System.out.println(e.getMessage());      // "Failed to initialize application"
    System.out.println(e.getCause());        // the original IOException
    System.out.println(e.getCause().getMessage());  // the IO error message
}
```

### Java 7+ Rethrow Behavior

Before Java 7, rethrowing a checked exception from a multi-catch block was not allowed if the method signature did not declare it. Java 7 relaxed this rule.

```java
// Java 7+ — this compiles even though FileNotFoundException
// is a checked exception and the method does not declare it
public void processFile(String path) throws IOException {
    try {
        readFile(path);
    } catch (FileNotFoundException | IOException e) {
        log("Error processing file", e);
        throw e;  // Java 7+ allows this
    }
}
```

The compiler analyzes the multi-catch block and determines which exceptions can actually be rethrown. It only requires the method to declare exceptions that are provably thrown from the try body.

**Pre-Java 7 workaround:**

```java
// Before Java 7 — had to wrap in RuntimeException
public void processFile(String path) {
    try {
        readFile(path);
    } catch (FileNotFoundException | IOException e) {
        throw new RuntimeException(e);  // wrap to avoid declaring checked exception
    }
}
```

### Catch, Log, and Rethrow Pattern

This is a common production pattern. Log the exception for debugging, then let the caller handle it.

```java
public User getUser(int id) {
    try {
        return userRepository.findById(id);
    } catch (SQLException e) {
        logger.error("Database query failed for user {}", id, e);
        throw new DataAccessException("Could not retrieve user", e);
    }
}
```

### Rethrowing vs. Wrapping

| Aspect | Rethrow (`throw e`) | Wrap (`throw new XException(e)`) |
|--------|---------------------|----------------------------------|
| Preserves type | Yes | No — new exception type |
| Adds context | No | Yes |
| Caller handling | Must catch same type | Catches new type |
| Stack trace | Original | New + original as cause |
| Use case | Transparent propagation | Layered error reporting |

---

## 4. Common Mistakes

These are the errors that appear in code reviews every week.

### Mistake 1: Empty Catch Block

```java
try {
    readFile("config.txt");
} catch (IOException e) {
    // nothing here — silent failure
}
```

**Why it is bad:** The exception is swallowed. No log, no recovery, no indication anything went wrong. Bugs become invisible.

**Fix:**
```java
try {
    readFile("config.txt");
} catch (IOException e) {
    logger.warn("Config file not found, using defaults", e);
    useDefaultConfig();
}
```

### Mistake 2: Catching Exception or Throwable

```java
try {
    // do something
} catch (Exception e) {
    // catches everything including RuntimeException, Errors
}
```

**Why it is bad:** You catch `NullPointerException`, `OutOfMemoryError`, and `ThreadDeath` in the same block. You lose the ability to handle specific failures differently.

**Fix:** Catch the most specific exception type you can handle.

```java
try {
    parseInteger(input);
} catch (NumberFormatException e) {
    // handle bad input
} catch (IOException e) {
    // handle I/O failure
}
```

### Mistake 3: Swallowing Exceptions

```java
try {
    sendEmail(to, subject, body);
} catch (Exception e) {
    // caught and forgotten
}
```

**Why it is bad:** The user thinks the email was sent. It was not. No one knows.

**Fix:** At minimum, log it. If you cannot fix it, propagate it.

```java
try {
    sendEmail(to, subject, body);
} catch (MessagingException e) {
    logger.error("Failed to send email to {}", to, e);
    throw new EmailDeliveryException("Email send failed", e);
}
```

### Mistake 4: Using try-catch for Control Flow

```java
try {
    String value = map.get(key);
    process(value);
} catch (NullPointerException e) {
    // map did not contain key
    useDefault(key);
}
```

**Why it is bad:** Exceptions are expensive to create. The JVM builds a stack trace for every exception. This is 100x slower than a null check.

**Fix:**
```java
String value = map.get(key);
if (value != null) {
    process(value);
} else {
    useDefault(key);
}
```

### Mistake 5: Wrong Order of catch Blocks

```java
// WRONG — FileNotFoundException never reached
try {
    readFile("data.txt");
} catch (Exception e) {
    System.out.println("general");
} catch (FileNotFoundException e) {
    System.out.println("file not found");
}
```

**Why it is bad:** `Exception` catches `FileNotFoundException` first. The second block is unreachable dead code. The compiler rejects this.

**Fix:** Most specific to least specific.

```java
try {
    readFile("data.txt");
} catch (FileNotFoundException e) {
    System.out.println("file not found");
} catch (IOException e) {
    System.out.println("other I/O error");
} catch (Exception e) {
    System.out.println("something else");
}
```

### Mistake 6: Catching and Not Using the Exception Variable

```java
try {
    parseJSON(input);
} catch (JSONException e) {
    System.out.println("Parse failed");  // e is unused
}
```

**Why it is bad:** You lose the error message, stack trace, and cause chain. Debugging becomes guesswork.

**Fix:**
```java
try {
    parseJSON(input);
} catch (JSONException e) {
    logger.error("Parse failed: {}", e.getMessage(), e);
}
```

### Common Mistakes Quick Reference

| Mistake | Impact | Fix |
|---------|--------|-----|
| Empty catch block | Silent failure | Log or rethrow |
| Catching `Exception` | Overly broad handling | Catch specific types |
| Swallowing exceptions | Hidden bugs | Log + rethrow |
| try-catch for control flow | Performance penalty | Use null checks / if-else |
| Wrong catch order | Unreachable blocks | Specific to general |
| Unused exception variable | No diagnostic info | Always log `e.getMessage()` |

---

## 5. Best Practices

### Rule 1: Catch Specific Exceptions

```java
// BAD
catch (Exception e) { }

// GOOD
catch (FileNotFoundException e) { }
```

### Rule 2: Don't Swallow Exceptions

Always log, rethrow, or both. An exception that disappears is a bug waiting to happen.

### Rule 3: Prefer Multi-catch Over Duplicate Blocks

```java
// BAD — duplicate code
catch (IOException e) { handleError(e); }
catch (SQLException e) { handleError(e); }

// GOOD — multi-catch
catch (IOException | SQLException e) { handleError(e); }
```

### Rule 4: Add Context When Rethrowing

```java
// BAD — no context
catch (IOException e) {
    throw e;
}

// GOOD — adds context
catch (IOException e) {
    throw new DataAccessException("Failed to load user profile", e);
}
```

### Rule 5: Use try-with-resources for AutoCloseable

```java
// BAD — manual close, error-prone
BufferedReader reader = null;
try {
    reader = new BufferedReader(new FileReader("data.txt"));
    // ...
} finally {
    if (reader != null) reader.close();
}

// GOOD — try-with-resources
try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
    // ...
}
```

### Rule 6: Don't Use try-catch for Control Flow

Reserve exceptions for exceptional conditions. Use null checks and validation for expected branches.

### Rule 7: Catch Exceptions at the Right Level

Don't catch exceptions you cannot handle. Let them propagate to a layer that can.

```java
// BAD — catches and does nothing useful
try {
    db.execute(sql);
} catch (SQLException e) {
    throw new RuntimeException(e);  // just wrapping for no reason
}

// GOOD — catch where you can actually do something
try {
    db.execute(sql);
} catch (SQLException e) {
    if (isDuplicateKey(e)) {
        logger.warn("Duplicate record, skipping");
    } else {
        throw e;  // rethrow what you cannot handle
    }
}
```

### Rule 8: Prefer Unchecked Exceptions for Programming Errors

Use `IllegalArgumentException`, `IllegalStateException`, `NullPointerException` for bugs in your code. Use checked exceptions for recoverable external failures.

### Rule 9: Log at the Right Level

```java
catch (IOException e) {
    logger.debug("File not found, using defaults: {}", e.getMessage());  // expected
}
catch (SQLException e) {
    logger.error("Database failure", e);  // unexpected, needs attention
}
```

### Rule 10: Test Your Exception Paths

Exception handling code is code. It can have bugs. Write tests that verify your catch blocks actually work.

```java
@Test
void handlesFileNotFoundGracefully() {
    Config config = loadConfig("nonexistent.txt");
    assertEquals(Config.defaultConfig(), config);
}
```

---

## 6. Production Examples
